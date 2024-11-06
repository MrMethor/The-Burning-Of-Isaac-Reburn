import Engine.Wrap;
import Engine.Component;
import Tools.Hitbox;
import Tools.Image;

import java.awt.Graphics;

public class Room implements Component {

    private final Image image;
    private final Hitbox hitbox;

    public Room(Wrap wrap) {
        image = new Image(wrap, "resource/room.jpg", 0, 0);
        hitbox = new Hitbox(wrap, 1920.0 / 2, 1080.0 / 2, 1550, 820, false, false);
    }

    public void update() {}

    public void render(Graphics g) {
        image.draw(g);
    }

    public Hitbox getHitbox() {
        return hitbox;
    }
}
