package tboir.entities.dynamic.physical.enemies;

import tboir.engine.Wrap;
import tboir.entities.dynamic.physical.PhysicalEntity;
import tboir.entities.dynamic.physical.PickUp;
import tboir.entities.dynamic.projectiles.Projectile;
import tboir.entities.EntityType;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

import java.util.Random;

public abstract class Enemy extends PhysicalEntity {

    private double health;
    private int initialWait;
    private boolean[][] tileObstacleGrid;

    public Enemy(Wrap wrap, EntityManager entities, EntityType type, double health, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.health = health;
        this.initialWait = 20;
    }

    @Override
    protected void applyCollision(Collision collision) {
        switch (collision.entity().getType()) {
            case WALL -> this.applySolidCollision(collision);
            case PLAYER, ENEMY -> this.applyRelativeCollision(collision);
            case HALF_HEART, FULL_HEART, SOUL_HEART -> {
                if (!this.canFly()) {
                    this.applyRelativeCollision(collision);
                }
            }
            case SPIKE -> {
                if (!this.canFly()) {
                    this.health -= 0.1;
                }
            }
            case FRIENDLY_PROJECTILE -> {
                this.health -= ((Projectile)collision.entity()).getDamage();
                int addVelocityX = 0;
                int addVelocityY = 0;
                switch (collision.side()) {
                    case LEFT -> addVelocityX += 10;
                    case RIGHT -> addVelocityX -= 10;
                    case UP -> addVelocityY += 10;
                    case DOWN -> addVelocityY -= 10;
                }
                this.addVelocity(addVelocityX, addVelocityY);
            }
        }
        if (this.health <= 0) {
            this.dropRoll();
            this.destroy();
        }
    }

    private void dropRoll() {
        Random random = new Random();
        int chance = random.nextInt(8);
        if (chance == 0) {
            addEntity(new PickUp(this.getWrap(), this.getEntities(), EntityType.HALF_HEART, this.getX(), this.getY()));
        }
    }

    protected boolean waitInitially() {
        if (this.initialWait == 0) {
            return false;
        } else {
            this.initialWait--;
        }
        return true;
    }

    public void updateObstacleGrid(boolean[][] tileObstacleGrid) {
        this.tileObstacleGrid = tileObstacleGrid;
    }
}
