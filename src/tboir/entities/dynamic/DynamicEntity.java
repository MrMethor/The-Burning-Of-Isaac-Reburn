package tboir.entities.dynamic;

import tboir.engine.Wrap;
import tboir.entities.Entity;
import tboir.entities.EntityType;
import tboir.tools.EntityManager;

import java.awt.Graphics;

public abstract class DynamicEntity extends Entity {

    private double previousX;
    private double previousY;
    private double speed;

    public DynamicEntity(Wrap wrap, EntityManager entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = 1;
    }

    public DynamicEntity(Wrap wrap, EntityManager entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = 1;
    }

    protected abstract void calculateMovement();

    @Override
    public void render(Graphics g) {
        double renderedX = this.getWrap().interpolate(this.previousX, this.getX()) - this.getWidth() / 2.0;
        double renderedY = this.getWrap().interpolate(this.previousY, this.getY()) - this.getHeight() / 2.0;
        this.getTexture().changePosition((int)renderedX, (int)renderedY);
        super.render(g);
    }

    public void applyMovement() {
        this.previousX = this.getX();
        this.previousY = this.getY();
        this.calculateMovement();
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
        return this.speed;
    }
}
