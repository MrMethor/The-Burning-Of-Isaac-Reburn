package Entities;

import Engine.Component;
import Engine.Wrap;
import Enums.EntityType;
import Enums.Side;
import Tools.*;
import Tools.Image;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity implements Component, Comparable<Entity> {

    protected Wrap wrap;
    protected ArrayList<Entity> entities;
    protected EntityType type;

    protected boolean toDestroy = false;

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    private double hitboxWidthScale;
    private double hitboxHeightScale;
    private double hitboxOffsetX;
    private double hitboxOffsetY;

    protected Image texture;
    protected long animationCounter;

    protected final ArrayList<Collision> collisions = new ArrayList<>();

    public Entity(Wrap wrap, ArrayList<Entity> entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        this.wrap = wrap;
        this.entities = entities;
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitboxWidthScale = widthScale;
        this.hitboxHeightScale = heightScale;
        this.hitboxOffsetX = offsetX;
        this.hitboxOffsetY = offsetY;
        texture = new Image(wrap, texturePath, x - width / 2.0, y - height / 2.0, width, height);
    }

    public Entity(Wrap wrap, ArrayList<Entity> entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        this.wrap = wrap;
        this.entities = entities;
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitboxWidthScale = widthScale;
        this.hitboxHeightScale = heightScale;
        this.hitboxOffsetX = offsetX;
        this.hitboxOffsetY = offsetY;
        texture = new SpriteSheet(wrap, spriteSheetPath, x - width / 2.0, y - height / 2.0, width, height, column, row);
    }

    public void update() {
        applyBehavior();
        collisions();
        animate(); //country roads take me hooooome to the place i beloooong
    }

    protected void collisions() {
        for (Entity entity : entities) {
            if (entity != this)
                collision(entity);
        }
        for (Collision collision : collisions)
            applyCollision(collision);
        resetCollisions();
    }

    private void collision(Entity other) {

        double[] sides = new double[4];

        sides[Side.UP.num()] = (getHitboxY() - getHitboxHeight() / 2) - (other.getHitboxY() + getHitboxHeight() / 2);
        sides[Side.DOWN.num()] = (other.getHitboxY() - other.getHitboxHeight() / 2) - (getHitboxY() + getHitboxHeight() / 2);
        sides[Side.LEFT.num()] = (getHitboxX() - getHitboxWidth() / 2) - (other.getHitboxX() + getHitboxWidth() / 2);
        sides[Side.RIGHT.num()] = (other.getHitboxX() - other.getHitboxWidth() / 2) - (getHitboxX() + getHitboxWidth() / 2);
        if ((sides[Side.UP.num()] < 0 && sides[Side.DOWN.num()] < 0) && (sides[Side.LEFT.num()] < 0 && sides[Side.RIGHT.num()] < 0)) {
            double penetration = -1920;
            Side sideOut = null;
            for (int i = 0; i < 4; i++) {
                if (sides[i] <= 0 && penetration < sides[i]) {
                    penetration = sides[i];
                    sideOut = Side.getSide(i);
                }
            }
            collisions.add(new Collision(other.type, sideOut, penetration));
        }
    }

    protected void applyCollision(Collision collision) {}

    private void resetCollisions() {
        collisions.clear();
    }

    protected void applyBehavior() {}

    protected void animate() {}

    public void render(Graphics g) {
        texture.draw(g);
        if (wrap.isHitboxes())
            drawHitbox(g);
    }

    private void drawHitbox(Graphics g) {
        Color c = g.getColor();
        if (type == EntityType.ROOM)
            g.setColor(new Color(0f,1f,0f,.2f));
        else
            g.setColor(new Color(1f,0f,0f,.2f));
        g.fillRect((int)((getHitboxX() - getHitboxWidth() / 2) * wrap.getScale()), (int) ((getHitboxY() - getHitboxHeight() / 2) * wrap.getScale()), (int) (getHitboxWidth() * wrap.getScale()), (int) (getHitboxHeight() * wrap.getScale()));
        g.setColor(c);
    }

    protected void changeSize(int width, int height) {
        this.width = width;
        this.height = height;
        texture.changeSize(width, height);
    }

    protected void changeSpriteSheet(String path, int column, int row) {
        texture = new SpriteSheet(wrap, path, x, y, width, height, column, row);
    }

    protected void changeTexture(String path) {
        texture = new Image(wrap, path, x, y, width, height);
    }

    protected void swapTexture(int column, int row) {
        if (texture instanceof SpriteSheet)
            ((SpriteSheet) texture).swapImage(column, row);
    }

    protected void destroy() {
        toDestroy = true;
    }



    // Misc
    public int compareTo(Entity o) {
        return (int)(o.y - y);
    }

    public boolean isToDestroy() {
        return toDestroy;
    }

    public EntityType getType() {
        return type;
    }

    public double getHitboxWidth() {
        return width * hitboxWidthScale;
    }

    public double getHitboxHeight() {
        return height * hitboxHeightScale;
    }

    public double getHitboxX() {
        return x + width * hitboxOffsetX;
    }

    public double getHitboxY() {
        return y + height * hitboxOffsetY;
    }
}
