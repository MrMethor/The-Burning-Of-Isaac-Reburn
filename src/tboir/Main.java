package tboir;

import tboir.enums.StateTransition;
import tboir.engine.Wrap;
import tboir.menus.*;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.image.BufferStrategy;

public class Main extends Canvas implements Runnable {

    private boolean running;
    private Thread thread;
    private JFrame frame;

    private final Wrap wrap;
    private Menu menu;
    private Game game;
    private Pause pause;
    private Settings settings;
    private Keybinds keybinds;
    private DeathScreen deathScreen;

    public Main() {
        this.frame = null;
        this.wrap = new Wrap();
        this.menu = new Menu(this.wrap);
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(this.wrap.getControls());
        this.addMouseListener(this.wrap.getControls());
        this.addMouseMotionListener(this.wrap.getControls());
        this.setupFrame();
    }

    public void run() {
        double timeToElapse = this.wrap.getTimeToElapse();
        double delta = 0;
        long last = System.nanoTime();

        while (this.running) {
            long current = System.nanoTime();
            delta += (current - last) / timeToElapse;
            if (delta >= 1.0) {
                delta--;
                this.update();
                this.wrap.addUPS();
            }

            this.wrap.setInterpolation(delta);
            this.render();
            this.wrap.addFPS();
            this.wrap.addTime(current - last);
            last = current;
        }
        System.exit(0);
    }

    private void update() {
        this.wrap.setCursor(null);

        if (this.wrap.updateSettings()) {
            this.setupFrame();
        }

        if (this.wrap.getGameState() != this.wrap.getNewState()) {
            this.updateState();
        }

        if (this.wrap.isDebug()) {
            this.wrap.updateDebug();
        }

        switch (this.wrap.getGameState()) {
            case MENU -> this.menu.update();
            case GAME -> this.game.update();
            case PAUSE -> this.pause.update();
            case SETTINGS -> this.settings.update();
            case KEYBINDS -> this.keybinds.update();
            case DEATH -> this.deathScreen.update();
        }

        this.frame.setCursor(this.wrap.getCursor());
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();

        switch (this.wrap.getGameState()) {
            case MENU -> this.menu.render(g);
            case GAME -> this.game.render(g);
            case PAUSE -> this.pause.render(g);
            case SETTINGS -> this.settings.render(g);
            case KEYBINDS -> this.keybinds.render(g);
            case DEATH -> this.deathScreen.render(g);
        }

        if (this.wrap.isDebug()) {
            this.wrap.drawDebug(g);
        }
        g.dispose();
        bs.show();
        Toolkit.getDefaultToolkit().sync();
    }

    private void updateState() {
        switch (StateTransition.getTransition(this.wrap.getGameState(), this.wrap.getNewState())) {
            case START_GAME -> {
                this.game = new Game(this.wrap);
                this.menu = null;
            }
            case PAUSE_GAME -> this.pause = new Pause(this.wrap);
            case RESUME_GAME -> this.pause = null;
            case DEATH -> this.deathScreen = new DeathScreen(this.wrap);
            case EXIT_DEATH -> {
                this.menu = new Menu(this.wrap);
                this.deathScreen = null;
                this.game = null;
            }
            case RESTART -> {
                this.game = new Game(this.wrap);
                this.deathScreen = null;
            }
            case BACK_TO_MENU -> {
                this.menu = new Menu(this.wrap);
                this.game = null;
            }
            case MENU_TO_SETTINGS -> {
                this.settings = new Settings(this.wrap);
                this.menu = null;
            }
            case SETTINGS_TO_MENU -> {
                this.menu = new Menu(this.wrap);
                this.settings = null;
            }
            case SETTINGS_TO_KEYBINDS -> {
                this.keybinds = new Keybinds(this.wrap);
            }
            case APPLY_KEYBINDS -> {
                this.keybinds = null;
            }
            case EXIT_GAME -> this.stop();
        }
        this.wrap.updateGameState(this.wrap.getNewState());
    }

    private void setupFrame() {
        if (this.frame != null) {
            this.frame.setVisible(false);
            this.frame = null;
        }
        this.frame = new JFrame();
        Dimension screenSize = new Dimension(this.wrap.getWidth(), this.wrap.getHeight());
        this.setPreferredSize(screenSize);
        if (this.wrap.isFullscreen()) {
            this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.frame.setUndecorated(true);
        }
        this.frame.setTitle("The Burning Of Isaac: Reburn");
        this.frame.setResizable(false);
        this.frame.add(this);
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setIconImage(new ImageIcon("resource/icon.png").getImage());
        this.frame.setVisible(true);
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        System.setProperty("sun.java2d.opengl", "true");
        Main main = new Main();
        main.start();
    }

    private synchronized void start() {
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    private synchronized void stop() {
        this.running = false;
        this.removeKeyListener(this.wrap.getControls());
        this.removeMouseListener(this.wrap.getControls());
        try {
            this.thread.join(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}