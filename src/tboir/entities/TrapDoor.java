package tboir.entities;

import tboir.engine.Wrap;
import tboir.enums.EntityType;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

public class TrapDoor extends Entity{

    public TrapDoor(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.TRAP_DOOR, "resource/entities/trapDoor.png", 2, 1, x, y, 100, 100, 0.5, 0.5, 0, 0);
        this.swapTexture(1, 0);
    }

    public void openTrapDoor() {
        this.swapTexture(0, 0);
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
