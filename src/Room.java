import Engine.Wrap;
import Tools.Image;

import java.awt.Graphics;

public class Room {

    private final Wrap wrap;
    private Tools.Image image;

    public Room(Wrap wrap) {
        this.wrap = wrap;
        image = new Image(wrap, "resource/room.jpg", 0, 0, 1920, 1080);
    }

    public void render(Graphics g) {
        image.draw(g);
    }
}
