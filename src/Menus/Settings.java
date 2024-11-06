package Menus;

import Engine.Wrap;
import Enums.GameState;
import Tools.Button;

public class Settings extends MenuType {

    private boolean fullscreen;
    private boolean debug;
    private boolean hitbox;

    public Settings(Wrap wrap) {
        super(wrap, "resource/menu.jpg");
        fullscreen = wrap.isFullscreen();
        debug = wrap.isDebug();
        hitbox = wrap.isHitboxes();
    }

    @Override
    protected void setupButtons() {
        int buttonSize = 300;
        int centerX = 1920 / 2 - buttonSize / 2;
        buttons.put("fullscreen", new Button(wrap, "FULLSCREEN", buttonOn(wrap.isFullscreen()), centerX, 700, buttonSize));
        buttons.put("debug", new Button(wrap, "DEBUG", buttonOn(wrap.isDebug()), centerX, 775, buttonSize));
        buttons.put("hitbox", new Button(wrap, "HITBOX", buttonOn(wrap.isHitboxes()), centerX, 850, buttonSize));
        buttons.put("apply", new Button(wrap, "APPLY", buttonOn(true), centerX, 925, buttonSize));
        buttons.put("menu", new Button(wrap, "BACK", buttonOn(false), centerX, 1000, buttonSize));
    }

    @Override
    protected void buttonClicked(String name) {
        switch (name) {
            case "fullscreen" -> {
                fullscreen = !fullscreen;
                buttons.get("fullscreen").changeImage(buttonOn(fullscreen));
            }
            case "debug" -> {
                debug = !debug;
                buttons.get("debug").changeImage(buttonOn(debug));
            }
            case "hitbox" -> {
                hitbox = !hitbox;
                buttons.get("hitbox").changeImage(buttonOn(hitbox));
            }
            case "apply" -> {
                wrap.applySettings(fullscreen, debug, hitbox);
                wrap.changeState(GameState.MENU);
            }
            case "menu" -> wrap.changeState(GameState.MENU);
        }
    }

    @Override
    protected void keyPressed(String name) {
        switch (name) {
            case "escape" -> wrap.changeState(GameState.MENU);
        }
    }
}