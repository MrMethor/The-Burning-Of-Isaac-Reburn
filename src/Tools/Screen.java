package Tools;

import java.awt.*;

public class Screen {

    public static final boolean FULLSCREEN = false;
    public static final boolean FPS = true;
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    private static int desiredWidth = 1280;
    private static int desiredHeight = desiredWidth * 9 / 16 ;
    private static double scale;

    public Screen() {
        if (FULLSCREEN) {
            desiredWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            desiredHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
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
