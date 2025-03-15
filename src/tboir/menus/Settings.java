package tboir.menus;

import tboir.engine.Wrap;
import tboir.enums.GameState;

import java.util.HashMap;

public class Settings extends MenuType {

    private final HashMap<String, Boolean> settings;

    public Settings(Wrap wrap) {
        super(wrap, "resource/backgrounds/menu.jpg");
        this.settings = new HashMap<>();
        this.settings.put("debug", this.getWrap().isDebug());
        this.settings.put("fullscreen", this.getWrap().isFullscreen());
        this.settings.put("hitbox", this.getWrap().isHitboxes());
    }

    @Override
    protected void setupButtons() {
        int buttonSize = 300;
        int centerX = 1920 / 2 - buttonSize / 2;
        this.addButton("fullscreen", "FULLSCREEN", this.getWrap().isFullscreen(), centerX, 700, buttonSize);
        this.addButton("debug", "DEBUG", this.getWrap().isDebug(), centerX, 775, buttonSize);
        this.addButton("hitbox", "HITBOX", this.getWrap().isHitboxes(), centerX, 850, buttonSize);
        this.addButton("apply", "APPLY", true, centerX, 925, buttonSize);
        this.addButton("menu", "BACK", false, centerX, 1000, buttonSize);
    }

    @Override
    protected void buttonClicked(String name) {
        switch (name) {
            case "fullscreen", "debug", "hitbox" -> {
                this.toggleButton(name);
                this.settings.replace(name, !this.settings.get(name));
            }
            case "apply" -> {
                this.getWrap().applySettings(this.settings);
                this.getWrap().changeState(GameState.MENU);
            }
            case "menu" -> this.getWrap().changeState(GameState.MENU);
        }
    }

    @Override
    protected void keyPressed(String name) {
        switch (name) {
            case "escape" -> this.getWrap().changeState(GameState.MENU);
        }
    }
}