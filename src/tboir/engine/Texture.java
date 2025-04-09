package tboir.engine;

import java.awt.image.BufferedImage;

public class Texture {

    private BufferedImage image;

    public Texture(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return this.image.getSubimage(0, 0, this.image.getWidth(), this.image.getHeight());
    }
}
