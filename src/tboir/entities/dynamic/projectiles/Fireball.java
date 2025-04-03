package tboir.entities.dynamic.projectiles;

import tboir.engine.Wrap;
import tboir.tools.EntityManager;

public class Fireball extends Projectile {

    public Fireball(Wrap wrap, EntityManager entities, double damage, double range, double x, double y, int width, int height, double speed, double velocityX, double velocity, int angle) {
        super(wrap, entities, true, damage, range, "fireball", x, y, width, height, .5, .5, 0, .3, speed, velocityX, velocity, angle);
        int rotation = angle / 90;
        if (rotation == 1) {
            rotation = 3;
        } else if (rotation == 3) {
            rotation = 1;
        }
        this.changeRotation(rotation);
    }

    @Override
    public void animate() {
        if (this.getWrap().isTimeToAnimate(10)) {
            this.getAnimation().incrementAndReset(3);
        }
    }
}
