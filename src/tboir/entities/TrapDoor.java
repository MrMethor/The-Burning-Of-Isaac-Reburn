package tboir.entities;

import tboir.engine.Wrap;
import tboir.map.EntityManager;
import tboir.tools.Collision;

public class TrapDoor extends Entity {

    public TrapDoor(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities,
                EntityType.TRAP_DOOR,
                "objects",
                3, 0,
                x, y,
                100, 100,
                0.5, 0.5,
                0, 0
        );
    }

    public void openTrapDoor() {
        this.changeImage("objects", 2, 0);
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
