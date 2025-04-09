package tboir.entities;

import tboir.engine.Wrap;
import tboir.map.EntityManager;
import tboir.tools.Collision;

import java.util.Random;

public class Poop extends Entity {

    private int health;

    public Poop(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities,
                EntityType.OBSTACLE,
                "objects",
                0, 1,
                x, y,
                120, 120,
                0.8, 0.8,
                0, 0
        );
        this.health = 30;
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
                this.changeType(EntityType.VISUAL);
                this.changeImage("objects", 4, 1);
                return;
            }
            this.changeImage("objects", 3 - this.health / 10, 1);
        }
    }

    @Override
    public void animate() {
    }
}
