import fri.shapesge.Image;

public class Menu {

    private final Image background = new Image("resource/menu.jpg");
    private final Button start;
    private final Button exit;
    private boolean newGame;
    private boolean closeGame;

    public Menu() {
        background.changePosition(0,0);
        background.makeVisible();
        start = new Button("BEGIN", "resource/start.png",1920 / 2 - 480 / 2, 750, 480, 100);
        exit = new Button("EXIT", "resource/exit.png", 1920 / 2 - 480 / 2, 900, 480, 100);
    }

    public void update(Controls controls) {
        if (controls.mouse()[0] != null) {

            if (start.isPressed(controls.mouse()[0].x(), controls.mouse()[0].y()))
                newGame = true;
            else if (exit.isPressed(controls.mouse()[0].x(), controls.mouse()[0].y()))
                closeGame = true;

            controls.mouse()[0] = null;
        }
    }

    public boolean isNewGame() {
        return newGame;
    }

    public boolean isCloseGame() {
        return closeGame;
    }

    public void close() {
        this.background.makeInvisible();
        this.start.close();
        this.exit.close();
    }
}
