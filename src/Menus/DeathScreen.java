package Menus;

import Engine.Wrap;
import Enums.GameState;
import Tools.Button;

public class DeathScreen extends MenuType {

    public DeathScreen(Wrap wrap) {
        super(wrap, "resource/textures/deathScreen.png");
    }

    protected void setupButtons() {
        int buttonSize = 400;
        int centerX = 1920 / 2 - buttonSize / 2 + 27;
        buttons.put("restart", new Button(wrap, "RESTART", buttonOn(true), centerX, 500, buttonSize));
        buttons.put("exit", new Button(wrap, "EXIT", buttonOn(false), centerX, 600, buttonSize));
    }

    protected void buttonClicked(String name) {
        switch (name) {
            case "restart" -> wrap.changeState(GameState.GAME);
            case "exit" -> wrap.changeState(GameState.MENU);
        }
    }

    protected void keyPressed(String name) {

    }
}
