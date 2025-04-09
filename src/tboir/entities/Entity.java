package tboir.entities;

import tboir.engine.Wrap;
import tboir.engine.Side;
import tboir.map.EntityManager;
import tboir.tools.Animation;
import tboir.tools.Image;
import tboir.tools.Collision;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
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

    private final ArrayList<Collision> collisions;

    public Entity(Wrap wrap, EntityManager entities, EntityType type, String name, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
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
        if (name == null) {
            this.texture = new Image(wrap, (BufferedImage)null, x - width / 2.0, y - height / 2.0, width, height);
        } else if (this.wrap.getResourceManager().getTexture(name) != null) {
            this.texture = new Image(wrap, name, x - width / 2.0, y - height / 2.0, width, height);
        } else if (this.wrap.getResourceManager().getAnimation(name) != null) {
            this.texture = new Animation(wrap, this.wrap.getResourceManager().getAnimation(name), x - width / 2.0, y - height / 2.0, width, height);
        } else {
            System.out.println("Couldn't find animationsheet or texture: " + name);
        }
    }

    public Entity(Wrap wrap, EntityManager entities, EntityType type, String spriteSheet, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
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
        this.texture = new Image(wrap, column, row, spriteSheet, x - width / 2.0, y - height / 2.0, width, height);
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
        g.fillRect((int)(this.getHitboxX() - this.getHitboxWidth() / 2), (int)(this.getHitboxY() - this.getHitboxHeight() / 2), (int)this.getHitboxWidth(), (int)this.getHitboxHeight());
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

    protected void changeImage(String texture) {
        this.texture.changeImage(texture);
    }

    protected void changeImage(String spriteSheet, int column, int row) {
        this.texture.changeImage(spriteSheet, column, row);
    }

    protected void changeRotation(int times) {
        this.texture.rotate(times);
    }

    protected void resetRotation() {
        this.texture.resetRotation();
    }

    protected void mirrorHorizontally(boolean mirror) {
        this.texture.mirrorHorizontally(mirror);
    }

    protected void mirrorVertically(boolean mirror) {
        this.texture.mirrorVertically(mirror);
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

    protected Animation getAnimation() {
        if (this.texture instanceof Animation) {
            return (Animation)this.texture;
        }
        return null;
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
