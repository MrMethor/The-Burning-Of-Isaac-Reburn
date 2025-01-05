package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Tools.EntityManager;

public class TrapDoor extends Entity{

    public TrapDoor(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.TRAP_DOOR, "resource/entities/trapDoor.png", 2, 1, x, y, 100, 100, 0.5, 0.5, 0, 0);
        swapTexture(1, 0);
    }

    public void openTrapDoor() {
        swapTexture(0, 0);
    }
}
