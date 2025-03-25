package tboir.menus;

import tboir.engine.Wrap;
import tboir.engine.GameState;

public class DeathScreen extends MenuType {

    public DeathScreen(Wrap wrap) {
        super(wrap, "resource/backgrounds/deathScreen.png");
    }

    @Override
    protected void setupButtons() {
        int buttonSize = 400;
        int centerX = this.getCenter(buttonSize)  + 27;
        this.addButton("restart", "RESTART", true, centerX, 500, buttonSize);
        this.addButton("exit", "EXIT", false, centerX, 600, buttonSize);
    }

    @Override
    protected void buttonClicked(String name) {
        switch (name) {
            case "restart" -> this.getWrap().changeState(GameState.GAME);
            case "exit" -> this.getWrap().changeState(GameState.MENU);
        }
    }

    @Override
    protected void keyPressed(String name) {

    }
}
