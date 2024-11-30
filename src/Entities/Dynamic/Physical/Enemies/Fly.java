package Entities.Dynamic.Physical.Enemies;

import Engine.Wrap;
import Entities.Entity;
import Enums.EntityType;
import Tools.Collision;

import java.util.ArrayList;

public class Fly extends Enemy {

    private Entity player;

    public Fly(Wrap wrap, ArrayList<Entity> entities, Entity room, double x, double y) {
        super(wrap, entities, room, EntityType.ENEMY, "resource/fly.png", 2, 2, x, y, 100, 100, .7, .7, 0, 0);
        changeSpeed(.5);
    }

    protected void applyBehavior() {
        if (player == null) {
            for (Entity entity : entities) {
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

    protected void applyMovement() {
        x += velocityX;
        y += velocityY;
        velocityX *= slideFactor;
        velocityY *= slideFactor;
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entityType()) {
            case ROOM -> applySolidCollision(collision);
            case PLAYER, ENEMY -> applyRelativeCollision(collision);
            case FRIENDLY_PROJECTILE -> health--;
        }
    }

    protected void animate() {
        int column = (int) (animationCounter / 10.0 % 2);
        double playerDistanceX = -1;

        if (player != null)
            playerDistanceX = player.getHitboxX() - getHitboxX();

        int row = playerDistanceX > 0 ? 0 : 1;

        swapTexture(column, row);
        animationCounter++;
    }
}
