package tboir.menus;

import tboir.engine.Wrap;
import tboir.enums.GameState;
import tboir.tools.Button;

public class DeathScreen extends MenuType {

    public DeathScreen(Wrap wrap) {
        super(wrap, "resource/backgrounds/deathScreen.png");
    }

    @Override
    protected void setupButtons() {
        int buttonSize = 400;
        int centerX = 1920 / 2 - buttonSize / 2 + 27;
        this.buttons.put("restart", new Button(this.wrap, "RESTART", this.buttonOn(true), centerX, 500, buttonSize));
        this.buttons.put("exit", new Button(this.wrap, "EXIT", this.buttonOn(false), centerX, 600, buttonSize));
    }

    @Override
    protected void buttonClicked(String name) {
        switch (name) {
            case "restart" -> this.wrap.changeState(GameState.GAME);
            case "exit" -> this.wrap.changeState(GameState.MENU);
        }
    }

    @Override
    protected void keyPressed(String name) {

    }
}
