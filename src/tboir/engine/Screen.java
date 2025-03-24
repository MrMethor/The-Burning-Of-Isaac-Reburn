package tboir.engine;

import java.awt.Toolkit;

public class Screen {

    public static final int LOGICAL_WIDTH = 1920;
    private final int windowedWidth;
    private final int maxWidth;
    private boolean fullscreen;
    private boolean debug;

    public Screen(boolean fullscreen, boolean debug, int width) {
        this.maxWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        this.fullscreen = fullscreen;
        this.debug = debug;
        this.windowedWidth = width <= 300 || width >= this.maxWidth ? this.maxWidth / 2 : width;
    }

    public void setFullscreen(boolean fc) {
        this.fullscreen = fc;
    }

    public void toggleFullscreen() {
        this.fullscreen = !this.fullscreen;
    }

    // Getters
    public int getWidth() {
        return this.fullscreen ? this.maxWidth : this.windowedWidth;
    }

    public int getWindowedWidth() {
        return this.windowedWidth;
    }

    public int getHeight() {
        return this.fullscreen ? this.maxWidth * 9 / 16 : this.windowedWidth * 9 / 16;
    }

    public double getScale() {
        return this.fullscreen ? this.maxWidth / (double)LOGICAL_WIDTH : this.windowedWidth / (double)LOGICAL_WIDTH;
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
