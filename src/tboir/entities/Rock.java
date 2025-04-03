package tboir.entities;

import tboir.engine.Wrap;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

import java.util.Random;

public class Rock extends Entity {

    public Rock(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities,
                EntityType.OBSTACLE,
                "objects",
                0, 0,
                x, y,
                120, 120,
                0.8, 0.8,
                0, 0
        );
        Random random = new Random();
        this.changeImage("objects", random.nextInt(2), 0);
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
