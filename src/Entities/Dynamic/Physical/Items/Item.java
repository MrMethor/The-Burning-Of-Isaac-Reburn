package Entities.Dynamic.Physical.Items;

import Engine.Wrap;
import Entities.Dynamic.Physical.PhysicalEntity;
import Map.Room;
import Enums.EntityType;
import Tools.Collision;
import Tools.EntityList;

public class Item extends PhysicalEntity {

    public Item(Wrap wrap, EntityList entities, Room room) {
        super(wrap, entities, room, EntityType.ITEM, "resource/blank.png", 300, 300, 50, 50, 1, 1, 0, 0);
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entityType()) {
            case PLAYER -> applyRelativeCollision(collision);
        }
    }
}
