package tboir;

import tboir.engine.Commands;
import tboir.engine.GameState;
import tboir.engine.StateTransition;
import tboir.engine.Wrap;
import tboir.menus.MenuType;
import tboir.menus.Settings;
import tboir.menus.Menu;
import tboir.menus.DeathScreen;
import tboir.menus.Pause;
import tboir.menus.Keybinds;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.RenderingHints;
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
    private Game game;
    private MenuType menu;

    private final RenderingHints renderingHints;

    public Main() {
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        hints.put(RenderingHints.KEY_COLOR_RENDERING,  RenderingHints.VALUE_COLOR_RENDER_SPEED);
        hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        this.renderingHints = hints;

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

        if (this.wrap.toggleFullscreen()) {
            this.setupFrame();
        }

        if (this.wrap.getGameState() != this.wrap.getNewState()) {
            this.updateState();
        }

        if (this.wrap.isDebug()) {
            this.wrap.updateDebug();
        }

        for (int i = 0; i < this.wrap.getPressCommands().size(); i++) {
            if (this.wrap.getPressCommands().get(i).command() == Commands.fullscreen) {
                this.wrap.isToToggleFullscreen();
                this.wrap.getControls().removePressCommand(this.wrap.getPressCommands().get(i));
            }
        }

        if (this.wrap.getGameState() == GameState.GAME) {
            this.game.update();
        } else {
            this.menu.update();
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
        g.setRenderingHints(this.renderingHints);
        g.scale(this.wrap.getScale(), this.wrap.getScale());

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (this.wrap.performanceDrop()) {
            this.wrap.setInterpolation(0);
        }

        if (this.wrap.getGameState() == GameState.GAME) {
            this.game.render(g);
        } else {
            if (this.menu instanceof Pause) {
                this.wrap.setInterpolation(0);
                this.game.render(g);
            }
            this.menu.render(g);
        }

        if (this.wrap.isDebug()) {
            this.wrap.drawDebug(g);
        }
        g.dispose();
        bs.show();
        if (!this.wrap.performanceDrop()) {
            Toolkit.getDefaultToolkit().sync();
        }
    }

    private void updateState() {
        switch (StateTransition.getTransition(this.wrap.getGameState(), this.wrap.getNewState())) {
            case START_GAME, RESTART -> {
                this.game = new Game(this.wrap);
                this.menu = null;
            }
            case PAUSE_GAME -> this.menu = new Pause(this.wrap);
            case RESUME_GAME -> this.menu = null;
            case DEATH -> this.menu = new DeathScreen(this.wrap);
            case EXIT_DEATH, BACK_TO_MENU -> {
                this.menu = new Menu(this.wrap);
                this.game = null;
            }
            case MENU_TO_SETTINGS, APPLY_KEYBINDS -> this.menu = new Settings(this.wrap);
            case SETTINGS_TO_MENU -> this.menu = new Menu(this.wrap);
            case SETTINGS_TO_KEYBINDS -> this.menu = new Keybinds(this.wrap);
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
        System.setProperty("awt.image.scaling", "false");
        System.setProperty("sun.java2d.accthreshold", "0");
        System.setProperty("sun.java2d.pmoffscreen", "false");
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