package tboir.tools;

import tboir.engine.Wrap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteSheet extends Image {

    private BufferedImage spriteSheet;
    private final int spriteWidth;
    private final int spriteHeight;
    private int currentColumn = 500;
    private int currentRow = 500;

    public SpriteSheet(Wrap wrap, String path, double x, double y, double width, double height, int spriteNumColumn, int spriteNumRow) {
        super(wrap, "resource/entities/blank.png", x, y);
        try {
            this.spriteSheet = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file: " + path);
        }
        this.spriteWidth = this.spriteSheet.getWidth() / spriteNumColumn;
        this.spriteHeight = this.spriteSheet.getHeight() / spriteNumRow;
        this.changeSize((int)width, (int)height);
    }

    public void swapImage(int column, int row) {
        if (this.currentColumn == column && this.currentRow == row) {
            return;
        }
        this.currentColumn = column;
        this.currentRow = row;
        this.changeImage(this.spriteSheet.getSubimage(column * this.spriteWidth, row * this.spriteHeight, this.spriteWidth, this.spriteHeight));
    }

    public BufferedImage getImage(int column, int row) {
        return this.spriteSheet.getSubimage(column * this.spriteWidth, row * this.spriteHeight, this.spriteWidth, this.spriteHeight);
    }
}
