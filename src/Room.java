import Tools.Image;
import Tools.Interpolation;

import java.awt.Graphics;

public class Room {

    private Tools.Image image = new Image("resource/room.jpg", 0, 0, 1920, 1080);

    public void render(Graphics g, Interpolation interpolation) {
        image.draw(g);
    }
}
