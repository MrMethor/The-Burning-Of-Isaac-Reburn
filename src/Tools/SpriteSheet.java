package Tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteSheet {

    private BufferedImage image;
    private int spriteWidth;
    private int spriteHeight;

    public SpriteSheet(String path, int spriteNumColumn, int spriteNumRow) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file");
        }
        spriteWidth = image.getWidth() / spriteNumColumn;
        spriteHeight = image.getHeight() / spriteNumRow;
    }

    public BufferedImage getSprite(int column, int row) {
        return image.getSubimage(column * spriteWidth, row * spriteHeight, spriteWidth, spriteHeight);
    }
}
