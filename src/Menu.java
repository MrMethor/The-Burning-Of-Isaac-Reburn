import fri.shapesge.Image;

public class Menu {

    private final Image background;
    private boolean newGame = false;
    private boolean closeGame = false;
    private final Button start;
    private final Button exit;

    public Menu() {
        this.background = new Image("resource/menu.jpg");
        this.background.changePosition(0,0);
        this.background.makeVisible();
        this.start = new Button("BEGIN", "resource/start.png",1920 / 2 - 480 / 2, 750, 480, 100);
        this.exit = new Button("EXIT", "resource/exit.png", 1920 / 2 - 480 / 2, 900, 480, 100);
    }

    public void update(Controls controls) {
        if (controls.mouse()[0] != null) {

            if (this.start.isPressed(controls.mouse()[0].x(), controls.mouse()[0].y()))
                this.newGame = true;
            else if (this.exit.isPressed(controls.mouse()[0].x(), controls.mouse()[0].y()))
                this.closeGame = true;

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
