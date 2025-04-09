package tboir.entities;

import tboir.engine.Wrap;
import tboir.map.EntityManager;
import tboir.tools.Collision;

public class Wall extends Entity {

    public Wall(Wrap wrap, EntityManager entities, double x, double y, int width, int height) {
        super(wrap, entities, EntityType.WALL, null, x, y, width, height, 1, 1, 0, 0);
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
