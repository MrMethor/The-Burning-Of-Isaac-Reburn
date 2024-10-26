package Engine;

import java.awt.Toolkit;

public class Screen {

    public final int WIDTH = 1920;
    private final int maxWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private boolean fullscreen;
    private boolean debug;
    private int desiredWidth;
    private int desiredHeight;
    private double scale;

    public Screen(boolean fullscreen, boolean debug, int width) {
        this.fullscreen = fullscreen;
        this.debug = debug;
        desiredWidth = width == 0 ? maxWidth / 2 : width;

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

    public boolean isDebug() {
        return debug;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fc) {
        if (fullscreen && !fc) {
            desiredWidth = maxWidth / 2;
            desiredHeight = desiredWidth * 9 / 16;
            scale = desiredWidth /(double)WIDTH;
        }
        else if (!fullscreen && fc) {
            desiredWidth = maxWidth;
            desiredHeight = desiredWidth * 9 / 16;
            scale = desiredWidth /(double)WIDTH;
        }
        fullscreen = fc;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
