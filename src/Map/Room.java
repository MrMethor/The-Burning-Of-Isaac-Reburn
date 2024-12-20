package Map;

import Engine.Component;
import Engine.Wrap;
import Entities.Dynamic.Physical.Enemies.Fly;
import Entities.Fire;
import Entities.Poop;
import Entities.Rock;
import Tools.EntityList;
import Tools.Image;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Room implements Component {

    protected Wrap wrap;

    protected EntityList entities = new EntityList();

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public final int maxWidthTiles = 13;
    public final int maxHeightTiles = 7;

    protected Image background;
    protected Image shade;

    public Room(Wrap wrap, String mapPath) {
        this.wrap = wrap;
        this.x = 1920/2.0;
        this.y = 1080/2.0;
        this.width = 1920;
        this.height = 1080;
        background = new Image(wrap, "resource/textures/room.png", x - width / 2.0, y - height / 2.0, width, height);
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

    public void update() {
        entities.update();
    }

    public void render(Graphics g) {
        renderBackground(g);
        entities.renderTiles(g);
        renderShade(g);
        entities.renderDynamic(g);
        entities.renderProjectiles(g);
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
            case 'F' -> entities.addEntity(new Fly(wrap, entities, x, y));
            case 'P' -> entities.addEntity(new Poop(wrap, entities, x, y));
            case 'f' -> entities.addEntity(new Fire(wrap, entities, x, y));
        }
    }

    public static double getHitboxWidth() {
        return 1920 - 595;
    }

    public static double getHitboxHeight() {
        return 1080 - 325;
    }
}
