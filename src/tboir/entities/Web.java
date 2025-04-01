package tboir.entities;

import tboir.engine.Wrap;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

import java.util.Random;

public class Web extends Entity {
    public Web(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.WEB, "resource/entities/web.png", 4, 1, x, y, 130, 130, 0.8, 0.8, 0, 0);
        Random random = new Random();
        swapTexture(random.nextInt(3), 0);
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
