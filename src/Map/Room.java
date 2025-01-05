package Map;

import Engine.Component;
import Engine.Wrap;
import Entities.*;
import Entities.Dynamic.Physical.Enemies.Fly;
import Entities.Dynamic.Physical.PickUp;
import Enums.*;
import Tools.EntityManager;
import Tools.Image;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Room implements Component {

    private Wrap wrap;

    private EntityManager entities;

    private boolean completed = false;

    private final double x = 1920 / 2.0;
    private final double y = 1080 / 2.0;
    private double width = 1920;
    private double height = 1080;

    private RoomType roomType;
    private FloorType floorType;

    private DoorType[] doors;

    private final int maxWidthTiles = 13;
    private final int maxHeightTiles = 7;

    private Image background;
    private Image shade;

    public Room(Wrap wrap, String mapPath, RoomType roomType, FloorType floorType) {
        this.wrap = wrap;
        this.roomType = roomType;
        this.floorType = floorType;
        this.entities = new EntityManager(wrap);
        background = new Image(wrap, "resource/rooms/" + getRoomType() + ".png", x - width / 2.0, y - height / 2.0, width, height);
        shade = new Image(wrap, "resource/rooms/roomShade.png", x - width / 2.0, y - height / 2.0, width, height);
        String content = "";
        try {
            File file = new File(mapPath);
            Scanner reader = new Scanner(file);
            StringBuilder tmp = new StringBuilder();
            while (reader.hasNextLine())
               tmp.append(reader.nextLine());
            content = tmp.toString();
        } catch (FileNotFoundException e) {
            System.out.println("Error with loading map file");
        }
        content = content.replaceAll("\\s+","");
        char[] spawnEntities = content.toCharArray();
        for (int i = 0; i < maxWidthTiles; i++) {
            for (int j = 0; j < maxHeightTiles; j++)
                addEntity(spawnEntities[j * maxWidthTiles + i], i, j);
        }
    }

    public void setupDoors(DoorType up, DoorType down, DoorType left, DoorType right) {
        doors = new DoorType[]{up, down, left, right};
        for (int i = 0; i < doors.length; i++) {
            addWall(Side.getSide(i));
            if (doors[i] == null)
                continue;
            switch (roomType) {
                case BOSS -> entities.addEntity(new Door(wrap, entities, Side.getSide(i), DoorType.BOSS));
                case GOLDEN -> entities.addEntity(new Door(wrap, entities, Side.getSide(i), DoorType.GOLDEN));
                case DEFAULT -> entities.addEntity(new Door(wrap, entities, Side.getSide(i), doors[i]));
            }
        }
    }

    public void update() {
        entities.update();
        if (!completed && !entities.hasEnemies()) {
            completed = true;
            openDoors();
        }
    }

    public void render(Graphics g) {
        renderBackground(g);
        entities.renderDoors(g);
        entities.renderTiles(g);
        renderShade(g);
        entities.renderItems(g);
        entities.renderDynamic(g);
    }

    private String getRoomType() {
        if (roomType == RoomType.GOLDEN)
            return "golden";
        if (roomType == RoomType.BOSS)
            return "boss";
        return switch (floorType) {
            case BASEMENT -> "basement";
            case CAVES -> "caves";
            case DEPTHS -> "depths";
        };
    }

    private void renderBackground(Graphics g) {
        background.draw(g);
    }

    private void renderShade(Graphics g) {
        shade.draw(g);
    }

    private void addEntity(char type, int xTile, int yTile) {
        int x = xTile * 102 + 350;
        int y = yTile * 107 + 215;
        Random random = new Random();
        int randomItem = random.nextInt(wrap.getNumOfItems() - 1);
        switch (type) {
            case 'R' -> entities.addEntity(new Rock(wrap, entities, x, y));
            case 'f' -> entities.addEntity(new Fly(wrap, entities, x, y));
            case 'P' -> entities.addEntity(new Poop(wrap, entities, x, y));
            case 'F' -> entities.addEntity(new Fire(wrap, entities, x, y));
            case 'S' -> entities.addEntity(new Spikes(wrap, entities, x, y));
            case 'T' -> entities.addEntity(new TrapDoor(wrap, entities, x, y));
            case 'H' -> entities.addEntity(new PickUp(wrap, entities, EntityType.FULL_HEART, x, y));
            case 'N' -> entities.addEntity(new PickUp(wrap, entities, EntityType.HALF_HEART, x, y));
            case 'O' -> entities.addEntity(new PickUp(wrap, entities, EntityType.SOUL_HEART, x, y));
            case 'I' -> entities.addEntity(new Item(wrap, entities, randomItem, wrap.getItemFromRegistry(randomItem).path(), x, y));
        }
    }

    private void addWall(Side side) {
        int width, height, x, y;
        if (side == Side.UP || side == Side.DOWN) {
            width = 1920;
            height = 200;
            x = 1920 / 2;
            if (side == Side.UP)
                y = 75;
            else
                y = 1080 - 75;
        }
        else {
            width = 200;
            height = 1080;
            y = 1080 / 2;
            if (side == Side.LEFT)
                x = 200;
            else
                x = 1920 - 200;
        }
        entities.addEntity(new Wall(wrap, entities, x, y, width, height));
    }

    private void addDoorWall(Side side) {
        int width1, height1, x1, y1;
        int width2, height2, x2, y2;
        if (side == Side.UP || side == Side.DOWN) {
            width1 = width2 = 1920 / 2 - 125;
            height1 = height2 = 200;
            x1 = 1920 / 4;
            x2 = 1920 / 4 * 3;
            if (side == Side.UP)
                y1 = y2 = 63;
            else
                y1 = y2 = 1080 - 63;
        }
        else {
            width1 = width2 = 200;
            height1 = height2 = 1080 / 2 - 125;
            y1 = 1080 / 4;
            y2 = 1080 / 4 * 3;
            if (side == Side.LEFT)
                x1 = x2 = 200;
            else
                x1 = x2 = 1920 - 200;
        }
        entities.addEntity(new Wall(wrap, entities, x1, y1, width1, height1));
        entities.addEntity(new Wall(wrap, entities, x2, y2, width2, height2));
    }

    private void openDoors() {
        entities.openDoors();
        for (int i = 0; i < doors.length; i++) {
            if (doors[i] == null) {
                addWall(Side.getSide(i));
                continue;
            }
            addDoorWall(Side.getSide(i));
        }
    }

    public EntityManager getEntities() {
        return entities;
    }

    public RoomType getType() {
        return roomType;
    }

    public boolean isCompleted() {
        return completed;
    }
}
