package tboir.menus;

import tboir.engine.Wrap;
import tboir.tools.Image;
import tboir.tools.Button;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

public abstract class MenuType {

    private final Wrap wrap;
    private final Image background;
    private final Color backgroundColor;
    private final HashMap<String, Button> buttons;

    public MenuType(Wrap wrap, String texture) {
        this.wrap = wrap;
        this.backgroundColor = null;
        this.background = new Image(wrap, texture, 0, 0);
        this.buttons = new HashMap<>();
        this.setupButtons();
    }

    public MenuType(Wrap wrap, Color color) {
        this.wrap = wrap;
        this.backgroundColor = color;
        this.background = null;
        this.buttons = new HashMap<>();
        this.setupButtons();
    }

    public void update() {
        var commands = this.wrap.getPressCommands();
        for (int i = 0; i < commands.size(); i++) {
            switch (commands.get(i).command()) {
                case interact -> {
                    int fi = i;
                    this.buttons.forEach((string, button) -> {
                        if (button.isWithinBounds(commands.get(fi).x(), commands.get(fi).y())) {
                            this.buttonClicked(string);
                        }
                    });
                }
                case menu -> this.keyPressed("menu");
            }
            this.wrap.getControls().removePressCommand(commands.get(i));
        }
        this.buttons.forEach((_, button) -> button.update());
    }

    public void render(Graphics2D g) {
        if (this.background != null) {
            this.background.draw(g);
        } else {
            Color c = g.getColor();
            g.setColor(this.backgroundColor);
            g.fillRect(0, 0, 1920, 1080);
            g.setColor(c);
        }
        this.buttons.forEach((_, button) -> button.render(g));
    }

    protected int getCenter(int buttonSize) {
        return 1920 / 2 - buttonSize / 2;
    }

    protected void addButton(String name, String label, boolean isOn, int x, int y, int size) {
        this.buttons.put(name, new Button(this.wrap, label, x, y, size, isOn, true));
    }

    protected void addGhostButton(String name, String label, boolean isOn, int x, int y, int size) {
        this.buttons.put(name, new Button(this.wrap, label, x, y, size, isOn, false));
    }

    protected void toggleButton(String name) {
        this.buttons.get(name).toggle();
    }

    protected Wrap getWrap() {
        return this.wrap;
    }

    protected Button getButton(String name) {
        return this.buttons.get(name);
    }

    protected abstract void setupButtons();

    protected abstract void buttonClicked(String name);

    protected abstract void keyPressed(String name);
}
