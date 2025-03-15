package tboir.entities;

import tboir.engine.Wrap;
import tboir.enums.EntityType;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

public class Spikes extends Entity {

    public Spikes(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.SPIKE, "resource/entities/spikes.png", x, y, 120, 120, 0.5, 0.5, 0, 0);
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
