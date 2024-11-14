package Entities;

import Engine.Component;
import Engine.Wrap;
import Enums.Side;
import Enums.EntityType;
import Tools.Hitbox;
import Tools.Image;
import Tools.SpriteSheet;

import java.awt.*;

public abstract class Entity implements Component, Comparable<Entity>{

    protected Wrap wrap;

    protected EntityType type;

    protected double previousX;
    protected double previousY;
    protected double x = 1920 / 2.0;
    protected double y = 1080 / 2.0;
    protected double velocityX = 0.0;
    protected double velocityY = 0.0;
    protected double slideFactor = 0.9;
    protected double speed = 1;

    protected Hitbox hitbox;
    protected int width = 150;
    protected int height = 150;

    protected SpriteSheet spriteSheet;
    protected Image texture;

    /*********************************
            changeSpriteSheet
            changePosition
            changeSlideFactor
            changeSize
            changeHitboxOffset
            changeSpeed
    *********************************/

    public Entity(Wrap wrap, EntityType type) {
        this.wrap = wrap;
        this.type = type;
        spriteSheet = null;
        texture = new Image(wrap, "resource/characterDefault.png", x, y, width, height);
        hitbox = new Hitbox(wrap, x, y, width, height, true, true);
    }

    public Entity(Wrap wrap, boolean solid, boolean movable) {
        this.wrap = wrap;
        spriteSheet = null;
        texture = new Image(wrap, "resource/characterDefault.png", x, y, width, height);
        hitbox = new Hitbox(wrap, x, y, width, height, solid, movable);
        if (!movable) {
            previousX = x;
            previousY = y;
        }
    }

    public void update() {
        applyBehavior();
        previousX = x;
        previousY = y;

        velocityX += hitbox.getReceivedVelocityX();
        velocityY += hitbox.getReceivedVelocityY();

        x += velocityX;
        y += velocityY;

        if (velocityX < 0 && hitbox.getSolidCollision(Side.LEFT.num()) < 0)
            x -= hitbox.getSolidCollision(Side.LEFT.num());
        else if (velocityX > 0 && hitbox.getSolidCollision(Side.RIGHT.num()) < 0)
            x += hitbox.getSolidCollision(Side.RIGHT.num());
        if (velocityY < 0 && hitbox.getSolidCollision(Side.UP.num()) < 0)
            y -= hitbox.getSolidCollision(Side.UP.num());
        else if (velocityY > 0 && hitbox.getSolidCollision(Side.DOWN.num()) < 0)
            y += hitbox.getSolidCollision(Side.DOWN.num());

        velocityX *= slideFactor;
        velocityY *= slideFactor;
        hitbox.move(x, y);
        animate();//country roads take me hooooome to the place i beloooong
        hitbox.resetCollisions();
    }

    public void render(Graphics g) {
        double renderedX = wrap.interpolate(previousX, x) - width / 2.0;
        double renderedY = wrap.interpolate(previousY, y) - height / 2.0;
        texture.changePosition((int)renderedX, (int)renderedY);
        texture.draw(g);
        if (wrap.isHitboxes())
            hitbox.render(g);
    }

    public void collision(Entity entity) {
        if (hitbox.collision(entity.hitbox))
            isTouched(entity.type);
    }

    protected void applyBehavior() {}

    protected void isTouched() {}

    protected void animate() {}

    protected void isTouched(EntityType type) {}

    protected void changeSpriteSheet(String path, int column, int row, int firstX, int firstY) {
        spriteSheet = new SpriteSheet(path, column, row);
        texture = new Image(wrap, spriteSheet.getSprite(firstX, firstY), x, y, width, height);
    }

    protected void changeTexture(String path) {
        texture = new Image(wrap, path, x, y, width, height);
    }

    protected void changePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected void changeSlideFactor(double factor) {
        this.slideFactor = factor;
    }

    protected void changeSpeed(double speed) {
        this.speed = speed;
    }

    protected void changeSize(int width, int height, double hitboxWidth, double hitboxHeight) {
        this.width = width;
        this.height = height;
        hitbox.changeSize(hitboxWidth * width, hitboxHeight * height);
        texture.changeSize(width, height);
    }

    protected void changeHitboxOffset(double offsetX, double offsetY) {
        hitbox.changeOffset(offsetX * height, offsetY * width);
    }

    @Override
    public int compareTo(Entity o) {
        return (int)(o.y - y);
    }
}
