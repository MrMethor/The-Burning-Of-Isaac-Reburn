package tboir.tools;

import tboir.engine.AnimationSheet;
import tboir.engine.Wrap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Animation extends Image {

    private int spriteSize;
    private int row;
    private int animationFrame;
    private Integer tmpFrame;

    public Animation(Wrap wrap, AnimationSheet animationSheet, double x, double y, double width, double height) {
        super(wrap, x, y, width, height);
        this.changeImage(animationSheet.getImage());
        this.spriteSize = animationSheet.getSpriteSize();
        this.row = 0;
        this.tmpFrame = null;
    }

    @Override
    protected void drawImage(Graphics2D g, BufferedImage image) {
        g.drawImage(
            image,
            -this.getWidth() / 2,
            -this.getHeight() / 2,
            this.getWidth() / 2,
            this.getHeight() / 2,
            this.spriteSize * (this.tmpFrame != null ? this.tmpFrame : this.animationFrame),
            this.spriteSize * this.row,
            this.spriteSize * ((this.tmpFrame != null ? this.tmpFrame : this.animationFrame) + 1),
            this.spriteSize * (this.row + 1),
            null
        );
    }

    public void changeRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return this.row;
    }

    public void changeAnimationFrame(int frame) {
        this.animationFrame = frame;
        this.tmpFrame = null;
    }

    public int getAnimationFrame() {
        return this.animationFrame;
    }

    public void incrementAndReset(int ceiling) {
        this.animationFrame = (this.animationFrame + 1) % (ceiling + 1);
        this.tmpFrame = null;
    }

    public void addTmp(int tmp) {
        this.tmpFrame = tmp;
    }
}
