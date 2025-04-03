package tboir.tools;

import tboir.engine.Wrap;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Image {

    private final Wrap wrap;
    private BufferedImage image;
    private int rotatedTimes;
    private boolean mirroredHorizontally;
    private boolean mirroredVertically;
    private int x;
    private int y;
    private int width;
    private int height;

    public Image(Wrap wrap, String texture, double x, double y, double width, double height) {
        this.wrap = wrap;
        this.image = this.wrap.getResourceManager().getTexture(texture);
        this.x = (int)x;
        this.y = (int)y;
        this.width = (int)width;
        this.height = (int)height;
    }

    public Image(Wrap wrap, String texture, double x, double y) {
        this.wrap = wrap;
        this.image = this.wrap.getResourceManager().getTexture(texture);
        this.x = (int)x;
        this.y = (int)y;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
    }

    public Image(Wrap wrap, int column, int row, String spriteSheet, double x, double y, double width, double height) {
        this.wrap = wrap;
        this.image = this.wrap.getResourceManager().getSprite(spriteSheet, column, row);
        this.x = (int)x;
        this.y = (int)y;
        this.width = (int)width;
        this.height = (int)height;
    }

    public Image(Wrap wrap, BufferedImage image, double x, double y, double width, double height) {
        this.wrap = wrap;
        this.image = image;
    }

    protected Image(Wrap wrap, double x, double y, double width, double height) {
        this.wrap = wrap;
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

    public void changeImage(String texture) {
        this.image = this.wrap.getResourceManager().getTexture(texture);
    }

    public void changeImage(BufferedImage image) {
        this.image = image;
    }

    public void changeImage(String sprite, int column, int row) {
        this.image = this.wrap.getResourceManager().getSprite(sprite, column, row);
    }

    public void draw(Graphics2D g) {
        if (this.image == null) {
            return;
        }

        AffineTransform oldTransform = g.getTransform();

        double centerX = this.x + this.width / 2.0;
        double centerY = this.y + this.height / 2.0;
        g.translate(centerX, centerY);
        g.rotate(Math.toRadians(this.rotatedTimes * 90));
        if (this.mirroredHorizontally) {
            g.scale(-1, 1);
        }
        if (this.mirroredVertically) {
            g.scale(1, -1);
        }
        this.drawImage(g, this.image);

        g.setTransform(oldTransform);
    }

    protected void drawImage(Graphics2D g, BufferedImage image) {
        g.drawImage(image, -this.width / 2, -this.height / 2, this.width, this.height, null);
    }

    protected int getWidth() {
        return this.width;
    }

    protected int getHeight() {
        return this.height;
    }

    public void rotate(int times) {
        this.rotatedTimes = (this.rotatedTimes + times) % 4;
    }

    public void resetRotation() {
        this.rotatedTimes = 0;
    }

    public void mirrorHorizontally(boolean mirror) {
        this.mirroredHorizontally = mirror;
    }

    public void mirrorVertically(boolean mirror) {
        this.mirroredVertically = mirror;
    }
}