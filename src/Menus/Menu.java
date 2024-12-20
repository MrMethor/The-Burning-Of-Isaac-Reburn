package Menus;

import Engine.Wrap;
import Enums.GameState;
import Tools.Button;

public class Menu extends MenuType {

    public Menu(Wrap wrap) {
        super(wrap, "resource/textures/menu.jpg");
    }

    protected void setupButtons() {
        int buttonSize = 400;
        int centerX = 1920 / 2 - buttonSize / 2;
        buttons.put("start", new Button(wrap, "BEGIN", buttonOn(true), centerX, 750, buttonSize));
        buttons.put("settings", new Button(wrap, "SETTINGS", buttonOn(false), centerX, 850, buttonSize));
        buttons.put("exit", new Button(wrap, "EXIT", buttonOn(false), centerX, 950, buttonSize));
    }

    protected void buttonClicked(String name) {
        switch (name) {
            case "start" -> wrap.changeState(GameState.GAME);
            case "settings" -> wrap.changeState(GameState.SETTINGS);
            case "exit" -> wrap.changeState(GameState.EXIT);
        }
    }

    protected void keyPressed(String name) {

    }
}
