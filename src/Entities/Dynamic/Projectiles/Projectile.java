package Entities.Dynamic.Projectiles;

import Engine.Wrap;
import Entities.Dynamic.DynamicEntity;
import Enums.EntityType;
import Tools.Collision;
import Tools.EntityManager;

public abstract class Projectile extends DynamicEntity {

    protected int angle;
    protected double speed;
    private int timer;
    private double damage;

    public Projectile(Wrap wrap, EntityManager entities, boolean isFriendly, double damage, double range, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY, double speed, double velocityX, double velocity, int angle) {
        super (wrap, entities, isFriendly ? EntityType.FRIENDLY_PROJECTILE : EntityType.ENEMY_PROJECTILE, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = speed;
        this.angle = angle;
        this.damage = damage;
        timer = (int)(range * 60);
    }

    public Projectile(Wrap wrap, EntityManager entities, boolean isFriendly, double damage, double range, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY, double speed, double velocityX, double velocity, int angle) {
        super (wrap, entities, isFriendly ? EntityType.FRIENDLY_PROJECTILE : EntityType.ENEMY_PROJECTILE, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = speed;
        this.angle = angle;
        this.damage = damage;
        timer = (int)(range * 60);
    }

    public void applyBehavior() {
        if (timer > 0)
            timer--;
        else
            destroy();
    }

    protected void calculateMovement() {
        double x = Math.cos(Math.toRadians(angle));
        double y = -Math.sin(Math.toRadians(angle));
        this.x += x * speed;
        this.y += y * speed;
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entity().getType()) {
            case ENEMY, WALL, OBSTACLE -> destroy();
        }
    }

    public double getDamage() {
        return damage;
    }
}
