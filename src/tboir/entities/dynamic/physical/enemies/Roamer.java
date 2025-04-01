package tboir.entities.dynamic.physical.enemies;

import tboir.engine.Wrap;
import tboir.entities.EntityType;
import tboir.tools.EntityManager;

public class Roamer extends Enemy {

    public Roamer(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.ENEMY, 5, "resource/entities/roamer.png", 2, 2, x, y, 100, 100, .7, .7, 0, 0);
    }

    @Override
    public void applyBehavior() {

    }

    @Override
    public void animate() {

    }
}
