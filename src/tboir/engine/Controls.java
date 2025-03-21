package tboir.engine;

import tboir.enums.Commands;
import tboir.tools.Coords;
import tboir.tools.EditedKey;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Controls implements MouseListener, KeyListener, MouseMotionListener {

    private final Wrap wrap;

    public static final String DEFAULT_KEY_PATH = "resource/defaultKeyConfig.txt";
    public static final String USER_KEY_PATH = "resource/userKeyConfig.txt";

    private final Commands[] keybinds;

    private final ArrayList<Coords> pressCommands;
    private final ArrayList<Commands> toggleCommands;
    private final double[] mouseCoords;

    private EditedKey editedKey;

    public Controls(Wrap wrap) {
        this.wrap = wrap;
        this.keybinds = new Commands[255];
        this.pressCommands = new ArrayList<>();
        this.toggleCommands = new ArrayList<>();
        this.mouseCoords = new double[2];

        this.reloadConfig();
    }

    public void changeKeybinds(HashMap<Commands, Integer> keybinds) {
        if (!Wrap.replaceFile(USER_KEY_PATH, "user key")) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(USER_KEY_PATH);
            for (int i = 0; i < Commands.numOfEditable(); i++) {
                writer.write(Commands.getCommand(i) + " " + keybinds.get(Commands.getCommand(i)) + "\n");
            }
            writer.close();
            System.out.println("New config saved");
            this.reloadConfig();
        } catch (IOException e) {
            System.out.println("Error when writing to a file");
        }
    }

    public void resetToDefault() {
        Wrap.removeFile(USER_KEY_PATH, "user path");
        this.reloadConfig();
    }

    private void reloadConfig() {
        boolean useDefaultKeyConfig = true;
        if (this.hasUserConfig()) {
            if (this.loadKeyConfig(USER_KEY_PATH)) {
                useDefaultKeyConfig = false;
            }
        }
        if (useDefaultKeyConfig) {
            System.out.println("User keyconfig not present, proceeding with default keyconfig");
            if (!this.loadKeyConfig(DEFAULT_KEY_PATH)) {
                System.out.println("Couldn't load default key config");
            }
        }
    }

    private boolean hasUserConfig() {
        File file = new File(USER_KEY_PATH);
        return file.exists();
    }

    private boolean loadKeyConfig(String path) {
        boolean isSuccess = false;
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            Arrays.fill(this.keybinds, null);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] input = line.split(" ", 2);
                if (input.length != 2) {
                    System.out.println("Corrupted keyConfig file!");
                    return isSuccess;
                }

                if (Arrays.asList(Commands.values()).contains(Commands.valueOf(input[0]))) {
                    this.keybinds[Integer.parseInt(input[1])] = Commands.valueOf(input[0]);
                }
            }
            reader.close();
            isSuccess = true;
        } catch (FileNotFoundException e) {

        }
        return isSuccess;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.editedKey != null) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                this.editedKey.keybinds().restoreButton();
                this.editedKey = null;
                return;
            }
            this.editedKey.keybinds().updateKeybinds(this.editedKey.name(), e.getKeyCode());
            this.editedKey = null;
        }
        if (this.keybinds[e.getKeyCode()] != null) {
            this.addToggleCommand(this.keybinds[e.getKeyCode()]);
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.addPressCommand(Commands.menu, 0, 0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (this.editedKey != null) {
            return;
        }
        if (this.keybinds[e.getKeyCode()] != null) {
            this.removeToggleCommand(this.keybinds[e.getKeyCode()]);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.editedKey != null) {
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            this.addPressCommand(Commands.interact, (int)((double)e.getX() / this.wrap.getScale()), (int)((double)e.getY() / this.wrap.getScale()));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseCoords[0] = e.getX() / this.wrap.getScale();
        this.mouseCoords[1] = e.getY() / this.wrap.getScale();
    }

    private void addToggleCommand(Commands toggleCommand) {
        if (!this.toggleCommands.contains(toggleCommand)) {
            this.toggleCommands.add(toggleCommand);
        }
    }

    private void removeToggleCommand(Commands toggleCommand) {
        this.toggleCommands.remove(toggleCommand);
    }

    private void addPressCommand(Commands pressCommand, int x, int y) {
        this.pressCommands.add(new Coords(x, y, pressCommand));
    }

    public void removePressCommand(Coords pressCommand) {
        this.pressCommands.remove(pressCommand);
    }

    public void newEditedKey(EditedKey editedKey) {
        this.editedKey = editedKey;
    }

    // Getters
    public ArrayList<Coords> getPressCommands() {
        return this.pressCommands;
    }

    public ArrayList<Commands> getToggleCommands() {
        return this.toggleCommands;
    }

    public double[] getMouseCoords() {
        return this.mouseCoords;
    }

    public Commands[] getKeybinds() {
        return this.keybinds;
    }

    // Unused
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
