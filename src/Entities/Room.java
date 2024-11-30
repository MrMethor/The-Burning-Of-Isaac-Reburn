package Entities;

import Engine.Wrap;
import Entities.Dynamic.Physical.Enemies.Fly;
import Entities.Dynamic.Physical.Items.Item;
import Enums.EntityType;

import java.awt.*;
import java.util.ArrayList;

public class Room extends Entity {

    public Room(Wrap wrap, ArrayList<Entity> entities) {
        super(wrap, entities, EntityType.ROOM, "resource/room.jpg", 1920/2.0, 1080/2.0, 1920, 1080, 0.8, 0.75, 0, 0);
        entities.add(new Item(wrap, entities, this));
        entities.add(new Fly(wrap, entities, this, 600, 600));
    }

    public void update() {
        entities.sort((a, b) -> -1 * a.compareTo(b));
        for (int i = 0; i < entities.size(); i++)
            entities.get(i).update();
        entities.removeIf(Entity::isToDestroy);
    }

    public void render(Graphics g) {
        super.render(g);
        for (Entity entity : entities)
            entity.render(g);
    }
}
