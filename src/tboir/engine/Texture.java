package tboir.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture {

    private BufferedImage image;

    public Texture(String path) {
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't find the file: " + path);
        }
    }

    public BufferedImage getImage() {
        return this.image.getSubimage(0, 0, this.image.getWidth(), this.image.getHeight());
    }
}
