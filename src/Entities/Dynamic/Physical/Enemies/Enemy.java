package Entities.Dynamic.Physical.Enemies;

import Engine.Wrap;
import Entities.Dynamic.Physical.PhysicalEntity;
import Entities.Dynamic.Physical.PickUp;
import Entities.Dynamic.Projectiles.Projectile;
import Enums.EntityType;
import Tools.Collision;
import Tools.EntityManager;

import java.util.Random;

public abstract class Enemy extends PhysicalEntity {

    protected double health;

    public Enemy(Wrap wrap, EntityManager entities, EntityType type, double health, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.health = health;
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entity().getType()) {
            case WALL -> applySolidCollision(collision);
            case PLAYER, ENEMY -> applyRelativeCollision(collision);
            case HALF_HEART, FULL_HEART, SOUL_HEART -> {
                if (!flying)
                    applyRelativeCollision(collision);
            }
            case SPIKE -> {
                if (!flying)
                    health -= 0.1;
            }
            case FRIENDLY_PROJECTILE -> {
                health -= ((Projectile) collision.entity()).getDamage();
                switch(collision.side()) {
                    case LEFT -> velocityX += 10;
                    case RIGHT -> velocityX -= 10;
                    case UP -> velocityY += 10;
                    case DOWN -> velocityY -= 10;
                }
            }
        }
        if (health <= 0){
            dropRoll();
            destroy();
        }
    }

    private void dropRoll() {
        Random random = new Random();
        int chance = random.nextInt(8);
        if (chance == 0)
            entities.addEntity(new PickUp(wrap, entities, EntityType.HALF_HEART, x, y));
    }
}
