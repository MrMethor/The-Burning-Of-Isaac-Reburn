package Entities.Dynamic.Physical.Enemies;

import Engine.Wrap;
import Entities.Dynamic.Physical.PhysicalEntity;
import Enums.EntityType;
import Tools.Collision;
import Tools.EntityList;

public abstract class Enemy extends PhysicalEntity {

    protected double health = 10;

    public Enemy(Wrap wrap, EntityList entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entityType()) {
            case ROOM -> applySolidCollision(collision);
            case PLAYER, ENEMY -> applyRelativeCollision(collision);
            case FRIENDLY_PROJECTILE -> {
                health--;
                switch(collision.side()) {
                    case LEFT -> velocityX += 10;
                    case RIGHT -> velocityX -= 10;
                    case UP -> velocityY += 10;
                    case DOWN -> velocityY -= 10;
                }
            }
        }
        if (health <= 0)
            destroy();
    }
}
