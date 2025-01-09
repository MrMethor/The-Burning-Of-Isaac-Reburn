package tboir.engine;

import tboir.enums.Actions;
import tboir.enums.GameState;
import tboir.tools.Coords;
import tboir.tools.ItemTemplate;
import tboir.tools.TextBox;

import java.awt.Cursor;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;

public class Wrap {

    private GameState newState;
    private GameState gameState;
    private boolean[] newSettings;
    private final Screen screen;
    private final Controls controls;
    private final Counter counter;
    private final Interpolation interpolation;
    private boolean hitboxes;
    private int entityCount;
    private Cursor cursor;
    private ItemTemplate[] itemRegistry;

    private final ArrayList<TextBox> debug;

    public Wrap(String path) {
        this.debug = new ArrayList<>();
        boolean fullscreen = false;
        boolean debug = false;
        int width = 0;
        int ups = 60;
        try {
            File file = new File(path);
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

        this.newSettings = null;
        this.newState = GameState.MENU;
        this.gameState = GameState.MENU;
        this.counter = new Counter(ups);
        this.screen = new Screen(fullscreen, debug, width);
        this.controls = new Controls(this);
        this.interpolation = new Interpolation();
        this.entityCount = 0;

        this.setupDebug();

        try {
            File file = new File("resource/items/itemRegistry.txt");
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
            while (itemReader.hasNextInt()) {
                int id = itemReader.nextInt();
                String itemPath = itemReader.next();
                int redHearts = itemReader.nextInt();
                int redContainers = itemReader.nextInt();
                int soulHearts = itemReader.nextInt();
                double damage = itemReader.nextDouble();
                double range = itemReader.nextDouble();
                double shotSpeed = itemReader.nextDouble();
                double fireSpeed = itemReader.nextDouble();
                double shotSize = itemReader.nextDouble();
                double speed = itemReader.nextDouble();
                int size = itemReader.nextInt();
                boolean special = itemReader.nextInt() == 1;
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

    public boolean updateSettings() {
        if (this.newSettings != null) {
            boolean updateMenu = this.isFullscreen() != this.newSettings[0];
            this.screen.setFullscreen(this.newSettings[0]);
            this.screen.setDebug(this.newSettings[1]);
            this.hitboxes = this.newSettings[2];
            this.newSettings = null;
            this.setupDebug();
            return updateMenu;
        }
        return false;
    }

    public void applySettings(boolean fullscreen, boolean debug, boolean hitbox) {
        this.newSettings = new boolean[]{fullscreen, debug, hitbox};
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

    public ArrayList<Coords> getCommands() {
        return this.controls.getCommands();
    }

    public ArrayList<Actions> getActions() {
        return this.controls.getActions();
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
