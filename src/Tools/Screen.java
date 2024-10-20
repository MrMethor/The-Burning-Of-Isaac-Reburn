package Tools;

import java.awt.Toolkit;

public class Screen {

    // You can change these
    //#############################################
    public static final boolean FULLSCREEN = true;
    public static final boolean FPS = true; // FPS counter showed
    private static int desiredWidth = 1920; // what resolution should the game be, in case fullscreen is off
    public static final int DESIRED_UPS = 60; // how quickly the time passes 60 is normal
    //###############################################

    private static int desiredHeight = desiredWidth * 9 / 16;
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    private static double scale;

    public Screen() {
        int maxWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int maxHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        if (FULLSCREEN || desiredWidth >= maxWidth) {
            desiredWidth = maxWidth;
            desiredHeight = maxHeight;
        }
        else if (desiredWidth < 300) {
            desiredWidth = 300;
            desiredHeight = desiredWidth * 9 / 16;
        }
        scale = desiredWidth /(double)WIDTH;
    }

    public static int getWidth() {
        return desiredWidth;
    }

    public static int getHeight() {
        return desiredHeight;
    }

    public static double getScale() {
        return scale;
    }
}
