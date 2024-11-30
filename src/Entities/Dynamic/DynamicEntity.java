package Entities.Dynamic;

import Engine.Wrap;
import Entities.Entity;
import Enums.EntityType;
import Enums.Side;
import Tools.Collision;

import java.awt.*;
import java.util.ArrayList;

public abstract class DynamicEntity extends Entity {

    protected Entity room;

    protected double previousX;
    protected double previousY;
    protected double speed;

    public DynamicEntity(Wrap wrap, ArrayList<Entity> entities, Entity room, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.room = room;
        speed = 1;
    }

    public DynamicEntity(Wrap wrap, ArrayList<Entity> entities, Entity room, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.room = room;
        speed = 1;
    }

    public void update() {
        super.update();
        previousX = x;
        previousY = y;
        applyMovement();
    }

    protected void applyMovement() {}

    protected void collisions() {

        double[] sides = new double[4];

        sides[Side.UP.num()] = (getHitboxY() - getHitboxHeight() / 2) - (room.getHitboxY() - room.getHitboxHeight() / 2);
        sides[Side.DOWN.num()] = (room.getHitboxY() + room.getHitboxHeight() / 2) - (getHitboxY() + getHitboxHeight() / 2);
        sides[Side.LEFT.num()] = (getHitboxX() - getHitboxWidth() / 2) - (room.getHitboxX() - room.getHitboxWidth() / 2);
        sides[Side.RIGHT.num()] = (room.getHitboxX() + room.getHitboxWidth() / 2) - (getHitboxX() + getHitboxWidth() / 2);
        for (int i = 0; i < 4; i++) {
            if (sides[i] < 0)
                collisions.add(new Collision(EntityType.ROOM, Side.getSide(i), sides[i]));
        }
        super.collisions();
    }

    public void render(Graphics g) {
        double renderedX = wrap.interpolate(previousX, x) - width / 2.0;
        double renderedY = wrap.interpolate(previousY, y) - height / 2.0;
        texture.changePosition((int)renderedX, (int)renderedY);
        super.render(g);
    }

    // Setup options
    protected void move(double x, double y) {
        previousX = this.x;
        previousY = this.y;
        this.x = x;
        this.y = y;
    }

    protected void changePosition(double x, double y) {
        previousX = x;
        previousY = y;
        this.x = x;
        this.y = y;
    }

    protected void changeSpeed(double speed) {
        this.speed = speed;
    }
}
