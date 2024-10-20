package Tools;

import Engine.Wrap;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {

    private final Wrap wrap;
    private BufferedImage image;
    private int width;
    private int height;
    private int x;
    private int y;

    public Image(Wrap wrap, String path, int x, int y, int width, int height) {
        this.wrap = wrap;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file");
        }
        this.x = (int) ((double)x * wrap.getScale());
        this.y = (int) ((double)y * wrap.getScale());
        this.width = (int) ((double)width * wrap.getScale());
        this.height = (int) ((double)height * wrap.getScale());
    }

    public Image(Wrap wrap, String path, int x, int y) {
        this.wrap = wrap;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file");
        }
        this.x = (int) ((double)x * wrap.getScale());
        this.y = (int) ((double)y * wrap.getScale());
        this.width = (int)((double)image.getWidth() * wrap.getScale());
        this.height = (int)((double)image.getHeight() * wrap.getScale());
    }

    public Image (Wrap wrap, BufferedImage image, int x, int y, int width, int height) {
        this.wrap = wrap;
        this.image = image;
        this.x = (int) ((double)x * wrap.getScale());
        this.y = (int) ((double)y * wrap.getScale());
        this.width = (int) ((double)width * wrap.getScale());
        this.height = (int) ((double)height * wrap.getScale());
    }

    public Image (Wrap wrap, BufferedImage image, int x, int y) {
        this.wrap = wrap;
        this.image = image;
        this.x = (int) ((double)x * wrap.getScale());
        this.y = (int) ((double)y * wrap.getScale());
        this.width = (int)((double)image.getWidth() * wrap.getScale());
        this.height = (int)((double)image.getHeight() * wrap.getScale());
    }

    public void changePosition(int x, int y) {
        this.x = (int) (x * wrap.getScale());
        this.y = (int) (y * wrap.getScale());
    }

    public void changeImage(String path){
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void changeImage(BufferedImage image){
        this.image = image;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
