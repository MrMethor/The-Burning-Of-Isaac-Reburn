package tboir.entities;

import tboir.engine.Wrap;
import tboir.map.EntityManager;
import tboir.tools.Collision;

import java.util.Random;

public class Fire extends Entity {

    private int health;

    public Fire(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities,
                EntityType.ENEMY,
                "fire",
                x, y,
                100, 100,
                .3, .3,
                0, .2
        );
        this.health = 15;
    }

    @Override
    public void applyBehavior() {
    }

    @Override
    protected void applyCollision(Collision collision) {
        if (this.health <= 0) {
            return;
        }
        if (collision.entity().getType() == EntityType.FRIENDLY_PROJECTILE) {
            Random rand = new Random();
            this.health -= rand.nextInt(5) + 3;
            if (this.health <= 0) {
                destroy();
            }
        }
    }

    @Override
    public void animate() {
        if (this.getWrap().isTimeToAnimate(10)) {
            this.getAnimation().incrementAndReset(5);
        }
    }
}
