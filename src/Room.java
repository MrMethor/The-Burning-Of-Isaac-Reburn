import Engine.Wrap;
import Tools.Hitbox;
import Tools.Image;

import java.awt.Graphics;
import java.util.ArrayList;

public class Room {

    private Image image;
    private final Hitbox hitbox;

    public Room(Wrap wrap, ArrayList<Hitbox> hitboxes) {
        image = new Image(wrap, "resource/room.jpg", 0, 0);
        hitbox = new Hitbox(wrap, 1920.0 / 2, 1080.0 / 2, 1550, 820, false, false);
        hitboxes.add(hitbox);
    }

    public void render(Graphics g) {
        image.draw(g);
    }
}
