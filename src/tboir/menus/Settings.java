package tboir.menus;

import tboir.engine.Wrap;
import tboir.enums.GameState;
import tboir.tools.Button;

public class Settings extends MenuType {

    private boolean fullscreen;
    private boolean debug;
    private boolean hitbox;

    public Settings(Wrap wrap) {
        super(wrap, "resource/backgrounds/menu.jpg");
        this.fullscreen = wrap.isFullscreen();
        this.debug = wrap.isDebug();
        this.hitbox = wrap.isHitboxes();
    }

    protected void setupButtons() {
        int buttonSize = 300;
        int centerX = 1920 / 2 - buttonSize / 2;
        this.buttons.put("fullscreen", new Button(this.wrap, "FULLSCREEN", this.buttonOn(this.wrap.isFullscreen()), centerX, 700, buttonSize));
        this.buttons.put("debug", new Button(this.wrap, "DEBUG", this.buttonOn(this.wrap.isDebug()), centerX, 775, buttonSize));
        this.buttons.put("hitbox", new Button(this.wrap, "HITBOX", this.buttonOn(this.wrap.isHitboxes()), centerX, 850, buttonSize));
        this.buttons.put("apply", new Button(this.wrap, "APPLY", this.buttonOn(true), centerX, 925, buttonSize));
        this.buttons.put("menu", new Button(this.wrap, "BACK", this.buttonOn(false), centerX, 1000, buttonSize));
    }

    protected void buttonClicked(String name) {
        switch (name) {
            case "fullscreen" -> {
                this.fullscreen = !this.fullscreen;
                this.buttons.get("fullscreen").changeImage(buttonOn(this.fullscreen));
            }
            case "debug" -> {
                this.debug = !this.debug;
                this.buttons.get("debug").changeImage(buttonOn(this.debug));
            }
            case "hitbox" -> {
                this.hitbox = !this.hitbox;
                this.buttons.get("hitbox").changeImage(buttonOn(this.hitbox));
            }
            case "apply" -> {
                this.wrap.applySettings(this.fullscreen, this.debug, this.hitbox);
                this.wrap.changeState(GameState.MENU);
            }
            case "menu" -> this.wrap.changeState(GameState.MENU);
        }
    }

    protected void keyPressed(String name) {
        switch (name) {
            case "escape" -> this.wrap.changeState(GameState.MENU);
        }
    }
}