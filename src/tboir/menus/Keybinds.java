package tboir.menus;

import tboir.engine.Wrap;
import tboir.enums.Commands;
import tboir.enums.GameState;
import tboir.tools.EditedKey;

import java.util.HashMap;

public class Keybinds extends MenuType {

    private Commands[] currentKeybinds;
    private HashMap<Commands, Character> keybinds;

    private String prevKeyName;
    private String prevKeyLabel;

    public Keybinds(Wrap wrap) {
        super(wrap, "resource/backgrounds/menu.jpg");
    }

    @Override
    protected void setupButtons() {
        this.loadKeybindings();
        int otherButtonSize = 300;
        int buttonSize = 300;
        int otherCenterX = this.getCenter(otherButtonSize);
        int nameButtonX = this.getCenter(buttonSize) - buttonSize / 2;
        int keyButtonX = this.getCenter(buttonSize) + buttonSize / 2;
        int buttonY = 275;
        for (int i = 0; i < Commands.values().length; i++) {
            if (Commands.values()[i].isEditable()) {
                this.addGhostButton(String.valueOf(i), Commands.values()[i].getName(), true, nameButtonX, buttonY + i * buttonSize / 4, buttonSize);
                this.addButton(Commands.values()[i].toString(), String.valueOf(this.keybinds.get(Commands.values()[i])), false, keyButtonX, buttonY + i * buttonSize / 4, buttonSize);
            }
        }
        this.addButton("apply", "APPLY", true, otherCenterX + 150, 1000, otherButtonSize);
        this.addButton("restore", "RESTORE", false, otherCenterX, 900, otherButtonSize);
        this.addButton("menu", "BACK", false, otherCenterX - 150, 1000, otherButtonSize);
    }

    @Override
    protected void buttonClicked(String name) {
        switch (name) {
            case "apply" -> {
                // apply keybindings
                this.getWrap().changeState(GameState.SETTINGS);
            }
            case "menu" -> this.getWrap().changeState(GameState.SETTINGS);
            default -> {
                for (int i = 0; i < 255; i++) {
                    if (this.currentKeybinds[i] != null && this.currentKeybinds[i].toString().equals(name)) {
                        this.getWrap().getControls().newEditedKey(new EditedKey(name, this));
                        this.prevKeyName = name;
                        this.prevKeyLabel = this.getButton(name).getLabel();
                        this.getButton(name).changeLabel("");
                    }
                }
            }
        }
    }

    @Override
    protected void keyPressed(String name) {
        switch (name) {
            case "menu" -> this.getWrap().changeState(GameState.SETTINGS);
        }
    }

    private void loadKeybindings() {
        this.keybinds = new HashMap<>();
        this.currentKeybinds = this.getWrap().getControls().getKeybinds();
        for (Commands command : Commands.values()) {
            if (command.isEditable()) {
                this.addKeybind(command);
            }
        }
    }

    private void addKeybind(Commands command) {
        this.keybinds.put(command, (char)this.getCharByCommand(command));
    }

    private int getCharByCommand(Commands commandToFind) {
        for (int i = 0; i < this.currentKeybinds.length; i++) {
            if (this.currentKeybinds[i] == commandToFind) {
                return i;
            }
        }
        return 0;
    }

    public void restoreButton() {
        this.getButton(this.prevKeyName).changeLabel(this.prevKeyLabel);
    }

    public void updateKeybinds(String name, int e) {
        this.keybinds.replace(Commands.valueOf(name), (char)e);
        this.getButton(name).changeLabel(String.valueOf((char)e));
    }
}
