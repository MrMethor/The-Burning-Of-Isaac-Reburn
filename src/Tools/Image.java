package Tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {

    private BufferedImage image;
    private int width;
    private int height;
    private int x;
    private int y;

    public Image(String path, int x, int y, int width, int height) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.x = (int) ((double)x * Screen.getScale());
        this.y = (int) ((double)y * Screen.getScale());
        this.width = (int) ((double)width * Screen.getScale());
        this.height = (int) ((double)height * Screen.getScale());
    }

    public void changePosition(int x, int y) {
        this.x = (int) (x * Screen.getScale());
        this.y = (int) (y * Screen.getScale());
    }

    public void changeImage(String path){
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
