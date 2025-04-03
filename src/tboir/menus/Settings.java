package tboir.menus;

import tboir.engine.Wrap;
import tboir.engine.GameState;

import java.util.HashMap;

public class Settings extends MenuType {

    private final HashMap<String, Boolean> settings;

    public Settings(Wrap wrap) {
        super(wrap, "menu");
        this.settings = new HashMap<>();
        this.settings.put("debug", this.getWrap().isDebug());
        this.settings.put("fullscreen", this.getWrap().isFullscreen());
        this.settings.put("hitbox", this.getWrap().isHitboxes());
    }

    @Override
    protected void setupButtons() {
        int otherButtonSize = 300;
        int settingButtonSize = 400;
        int otherCenterX = this.getCenter(otherButtonSize);
        int settingCenterX = this.getCenter(settingButtonSize);
        this.addButton("keybinds", "KEYBINDS", this.getWrap().isFullscreen(), settingCenterX, 475, settingButtonSize);
        this.addButton("fullscreen", "FULLSCREEN", this.getWrap().isFullscreen(), settingCenterX, 575, settingButtonSize);
        this.addButton("debug", "DEBUG", this.getWrap().isDebug(), settingCenterX, 675, settingButtonSize);
        this.addButton("hitbox", "HITBOX", this.getWrap().isHitboxes(), settingCenterX, 775, settingButtonSize);
        this.addButton("apply", "APPLY", true, otherCenterX + 150, 1000, otherButtonSize);
        this.addButton("keybinds", "KEYBINDS", false, otherCenterX, 900, otherButtonSize);
        this.addButton("menu", "BACK", false, otherCenterX - 150, 1000, otherButtonSize);
    }

    @Override
    protected void buttonClicked(String name) {
        switch (name) {
            case "keybinds" -> this.getWrap().changeState(GameState.KEYBINDS);
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
            case "menu" -> this.getWrap().changeState(GameState.MENU);
        }
    }
}