package tboir.map;

import tboir.engine.Wrap;
import tboir.entities.Entity;
import tboir.engine.Side;
import tboir.entities.EntityType;
import tboir.tools.Collision;

public class Door extends Entity {

    private final DoorType type;

    public Door(Wrap wrap, EntityManager entities, Side side, DoorType type) {
        super(wrap, entities,
                EntityType.DOOR,
                "doors",
                type.ordinal(), 0,
                Door.getX(side), Door.getY(side),
                200, 200,
                0.3, 0.3,
                0, 0
        );
        this.type = type;
        int rotation = 0;
        switch (side) {
            case DOWN -> rotation = 2;
            case LEFT -> rotation = 3;
            case RIGHT -> rotation = 1;
        }
        this.changeRotation(rotation);
    }

    public void openDoor() {
        this.changeImage("doors", this.type.ordinal(), 1);
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

    private static int getX(Side side) {
        switch (side) {
            case LEFT -> {
                return 238;
            }
            case RIGHT -> {
                return 1682;
            }
            case UP, DOWN -> {
                return 1920 / 2;
            }
        }
        return 0;
    }

    private static int getY(Side side) {
        switch (side) {
            case LEFT, RIGHT -> {
                return 1080 / 2;
            }
            case UP -> {
                return 105;
            }
            case DOWN -> {
                return 975;
            }
        }
        return 0;
    }
}
