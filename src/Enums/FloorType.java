package Enums;

public enum FloorType {
    BASEMENT,
    CAVES,
    DEPTHS;

    public static FloorType getType(int i) {
        return switch (i) {
            case 3, 4 -> CAVES;
            case 5, 6 -> DEPTHS;
            default -> BASEMENT;
        };
    }
}


