package Entities;

import Engine.Wrap;
import Tools.Collision;
import Tools.EntityList;
import Enums.EntityType;

import java.util.Random;

public class Poop extends Entity {

    private int health = 30;

    public Poop(Wrap wrap, EntityList entities, double x, double y) {
        super(wrap, entities, EntityType.OBSTACLE, "resource/spriteSheets/poop.png", 5, 1, x, y, 130, 130, 0.8, 0.8, 0, 0);
        swapTexture(0, 0);
    }

    protected void applyCollision(Collision collision) {
        if (health <= 0)
            return;
        if (collision.entityType() == EntityType.FRIENDLY_PROJECTILE){
            Random rand = new Random();
            health -= rand.nextInt(5) + 3;
            if (health <= 0){
                type = EntityType.VISUAL;
                swapTexture(4, 0);
                return;
            }
            swapTexture(3 - health / 10, 0);
        }
    }
}
