import Enums.StateTransition;
import Engine.Wrap;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.image.BufferStrategy;

public class Main extends Canvas implements Runnable {

    private boolean running;
    private Thread thread;
    private JFrame frame = null;

    private final Wrap wrap = new Wrap("resource/config.txt");
    private Menu menu = new Menu(wrap);
    private Game game;
    private Pause pause;
    private Settings settings;

    public Main() {
        setFocusable(true);
        requestFocus();
        addKeyListener(wrap.getControls());
        addMouseListener(wrap.getControls());
        addMouseMotionListener(wrap.getControls());
        setupFrame();
    }

    public void run() {
        double timeToElapse = wrap.getTimeToElapse();
        double delta = 0;
        long last = System.nanoTime();

        while (running) {
            long current = System.nanoTime();
            delta += (current - last) / timeToElapse;
            if (delta >= 1.0) {
                delta--;
                update();
                wrap.addUPS();
            }
            wrap.setInterpolation(delta);
            render();
            wrap.addFPS();
            wrap.addTime(current - last);
            last = current;
        }
    }

    private void update() {
        wrap.setCursor(null);

        if (wrap.updateSettings())
            setupFrame();

        if (wrap.getGameState() != wrap.getNewState())
            updateState();

        if (wrap.isDebug())
            wrap.updateDebug();

        switch (wrap.getGameState()) {
            case MENU: menu.update(); break;
            case GAME: game.update(); break;
            case PAUSE: pause.update(); break;
            case SETTINGS: settings.update(); break;
        }

        frame.setCursor(wrap.getCursor());
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        //#############################################

        switch (wrap.getGameState()) {
            case MENU: menu.render(g); break;
            case GAME: game.render(g); break;
            case PAUSE: pause.render(g); break;
            case SETTINGS: settings.render(g); break;
        }

        if (wrap.isDebug())
            wrap.drawDebug(g);

        //#############################################
        g.dispose();
        bs.show();
    }

    private void updateState() {
        switch (StateTransition.getTransition(wrap.getGameState(), wrap.getNewState())) {
            case START_GAME -> {
                game = new Game(wrap);
                menu = null;
            }
            case PAUSE_GAME -> pause = new Pause(wrap);
            case RESUME_GAME -> pause = null;
            case BACK_TO_MENU -> {
                menu = new Menu(wrap);
                game = null;
            }
            case MENU_TO_SETTINGS -> {
                settings = new Settings(wrap);
                menu = null;
            }
            case SETTINGS_TO_MENU -> {
                menu = new Menu(wrap);
                settings = null;
            }
            case EXIT_GAME -> stop();
        }
        wrap.updateGameState(wrap.getNewState());
    }

    private void setupFrame() {
        if (frame != null){
            frame.setVisible(false);
            frame = null;
        }
        frame = new JFrame();
        Dimension screenSize = new Dimension(wrap.getWidth(), wrap.getHeight());
        setPreferredSize(screenSize);
        if (wrap.isFullscreen()) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
        }
        frame.setTitle("The Burning Of Isaac: Reburn");
        frame.setResizable(false);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("resource/icon.png").getImage());
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        Main main = new Main();
        main.start();
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
}