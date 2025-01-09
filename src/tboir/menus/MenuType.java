package tboir.menus;

import tboir.engine.Wrap;
import tboir.tools.Image;
import tboir.tools.Button;

import java.awt.Graphics;
import java.util.HashMap;

public abstract class MenuType {

    protected final Wrap wrap;
    protected final Image background;
    protected HashMap<String, Button> buttons;

    public MenuType(Wrap wrap, String BGpath) {
        this.wrap = wrap;
        this.background = new Image(wrap, BGpath, 0, 0);
        this.buttons = new HashMap<>();
        this.setupButtons();
    }

    public MenuType(Wrap wrap) {
        this.wrap = wrap;
        this.background = null;
        this.buttons = new HashMap<>();
        this.setupButtons();
    }

    public void update() {
        var commands = this.wrap.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            switch (commands.get(i).command()) {
                case leftClick -> {
                    int fi = i;
                    this.buttons.forEach((string, button) -> {
                        if (button.isWithinBounds(commands.get(fi).x(), commands.get(fi).y()))
                            buttonClicked(string);
                    });
                }
                case escape -> keyPressed("escape");
            }
            this.wrap.getControls().removeCommand(commands.get(i));
        }
        this.buttons.forEach((string, button) ->{
            button.update();
        });
    }

    public void render(Graphics g) {
        if (this.background != null)
            this.background.draw(g);
        this.buttons.forEach((string, button) ->{
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
