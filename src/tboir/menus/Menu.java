package tboir.menus;

import tboir.engine.Wrap;
import tboir.enums.GameState;

public class Menu extends MenuType {

    public Menu(Wrap wrap) {
        super(wrap, "resource/backgrounds/menu.jpg");
    }

    @Override
    protected void setupButtons() {
        int buttonSize = 400;
        int centerX = 1920 / 2 - buttonSize / 2;
        this.addButton("start", "BEGIN", true, centerX, 750, buttonSize);
        this.addButton("settings", "SETTINGS", false, centerX, 850, buttonSize);
        this.addButton("exit", "EXIT", false, centerX, 950, buttonSize);
    }

    @Override
    protected void buttonClicked(String name) {
        switch (name) {
            case "start" -> this.getWrap().changeState(GameState.GAME);
            case "settings" -> this.getWrap().changeState(GameState.SETTINGS);
            case "exit" -> this.getWrap().changeState(GameState.EXIT);
        }
    }

    @Override
    protected void keyPressed(String name) {

    }
}
