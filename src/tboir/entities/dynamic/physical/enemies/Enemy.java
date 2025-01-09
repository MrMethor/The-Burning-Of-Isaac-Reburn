package tboir.entities.dynamic.physical.enemies;

import tboir.engine.Wrap;
import tboir.entities.dynamic.physical.PhysicalEntity;
import tboir.entities.dynamic.physical.PickUp;
import tboir.entities.dynamic.projectiles.Projectile;
import tboir.enums.EntityType;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

import java.util.Random;

public abstract class Enemy extends PhysicalEntity {

    protected double health;

    public Enemy(Wrap wrap, EntityManager entities, EntityType type, double health, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.health = health;
    }

    @Override
    protected void applyCollision(Collision collision) {
        switch (collision.entity().getType()) {
            case WALL -> this.applySolidCollision(collision);
            case PLAYER, ENEMY -> this.applyRelativeCollision(collision);
            case HALF_HEART, FULL_HEART, SOUL_HEART -> {
                if (!flying) {
                    this.applyRelativeCollision(collision);
                }
            }
            case SPIKE -> {
                if (!flying) {
                    health -= 0.1;
                }
            }
            case FRIENDLY_PROJECTILE -> {
                this.health -= ((Projectile) collision.entity()).getDamage();
                switch(collision.side()) {
                    case LEFT -> this.velocityX += 10;
                    case RIGHT -> this.velocityX -= 10;
                    case UP -> this.velocityY += 10;
                    case DOWN -> this.velocityY -= 10;
                }
            }
        }
        if (this.health <= 0){
            dropRoll();
            destroy();
        }
    }

    private void dropRoll() {
        Random random = new Random();
        int chance = random.nextInt(8);
        if (chance == 0) {
            this.entities.addEntity(new PickUp(this.wrap, this.entities, EntityType.HALF_HEART, this.x, this.y));
        }
    }
}
