package tboir.entities;

import tboir.engine.Wrap;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

public class Wall extends Entity {

    public Wall(Wrap wrap, EntityManager entities, double x, double y, int width, int height) {
        super(wrap, entities, EntityType.OBSTACLE, "resource/entities/blank.png", x, y, width, height, 1, 1, 0, 0);
    }

    @Override
    public void applyBehavior() {
    }

    @Override
    protected void applyCollision(Collision collision) {
    }

    @Override
    public void animate() {
    }
}
