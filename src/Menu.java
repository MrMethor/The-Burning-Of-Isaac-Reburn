import Enums.GameState;
import Tools.Controls;
import Tools.Image;
import Tools.Interpolation;

import java.awt.*;

public class Menu {

    private Tools.Image background = new Image("resource/menu.jpg", 0, 0, 1920, 1080);
    private final Button start;
    private final Button exit;

    public Menu() {
        start = new Button("BEGIN", "resource/start.png",1920 / 2 - 480 / 2, 750, 480, 100);
        exit = new Button("EXIT", "resource/exit.png", 1920 / 2 - 480 / 2, 900, 480, 100);
    }

    public void update(Controls controls) {
        for (int i = 0; i < controls.commands().size(); i++) {
            switch (controls.commands().get(i).command()) {
                case leftClick:
                    if (start.isPressed(controls.commands().get(i).x(), controls.commands().get(i).y()))
                        Main.changeState(GameState.GAME);
                    else if (exit.isPressed(controls.commands().get(i).x(), controls.commands().get(i).y()))
                        Main.changeState(GameState.EXIT);
                break;
            }
            controls.removeCommand(controls.commands().get(i));
        }
    }

    public void render(Graphics g, Interpolation interpolation) {
        background.draw(g);
        start.render(g, interpolation);
        exit.render(g, interpolation);
    }
}
