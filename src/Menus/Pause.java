package Menus;

import Engine.Wrap;
import Enums.GameState;
import Tools.Button;

public class Pause extends MenuType {

    public Pause(Wrap wrap) {
        super(wrap);
    }

    protected void setupButtons() {
        int buttonSize = 500;
        int centerX = 1920 / 2 - buttonSize / 2;
        buttons.put("resume", new Button(wrap, "RESUME", buttonOn(true), centerX, 550, buttonSize));
        buttons.put("menu", new Button(wrap, "EXIT", buttonOn(false), centerX, 700, buttonSize));
    }

    protected void buttonClicked(String name) {
        switch (name) {
            case "resume" -> wrap.changeState(GameState.GAME);
            case "menu" -> wrap.changeState(GameState.MENU);
        }
    }

    protected void keyPressed(String name) {
        switch(name) {
            case "escape" -> wrap.changeState(GameState.GAME);
        }
    }
}
