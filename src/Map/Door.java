package Map;

import Engine.Wrap;
import Entities.Entity;
import Enums.DoorType;
import Enums.Side;
import Enums.EntityType;
import Tools.EntityManager;

enum DoorState {
    CLOSED,
    OPENED
}

public class Door extends Entity {

    private Wrap wrap;
    private Side side;

    public Door(Wrap wrap, EntityManager entities, Side side, DoorType type) {
        super(wrap, entities, EntityType.DOOR, getPath(type), 4, 2, Door.getX(side), Door.getY(side), 175, 175, 0.3, 0.3, 0,0);
        this.wrap = wrap;
        this.side = side;
        swapTexture(side.num(), DoorState.CLOSED.ordinal());
    }

    public void openDoor() {
        swapTexture(side.num(), DoorState.OPENED.ordinal());
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

    private static String getPath(DoorType type) {
        String typeString = "";
        switch (type) {
            case BOSS -> typeString = "boss";
            case BASEMENT -> typeString = "basement";
            case DEPTHS -> typeString = "depths";
            case GOLDEN -> typeString = "golden";
        }
        return "resource/doors/" + typeString + ".png";
    }
}
