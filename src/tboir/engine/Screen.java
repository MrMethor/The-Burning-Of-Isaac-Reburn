package tboir.engine;

import java.awt.Toolkit;

public class Screen {

    public static final int WIDTH = 1920;
    private final int maxWidth;
    private boolean fullscreen;
    private boolean debug;
    private int desiredWidth;
    private int desiredHeight;
    private double scale;

    public Screen(boolean fullscreen, boolean debug, int width) {
        this.maxWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        this.fullscreen = fullscreen;
        this.debug = debug;
        this.desiredWidth = width == 0 ? this.maxWidth / 2 : width;

        if (fullscreen || this.desiredWidth >= this.maxWidth) {
            this.desiredWidth = this.maxWidth;
        } else if (this.desiredWidth < 300) {
            this.desiredWidth = 300;
        }
        this.desiredHeight = this.desiredWidth * 9 / 16;
        this.scale = this.desiredWidth / (double)WIDTH;
    }

    public void setFullscreen(boolean fc) {
        if (this.fullscreen && !fc) {
            this.desiredWidth = this.maxWidth / 2;
            this.desiredHeight = this.desiredWidth * 9 / 16;
            this.scale = this.desiredWidth / (double)WIDTH;
        } else if (!this.fullscreen && fc) {
            this.desiredWidth = this.maxWidth;
            this.desiredHeight = this.desiredWidth * 9 / 16;
            this.scale = this.desiredWidth / (double)WIDTH;
        }
        this.fullscreen = fc;
    }

    // Getters
    public int getWidth() {
        return this.desiredWidth;
    }

    public int getHeight() {
        return this.desiredHeight;
    }

    public double getScale() {
        return this.scale;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public boolean isFullscreen() {
        return this.fullscreen;
    }

    // Setter
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
