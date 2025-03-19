package tboir.menus;

import tboir.engine.Wrap;
import tboir.enums.GameState;

public class Pause extends MenuType {

    public Pause(Wrap wrap) {
        super(wrap);
    }

    @Override
    protected void setupButtons() {
        int buttonSize = 500;
        int centerX = this.getCenter(buttonSize);
        this.addButton("resume", "RESUME", true, centerX, 550, buttonSize);
        this.addButton("menu", "EXIT", false, centerX, 700, buttonSize);
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
