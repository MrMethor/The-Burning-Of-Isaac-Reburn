package tboir.engine;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage spriteSheet;
    private final int spriteSize;

    public SpriteSheet(BufferedImage image, int spriteSize) {
        this.spriteSheet = image;
        this.spriteSize = spriteSize;
    }

    public BufferedImage getImage(int column, int row) {
        return this.spriteSheet.getSubimage(column * this.spriteSize, row * this.spriteSize, this.spriteSize, this.spriteSize);
    }
}
