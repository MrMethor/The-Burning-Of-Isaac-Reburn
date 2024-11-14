package Entities;

import Engine.Wrap;
import Enums.EntityType;

public class Projectile extends Entity {

    private int degree;

    /*********************************
     changeSpriteSheet
     changePosition
     changeSize
     changeHitboxOffset
     *********************************/

    public Projectile(Wrap wrap, double x, double y, double speed, int degree) {
        super(wrap, EntityType.PROJECTILE);
        this.degree = degree;
        changePosition(x, y);
        changeSpeed(speed);
    }
}
