package Entities.Dynamic.Physical.Enemies;

import Engine.Wrap;
import Entities.Entity;
import Enums.EntityType;
import Tools.EntityManager;

public class Fly extends Enemy {

    private Entity player;

    public Fly(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.ENEMY, 5, "resource/entities/fly.png", 2, 2, x, y, 100, 100, .7, .7, 0, 0);
        changeSpeed(.5);
        flying = true;
    }

    public void applyBehavior() {
        if (player == null) {
            for (Entity entity : entities.getEntities()) {
                if (entity.getType() == EntityType.PLAYER)
                    player = entity;
            }
            return;
        }
        double playerX = player.getHitboxX() - getHitboxX();
        double playerY = player.getHitboxY() - getHitboxY();

        double distance = (Math.abs(playerX) + Math.abs(playerY));

        double directionX = playerX / distance;
        double directionY = playerY / distance;

        velocityX += directionX * speed;
        velocityY += directionY * speed;
    }

    public void animate() {
        int column = (int) (animationCounter / 10.0 % 2);
        double playerDistanceX = -1;

        if (player != null)
            playerDistanceX = player.getHitboxX() - getHitboxX();

        int row = playerDistanceX > 0 ? 0 : 1;

        swapTexture(column, row);
        animationCounter++;
    }
}
