package Enums;

public enum Side {

    UP(0, "up"),
    DOWN(1, "down"),
    LEFT(2, "left"),
    RIGHT(3, "right");

    private final int value;
    private final String str;

    Side(int value, String str) {
        this.value = value;
        this.str = str;
    }

    public int num() {
        return value;
    }

    public String str() {
        return str;
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

}
