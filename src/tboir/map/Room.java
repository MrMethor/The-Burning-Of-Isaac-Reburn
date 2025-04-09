package tboir.map;

import tboir.engine.Wrap;
import tboir.entities.Entity;
import tboir.entities.Fire;
import tboir.entities.Poop;
import tboir.entities.Rock;
import tboir.entities.Spikes;
import tboir.entities.Wall;
import tboir.entities.Item;
import tboir.entities.Web;
import tboir.entities.TrapDoor;
import tboir.entities.dynamic.physical.PickUp;
import tboir.entities.dynamic.physical.enemies.Body;
import tboir.entities.dynamic.physical.enemies.Fly;
import tboir.entities.EntityType;
import tboir.engine.Side;
import tboir.tools.Image;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Room {

    public static final int TILES_WIDTH = 13;
    public static final int TILES_HEIGHT = 7;
    public static final int TILE_WIDTH_PX = 102;
    public static final int TILE_HEIGHT_PX = 107;
    public static final int OFFSET_WIDTH_PX = 350;
    public static final int OFFSET_HEIGHT_PX = 215;

    private final Wrap wrap;

    private final EntityManager entities;
    private boolean completed;
    private boolean explored;
    private final RoomType roomType;
    private final FloorType floorType;
    private DoorType[] doors;

    private final Image background;
    private final Image shade;

    public Room(Wrap wrap, String mapPath, RoomType roomType, FloorType floorType) {
        this.wrap = wrap;
        this.roomType = roomType;
        this.floorType = floorType;
        this.explored = false;
        this.entities = new EntityManager(wrap);
        this.completed = false;
        this.background = new Image(wrap, this.getRoomType(), 0, 0, 1920, 1080);
        this.shade = new Image(wrap, "roomShade", 0, 0, 1920, 1080);
        String content = "";
        try {
            File file = new File(mapPath);
            Scanner reader = new Scanner(file);
            reader.nextLine();
            StringBuilder tmp = new StringBuilder();
            while (reader.hasNextLine()) {
                tmp.append(reader.nextLine());
            }
            content = tmp.toString();
        } catch (FileNotFoundException e) {
            System.out.println("Error with loading map file");
        }
        content = content.replaceAll("\\s+", "");
        char[] spawnEntities = content.toCharArray();
        int maxWidthTiles = 13;
        int maxHeightTiles = 7;
        for (int i = 0; i < maxWidthTiles; i++) {
            for (int j = 0; j < maxHeightTiles; j++) {
                this.addEntity(spawnEntities[j * maxWidthTiles + i], i, j);
            }
        }
    }

    public void setupDoors(DoorType up, DoorType down, DoorType left, DoorType right) {
        this.doors = new DoorType[]{up, down, left, right};
        for (int i = 0; i < this.doors.length; i++) {
            this.addWall(Side.getSide(i));
            if (this.doors[i] == null) {
                continue;
            }
            switch (this.roomType) {
                case BOSS -> this.entities.addEntity(new Door(this.wrap, this.entities, Side.getSide(i), DoorType.BOSS));
                case GOLDEN -> this.entities.addEntity(new Door(this.wrap, this.entities, Side.getSide(i), DoorType.GOLDEN));
                case DEFAULT -> this.entities.addEntity(new Door(this.wrap, this.entities, Side.getSide(i), this.doors[i]));
            }
        }
    }

    public void update() {
        this.entities.update();
        if (!this.completed && !this.entities.hasEnemies()) {
            this.completed = true;
            this.openDoors();
        }
    }

    public void render(Graphics2D g) {
        this.renderBackground(g);
        this.entities.renderBack(g);
        this.renderShade(g);
        this.entities.renderFront(g);
    }

    private String getRoomType() {
        if (this.roomType == RoomType.GOLDEN) {
            return "golden";
        }
        if (this.roomType == RoomType.BOSS) {
            return "boss";
        }
        return switch (this.floorType) {
            case BASEMENT -> "basement";
            case CAVES -> "caves";
            case DEPTHS -> "depths";
        };
    }

    private void renderBackground(Graphics2D g) {
        this.background.draw(g);
    }

    private void renderShade(Graphics2D g) {
        this.shade.draw(g);
    }

    private void addEntity(char type, int xTile, int yTile) {
        int x = xTile * TILE_WIDTH_PX + OFFSET_WIDTH_PX;
        int y = yTile * TILE_HEIGHT_PX + OFFSET_HEIGHT_PX;
        Random random = new Random();
        int randomItem = random.nextInt(this.wrap.getNumOfItems() - 1);
        switch (type) {
            case 'R' -> this.entities.addEntity(new Rock(this.wrap, this.entities, x, y));
            case 'f' -> this.entities.addEntity(new Fly(this.wrap, this.entities, x, y));
            case 'P' -> this.entities.addEntity(new Poop(this.wrap, this.entities, x, y));
            case 'F' -> this.entities.addEntity(new Fire(this.wrap, this.entities, x, y));
            case 'S' -> this.entities.addEntity(new Spikes(this.wrap, this.entities, x, y));
            case 'T' -> this.entities.addEntity(new TrapDoor(this.wrap, this.entities, x, y));
            case 'H' -> this.entities.addEntity(new PickUp(this.wrap, this.entities, EntityType.FULL_HEART, x, y));
            case 'N' -> this.entities.addEntity(new PickUp(this.wrap, this.entities, EntityType.HALF_HEART, x, y));
            case 'O' -> this.entities.addEntity(new PickUp(this.wrap, this.entities, EntityType.SOUL_HEART, x, y));
            case 'I' -> this.entities.addEntity(new Item(this.wrap, this.entities, randomItem, x, y));
            case 'W' -> this.entities.addEntity(new Web(this.wrap, this.entities, x, y));
            case 'b' -> this.entities.addEntity(new Body(this.wrap, this.entities, x, y));
        }
    }

    private void addWall(Side side) {
        int width;
        int height;
        int x;
        int y;
        if (side == Side.UP || side == Side.DOWN) {
            width = 1920;
            height = 200;
            x = 1920 / 2;
            if (side == Side.UP) {
                y = 75;
            } else {
                y = 1080 - 75;
            }
        } else {
            width = 200;
            height = 1080;
            y = 1080 / 2;
            if (side == Side.LEFT) {
                x = 200;
            } else {
                x = 1920 - 200;
            }
        }
        this.entities.addEntity(new Wall(this.wrap, this.entities, x, y, width, height));
    }

    private void addDoorWall(Side side) {
        int width1;
        int height1;
        int x1;
        int y1;
        int width2;
        int height2;
        int x2;
        int y2;
        if (side == Side.UP || side == Side.DOWN) {
            width1 = 1920 / 2 - 125;
            width2 = width1;
            height1 = 200;
            height2 = height1;
            x1 = 1920 / 4;
            x2 = 1920 / 4 * 3;
            if (side == Side.UP) {
                y1 = 63;
            } else {
                y1 = 1080 - 63;
            }
            y2 = y1;
        } else {
            width1 = 200;
            width2 = width1;
            height1 = 1080 / 2 - 125;
            height2 = height1;
            y1 = 1080 / 4;
            y2 = 1080 / 4 * 3;
            if (side == Side.LEFT) {
                x1 = 200;
            } else {
                x1 = 1920 - 200;
            }
            x2 = x1;
        }
        this.entities.addEntity(new Wall(this.wrap, this.entities, x1, y1, width1, height1));
        this.entities.addEntity(new Wall(this.wrap, this.entities, x2, y2, width2, height2));
    }

    private void openDoors() {
        this.entities.openDoors();
        for (int i = 0; i < this.doors.length; i++) {
            if (this.doors[i] == null) {
                this.addWall(Side.getSide(i));
                continue;
            }
            this.addDoorWall(Side.getSide(i));
        }
    }

    // Getters
    public EntityManager getEntities() {
        return this.entities;
    }

    public RoomType getType() {
        return this.roomType;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public boolean isExplored() {
        return this.explored;
    }

    public void explored() {
        this.explored = true;
    }

    public static int getTileX(Entity entity) {
        return (int)Math.round((entity.getHitboxX() - OFFSET_WIDTH_PX) / TILE_WIDTH_PX);
    }

    public static int getTileY(Entity entity) {
        return (int)Math.round(((entity.getHitboxY() - OFFSET_HEIGHT_PX) / TILE_HEIGHT_PX));
    }

    public static int getTileCenterX(int tileX) {
        return OFFSET_WIDTH_PX + tileX * TILE_WIDTH_PX;
    }

    public static int getTileCenterY(int tileY) {
        return OFFSET_HEIGHT_PX + tileY * TILE_HEIGHT_PX;
    }
}
