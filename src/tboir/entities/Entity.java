package tboir.entities;

import tboir.engine.Wrap;
import tboir.enums.EntityType;
import tboir.enums.Side;
import tboir.tools.EntityManager;
import tboir.tools.Image;
import tboir.tools.Collision;
import tboir.tools.SpriteSheet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Entity implements Comparable<Entity> {

    protected final Wrap wrap;
    protected EntityManager entities;
    protected EntityType type;

    protected boolean toDestroy;

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

    protected final ArrayList<Collision> collisions;

    public Entity(Wrap wrap, EntityManager entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        this.wrap = wrap;
        this.toDestroy = false;
        this.collisions = new ArrayList<>();
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
        this.texture = new Image(wrap, texturePath, x - width / 2.0, y - height / 2.0, width, height);
    }

    public Entity(Wrap wrap, EntityManager entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        this.wrap = wrap;
        this.toDestroy = false;
        this.collisions = new ArrayList<>();
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
        this.texture = new SpriteSheet(wrap, spriteSheetPath, x - width / 2.0, y - height / 2.0, width, height, column, row);
    }

    public abstract void applyBehavior();

    protected abstract void applyCollision(Collision collision);

    public abstract void animate();

    public void render(Graphics g) {
        this.texture.draw(g);
        if (this.wrap.isHitboxes()) {
            this.drawHitbox(g);
        }
    }

    public void applyCollisions() {
        for (Collision collision : this.collisions) {
            this.applyCollision(collision);
        }
        this.resetCollisions();
    }

    public void addCollision(Side side, double penetration, Entity entity) {
        this.collisions.add(new Collision(side, penetration, entity));
    }

    private void resetCollisions() {
        this.collisions.clear();
    }

    private void drawHitbox(Graphics g) {
        Color c = g.getColor();
        g.setColor(new Color(1f,0f,0f,.2f));
        g.fillRect((int)((this.getHitboxX() - this.getHitboxWidth() / 2) * this.wrap.getScale()), (int) ((this.getHitboxY() - this.getHitboxHeight() / 2) * this.wrap.getScale()), (int) (this.getHitboxWidth() * this.wrap.getScale()), (int) (this.getHitboxHeight() * this.wrap.getScale()));
        g.setColor(c);
    }

    protected void changeSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.texture.changeSize(width, height);
    }

    protected void changeTextureSize(int width, int height) {
        double widthScale = width / this.width;
        double heightScale = height / this.height;
        this.hitboxWidthScale *= widthScale;
        this.hitboxHeightScale *= heightScale;
        this.width = width;
        this.height = height;
        this.texture.changeSize(width, height);
    }

    protected void changeSpriteSheet(String path, int column, int row) {
        this.texture = new SpriteSheet(this.wrap, path, this.x, this.y, this.width, this.height, column, row);
    }

    protected void changeTexture(String path) {
        this.texture = new Image(this.wrap, path, this.x, this.y, this.width, this.height);
    }

    protected void swapTexture(int column, int row) {
        if (this.texture instanceof SpriteSheet)
            ((SpriteSheet) this.texture).swapImage(column, row);
    }

    public void destroy() {
        this.toDestroy = true;
    }

    public int compareTo(Entity o) {
        return (int)(o.getHitboxY() - this.getHitboxY());
    }


    // Getters
    public double getWeight() {
        return this.getHitboxWidth() * this.getHitboxHeight();
    }

    public boolean isToDestroy() {
        return this.toDestroy;
    }

    public EntityType getType() {
        return this.type;
    }

    public double getHitboxWidth() {
        return this.width * this.hitboxWidthScale;
    }

    public double getHitboxHeight() {
        return this.height * this.hitboxHeightScale;
    }

    public double getHitboxX() {
        return this.x + this.width * this.hitboxOffsetX;
    }

    public double getHitboxY() {
        return this.y + this.height * this.hitboxOffsetY;
    }
}
