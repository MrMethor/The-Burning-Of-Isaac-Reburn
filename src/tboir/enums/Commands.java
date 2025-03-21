package tboir.enums;

public enum Commands {
    moveUp ("Move Up", true),
    moveDown ("Move Down", true),
    moveLeft ("Move Left", true),
    moveRight ("Move Right", true),
    fireUp ("Fire Up", true),
    fireDown ("Fire Down", true),
    fireLeft ("Fire Left", true),
    fireRight ("Fire Right", true),

    interact ("Interact", false),
    menu ("Menu", false);

    private final String name;
    private final boolean isEditable;

    Commands(String name, boolean isEditable) {
        this.name = name;
        this.isEditable = isEditable;
    }

    public String getName() {
        return this.name;
    }

    public boolean isEditable() {
        return this.isEditable;
    }

    public static int numOfEditable() {
        int i = 0;
        for (Commands command : Commands.values()) {
            if (command.isEditable()) {
                i++;
            }
        }
        return i;
    }

    public static Commands getCommand(int index) {
        if (index >= 0 && index < Commands.values().length) {
            return Commands.values()[index];
        }
        throw (new IndexOutOfBoundsException("Invalid index: " + index));
    }
}
