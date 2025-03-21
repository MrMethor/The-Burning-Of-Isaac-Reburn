package tboir.menus;

import tboir.engine.Wrap;
import tboir.enums.Commands;
import tboir.enums.GameState;
import tboir.tools.EditedKey;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Keybinds extends MenuType {

    private HashMap<Commands, Integer> keybinds;

    private String prevKeyName;
    private String prevKeyLabel;

    public Keybinds(Wrap wrap) {
        super(wrap, "resource/backgrounds/menu.jpg");
    }

    @Override
    protected void setupButtons() {
        this.loadKeybindings();
        int buttonSize = 300;
        int otherCenterX = this.getCenter(buttonSize);
        int nameButtonX = this.getCenter(buttonSize) - buttonSize / 2;
        int keyButtonX = this.getCenter(buttonSize) + buttonSize / 2;
        int buttonY = 275;
        for (int i = 0; i < Commands.numOfEditable(); i++) {
            this.addGhostButton(String.valueOf(i), Commands.getCommand(i).getName(), true, nameButtonX, buttonY + i * buttonSize / 4, buttonSize);
            String keyName = KeyEvent.getKeyText((this.keybinds.get(Commands.getCommand(i))));
            this.addButton(Commands.getCommand(i).toString(), keyName, false, keyButtonX, buttonY + i * buttonSize / 4, buttonSize);
        }
        this.addButton("apply", "APPLY", true, otherCenterX + 150, 1000, buttonSize);
        this.addButton("restore", "RESTORE", false, otherCenterX, 900, buttonSize);
        this.addButton("menu", "BACK", false, otherCenterX - 150, 1000, buttonSize);
    }

    @Override
    protected void buttonClicked(String name) {
        switch (name) {
            case "apply" -> {
                this.getWrap().getControls().changeKeybinds(this.keybinds);
                this.getWrap().changeState(GameState.SETTINGS);
            }
            case "menu" -> this.getWrap().changeState(GameState.SETTINGS);
            case "restore" -> {
                this.getWrap().getControls().resetToDefault();
                this.getWrap().changeState(GameState.SETTINGS);
            }
            default -> {
                for (int i = 0; i < Commands.numOfEditable(); i++) {
                    if (name.equals(Commands.getCommand(i).toString())) {
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
        for (int i = 0; i < Commands.numOfEditable(); i++) {
            this.keybinds.put(Commands.getCommand(i), this.getCharByCommand(Commands.getCommand(i)));
        }
    }

    private int getCharByCommand(Commands commandToFind) {
        for (int i = 0; i < this.getWrap().getControls().getKeybinds().length; i++) {
            if (this.getWrap().getControls().getKeybinds()[i] == commandToFind) {
                return i;
            }
        }
        return 0;
    }

    public void restoreButton() {
        this.getButton(this.prevKeyName).changeLabel(this.prevKeyLabel);
    }

    public void updateKeybinds(String name, int e) {
        if (e != 0) {
            for (int i = 0; i < Commands.numOfEditable(); i++) {
                if (this.keybinds.get(Commands.getCommand(i)) == e) {
                    this.updateKeybinds(Commands.getCommand(i).toString(), 0);
                }
            }
        }
        this.keybinds.replace(Commands.valueOf(name), e);
        this.getButton(name).changeLabel(e == 0 ? "" : KeyEvent.getKeyText(e));
    }
}
