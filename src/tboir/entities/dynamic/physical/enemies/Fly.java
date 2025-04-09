package tboir.entities.dynamic.physical.enemies;

import tboir.engine.Wrap;
import tboir.entities.EntityType;
import tboir.map.EntityManager;

public class Fly extends Enemy {

    public Fly(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities,
                EntityType.ENEMY,
                3,
                "fly",
                x, y,
                100, 100,
                .7, .7,
                0, 0
        );
        this.changeSpeed(.5);
        this.canFly(true);
    }

    @Override
    public void applyBehavior() {
        if (this.waitInitially() || this.getPlayer() == null) {
            return;
        }
        double playerX = this.getPlayer().getHitboxX() - this.getHitboxX();
        double playerY = this.getPlayer().getHitboxY() - this.getHitboxY();

        double distance = (Math.abs(playerX) + Math.abs(playerY));

        double directionX = playerX / distance;
        double directionY = playerY / distance;

        this.addVelocity(directionX * this.getSpeed(), directionY * this.getSpeed());
    }

    @Override
    public void animate() {
        if (this.getWrap().isTimeToAnimate(10)) {
            this.getAnimation().incrementAndReset(1);
        }
        this.mirrorHorizontally(false);
        if (this.getPlayer() != null && this.getPlayer().getHitboxX() - getHitboxX() < 0) {
            this.mirrorHorizontally(true);
        }
    }
}
