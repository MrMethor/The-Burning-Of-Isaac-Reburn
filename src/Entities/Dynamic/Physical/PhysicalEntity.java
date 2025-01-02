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
        switch (collision.side()) {
            case UP -> velocityY += 1;
            case DOWN -> velocityY -= 1;
            case LEFT -> velocityX += 1;
            case RIGHT -> velocityX -= 1;
        }
    }

    protected void applySolidCollision(Collision collision) {
        if (flying && collision.entityType() != EntityType.WALL)
            return;
        switch (collision.side()) {
            case UP -> {
                if (velocityY < 0){
                    y -= collision.penetration();
                    velocityY = 0;
                }
            }
            case DOWN -> {
                if (velocityY > 0){
                    y += collision.penetration();
                    velocityY = 0;
                }
            }
            case LEFT -> {
                if (velocityX < 0){
                    x -= collision.penetration();
                    velocityX = 0;
                }
            }
            case RIGHT -> {
                if (velocityX > 0){
                    x += collision.penetration();
                    velocityX = 0;
                }
            }
        }
    }

    protected void changeSlideFactor(double factor) {
        this.slideFactor = factor;
    }
}
