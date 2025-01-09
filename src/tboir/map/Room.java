package tboir.map;

import tboir.engine.Wrap;
import tboir.entities.*;
import tboir.entities.dynamic.physical.*;
import tboir.entities.dynamic.physical.enemies.*;
import tboir.enums.FloorType;
import tboir.enums.EntityType;
import tboir.enums.DoorType;
import tboir.enums.RoomType;
import tboir.enums.Side;
import tboir.tools.EntityManager;
import tboir.tools.Image;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Room {

    private final Wrap wrap;

    private final EntityManager entities;
    private boolean completed;
    private final RoomType roomType;
    private final FloorType floorType;
    private DoorType[] doors;

    private final Image background;
    private final Image shade;

    public Room(Wrap wrap, String mapPath, RoomType roomType, FloorType floorType) {
        this.wrap = wrap;
        this.roomType = roomType;
        this.floorType = floorType;
        this.entities = new EntityManager(wrap);
        this.completed = false;
        double x = 1920 / 2.0;
        double y = 1080 / 2.0;
        double width = 1920;
        double height = 1080;
        this.background = new Image(wrap, "resource/rooms/" + getRoomType() + ".png", x - width / 2.0, y - height / 2.0, width, height);
        this.shade = new Image(wrap, "resource/rooms/roomShade.png", x - width / 2.0, y - height / 2.0, width, height);
        String content = "";
        try {
            File file = new File(mapPath);
            Scanner reader = new Scanner(file);
            StringBuilder tmp = new StringBuilder();
            while (reader.hasNextLine()) {
                tmp.append(reader.nextLine());
            }
            content = tmp.toString();
        } catch (FileNotFoundException e) {
            System.out.println("Error with loading map file");
        }
        content = content.replaceAll("\\s+","");
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

    public void render(Graphics g) {
        this.renderBackground(g);
        this.entities.renderDoors(g);
        this.entities.renderTiles(g);
        this.renderShade(g);
        this.entities.renderItems(g);
        this.entities.renderDynamic(g);
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

    private void renderBackground(Graphics g) {
        this.background.draw(g);
    }

    private void renderShade(Graphics g) {
        this.shade.draw(g);
    }

    private void addEntity(char type, int xTile, int yTile) {
        int x = xTile * 102 + 350;
        int y = yTile * 107 + 215;
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
            case 'I' -> this.entities.addEntity(new Item(this.wrap, this.entities, randomItem, this.wrap.getItemFromRegistry(randomItem).path(), x, y));
        }
    }

    private void addWall(Side side) {
        int width, height, x, y;
        if (side == Side.UP || side == Side.DOWN) {
            width = 1920;
            height = 200;
            x = 1920 / 2;
            if (side == Side.UP) {
                y = 75;
            } else {
                y = 1080 - 75;
            }
        }
        else {
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
        int width1, height1, x1, y1;
        int width2, height2, x2, y2;
        if (side == Side.UP || side == Side.DOWN) {
            width1 = width2 = 1920 / 2 - 125;
            height1 = height2 = 200;
            x1 = 1920 / 4;
            x2 = 1920 / 4 * 3;
            if (side == Side.UP) {
                y1 = y2 = 63;
            } else {
                y1 = y2 = 1080 - 63;
            }
        }
        else {
            width1 = width2 = 200;
            height1 = height2 = 1080 / 2 - 125;
            y1 = 1080 / 4;
            y2 = 1080 / 4 * 3;
            if (side == Side.LEFT) {
                x1 = x2 = 200;
            } else {
                x1 = x2 = 1920 - 200;
            }
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
}
