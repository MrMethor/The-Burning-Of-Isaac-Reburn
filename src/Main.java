import Enums.StateTransition;
import Tools.TextBox;
import Engine.Wrap;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.image.BufferStrategy;
import java.util.Objects;

public class Main extends Canvas implements Runnable {

    private boolean running;
    private Thread thread;
    private final JFrame frame;

    private final Wrap wrap = new Wrap("resource/config.txt");
    private Menu menu = new Menu(wrap);
    private Game game;
    private Pause pause;

    public Main() {
        Dimension screenSize = new Dimension(wrap.getWidth(), wrap.getHeight());
        setPreferredSize(screenSize);
        frame = new JFrame();
        if (wrap.isFullscreen()) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
        }
        setFocusable(true);
        requestFocus();
        addKeyListener(wrap.getControls());
        addMouseListener(wrap.getControls());
    }

    public void run() {
        long delta = 0;
        long last = System.nanoTime();

        while (running) {
            int timeToElapse = wrap.getTimeToElapse();
            if (delta >= timeToElapse) {
                delta -= timeToElapse;
                update();
                wrap.addUPS();
            }
            wrap.setInterpolation((double)delta / timeToElapse);
            wrap.addFPS();
            render();

            // Counter
            long current = System.nanoTime() ;
            long alpha = current - last;
            delta += alpha;
            last = current;
            wrap.addTime(alpha);
        }
    }

    private void update() {
        if (wrap.getGameState() != wrap.getNewState())
            updateState();

        switch (wrap.getGameState()) {
            case MENU: menu.update(); break;
            case GAME: game.update(); break;
            case PAUSE: pause.update(); break;
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        switch (wrap.getGameState()) {
            case MENU: menu.render(g); break;
            case GAME: game.render(g); break;
            case PAUSE: game.render(g); pause.render(g); break;
        }

        if (wrap.isFPS()) {
            int size = 15;
            Tools.TextBox fps = new TextBox(wrap,"FPS: " + wrap.getFPS(), 0, size, Color.WHITE, "Calibri", Font.PLAIN, size);
            fps.draw(g);
        }
        g.dispose();
        bs.show();
    }

    private void updateState() {
        switch(Objects.requireNonNull(StateTransition.getTransition(wrap.getGameState(), wrap.getNewState()))) {
            case START_GAME:
                game = new Game(wrap);
                menu = null;
                break;
            case PAUSE_GAME:
                pause = new Pause(wrap);
                break;
            case RESUME_GAME:
                pause = null;
                break;
            case BACK_TO_MENU:
                menu = new Menu(wrap);
                game = null;
                break;
            case EXIT_GAME:
                stop();
                break;
        }
        wrap.updateGameState(wrap.getNewState());
    }

    private synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        running = false;
        removeKeyListener(wrap.getControls());
        removeMouseListener(wrap.getControls());
        try {
            thread.join(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        Main main = new Main();
        main.frame.setTitle("The Burning Of Isaac: Reburn");
        main.frame.setResizable(false);
        main.frame.add(main);
        main.frame.pack();
        main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.frame.setLocationRelativeTo(null);
        main.frame.setIconImage(new ImageIcon("resource/icon.png").getImage());
        main.frame.setVisible(true);
        main.start();
    }
}