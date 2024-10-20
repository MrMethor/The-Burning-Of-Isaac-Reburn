import Enums.GameState;
import Enums.StateTransition;
import Tools.Screen;
import Tools.Controls;
import Tools.Counter;
import Tools.Interpolation;
import Tools.TextBox;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import java.awt.image.BufferStrategy;

public class Main extends Canvas implements Runnable {

    public static GameState newState = GameState.MENU;

    private GameState gameState = GameState.MENU;
    private boolean running;
    private Thread thread;
    private JFrame frame;

    private final Screen screen = new Screen();
    private Menu menu = new Menu();
    private Game game;
    private Pause pause;
    private final Controls controls = new Controls();
    private final Counter counter = new Counter();
    private final Interpolation interpolation = new Interpolation();

    public Main() {
        Dimension screenSize = new Dimension(Screen.getWidth(), Screen.getHeight());
        setPreferredSize(screenSize);
        frame = new JFrame();
        setFocusable(true);
        requestFocus();
        addKeyListener(controls);
        addMouseListener(controls);
    }

    public void run() {
        long delta = 0;
        long last = System.nanoTime();

        while (running) {
            if (delta >= Counter.MS) {
                delta -= Counter.MS;
                update();
                counter.addUPS();
            }
            interpolation.setInterpolation((double)delta / Counter.MS);
            counter.addFPS();
            render();

            // Counter
            long current = System.nanoTime() ;
            long alpha = current - last;
            delta += alpha;
            last = current;
            counter.addTime(alpha);
        }
    }

    private void update() {
        if (gameState != newState)
            updateState();

        switch (gameState) {
            case MENU: menu.update(controls); break;
            case GAME: game.update(controls); break;
            case PAUSE: pause.update(controls); break;
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        switch (gameState) {
            case MENU: menu.render(g, interpolation); break;
            case GAME: game.render(g, interpolation); break;
            case PAUSE: game.render(g, interpolation); pause.render(g, interpolation); break;
        }

        if (Screen.FPS) {
            int size = 15;
            Tools.TextBox fps = new TextBox("FPS: " + counter.getFPS(), 0, size, Color.WHITE, "Calibri", Font.PLAIN, size);
            fps.draw(g);
        }
        g.dispose();
        bs.show();

    }

    private void updateState() {
        switch(StateTransition.getTransition(gameState, newState)) {
            case START_GAME:
                game = new Game();
                menu = null;
                break;
            case PAUSE_GAME:
                pause = new Pause();
                break;
            case RESUME_GAME:
                pause = null;
                break;
            case BACK_TO_MENU:
                menu = new Menu();
                game = null;
                break;
            case EXIT_GAME:
                stop();
                break;
        }
        gameState = newState;
    }

    private synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        running = false;
        removeKeyListener(controls);
        removeMouseListener(controls);
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
        if (Screen.FULLSCREEN){
            main.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            main.frame.setUndecorated(true);
        }
        main.frame.add(main);
        main.frame.pack();
        main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.frame.setLocationRelativeTo(null);
        main.frame.setVisible(true);
        main.start();
    }

    public static void changeState(GameState ns) {
        newState = ns;
    }
}