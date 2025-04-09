package tboir.engine;

import java.awt.image.BufferedImage;

public class AnimationSheet {

    private BufferedImage animationSheet;
    private final int spriteSize;

    public AnimationSheet(BufferedImage image, int spriteSize) {
        this.animationSheet = image;
        this.spriteSize = spriteSize;
    }

    public BufferedImage getImage() {
        return this.animationSheet.getSubimage(0, 0, this.animationSheet.getWidth(), this.animationSheet.getHeight());
    }

    public int getSpriteSize() {
        return this.spriteSize;
    }
}