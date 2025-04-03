package tboir.entities;

import tboir.engine.Wrap;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

import java.util.Random;

public class Web extends Entity {

    public Web(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities,
                EntityType.WEB,
                "objects",
                0, 2,
                x, y,
                120, 120,
                0.7, 0.7,
                0, 0
        );
        Random random = new Random();
        this.changeImage("objects", random.nextInt(3), 2);
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
