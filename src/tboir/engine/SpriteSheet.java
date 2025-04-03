package tboir.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteSheet {

    private BufferedImage spriteSheet;
    private final int spriteSize;

    public SpriteSheet(String path, int spriteSize) {
        try {
            this.spriteSheet = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file: " + path);
        }
        this.spriteSize = spriteSize;
    }

    public BufferedImage getImage(int column, int row) {
        return this.spriteSheet.getSubimage(column * this.spriteSize, row * this.spriteSize, this.spriteSize, this.spriteSize);
    }
}
