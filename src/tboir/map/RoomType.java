package tboir.map;

public enum RoomType {
    GOLDEN ("golden"),
    BOSS ("boss"),
    DEFAULT ("default"),
    STARTING ("starting"),
    SHOP ("shop"),
    SACRIFICE ("sacrifice");

    private final String path;

    RoomType(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
