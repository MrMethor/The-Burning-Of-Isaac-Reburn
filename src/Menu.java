import Engine.Wrap;
import Enums.GameState;
import Tools.Button;
import Tools.Image;

import java.awt.Graphics;

public class Menu {

    private final Wrap wrap;
    private Tools.Image background;
    private final Button start;
    private final Button exit;

    public Menu(Wrap wrap) {
        this.wrap = wrap;
        background = new Image(wrap, "resource/menu.jpg", 0, 0);
        start = new Button(wrap, "BEGIN", "resource/start.png",1920 / 2 - 480 / 2, 750, 480, 100);
        exit = new Button(wrap, "EXIT", "resource/exit.png", 1920 / 2 - 480 / 2, 900, 480, 100);
    }

    public void update() {
        var commands = wrap.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            switch (commands.get(i).command()) {
                case leftClick:
                    if (start.isPressed(commands.get(i).x(), commands.get(i).y()))
                        wrap.changeState(GameState.GAME);
                    else if (exit.isPressed(commands.get(i).x(), commands.get(i).y()))
                        wrap.changeState(GameState.EXIT);
                break;
            }
            wrap.getControls().removeCommand(commands.get(i));
        }
    }

    public void render(Graphics g) {
        background.draw(g);
        start.render(g);
        exit.render(g);
    }
}
