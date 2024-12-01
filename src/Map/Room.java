package Map;

import Engine.Component;
import Engine.Wrap;
import Entities.Dynamic.Physical.Enemies.Fly;
import Tools.EntityList;
import Tools.Image;

import java.awt.*;

public class Room implements Component {

    protected Wrap wrap;
    protected EntityList entities;

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    private double hitboxWidthScale;
    private double hitboxHeightScale;

    protected Image texture;

    public Room(Wrap wrap, EntityList entities) {
        this.wrap = wrap;
        this.entities = entities;
        this.x = 1920/2.0;
        this.y = 1080/2.0;
        this.width = 1920;
        this.height = 1080;
        this.hitboxWidthScale = 0.8;
        this.hitboxHeightScale = 0.75;
        texture = new Image(wrap, "resource/room.jpg", x - width / 2.0, y - height / 2.0, width, height);

        entities.addEntity(new Fly(wrap, entities, this, 600, 600));
    }

    public void update() {
        entities.update();
    }

    public void render(Graphics g) {
        texture.draw(g);
        if (wrap.isHitboxes())
            drawHitbox(g);
        entities.render(g);
    }

    private void drawHitbox(Graphics g) {
        Color c = g.getColor();
        g.setColor(new Color(0f,1f,0f,.2f));
        g.fillRect((int)((getHitboxX() - getHitboxWidth() / 2) * wrap.getScale()), (int) ((getHitboxY() - getHitboxHeight() / 2) * wrap.getScale()), (int) (getHitboxWidth() * wrap.getScale()), (int) (getHitboxHeight() * wrap.getScale()));
        g.setColor(c);
    }

    public double getHitboxWidth() {
        return width * hitboxWidthScale;
    }

    public double getHitboxHeight() {
        return height * hitboxHeightScale;
    }

    public double getHitboxX() {
        return x;
    }

    public double getHitboxY() {
        return y;
    }
}
