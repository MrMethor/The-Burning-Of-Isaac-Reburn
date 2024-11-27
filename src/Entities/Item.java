package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Tools.Collision;

import java.util.ArrayList;

public class Item extends DynamicEntity {

    public Item(Wrap wrap, ArrayList<Entity> entities, Entity room) {
        super(wrap, entities, room, EntityType.ITEM, "resource/blank.png", 300, 300, 50, 50, 1, 1, 0, 0);
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entityType()) {
            case PLAYER -> {
                switch (collision.side()) {
                    case UP -> velocityY += 1;
                    case DOWN -> velocityY -= 1;
                    case LEFT -> velocityX += 1;
                    case RIGHT -> velocityX -= 1;
                }
            }
        }
    }
}
