package Enums;

public enum Side {

    UP(0),
    DOWN(1),
    LEFT(2),
    RIGHT(3);

    private final int value;

    Side(int value) {
        this.value = value;
    }

    public int num() {
        return value;
    }

    public static Side getSide(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1 -> DOWN;
            case 2 -> LEFT;
            case 3 -> RIGHT;
            default -> null;
        };
    }

    public static Side getOpposite(Side side) {
        return switch (side) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
