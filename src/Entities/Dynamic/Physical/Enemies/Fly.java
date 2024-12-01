package Entities.Dynamic.Physical.Enemies;

import Engine.Wrap;
import Entities.Entity;
import Map.Room;
import Enums.EntityType;
import Tools.EntityList;

public class Fly extends Enemy {

    private Entity player;

    public Fly(Wrap wrap, EntityList entities, Room room, double x, double y) {
        super(wrap, entities, room, EntityType.ENEMY, "resource/fly.png", 2, 2, x, y, 100, 100, .7, .7, 0, 0);
        changeSpeed(.5);
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
