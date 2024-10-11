import fri.shapesge.Manager;

public class Main {

    public static Interpolation interpolation = new Interpolation();

    private Menu menu;
    private Game game;
    private final Controls controls = new Controls();
    private boolean running = true;

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        new Main();
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

    private void update() {
        if (game == null) {
            if (menu == null)
                menu = new Menu();

            // Menu logic
            menu.update(controls);
            if (menu.isNewGame()) {
                menu.close();
                menu = null;
                game = new Game();
            }
            else if (menu.isCloseGame()) {
                menu.close();
                menu = null;
                running = false;
            }
            return;
        }
        // Game logic
        game.update(controls);
        if (game.exited())
            game = null;
    }

    private void render() {
        if (game != null)
            game.render();
    }

}