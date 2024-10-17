import java.awt.Graphics2D;

public class Pause {

    //private final Image background = new Image("resource/pause.png");
    private final Button resume;
    private final Button menu;

    public Pause() {
        //this.background.changePosition(0, 0);
        //this.background.makeVisible();
        resume = new Button("RESUME", "resource/start.png",1920 / 2 - 480 / 2, 450, 480, 100);
        menu = new Button("MENU", "resource/exit.png", 1920 / 2 - 480 / 2, 600, 480, 100);

    }

    public void update(Controls controls) {
        for (int i = 0; i < controls.keyboard().length; i++) {
            if (controls.keyboard()[i] == Keyboard.escape){
                controls.removeCommand(Keyboard.escape);
                Main.changeState(1);
                break;
            }
        }
        if (controls.mouse()[0] != null) {
            if (resume.isPressed(controls.mouse()[0].x(), controls.mouse()[0].y()))
                Main.changeState(1);
            else if (menu.isPressed(controls.mouse()[0].x(), controls.mouse()[0].y()))
                Main.changeState(0);
            controls.mouse()[0] = null;
        }
    }

    public void render(Graphics2D g) {}
}
