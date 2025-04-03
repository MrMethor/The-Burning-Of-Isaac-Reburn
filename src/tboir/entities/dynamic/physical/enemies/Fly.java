package tboir.entities.dynamic.physical.enemies;

import tboir.engine.Wrap;
import tboir.entities.Entity;
import tboir.entities.EntityType;
import tboir.tools.EntityManager;

public class Fly extends Enemy {

    private Entity player;

    public Fly(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities,
                EntityType.ENEMY,
                5,
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
        if (this.waitInitially()) {
            return;
        }
        if (this.player == null) {
            for (Entity entity : this.getEntities().getEntities()) {
                if (entity.getType() == EntityType.PLAYER) {
                    this.player = entity;
                }
            }
            return;
        }
        double playerX = this.player.getHitboxX() - this.getHitboxX();
        double playerY = this.player.getHitboxY() - this.getHitboxY();

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
        if (this.player != null && this.player.getHitboxX() - getHitboxX() < 0) {
            this.mirrorHorizontally(true);
        }
    }
}
