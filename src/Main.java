import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;

public class Main extends JPanel implements Runnable {

    private static int newState = 0;
    private int gameState = newState;

    private boolean running = true;
    private Thread thread;

    private Menu menu = new Menu();
    private Game game;
    private Pause pause;
    private final Controls controls = new Controls();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game");
        Main main = new Main();
        frame.add(main);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setVisible(true);
        main.start();
    }

    public Main() {
        setPreferredSize(new Dimension(1920, 1080));
        setFocusable(true);
        requestFocus();
        addKeyListener(controls);
    }

    public void run() {
        Counter counter = new Counter();
        long delta = 0;
        long last = System.nanoTime();

        while (running) {

            if (delta >= counter.MS) {
                delta = 0;
                update();
                counter.addUPS();
            }
            Interpolation.setInterpolation((double)delta / counter.MS);
            counter.addFPS();
            repaint();

            // Counter
            long current = System.nanoTime() ;
            long alpha = current - last;
            delta += alpha;
            last = current;
            counter.addTime(alpha);
        }
        stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;

        switch (gameState) {
            case 0: menu.render(graphics); break;
            case 1: game.render(graphics); break;
            case 2: pause.render(graphics); break;
        }
    }

    private void update() {
        if (gameState != newState)
            updateState();

        switch (gameState) {
            case 0: menu.update(controls); break;
            case 1: game.update(controls); break;
            case 2: pause.update(controls); break;
        }

    }

    private void updateState() {
        switch(newState) {
            case -1:
                menu = null;
                running = false;
            case 0:
                game = null;
                menu = new Menu();
                break;
            case 1:
                menu = null;
                game = new Game();
                break;
            case 2:
                pause = new Pause();
                break;
        }
        gameState = newState;
    }

    private void start() {
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }

    public static void changeState(int ns) {
        newState = ns;
    }

}