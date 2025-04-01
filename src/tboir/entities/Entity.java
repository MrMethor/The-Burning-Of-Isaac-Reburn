package tboir.entities;

import tboir.engine.Wrap;
import tboir.engine.Side;
import tboir.tools.EntityManager;
import tboir.tools.Image;
import tboir.tools.Collision;
import tboir.tools.SpriteSheet;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;

public abstract class Entity implements Comparable<Entity> {

    private final Wrap wrap;
    private EntityManager entities;
    private EntityType type;

    private double x;
    private double y;
    private double width;
    private double height;

    private double hitboxWidthScale;
    private double hitboxHeightScale;
    private double hitboxOffsetX;
    private double hitboxOffsetY;

    private Image texture;
    private int animationCounter;

    private final ArrayList<Collision> collisions;

    public Entity(Wrap wrap, EntityManager entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        this.wrap = wrap;
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

    public void addCollision(Side side, double penetration, Entity entity) {
        this.collisions.add(new Collision(side, penetration, entity));
    }

    public void applyCollisions() {
        for (Collision collision : this.collisions) {
            this.applyCollision(collision);
        }
        this.resetCollisions();
    }

    protected abstract void applyCollision(Collision collision);

    public abstract void animate();

    public void render(Graphics2D g) {
        this.texture.draw(g);
        if (this.wrap.isHitboxes()) {
            this.drawHitbox(g);
        }
    }

    private void resetCollisions() {
        this.collisions.clear();
    }

    private void drawHitbox(Graphics2D g) {
        Color c = g.getColor();
        g.setColor(new Color(1f, 0f, 0f, .2f));
        g.fillRect((int)((this.getHitboxX() - this.getHitboxWidth() / 2) * this.wrap.getScale()), (int)((this.getHitboxY() - this.getHitboxHeight() / 2) * this.wrap.getScale()), (int)(this.getHitboxWidth() * this.wrap.getScale()), (int)(this.getHitboxHeight() * this.wrap.getScale()));
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
        if (this.texture instanceof SpriteSheet) {
            ((SpriteSheet)this.texture).swapImage(column, row);
        }
    }

    protected void addEntity(Entity newEntity) {
        this.entities.addEntity(newEntity);
    }

    public void destroy() {
        this.entities.destroy(this);
    }

    public int compareTo(Entity o) {
        return (int)(o.getHitboxY() - this.getHitboxY());
    }

    public void referenceEntities(EntityManager e) {
        this.entities = e;
    }

    public boolean hasEntities() {
        return this.entities != null;
    }


    // Protected Getters
    protected Wrap getWrap() {
        return this.wrap;
    }

    protected EntityManager getEntities() {
        return this.entities;
    }

    protected void changeType(EntityType newType) {
        this.type = newType;
    }

    protected long getAnimationCounter() {
        return this.animationCounter;
    }

    protected void addToAnimationCounter() {
        this.animationCounter++;
    }

    protected void setX(double x) {
        this.x = x;
    }

    protected void setY(double y) {
        this.y = y;
    }

    protected double getWidth() {
        return this.width;
    }

    protected double getHeight() {
        return this.height;
    }

    protected void addSize(double size) {
        this.width += size;
        this.height += size;
    }

    protected Image getTexture() {
        return this.texture;
    }


    // Public Getters
    public double getWeight() {
        return this.getHitboxWidth() * this.getHitboxHeight();
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

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}
