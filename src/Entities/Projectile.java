package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Enums.Side;
import Tools.Collision;

import java.util.ArrayList;

public class Projectile extends DynamicEntity {

    private int angle;
    private Side facing;

    public Projectile(Wrap wrap, ArrayList<Entity> entities, Entity room, double x, double y, int width, int height, double speed, double velocityX, double velocity, int angle) {
        super(wrap, entities, room, EntityType.FRIENDLY_PROJECTILE, "resource/fireball.png", 4, 4, x, y, width, height, 0.2, 0.2, 0, 0);
        this.speed = speed;
        this.angle = angle;
        if (angle <= 45 && angle >= -45)
            facing = Side.RIGHT;
        else if (angle >= 45 && angle <= 90 + 45)
            facing = Side.UP;
        else if (angle >= 90 + 45 && angle <= 180 + 45)
            facing = Side.LEFT;
        else if (angle >= 180 + 45 && angle <= 270 + 45)
            facing = Side.DOWN;
    }

    protected void applyMovement() {
        double x = Math.cos(Math.toRadians(angle));
        double y = -Math.sin(Math.toRadians(angle));
        this.x += x * speed;
        this.y += y * speed;
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entityType()) {
            case ENEMY, ROOM -> destroy();
        }
    }

    protected void animate() {
        int column = (int) (animationCounter / 6 % 4);
        int row = facing.num();
        swapTexture(column, row);
        animationCounter++;
    }
}
