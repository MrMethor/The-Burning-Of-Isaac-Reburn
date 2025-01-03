package Entities.Dynamic.Physical;

import Engine.Wrap;
import Entities.Dynamic.DynamicEntity;
import Enums.EntityType;
import Tools.Collision;
import Tools.EntityManager;

public abstract class PhysicalEntity extends DynamicEntity {

    protected double velocityX;
    protected double velocityY;
    protected double slideFactor;
    protected boolean flying;

    public PhysicalEntity(Wrap wrap, EntityManager entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        velocityX = 0.0;
        velocityY = 0.0;
        slideFactor = 0.9;
        speed = 1;
    }

    public PhysicalEntity(Wrap wrap, EntityManager entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        velocityX = 0.0;
        velocityY = 0.0;
        slideFactor = 0.9;
        speed = 1;
    }

    protected void calculateMovement() {
        x += velocityX;
        y += velocityY;
        velocityX *= slideFactor;
        velocityY *= slideFactor;
    }

    protected void applyRelativeCollision(Collision collision) {
        double force = collision.entity().getWeight() / getWeight();
        switch (collision.side()) {
            case UP -> velocityY += force;
            case DOWN -> velocityY -= force;
            case LEFT -> velocityX += force;
            case RIGHT -> velocityX -= force;
        }
    }

    protected void applySolidCollision(Collision collision) {
        if (flying && collision.entity().getType() != EntityType.WALL)
            return;
        switch (collision.side()) {
            case UP -> {
                y -= collision.penetration();
                if (velocityY < 0)
                    velocityY = 0;
            }
            case DOWN -> {
                y += collision.penetration();
                if (velocityY > 0)
                    velocityY = 0;
            }
            case LEFT -> {
                x -= collision.penetration();
                if (velocityX < 0)
                    velocityX = 0;
            }
            case RIGHT -> {
                x += collision.penetration();
                if (velocityX > 0)
                    velocityX = 0;
            }
        }
    }

    protected void changeSlideFactor(double factor) {
        this.slideFactor = factor;
    }

    public boolean canFly() {
        return flying;
    }
}
