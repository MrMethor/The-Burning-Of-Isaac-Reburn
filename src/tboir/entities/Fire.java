package tboir.entities;

import tboir.engine.Wrap;
import tboir.enums.EntityType;
import tboir.tools.Collision;
import tboir.tools.EntityManager;

import java.util.Random;

public class Fire extends Entity {

    private int health;

    public Fire(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.ENEMY, "resource/entities/fire.png", 7, 1, x, y, 130, 130, .3, .3, 0, .2);
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
        int column = (int)(this.getAnimationCounter() / 10.0 % 7);
        this.swapTexture(column, 0);
        this.addToAnimationCounter();
    }
}
