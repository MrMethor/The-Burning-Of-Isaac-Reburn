package Tools;

import Engine.Wrap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteSheet extends Image{

    private BufferedImage spriteSheet;
    private int spriteWidth;
    private int spriteHeight;
    private int currentColumn = 500;
    private int currentRow = 500;

    public SpriteSheet(Wrap wrap, String path, double x, double y, double width, double height, int spriteNumColumn, int spriteNumRow) {
        super(wrap, "resource/spriteSheets/blank.png", x, y);
        try {
            spriteSheet = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file: " + path);
        }
        spriteWidth = spriteSheet.getWidth() / spriteNumColumn;
        spriteHeight = spriteSheet.getHeight() / spriteNumRow;
        changeSize((int)width, (int)height);
    }

    public void swapImage(int column, int row) {
        if (currentColumn == column && currentRow == row)
            return;
        currentColumn = column;
        currentRow = row;
        image = spriteSheet.getSubimage(column * spriteWidth, row * spriteHeight, spriteWidth, spriteHeight);
    }

    public BufferedImage getImage(int column, int row) {
        return spriteSheet.getSubimage(column * spriteWidth, row * spriteHeight, spriteWidth, spriteHeight);
    }
}
