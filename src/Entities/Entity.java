package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Enums.Side;
import Tools.*;
import Tools.Image;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity implements Comparable<Entity> {

    protected Wrap wrap;
    protected EntityManager entities;
    protected EntityType type;

    protected boolean toDestroy = false;

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    protected double hitboxWidthScale;
    protected double hitboxHeightScale;
    protected double hitboxOffsetX;
    protected double hitboxOffsetY;

    protected Image texture;
    protected long animationCounter;

    protected final ArrayList<Collision> collisions = new ArrayList<>();

    public Entity(Wrap wrap, EntityManager entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
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

    public Entity(Wrap wrap, EntityManager entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
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

    // Overridable methods
    //====================================
    public void applyBehavior() {}

    protected void applyCollision(Collision collision) {}

    public void animate() {}

    public void render(Graphics g) {
        texture.draw(g);
        if (wrap.isHitboxes())
            drawHitbox(g);
    }
    //====================================

    public void applyCollisions() {
        for (Collision collision : collisions)
            applyCollision(collision);
        resetCollisions();
    }

    public void addCollision(Side side, double penetration, Entity entity) {
        collisions.add(new Collision(side, penetration, entity));
    }

    private void resetCollisions() {
        collisions.clear();
    }

    private void drawHitbox(Graphics g) {
        Color c = g.getColor();
        g.setColor(new Color(1f,0f,0f,.2f));
        g.fillRect((int)((getHitboxX() - getHitboxWidth() / 2) * wrap.getScale()), (int) ((getHitboxY() - getHitboxHeight() / 2) * wrap.getScale()), (int) (getHitboxWidth() * wrap.getScale()), (int) (getHitboxHeight() * wrap.getScale()));
        g.setColor(c);
    }

    protected void changeSize(int width, int height) {
        this.width = width;
        this.height = height;
        texture.changeSize(width, height);
    }

    protected void changeTextureSize(int width, int height) {
        double widthScale = width / this.width;
        double heightScale = height / this.height;
        this.hitboxWidthScale *= widthScale;
        this.hitboxHeightScale *= heightScale;
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

    public void destroy() {
        toDestroy = true;
    }



    // Misc
    public int compareTo(Entity o) {
        return (int)(o.getHitboxY() - getHitboxY());
    }

    public double getWeight() {
        return getHitboxWidth() * getHitboxHeight();
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
