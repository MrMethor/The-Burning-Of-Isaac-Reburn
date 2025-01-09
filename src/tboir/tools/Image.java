package tboir.tools;

import tboir.engine.Wrap;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {

    protected final Wrap wrap;
    protected BufferedImage image;
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
        this.x = (int) (x * wrap.getScale());
        this.y = (int) (y * wrap.getScale());
        this.width = (int) (width * wrap.getScale());
        this.height = (int) (height * wrap.getScale());
    }

    public Image(Wrap wrap, String path, double x, double y) {
        this.wrap = wrap;
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file");
        }
        this.x = (int) (x * wrap.getScale());
        this.y = (int) (y * wrap.getScale());
        this.width = (int)((double)image.getWidth() * wrap.getScale());
        this.height = (int)((double)image.getHeight() * wrap.getScale());
    }

    public Image(Wrap wrap, BufferedImage image, double x, double y, double width, double height) {
        this.wrap = wrap;
        this.image = image;
        this.x = (int) (x * wrap.getScale());
        this.y = (int) (y * wrap.getScale());
        this.width = (int) (width * wrap.getScale());
        this.height = (int) (height * wrap.getScale());
    }

    public Image(Wrap wrap, BufferedImage image, double x, double y) {
        this.wrap = wrap;
        this.image = image;
        this.x = (int) (x * wrap.getScale());
        this.y = (int) (y * wrap.getScale());
        this.width = (int)((double)image.getWidth() * wrap.getScale());
        this.height = (int)((double)image.getHeight() * wrap.getScale());
    }

    public void changePosition(double x, double y) {
        this.x = (int)(x * this.wrap.getScale());
        this.y = (int)(y * this.wrap.getScale());
    }

    public void changeSize(double width, double height) {
        this.width = (int)(width * this.wrap.getScale());
        this.height = (int)(height * this.wrap.getScale());
    }

    public void changeImage(String path) {
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file");
        }
    }

    public void draw(Graphics g) {
        if (this.image == null) {
            return;
        }
        if (this.width != this.image.getWidth() || this.height != this.image.getHeight()) {
            g.drawImage(this.image, this.x, this.y, this.width, this.height, null);
        } else {
            g.drawImage(this.image, this.x, this.y, null);
        }
    }
}

