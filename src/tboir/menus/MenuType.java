package tboir.menus;

import tboir.engine.Wrap;
import tboir.tools.Image;
import tboir.tools.Button;

import java.awt.Graphics;
import java.util.HashMap;

public abstract class MenuType {

    private final Wrap wrap;
    private final Image background;
    private final HashMap<String, Button> buttons;

    public MenuType(Wrap wrap, String bgPath) {
        this.wrap = wrap;
        this.background = new Image(wrap, bgPath, 0, 0);
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
                        if (button.isWithinBounds(commands.get(fi).x(), commands.get(fi).y())) {
                            this.buttonClicked(string);
                        }
                    });
                }
                case escape -> this.keyPressed("escape");
            }
            this.wrap.getControls().removeCommand(commands.get(i));
        }
        this.buttons.forEach((_, button) -> button.update());
    }

    public void render(Graphics g) {
        if (this.background != null) {
            this.background.draw(g);
        }
        this.buttons.forEach((_, button) -> button.render(g));
    }

    protected void addButton(String name, String label, boolean isOn, int x, int y, int size) {
        this.buttons.put(name, new Button(this.wrap, label, x, y, size, isOn));
    }

    protected void toggleButton(String name) {
        this.buttons.get(name).toggle();
    }

    protected Wrap getWrap() {
        return this.wrap;
    }

    protected abstract void setupButtons();

    protected abstract void buttonClicked(String name);

    protected abstract void keyPressed(String name);
}
