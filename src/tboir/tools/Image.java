package tboir.tools;

import tboir.engine.Wrap;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {

    private final Wrap wrap;
    private BufferedImage image;
    private int x;
    private int y;
    private int width;
    private int height;

    public Image(Wrap wrap, String path, double x, double y, double width, double height) {
        this.wrap = wrap;
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file " + path);
        }
        this.x = (int)x;
        this.y = (int)y;
        this.width = (int)width;
        this.height = (int)height;
    }

    public Image(Wrap wrap, String path, double x, double y) {
        this.wrap = wrap;
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file");
        }
        this.x = (int)x;
        this.y = (int)y;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
    }

    public Image(Wrap wrap, BufferedImage image, double x, double y, double width, double height) {
        this.wrap = wrap;
        this.image = image;
        this.x = (int)x;
        this.y = (int)y;
        this.width = (int)width;
        this.height = (int)height;
    }

    public void changePosition(double x, double y) {
        this.x = (int)x;
        this.y = (int)y;
    }

    public void changeSize(double width, double height) {
        this.width = (int)width;
        this.height = (int)height;
    }

    public void changeImage(String path) {
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file");
        }
    }

    public void changeImage(BufferedImage image) {
        this.image = image;
    }

    public void draw(Graphics g) {
        if (this.image == null) {
            return;
        }
        g.drawImage(this.image, (int)(this.x * this.wrap.getScale()), (int)(this.y * this.wrap.getScale()), (int)(this.width * this.wrap.getScale()), (int)(this.height * this.wrap.getScale()), null);
    }
}

