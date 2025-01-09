package tboir.entities.dynamic.projectiles;

import tboir.engine.Wrap;
import tboir.enums.Side;
import tboir.tools.EntityManager;

public class Fireball extends Projectile {

    private Side facing;

    public Fireball(Wrap wrap, EntityManager entities, double damage, double range, double x, double y, int width, int height, double speed, double velocityX, double velocity, int angle) {
        super(wrap, entities, true, damage, range, "resource/entities/fireball.png", 4, 4, x, y, width, height, .5, .5, 0, .3, speed, velocityX, velocity, angle);
        if (angle <= 45 && angle >= -45) {
            this.facing = Side.RIGHT;
        } else if (angle >= 45 && angle <= 90 + 45) {
            this.facing = Side.UP;
        } else if (angle >= 90 + 45 && angle <= 180 + 45) {
            this.facing = Side.LEFT;
        } else if (angle >= 180 + 45 && angle <= 270 + 45) {
            this.facing = Side.DOWN;
        }
    }

    @Override
    public void animate() {
        int column = (int) (this.animationCounter / 6 % 4);
        int row = this.facing.num();
        this.swapTexture(column, row);
        this.animationCounter++;
    }
}
