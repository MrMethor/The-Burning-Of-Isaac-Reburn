import Engine.Wrap;
import Entities.Entity;

public class Room extends Entity {

    public Room(Wrap wrap) {
        super(wrap, false, false);
        changeTexture("resource/room.jpg");
        changeSize(1920, 1080, 0.8, 0.75);
    }
}
