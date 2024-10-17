import java.awt.Graphics2D;

public class Menu {

    //private final Image background = new Image("resource/menu.jpg");
    private final Button start;
    private final Button exit;

    public Menu() {
        //background.changePosition(0,0);
        //background.makeVisible();
        start = new Button("BEGIN", "resource/start.png",1920 / 2 - 480 / 2, 750, 480, 100);
        exit = new Button("EXIT", "resource/exit.png", 1920 / 2 - 480 / 2, 900, 480, 100);
    }

    public void update(Controls controls) {
        if (controls.mouse()[0] != null) {

            if (start.isPressed(controls.mouse()[0].x(), controls.mouse()[0].y()))
                Main.changeState(1);
            else if (exit.isPressed(controls.mouse()[0].x(), controls.mouse()[0].y()))
                Main.changeState(-1);

            controls.mouse()[0] = null;
        }
    }

    public void render(Graphics2D g) {

    }
}
