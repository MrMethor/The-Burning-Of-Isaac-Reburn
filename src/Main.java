import Enums.StateTransition;
import Engine.Wrap;
import Menus.DeathScreen;
import Menus.Menu;
import Menus.Pause;
import Menus.Settings;

import java.awt.*;
import javax.swing.*;
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
    private DeathScreen deathScreen;

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
        System.exit(0);
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
            case MENU -> menu.update();
            case GAME -> game.update();
            case PAUSE -> pause.update();
            case SETTINGS -> settings.update();
            case DEATH -> deathScreen.update();
        }

        frame.setCursor(wrap.getCursor());
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        //#############################################

        switch (wrap.getGameState()) {
            case MENU -> menu.render(g);
            case GAME -> game.render(g);
            case PAUSE -> pause.render(g);
            case SETTINGS -> settings.render(g);
            case DEATH -> deathScreen.render(g);
        }

        if (wrap.isDebug())
            wrap.drawDebug(g);

        //#############################################
        g.dispose();
        bs.show();
        Toolkit.getDefaultToolkit().sync();
    }

    private void updateState() {
        switch (StateTransition.getTransition(wrap.getGameState(), wrap.getNewState())) {
            case START_GAME -> {
                game = new Game(wrap);
                menu = null;
            }
            case PAUSE_GAME -> pause = new Pause(wrap);
            case RESUME_GAME -> pause = null;
            case DEATH -> deathScreen = new DeathScreen(wrap);
            case EXIT_DEATH -> {
                menu = new Menu(wrap);
                deathScreen = null;
                game = null;
            }
            case RESTART -> {
                game = new Game(wrap);
                deathScreen = null;
            }
            case BACK_TO_MENU -> {
                menu = new Menus.Menu(wrap);
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
        System.setProperty("sun.java2d.opengl", "true");
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
    }
}