package tboir.menus;

import tboir.engine.Wrap;
import tboir.enums.GameState;
import tboir.tools.Button;

public class Menu extends MenuType {

    public Menu(Wrap wrap) {
        super(wrap, "resource/backgrounds/menu.jpg");
    }

    @Override
    protected void setupButtons() {
        int buttonSize = 400;
        int centerX = 1920 / 2 - buttonSize / 2;
        this.buttons.put("start", new Button(this.wrap, "BEGIN", this.buttonOn(true), centerX, 750, buttonSize));
        this.buttons.put("settings", new Button(this.wrap, "SETTINGS", this.buttonOn(false), centerX, 850, buttonSize));
        this.buttons.put("exit", new Button(this.wrap, "EXIT", this.buttonOn(false), centerX, 950, buttonSize));
    }

    @Override
    protected void buttonClicked(String name) {
        switch (name) {
            case "start" -> this.wrap.changeState(GameState.GAME);
            case "settings" -> this.wrap.changeState(GameState.SETTINGS);
            case "exit" -> this.wrap.changeState(GameState.EXIT);
        }
    }

    @Override
    protected void keyPressed(String name) {

    }
}
