package Engine;

import Enums.Actions;
import Enums.GameState;
import Tools.Coords;
import Tools.ItemTemplate;
import Tools.TextBox;

import java.awt.*;
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

    private ArrayList<TextBox> debug = new ArrayList<>();

    public Wrap(String path) {
        boolean fullscreen = false;
        boolean debug = false;
        int width = 0;
        int ups = 60;
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.contains("fullscreen"))
                    fullscreen = line.contains("true");
                else if (line.contains("debug"))
                    debug = line.contains("true");
                else if (line.contains("width"))
                    width = Integer.parseInt(line.replaceAll("\\D", ""));
                else if (line.contains("ups"))
                    ups = Integer.parseInt(line.replaceAll("\\D", ""));
                else if (line.contains("hitbox"))
                    hitboxes = line.contains("true");

            }
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found. Proceeding with default settings.");
        }

        newSettings = null;
        newState = GameState.MENU;
        gameState = GameState.MENU;
        counter = new Counter(ups);
        screen = new Screen(fullscreen, debug, width);
        controls = new Controls(this);
        interpolation = new Interpolation();
        entityCount = 0;

        setupDebug();

        try {
            File file = new File("resource/items/itemRegistry.txt");
            Scanner itemCounter = new Scanner(file);
            itemCounter.nextLine();
            int itemCount = 0;
            while (itemCounter.hasNextLine()) {
                itemCounter.nextLine();
                itemCount++;
            }
            itemRegistry = new ItemTemplate[itemCount];
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
                itemRegistry[id] = new ItemTemplate(itemPath, redHearts, redContainers, soulHearts, damage, range, shotSpeed, fireSpeed, shotSize, speed, size, special);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Item registry not found.");
        }
    }

    private void setupDebug() {
        int numOfRows = 3;
        int size = 20;
        for (int i = 0; i < numOfRows; i++)
            debug.add(new TextBox(this, "", 0, size * (i + 1), Color.WHITE, "Calibri", Font.PLAIN, size));
    }

    public void updateDebug() {
        debug.get(0).changeText("FPS: " + counter.getFPS());
        debug.get(1).changeText("UPS: " + counter.getUPS());
        debug.get(2).changeText("Entity: " + entityCount);
    }

    public void drawDebug(Graphics g) {
        for(TextBox box : debug)
            box.draw(g);
    }

    public boolean updateSettings() {
        if (newSettings != null) {
            boolean updateMenu = isFullscreen() != newSettings[0];
            screen.setFullscreen(newSettings[0]);
            screen.setDebug(newSettings[1]);
            hitboxes = newSettings[2];
            newSettings = null;
            setupDebug();
            return updateMenu;
        }
        return false;
    }

    public void applySettings(boolean fullscreen, boolean debug, boolean hitbox) {
        newSettings = new boolean[]{fullscreen, debug, hitbox};
    }

    public void updateEntityCount(int count) {
        entityCount = count;
    }

    // Interpolation
    public double interpolate(double prev, double curr) {
        return interpolation.interpolate(prev, curr);
    }

    public void setInterpolation(double interpolation) {
        this.interpolation.setInterpolation(interpolation);
    }

    // Counter Increments
    public void addTime(double alpha) {
        counter.addTime(alpha);
    }

    public void addFPS() {
        counter.addFPS();
    }

    public void addUPS() {
        counter.addUPS();
    }

    // GameState Setters
    public void updateGameState(GameState newState) {
        gameState = newState;
    }

    public void changeState(GameState newState) {
        this.newState = newState;
    }

    // Getters
    public int getWidth() {
        return screen.getWidth();
    }

    public int getHeight() {
        return screen.getHeight();
    }

    public Controls getControls() {
        return controls;
    }

    public GameState getGameState() {
        return gameState;
    }

    public GameState getNewState() {
        return newState;
    }

    public double getTimeToElapse() {
        return counter.getTimeToElapse();
    }

    public double getScale() {
        return screen.getScale();
    }

    public ArrayList<Coords> getCommands() {
        return controls.getCommands();
    }

    public ArrayList<Actions> getActions() {
        return controls.getActions();
    }

    public double getMouseX() {
        return controls.getMouseCoords()[0];
    }

    public double getMouseY() { return controls.getMouseCoords()[1]; }

    public boolean isFullscreen() {
        return screen.isFullscreen();
    }

    public boolean isDebug() {
        return screen.isDebug();
    }

    public boolean isHitboxes() {
        return hitboxes;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public ItemTemplate getItemFromRegistry(int id) {
        return itemRegistry[id];
    }

    public int getNumOfItems() {
        return itemRegistry.length;
    }
}
