package Entities.Dynamic.Projectiles;

import Engine.Wrap;
import Map.Room;
import Enums.Side;
import Tools.EntityList;

public class Fireball extends Projectile {

    private Side facing;

    public Fireball(Wrap wrap, EntityList entities, Room room, double x, double y, int width, int height, double speed, double velocityX, double velocity, int angle) {
        super(wrap, entities, room, true, "resource/fireball.png", 4, 4, x, y, width, height, .5, .5, 0, 0, speed, velocityX, velocity, angle);
        if (angle <= 45 && angle >= -45)
            facing = Side.RIGHT;
        else if (angle >= 45 && angle <= 90 + 45)
            facing = Side.UP;
        else if (angle >= 90 + 45 && angle <= 180 + 45)
            facing = Side.LEFT;
        else if (angle >= 180 + 45 && angle <= 270 + 45)
            facing = Side.DOWN;
    }

    public void animate() {
        int column = (int) (animationCounter / 6 % 4);
        int row = facing.num();
        swapTexture(column, row);
        animationCounter++;
    }
}