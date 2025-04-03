package tboir.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AnimationSheet {

    private BufferedImage animationSheet;
    private final int spriteSize;

    public AnimationSheet(String path, int spriteSize) {
        try {
            this.animationSheet = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file: " + path);
        }
        this.spriteSize = spriteSize;
    }

    public BufferedImage getImage() {
        return this.animationSheet.getSubimage(0, 0, this.animationSheet.getWidth(), this.animationSheet.getHeight());
    }

    public int getSpriteSize() {
        return this.spriteSize;
    }
}