package Entities.Dynamic.Physical;

import Engine.Wrap;
import Entities.Dynamic.DynamicEntity;
import Entities.Entity;
import Enums.EntityType;
import Tools.Collision;

import java.util.ArrayList;

public abstract class PhysicalEntity extends DynamicEntity {

    protected double velocityX;
    protected double velocityY;
    protected double slideFactor;

    public PhysicalEntity(Wrap wrap, ArrayList<Entity> entities, Entity room, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, room, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.room = room;
        velocityX = 0.0;
        velocityY = 0.0;
        slideFactor = 0.9;
        speed = 1;
    }

    public PhysicalEntity(Wrap wrap, ArrayList<Entity> entities, Entity room, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, room, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.room = room;
        velocityX = 0.0;
        velocityY = 0.0;
        slideFactor = 0.9;
        speed = 1;
    }


    protected void applyRelativeCollision(Collision collision) {
        switch (collision.side()) {
            case UP -> velocityY += 1;
            case DOWN -> velocityY -= 1;
            case LEFT -> velocityX += 1;
            case RIGHT -> velocityX -= 1;
        }
    }

    protected void applySolidCollision(Collision collision) {
        switch (collision.side()) {
            case UP -> {
                if (velocityY < 0 && collision.penetration() < 0)
                    y -= collision.penetration();
            }
            case DOWN -> {
                if (velocityY > 0 && collision.penetration() < 0)
                    y += collision.penetration();
            }
            case LEFT -> {
                if (velocityX < 0 && collision.penetration() < 0)
                    x -= collision.penetration();
            }
            case RIGHT -> {
                if (velocityX > 0 && collision.penetration() < 0)
                    x += collision.penetration();
            }
        }
    }

    protected void changeSlideFactor(double factor) {
        this.slideFactor = factor;
    }

}
