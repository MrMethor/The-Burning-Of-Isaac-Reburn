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
}
