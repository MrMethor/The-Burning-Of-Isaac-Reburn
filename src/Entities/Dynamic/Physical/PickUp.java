package Entities.Dynamic.Physical;

import Engine.Wrap;
import Enums.EntityType;
import Tools.Collision;
import Tools.EntityManager;

public class PickUp extends PhysicalEntity{

    public PickUp(Wrap wrap, EntityManager entities, EntityType type, double x, double y) {
        super(wrap, entities, type, "resource/textures/" + getPath(type) + ".png", x, y, 50, 50, 0.9, 0.9, 0, 0);
        changeSlideFactor(0.92);
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entity().getType()) {
            case OBSTACLE, WALL -> applySolidCollision(collision);
            case HALF_HEART, FULL_HEART, SOUL_HEART -> applyRelativeCollision(collision);
            case PLAYER, ENEMY -> {
                if (!(collision.entity() instanceof PhysicalEntity physicalEntity && physicalEntity.canFly()))
                    applyRelativeCollision(collision);
            }
        }
    }

    private static String getPath(EntityType type) {
        switch(type) {
            case FULL_HEART -> {
                return "fullHeart";
            }
            case HALF_HEART -> {
                return "halfHeart";
            }
            case SOUL_HEART -> {
                return "soulHeart";
            }
        }
        return null;
    }
}
