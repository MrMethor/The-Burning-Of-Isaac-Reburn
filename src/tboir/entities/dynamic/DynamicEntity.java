package tboir.entities.dynamic;

import tboir.engine.Wrap;
import tboir.entities.Entity;
import tboir.entities.EntityType;
import tboir.tools.EntityManager;

import java.awt.Graphics2D;

public abstract class DynamicEntity extends Entity {

    public static final double SLOW_MODIFIER = 0.8;

    private double previousX;
    private double previousY;
    private double speed;
    private boolean slowed;

    public DynamicEntity(Wrap wrap, EntityManager entities, EntityType type, String name, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, name, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = 1;
    }

    public DynamicEntity(Wrap wrap, EntityManager entities, EntityType type, String spriteSheet, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, spriteSheet, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = 1;
    }

    protected abstract void calculateMovement();

    @Override
    public void render(Graphics2D g) {
        double renderedX = this.getWrap().interpolate(this.previousX, this.getX()) - this.getWidth() / 2.0;
        double renderedY = this.getWrap().interpolate(this.previousY, this.getY()) - this.getHeight() / 2.0;
        this.getTexture().changePosition((int)renderedX, (int)renderedY);
        super.render(g);
    }

    public void applyMovement() {
        this.previousX = this.getX();
        this.previousY = this.getY();
        this.calculateMovement();
        this.isSlowed(false);
    }

    // Setup options
    protected void move(double x, double y) {
        this.previousX = this.getX();
        this.previousY = this.getY();
        this.setX(x);
        this.setY(y);
    }

    protected void changePosition(double x, double y) {
        this.previousX = x;
        this.previousY = y;
        this.setX(x);
        this.setY(y);
    }

    protected void changeSpeed(double speed) {
        this.speed = speed;
    }

    protected void isSlowed(boolean slowed) {
        this.slowed = slowed;
    }

    // Getters
    protected void addToX(double addition) {
        this.setX(this.getX() + addition);
    }

    protected void addToY(double velocity) {
        this.setY(this.getY() + velocity);
    }

    protected void addSpeed(double addSpeed) {
        this.speed += addSpeed;
    }

    protected double getSpeed() {
        return this.slowed ? this.speed * SLOW_MODIFIER : this.speed;
    }
}
