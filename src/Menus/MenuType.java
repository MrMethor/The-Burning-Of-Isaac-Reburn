package Menus;

import Engine.Component;
import Engine.Wrap;
import Tools.Image;
import Tools.Button;

import java.awt.*;
import java.util.HashMap;

public abstract class MenuType implements Component {

    protected final Wrap wrap;
    protected final Image background;
    protected HashMap<String, Button> buttons;

    public MenuType(Wrap wrap, String BGpath) {
        this.wrap = wrap;
        background = new Image(wrap, BGpath, 0, 0);
        buttons = new HashMap<>();
        setupButtons();
    }

    public MenuType(Wrap wrap) {
        this.wrap = wrap;
        background = null;
        buttons = new HashMap<>();
        setupButtons();
    }

    public void update() {
        var commands = wrap.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            switch (commands.get(i).command()) {
                case leftClick -> {
                    int fi = i;
                    buttons.forEach((string, button) -> {
                        if (button.isWithinBounds(commands.get(fi).x(), commands.get(fi).y()))
                            buttonClicked(string);
                    });
                }
                case escape -> keyPressed("escape");
            }
            wrap.getControls().removeCommand(commands.get(i));
        }
        buttons.forEach((string, button) ->{
            button.update();
        });
    }

    public void render(Graphics g) {
        if (background != null)
            background.draw(g);
        buttons.forEach((string, button) ->{
            button.render(g);
        });
    }

    protected String buttonOn(boolean on) {
        return on ? "resource/hud/onButton.png" : "resource/hud/offButton.png";
    }

    protected abstract void setupButtons();

    protected abstract void buttonClicked(String name);

    protected abstract void keyPressed(String name);
}
