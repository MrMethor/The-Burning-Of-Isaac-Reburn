package Engine;

import Enums.Actions;
import Enums.GameState;
import Tools.Coords;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Wrap {

    private GameState newState;
    private GameState gameState;
    private final Screen screen;
    private final Controls controls;
    private final Counter counter;
    private final Interpolation interpolation;

    public Wrap(String path) {
        boolean fullscreen = false;
        boolean fps = false;
        int width = 1280;
        int ups = 60;
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.contains("fullscreen"))
                    fullscreen = line.contains("true");
                else if (line.contains("fps"))
                    fps = line.contains("true");
                else if (line.contains("width"))
                    width = Integer.parseInt(line.replaceAll("\\D", ""));
                else if (line.contains("ups"))
                    ups = Integer.parseInt(line.replaceAll("\\D", ""));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found. Proceeding with default settings.");
        }

        newState = GameState.MENU;
        gameState = GameState.MENU;
        screen = new Screen(fullscreen, fps, width);
        controls = new Controls(screen.getScale());
        counter = new Counter(ups);
        interpolation = new Interpolation();
    }

    public int getWidth() {
        return screen.getWidth();
    }

    public int getHeight() {
        return screen.getHeight();
    }

    public Controls getControls() {
        return controls;
    }

    public int getTimeToElapse() {
        return counter.getTimeToElapse();
    }

    public void addUPS() {
        counter.addUPS();
    }

    public void addFPS() {
        counter.addFPS();
    }

    public void addTime(long time) {
        counter.addTime(time);
    }

    public void setInterpolation(double interpolation) {
        this.interpolation.setInterpolation(interpolation);
    }

    public GameState getGameState() {
        return gameState;
    }

    public GameState getNewState() {
        return newState;
    }

    public void updateGameState(GameState newState) {
        gameState = newState;
    }

    public void changeState(GameState newState) {
        this.newState = newState;
    }

    public boolean isFPS() {
        return screen.isFPS();
    }

    public int getFPS() {
        return counter.getFPS();
    }

    public int getUPS() {
        return counter.getUPS();
    }

    public ArrayList<Coords> getCommands() {
        return controls.getCommands();
    }

    public ArrayList<Actions> getActions() {
        return controls.getActions();
    }

    public double interpolate(double prev, double curr) {
        return interpolation.interpolate(prev, curr);
    }

    public double getScale() {
        return screen.getScale();
    }

    public boolean isFullscreen() {
        return screen.isFullscreen();
    }
}
