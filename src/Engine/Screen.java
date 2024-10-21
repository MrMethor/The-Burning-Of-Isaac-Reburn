package Engine;

import java.awt.Toolkit;

public class Screen {

    public final int WIDTH = 1920;
    private boolean fullscreen;
    private boolean fps; // FPS counter showed
    private int desiredWidth; // what resolution should the game be, in case fullscreen is off
    private int desiredHeight;
    private double scale;

    public Screen(boolean fullscreen, boolean fps, int width) {
        this.fullscreen = fullscreen;
        this.fps = fps;
        desiredWidth = width;

        int maxWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        if (fullscreen || desiredWidth >= maxWidth)
            desiredWidth = maxWidth;
        else if (desiredWidth < 300)
            desiredWidth = 300;
        desiredHeight = desiredWidth * 9 / 16;
        scale = desiredWidth /(double)WIDTH;
    }

    public int getWidth() {
        return desiredWidth;
    }

    public int getHeight() {
        return desiredHeight;
    }

    public double getScale() {
        return scale;
    }

    public boolean isFPS() {
        return fps;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }
}
