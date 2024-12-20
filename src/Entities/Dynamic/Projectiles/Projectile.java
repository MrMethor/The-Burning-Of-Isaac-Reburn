package Entities.Dynamic.Projectiles;

import Engine.Wrap;
import Entities.Dynamic.DynamicEntity;
import Enums.EntityType;
import Tools.Collision;
import Tools.EntityList;

public abstract class Projectile extends DynamicEntity {

    protected int angle;
    protected double speed;

    public Projectile(Wrap wrap, EntityList entities, boolean isFriendly, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY, double speed, double velocityX, double velocity, int angle) {
        super (wrap, entities, isFriendly ? EntityType.FRIENDLY_PROJECTILE : EntityType.ENEMY_PROJECTILE, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = speed;
        this.angle = angle;
    }

    public Projectile(Wrap wrap, EntityList entities, boolean isFriendly, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY, double speed, double velocityX, double velocity, int angle) {
        super (wrap, entities, isFriendly ? EntityType.FRIENDLY_PROJECTILE : EntityType.ENEMY_PROJECTILE, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = speed;
        this.angle = angle;
    }

    protected void calculateMovement() {
        double x = Math.cos(Math.toRadians(angle));
        double y = -Math.sin(Math.toRadians(angle));
        this.x += x * speed;
        this.y += y * speed;
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entityType()) {
            case ENEMY, ROOM, OBSTACLE -> destroy();
        }
    }
}
