package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Tools.Collision;
import Tools.EntityManager;

import java.util.Random;

public class Fire extends Entity {

    private int health = 30;

    public Fire(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.ENEMY, "resource/spriteSheets/fire.png", 7, 1, x, y, 130, 130, .3, .3, 0, .2);
    }

    protected void applyCollision(Collision collision) {
        if (health <= 0){
            return;
        }
        if (collision.entityType() == EntityType.FRIENDLY_PROJECTILE){
            Random rand = new Random();
            health -= rand.nextInt(5) + 3;
            if (health <= 0)
                destroy();
        }
    }

    public void animate() {
        int column = (int) (animationCounter / 10.0 % 7);
        swapTexture(column, 0);
        animationCounter++;
    }
}
