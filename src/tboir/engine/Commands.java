package tboir.engine;

public enum Commands {
    moveUp ("Move Up", true, false),
    moveDown ("Move Down", true, false),
    moveLeft ("Move Left", true, false),
    moveRight ("Move Right", true, false),
    fireUp ("Fire Up", true, false),
    fireDown ("Fire Down", true, false),
    fireLeft ("Fire Left", true, false),
    fireRight ("Fire Right", true, false),
    fullscreen("Fullscreen", true, true),

    interact (),
    menu ();

    private final String name;
    private final boolean isEditable;
    private final boolean pressCommand;

    Commands() {
        this.name = "";
        this.isEditable = false;
        this.pressCommand = false;
    }

    Commands(String name, boolean isEditable, boolean pressCommand) {
        this.name = name;
        this.isEditable = isEditable;
        this.pressCommand = pressCommand;
    }

    public String getName() {
        return this.name;
    }

    public boolean isEditable() {
        return this.isEditable;
    }

    public boolean isPressCommand() {
        return this.pressCommand;
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
