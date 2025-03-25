package tboir.entities;

import tboir.engine.Wrap;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

import java.util.Random;

public class Rock extends Entity {
    public Rock(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.OBSTACLE, "resource/entities/rocks.png", 2, 1, x, y, 130, 130, 0.8, 0.8, 0, 0);
        Random random = new Random();
        swapTexture(random.nextInt(2), 0);
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
