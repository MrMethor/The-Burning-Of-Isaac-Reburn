package tboir.entities.dynamic.physical;

import tboir.engine.Wrap;
import tboir.entities.dynamic.DynamicEntity;
import tboir.enums.EntityType;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

public abstract class PhysicalEntity extends DynamicEntity {

    protected double velocityX;
    protected double velocityY;
    protected double slideFactor;
    protected boolean flying;

    public PhysicalEntity(Wrap wrap, EntityManager entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.slideFactor = 0.9;
        this.speed = 1;
    }

    public PhysicalEntity(Wrap wrap, EntityManager entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.slideFactor = 0.9;
        this.speed = 1;
    }

    @Override
    protected void calculateMovement() {
        this.x += this.velocityX;
        this.y += this.velocityY;
        this.velocityX *= this.slideFactor;
        this.velocityY *= this.slideFactor;
    }

    protected void applyRelativeCollision(Collision collision) {
        double force = collision.entity().getWeight() / this.getWeight();
        switch (collision.side()) {
            case UP -> this.velocityY += force;
            case DOWN -> this.velocityY -= force;
            case LEFT -> this.velocityX += force;
            case RIGHT -> this.velocityX -= force;
        }
    }

    protected void applySolidCollision(Collision collision) {
        if (this.flying && collision.entity().getType() != EntityType.WALL) {
            return;
        }
        switch (collision.side()) {
            case UP -> {
                this.y -= collision.penetration();
                if (this.velocityY < 0)
                    this.velocityY = 0;
            }
            case DOWN -> {
                this.y += collision.penetration();
                if (this.velocityY > 0)
                    this.velocityY = 0;
            }
            case LEFT -> {
                this.x -= collision.penetration();
                if (this.velocityX < 0)
                    this.velocityX = 0;
            }
            case RIGHT -> {
                this.x += collision.penetration();
                if (this.velocityX > 0)
                    this.velocityX = 0;
            }
        }
    }

    protected void changeSlideFactor(double factor) {
        this.slideFactor = factor;
    }

    // Getter
    public boolean canFly() {
        return this.flying;
    }
}
