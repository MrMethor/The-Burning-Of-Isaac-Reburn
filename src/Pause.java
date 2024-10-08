import fri.shapesge.Image;

public class Pause {

    private final Image background = new Image("resource/pause.png");
    private final Button resume;
    private final Button menu;
    private boolean exited = false;
    private boolean resumed = false;

    public Pause() {
        this.background.changePosition(0, 0);
        this.background.makeVisible();
        this.resume = new Button("RESUME", "resource/start.png",1920 / 2 - 480 / 2, 450, 480, 100);
        this.menu = new Button("MENU", "resource/exit.png", 1920 / 2 - 480 / 2, 600, 480, 100);
    }

    public void update(Controls controls) {
        for (int i = 0; i < controls.keyboard().length; i++) {
            if (controls.keyboard()[i] == Keyboard.escape){
                controls.removeCommand(Keyboard.escape);
                this.resumed = true;
                break;
            }
        }
        if (controls.mouse()[0] != null) {

            if (this.resume.isPressed(controls.mouse()[0].x(), controls.mouse()[0].y()))
                this.resumed = true;
            else if (this.menu.isPressed(controls.mouse()[0].x(), controls.mouse()[0].y()))
                this.exited = true;

            controls.mouse()[0] = null;
        }
    }

    public void close() {
        this.resume.close();
        this.menu.close();
        this.background.makeInvisible();
    }

    public boolean exited() {
        return exited;
    }

    public boolean resumed() {
        return resumed;
    }
}
