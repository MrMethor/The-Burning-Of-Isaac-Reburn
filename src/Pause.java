import Engine.Wrap;
import Enums.GameState;
import Tools.Button;

import java.awt.*;

public class Pause {

    private final Wrap wrap;
    private final Button resume;
    private final Button menu;

    public Pause(Wrap wrap) {
        this.wrap = wrap;
        resume = new Button(wrap,"RESUME", "resource/start.png",1920 / 2 - 480 / 2, 550, 480, 100);
        menu = new Button(wrap,"EXIT", "resource/exit.png", 1920 / 2 - 480 / 2, 700, 480, 100);
    }

    public void update() {
        var commands = wrap.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            switch (commands.get(i).command()) {
                case leftClick:
                    if (resume.isPressed(commands.get(i).x(), commands.get(i).y()))
                        wrap.changeState(GameState.GAME);
                    else if (menu.isPressed(commands.get(i).x(), commands.get(i).y()))
                        wrap.changeState(GameState.MENU);
                    break;
                case escape:
                    wrap.changeState(GameState.GAME);
                    break;
            }
            wrap.getControls().removeCommand(commands.get(i));
        }
    }

    public void render(Graphics g) {
        Color c = g.getColor();
        g.setColor(new Color(0f,0f,0f,.02f));
        g.fillRect(0, 0, 1920, 1080);
        g.setColor(c);
        resume.render(g);
        menu.render(g);
    }
}
