package Entities.Dynamic.Physical.Items;

import Engine.Wrap;
import Entities.Dynamic.Physical.PhysicalEntity;
import Enums.EntityType;
import Tools.Collision;
import Tools.EntityList;

public class Item extends PhysicalEntity {

    public Item(Wrap wrap, EntityList entities) {
        super(wrap, entities, EntityType.ITEM, "resource/spriteSheets/blank.png", 300, 300, 50, 50, 1, 1, 0, 0);
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entityType()) {
            case PLAYER -> applyRelativeCollision(collision);
        }
    }
}
