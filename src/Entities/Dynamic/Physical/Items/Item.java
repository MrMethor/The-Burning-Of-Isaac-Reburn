package Entities.Dynamic.Physical.Items;

import Engine.Wrap;
import Entities.Dynamic.Physical.PhysicalEntity;
import Entities.Entity;
import Enums.EntityType;
import Tools.Collision;

import java.util.ArrayList;

public class Item extends PhysicalEntity {

    public Item(Wrap wrap, ArrayList<Entity> entities, Entity room) {
        super(wrap, entities, room, EntityType.ITEM, "resource/blank.png", 300, 300, 50, 50, 1, 1, 0, 0);
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entityType()) {
            case PLAYER -> applyRelativeCollision(collision);
        }
    }
}
