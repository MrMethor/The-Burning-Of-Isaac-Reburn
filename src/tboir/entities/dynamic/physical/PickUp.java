package tboir.entities.dynamic.physical;

import tboir.engine.Wrap;
import tboir.entities.EntityType;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

public class PickUp extends PhysicalEntity {

    public PickUp(Wrap wrap, EntityManager entities, EntityType type, double x, double y) {
        super(wrap, entities, type, "resource/entities/" + getPath(type) + ".png", x, y, 50, 50, 0.9, 0.9, 0, 0);
        this.changeDefaultSlideFactor(0.92);
    }

    @Override
    public void applyBehavior() {
    }

    @Override
    protected void applyCollision(Collision collision) {
        switch (collision.entity().getType()) {
            case OBSTACLE, WALL -> this.applySolidCollision(collision);
            case HALF_HEART, FULL_HEART, SOUL_HEART -> this.applyRelativeCollision(collision);
            case PLAYER, ENEMY -> {
                if (!(collision.entity() instanceof PhysicalEntity physicalEntity && physicalEntity.canFly())) {
                    this.applyRelativeCollision(collision);
                }
            }
        }
    }

    @Override
    public void animate() {
    }

    private static String getPath(EntityType type) {
        switch (type) {
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
