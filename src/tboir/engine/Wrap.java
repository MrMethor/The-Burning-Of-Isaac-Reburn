package tboir.engine;

import tboir.tools.Coords;
import tboir.tools.ItemTemplate;
import tboir.tools.TextBox;

import java.awt.Cursor;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.Map;
import java.util.Iterator;

public class Wrap {

    public static final int CENTER_X = 1920 / 2;
    public static final int CENTER_Y = 1080 / 2;
    public static final String DEFAULT_SETTINGS_PATH = "resource/config.txt";

    private GameState newState;
    private GameState gameState;
    private boolean toggleFullscreen;
    private HashMap<String, Boolean> newSettings;
    private Screen screen;
    private final Controls controls;
    private Counter counter;
    private final Interpolation interpolation;
    private boolean hitboxes;
    private int entityCount;
    private Cursor cursor;
    private ItemTemplate[] itemRegistry;

    private final ArrayList<TextBox> debug;

    public Wrap() {
        this.toggleFullscreen = false;
        this.debug = new ArrayList<>();
        this.controls = new Controls(this);
        this.interpolation = new Interpolation();
        this.newSettings = null;
        this.newState = GameState.MENU;
        this.gameState = GameState.MENU;
        this.entityCount = 0;

        int defaultWidth = 0;
        int defaultUps = 60;
        boolean defaultFullscreen = false;
        boolean defaultDebug = false;
        this.loadConfigFile(defaultFullscreen, defaultDebug, defaultWidth, defaultUps);

        this.setupDebug();

        this.loadItemRegistry();
    }

    private void loadConfigFile(boolean fullscreen, boolean debug, int width, int ups) {
        try {
            File file = new File(DEFAULT_SETTINGS_PATH);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.contains("fullscreen")) {
                    fullscreen = line.contains("true");
                } else if (line.contains("debug")) {
                    debug = line.contains("true");
                } else if (line.contains("width")) {
                    width = Integer.parseInt(line.replaceAll("\\D", ""));
                } else if (line.contains("ups")) {
                    ups = Integer.parseInt(line.replaceAll("\\D", ""));
                } else if (line.contains("hitbox")) {
                    this.hitboxes = line.contains("true");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found. Proceeding with default settings.");
        }

        this.screen = new Screen(fullscreen, debug, width);
        this.counter = new Counter(ups);
    }

    private void loadItemRegistry() {
        try {
            File file = new File("resource/items/itemRegistry.csv");
            Scanner itemCounter = new Scanner(file);
            itemCounter.nextLine();
            int itemCount = 0;
            while (itemCounter.hasNextLine()) {
                itemCounter.nextLine();
                itemCount++;
            }
            this.itemRegistry = new ItemTemplate[itemCount];
            Scanner itemReader = new Scanner(file);
            itemReader.useLocale(Locale.US);
            itemReader.nextLine();
            while (itemReader.hasNextLine()) {
                String[] newItem = itemReader.nextLine().split(",", -1);
                for (int i = 0; i < newItem.length; i++) {
                    newItem[i] = newItem[i].isEmpty() ? "0" : newItem[i];
                }
                int id = Integer.parseInt(newItem[0]);
                String itemPath = newItem[1];
                int redHearts = Integer.parseInt(newItem[2]);
                int redContainers = Integer.parseInt(newItem[3]);
                int soulHearts = Integer.parseInt(newItem[4]);
                double damage = Double.parseDouble(newItem[5]);
                double range = Double.parseDouble(newItem[6]);
                double shotSpeed = Double.parseDouble(newItem[7]);
                double fireSpeed = Double.parseDouble(newItem[8]);
                double shotSize = Double.parseDouble(newItem[9]);
                double speed = Double.parseDouble(newItem[10]);
                int size = Integer.parseInt(newItem[11]);
                boolean special = Integer.parseInt(newItem[12]) == 1;
                this.itemRegistry[id] = new ItemTemplate(itemPath, redHearts, redContainers, soulHearts, damage, range, shotSpeed, fireSpeed, shotSize, speed, size, special);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Item registry not found.");
        }
    }

    private void setupDebug() {
        int numOfRows = 3;
        int size = 20;
        for (int i = 0; i < numOfRows; i++) {
            this.debug.add(new TextBox(this, "", 0, size * (i + 1), Color.WHITE, "Calibri", Font.PLAIN, size));
        }
    }

    public void updateDebug() {
        this.debug.get(0).changeText("FPS: " + this.counter.getFPS());
        this.debug.get(1).changeText("UPS: " + this.counter.getUPS());
        this.debug.get(2).changeText("Entity: " + this.entityCount);
    }

    public void drawDebug(Graphics g) {
        for (TextBox box : this.debug) {
            box.draw(g);
        }
    }

    public void applySettings(HashMap<String, Boolean> newSettings) {
        this.newSettings = newSettings;
    }

    public boolean updateSettings() {
        if (this.newSettings != null) {
            boolean updateMenu = this.isFullscreen() != this.newSettings.get("fullscreen");
            this.screen.setFullscreen(this.newSettings.get("fullscreen"));
            this.screen.setDebug(this.newSettings.get("debug"));
            this.hitboxes = this.newSettings.get("hitbox");
            this.saveNewSettings();
            this.newSettings = null;
            this.setupDebug();
            return updateMenu;
        }
        return false;
    }

    public boolean toggleFullscreen() {
        if (this.toggleFullscreen) {
            this.screen.toggleFullscreen();
            this.toggleFullscreen = false;
            return true;
        } else {
            return false;
        }

    }

    public void isToToggleFullscreen() {
        this.toggleFullscreen = true;
    }

    public static boolean removeFile(String path, String name) {
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Old " + name + " file removed");
            } else {
                System.out.println("Couldn't remove " + name + "  file, something went wrong");
                return false;
            }
        }
        return true;
    }

    public static boolean replaceFile(String path, String name) {
        if (!removeFile(path, name)) {
            System.out.println("Something bad happened while creating " + name);
            return false;
        }
        try {
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("New " + name + " created");
            } else {
                System.out.println("Couldn't create new " + name + " file");
                return false;
            }
        } catch (IOException e) {
            System.out.println("Something bad happened while creating " + name);
            return false;
        }
        return true;
    }

    private void saveNewSettings() {
        if (!replaceFile(DEFAULT_SETTINGS_PATH, "config")) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(DEFAULT_SETTINGS_PATH);

            writer.write("width " + this.screen.getWindowedWidth() + "\n");
            writer.write("ups " + this.counter.getDesiredUPS() + "\n");

            Iterator<Map.Entry<String, Boolean>> it = this.newSettings.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Boolean> pair = it.next();
                writer.write(pair.getKey() + " " + pair.getValue() + "\n");
                it.remove();
            }

            writer.close();
            System.out.println("New config saved");
        } catch (IOException e) {
            System.out.println("Error when writing to a file");
        }
    }

    public void updateEntityCount(int count) {
        this.entityCount = count;
    }

    // Interpolation
    public double interpolate(double prev, double curr) {
        return this.interpolation.interpolate(prev, curr);
    }

    public void setInterpolation(double interpolation) {
        this.interpolation.setInterpolation(interpolation);
    }

    // Counter Increments
    public void addTime(double alpha) {
        this.counter.addTime(alpha);
    }

    public void addFPS() {
        this.counter.addFPS();
    }

    public void addUPS() {
        this.counter.addUPS();
    }

    // GameState Setters
    public void updateGameState(GameState newState) {
        this.gameState = newState;
    }

    public void changeState(GameState newState) {
        this.newState = newState;
    }

    // Getters
    public int getWidth() {
        return this.screen.getWidth();
    }

    public int getHeight() {
        return this.screen.getHeight();
    }

    public Controls getControls() {
        return this.controls;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public GameState getNewState() {
        return this.newState;
    }

    public double getTimeToElapse() {
        return this.counter.getTimeToElapse();
    }

    public double getScale() {
        return this.screen.getScale();
    }

    public ArrayList<Coords> getPressCommands() {
        return this.controls.getPressCommands();
    }

    public ArrayList<Commands> getToggleCommands() {
        return this.controls.getToggleCommands();
    }

    public double getMouseX() {
        return this.controls.getMouseCoords()[0];
    }

    public double getMouseY() {
        return this.controls.getMouseCoords()[1];
    }

    public boolean isFullscreen() {
        return this.screen.isFullscreen();
    }

    public boolean isDebug() {
        return this.screen.isDebug();
    }

    public boolean isHitboxes() {
        return this.hitboxes;
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public ItemTemplate getItemFromRegistry(int id) {
        return this.itemRegistry[id];
    }

    public int getNumOfItems() {
        return this.itemRegistry.length;
    }
}
