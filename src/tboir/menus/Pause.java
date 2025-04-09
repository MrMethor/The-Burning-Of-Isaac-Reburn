package tboir.menus;

import tboir.engine.Wrap;
import tboir.engine.GameState;

import java.awt.Color;

public class Pause extends MenuType {

    public Pause(Wrap wrap) {
        super(wrap, new Color(0, 0, 0, 100));
    }

    @Override
    protected void setupButtons() {
        int buttonSize = 400;
        int centerX = this.getCenter(buttonSize);
        this.addButton("resume", "RESUME", true, centerX, 500, buttonSize);
        this.addButton("menu", "EXIT", false, centerX, 625, buttonSize);
    }

    @Override
    protected void buttonClicked(String name) {
        switch (name) {
            case "resume" -> this.getWrap().changeState(GameState.GAME);
            case "menu" -> this.getWrap().changeState(GameState.MENU);
        }
    }

    @Override
    protected void keyPressed(String name) {
        switch (name) {
            case "menu" -> this.getWrap().changeState(GameState.GAME);
        }
    }
}
