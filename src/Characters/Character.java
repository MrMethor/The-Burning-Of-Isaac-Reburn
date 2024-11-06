package Characters;

import Engine.Component;
import Engine.Wrap;
import Enums.Side;
import Tools.Hitbox;
import Tools.Image;
import Tools.SpriteSheet;

import java.awt.*;
import java.util.ArrayList;

public abstract class Character implements Component, Comparable<Character> {

    protected final Wrap wrap;

    // Movement
    protected double previousX;
    protected double previousY;
    protected double x = 1920 / 2.0;
    protected double y = 1080 / 2.0;
    protected Double velocityX = 0.0;
    protected Double velocityY = 0.0;
    protected double speed = 1;
    protected double slideFactor = 0.9;
    protected boolean enemy;

    // Body
    protected final Hitbox hitbox;
    protected int size = 150;

    // Attack
    protected final ArrayList<Integer> firingOrder = new ArrayList<>();
    protected Side facing = null;

    // Animations
    protected SpriteSheet spriteSheet;
    protected Image texture;

    public Character(Wrap wrap, boolean isEnemy) {
        this.wrap = wrap;
        enemy = isEnemy;
        spriteSheet = null;
        texture = new Image(wrap, "resource/characterDefault.png", x, y, size, size);
        hitbox = new Hitbox(wrap, x, y, size, size, true, true);
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
        animate();
    }

    public void render(Graphics g) {
        double renderedX = wrap.interpolate(previousX, x) - size / 2.0;
        double renderedY = wrap.interpolate(previousY, y) - size / 2.0;
        texture.changePosition((int)renderedX, (int)renderedY);
        texture.draw(g);
    }

    protected void applyBehavior() {

    }

    protected void animate() {}

    protected void changeSpriteSheet(String path, int column, int row, int firstX, int firstY) {
        spriteSheet = new SpriteSheet(path, column, row);
        texture = new Image(wrap, spriteSheet.getSprite(firstX, firstY), x, y, size, size);
    }

    protected void changePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected void changeSpeed(double speed) {
        this.speed = speed;
    }

    protected void changeSlideFactor(double factor) {
        this.slideFactor = factor;
    }

    protected void changeSize(int size, double hitboxWidth, double hitboxHeight) {
        this.size = size;
        hitbox.changeSize(hitboxWidth * size, hitboxHeight * size);
    }

    protected void changeHitboxOffset(double offsetX, double offsetY) {
        hitbox.changeOffset(offsetX * size, offsetY * size);
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public double getY() {
        return y;
    }

    @Override
    public int compareTo(Character o) {
        return (int)(o.getY() - y);
    }

}
