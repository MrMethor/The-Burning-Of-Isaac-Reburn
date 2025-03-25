package tboir.entities;

import tboir.engine.Wrap;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

import java.util.Random;

public class Poop extends Entity {

    private int health;

    public Poop(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.OBSTACLE, "resource/entities/poop.png", 5, 1, x, y, 130, 130, 0.8, 0.8, 0, 0);
        this.swapTexture(0, 0);
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
                this.swapTexture(4, 0);
                return;
            }
            this.swapTexture(3 - this.health / 10, 0);
        }
    }

    @Override
    public void animate() {
    }
}
