package tboir.menus;

import tboir.engine.Wrap;
import tboir.enums.GameState;
import tboir.tools.Button;

public class Pause extends MenuType {

    public Pause(Wrap wrap) {
        super(wrap);
    }

    protected void setupButtons() {
        int buttonSize = 500;
        int centerX = 1920 / 2 - buttonSize / 2;
        this.buttons.put("resume", new Button(this.wrap, "RESUME", this.buttonOn(true), centerX, 550, buttonSize));
        this.buttons.put("menu", new Button(this.wrap, "EXIT", this.buttonOn(false), centerX, 700, buttonSize));
    }

    protected void buttonClicked(String name) {
        switch (name) {
            case "resume" -> this.wrap.changeState(GameState.GAME);
            case "menu" -> this.wrap.changeState(GameState.MENU);
        }
    }

    protected void keyPressed(String name) {
        switch(name) {
            case "escape" -> this.wrap.changeState(GameState.GAME);
        }
    }
}
