import fri.shapesge.Manager;

public class Main {

    public static Interpolation interpolation = new Interpolation();

    private Menu menu = null;
    private Game game = null;
    private final Controls controls = new Controls();
    private boolean running = true;

    public static void main(String[] args) {
        Main main = new Main();
    }

    public Main() {
        Manager manager = new Manager();
        manager.manageObject(controls);

        Counter counter = new Counter();
        long delta = 0;
        long last = System.nanoTime();

        while (running) {

            if (delta >= counter.MS) {
                delta = 0;
                update();
                counter.addUPS();
            }
            interpolation.setInterpolation((double)delta / counter.MS);
            counter.addFPS();
            render();

            // Counter
            long current = System.nanoTime() ;
            long alpha = current - last;
            delta += alpha;
            last = current;
            counter.addTime(alpha);
        }
        System.exit(0);
    }

    // Updates Logic
    private void update() {
        if (this.game == null) {
            if (this.menu == null)
                this.menu = new Menu();

            // Menu logic
            this.menu.update(controls);
            if (this.menu.isNewGame()) {
                this.menu.close();
                this.menu = null;
                this.game = new Game();
            }
            else if (this.menu.isCloseGame()) {
                this.menu.close();
                this.menu = null;
                this.running = false;
            }
            return;
        }
        // Game logic
        this.game.update(controls);
        if (this.game.exited())
            this.game = null;
    }

    private void render() {
        if (this.game != null)
            this.game.render();
    }
}