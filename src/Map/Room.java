package Map;

import Engine.Component;
import Engine.Wrap;
import Entities.*;
import Entities.Dynamic.DynamicEntity;
import Entities.Dynamic.Physical.Enemies.Fly;
import Enums.DoorType;
import Enums.FloorType;
import Enums.RoomType;
import Enums.Side;
import Tools.EntityList;
import Tools.Image;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Room implements Component {

    private Wrap wrap;

    private EntityList entities = new EntityList();

    private boolean completed = false;

    private final double x = 1920 / 2.0;
    private final double y = 1080 / 2.0;
    private double width = 1920;
    private double height = 1080;

    private RoomType roomType;
    private FloorType floorType;

    private Door[] doors = new Door[4];

    private final int maxWidthTiles = 13;
    private final int maxHeightTiles = 7;

    private Image background;
    private Image shade;

    public Room(Wrap wrap, String mapPath, RoomType roomType, FloorType floorType) {
        this.wrap = wrap;
        this.roomType = roomType;
        this.floorType = floorType;
        background = new Image(wrap, "resource/textures/" + getRoomType() + ".png", x - width / 2.0, y - height / 2.0, width, height);
        shade = new Image(wrap, "resource/textures/roomShade.png", x - width / 2.0, y - height / 2.0, width, height);
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
        DoorType[] doors = {up, down, left, right};
        for (int i = 0; i < doors.length; i++) {
            if (doors[i] != null) {
                switch (roomType) {
                    case GOLDEN -> this.doors[i] = new Door(wrap, Side.getSide(i), DoorType.GOLDEN);
                    case DEFAULT -> this.doors[i] = new Door(wrap, Side.getSide(i), doors[i]);
                }
            }
        }
    }

    public void update() {
        entities.update();
        if (!completed && !entities.hasEnemies()) {
            completed = true;
            for (Door door : doors)
                if (door != null)
                    door.openDoor();
        }
    }

    public void render(Graphics g) {
        renderBackground(g);
        entities.renderTiles(g);
        for (Door door : doors)
            if (door != null)
                door.render(g);
        renderShade(g);
        entities.renderDynamic(g);
        entities.renderProjectiles(g);
    }

    private String getRoomType() {
        if (roomType == RoomType.GOLDEN)
            return "golden";
        return switch (floorType) {
            case BASEMENT -> "basement";
            case CAVES -> "caves";
            case DEPTHS -> "depths";
        };
    }

    private void renderBackground(Graphics g) {
        background.draw(g);
        if (wrap.isHitboxes())
            drawHitbox(g);
    }

    private void renderShade(Graphics g) {
        shade.draw(g);
    }

    private void drawHitbox(Graphics g) {
        Color c = g.getColor();
        g.setColor(new Color(0f,1f,0f,.2f));
        g.fillRect((int)((1920/2 - getHitboxWidth() / 2) * wrap.getScale()), (int) ((1080/2 - getHitboxHeight() / 2) * wrap.getScale()), (int) (getHitboxWidth() * wrap.getScale()), (int) (getHitboxHeight() * wrap.getScale()));
        g.setColor(c);
    }

    private void addEntity(char type, int xTile, int yTile) {
        int x = xTile * 102 + 350;
        int y = yTile * 107 + 215;
        switch (type) {
            case 'R' -> entities.addEntity(new Rock(wrap, entities, x, y));
            case 'f' -> entities.addEntity(new Fly(wrap, entities, x, y));
            case 'P' -> entities.addEntity(new Poop(wrap, entities, x, y));
            case 'F' -> entities.addEntity(new Fire(wrap, entities, x, y));
            case 'S' -> entities.addEntity(new Spikes(wrap, entities, x, y));
        }
    }

    public static double getHitboxWidth() {
        return 1920 - 595;
    }

    public static double getHitboxHeight() {
        return 1080 - 325;
    }

    public Door[] getDoors() {
        return doors;
    }

    public EntityList getEntities() {
        return entities;
    }

    public RoomType getType() {
        return roomType;
    }

    public boolean isCompleted() {
        return completed;
    }
}
