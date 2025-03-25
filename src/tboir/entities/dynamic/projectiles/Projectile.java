package tboir.entities.dynamic.projectiles;

import tboir.engine.Wrap;
import tboir.entities.dynamic.DynamicEntity;
import tboir.entities.EntityType;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

public abstract class Projectile extends DynamicEntity {

    private int angle;
    private double speed;
    private int timer;
    private double damage;

    public Projectile(Wrap wrap, EntityManager entities, boolean isFriendly, double damage, double range, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY, double speed, double velocityX, double velocity, int angle) {
        super (wrap, entities, isFriendly ? EntityType.FRIENDLY_PROJECTILE : EntityType.ENEMY_PROJECTILE, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = speed;
        this.angle = angle;
        this.damage = damage;
        this.timer = (int)(range * 60);
    }

    public Projectile(Wrap wrap, EntityManager entities, boolean isFriendly, double damage, double range, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY, double speed, double velocityX, double velocity, int angle) {
        super (wrap, entities, isFriendly ? EntityType.FRIENDLY_PROJECTILE : EntityType.ENEMY_PROJECTILE, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = speed;
        this.angle = angle;
        this.damage = damage;
        this.timer = (int)(range * 60);
    }

    @Override
    public void applyBehavior() {
        if (this.timer > 0) {
            this.timer--;
        } else {
            destroy();
        }
    }

    @Override
    protected void calculateMovement() {
        double x = Math.cos(Math.toRadians(this.angle));
        double y = -Math.sin(Math.toRadians(this.angle));
        this.addToX(x * this.speed);
        this.addToY(y * this.speed);
    }

    @Override
    protected void applyCollision(Collision collision) {
        switch (collision.entity().getType()) {
            case ENEMY, WALL, OBSTACLE -> this.destroy();
        }
    }

    // Getter
    public double getDamage() {
        return this.damage;
    }
}
