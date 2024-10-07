import fri.shapesge.Manager;

public class Main {

    static Menu menu = null;
    static Game game = null;
    static Controls controls = new Controls();
    static boolean running = true;

    public static void main(String[] args) {

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
            counter.addFPS();

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
    static void update() {
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
}