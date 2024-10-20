import Enums.GameState;
import Tools.Controls;
import Tools.Image;
import Tools.Interpolation;
import Tools.Screen;

import java.awt.*;

public class Pause {

    private Tools.Image background = new Image("resource/pause.png", 0, 0, Screen.WIDTH, Screen.HEIGHT);
    private final Button resume;
    private final Button menu;

    public Pause() {
        resume = new Button("RESUME", "resource/start.png",1920 / 2 - 480 / 2, 550, 480, 100);
        menu = new Button("EXIT", "resource/exit.png", 1920 / 2 - 480 / 2, 700, 480, 100);
    }

    public void update(Controls controls) {
        for (int i = 0; i < controls.commands().size(); i++) {
            switch (controls.commands().get(i).command()) {
                case leftClick:
                    if (resume.isPressed(controls.commands().get(i).x(), controls.commands().get(i).y()))
                        Main.changeState(GameState.GAME);
                    else if (menu.isPressed(controls.commands().get(i).x(), controls.commands().get(i).y()))
                        Main.changeState(GameState.MENU);
                    break;
                case escape:
                    Main.changeState(GameState.GAME);
                    break;
            }
            controls.removeCommand(controls.commands().get(i));
        }
    }

    public void render(Graphics g, Interpolation interpolation) {
        background.draw(g);
        resume.render(g, interpolation);
        menu.render(g, interpolation);
    }
}
