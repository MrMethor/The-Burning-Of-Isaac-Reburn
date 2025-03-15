package tboir.entities.dynamic.physical;

import tboir.engine.Wrap;
import tboir.entities.dynamic.DynamicEntity;
import tboir.enums.EntityType;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

public abstract class PhysicalEntity extends DynamicEntity {

    private double velocityX;
    private double velocityY;
    private double slideFactor;
    private boolean flying;

    public PhysicalEntity(Wrap wrap, EntityManager entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.slideFactor = 0.9;
        this.changeSpeed(1);
    }

    public PhysicalEntity(Wrap wrap, EntityManager entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.slideFactor = 0.9;
        this.changeSpeed(1);
    }

    @Override
    protected void calculateMovement() {
        this.addToX(this.velocityX);
        this.addToY(this.velocityY);
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
                this.addToY(-collision.penetration());
                if (this.velocityY < 0) {
                    this.velocityY = 0;
                }
            }
            case DOWN -> {
                this.addToY(collision.penetration());
                if (this.velocityY > 0) {
                    this.velocityY = 0;
                }
            }
            case LEFT -> {
                this.addToX(-collision.penetration());
                if (this.velocityX < 0) {
                    this.velocityX = 0;
                }
            }
            case RIGHT -> {
                this.addToX(collision.penetration());
                if (this.velocityX > 0) {
                    this.velocityX = 0;
                }
            }
        }
    }

    protected void changeSlideFactor(double factor) {
        this.slideFactor = factor;
    }

    public void resetPosition() {
        this.changePosition(1920 / 2, 1080 / 2);
    }

    public void resetVelocity() {
        this.velocityX = 0;
        this.velocityY = 0;
    }

    protected void addVelocity(double velocityX, double velocityY) {
        this.velocityX += velocityX;
        this.velocityY += velocityY;
    }

    // Getter
    public boolean canFly() {
        return this.flying;
    }

    protected void canFly(boolean canFly) {
        this.flying = canFly;
    }
}
