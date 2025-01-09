package tboir.entities.dynamic.physical.enemies;

import tboir.engine.Wrap;
import tboir.entities.Entity;
import tboir.enums.EntityType;
import tboir.tools.EntityManager;

public class Fly extends Enemy {

    private Entity player;

    public Fly(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.ENEMY, 5, "resource/entities/fly.png", 2, 2, x, y, 100, 100, .7, .7, 0, 0);
        this.changeSpeed(.5);
        this.flying = true;
    }

    @Override
    public void applyBehavior() {
        if (this.player == null) {
            for (Entity entity : this.entities.getEntities()) {
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

        this.velocityX += directionX * this.speed;
        this.velocityY += directionY * this.speed;
    }

    @Override
    public void animate() {
        int column = (int) (this.animationCounter / 10.0 % 2);
        double playerDistanceX = -1;

        if (this.player != null)
            playerDistanceX = this.player.getHitboxX() - getHitboxX();

        int row = playerDistanceX > 0 ? 0 : 1;

        this.swapTexture(column, row);
        this.animationCounter++;
    }
}
