package Tools;

import Engine.Wrap;
import Enums.Side;

import java.awt.Color;
import java.awt.Graphics;

public class Hitbox {

    private Wrap wrap;

    private double x;
    private double y;
    private double width;
    private double height;
    private boolean solid;
    private boolean movable;
    private double offsetX = 0;
    private double offsetY = 0;
    private double trueX;
    private double trueY;

    private double[] solidCollisions = new double[4];
    private double receivedVelocityX = 0;
    private double receivedVelocityY = 0;

    public Hitbox(Wrap wrap, double x, double y, int width, int height, boolean solid, boolean movable) {
        this.wrap = wrap;
        this.x = x;
        this.y = y;
        trueX = x + offsetX;
        trueY = y + offsetY;
        this.width = width;
        this.height = height;
        this.solid = solid;
        this.movable = movable;
    }

    public void changeOffset(double x, double y) {
        this.offsetX = x;
        this.offsetY = y;
        trueX = x + offsetX;
        trueY = y + offsetY;
    }

    public void move(double x, double y) {
        this.x = x;
        this.y = y;
        trueX = x + offsetX;
        trueY = y + offsetY;
    }

    public void changeSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public boolean collision(Hitbox other) {
        if (!movable)
            return false;

        double[] sides = new double[4];

        if (other.solid) {
            sides[Side.UP.num()] = -((other.trueY + other.height / 2) - (trueY - height / 2));
            sides[Side.DOWN.num()] = -((trueY + height / 2) - (other.trueY - other.height / 2));
            sides[Side.LEFT.num()] = -((other.trueX + other.width / 2) - (trueX - width / 2));
            sides[Side.RIGHT.num()] = -((trueX + width / 2) - (other.trueX - other.width / 2));

            if ((sides[Side.UP.num()] < 0 && sides[Side.DOWN.num()] < 0) && (sides[Side.LEFT.num()] < 0 && sides[Side.RIGHT.num()] < 0)) {
                double penetration = -1920;
                Side sideOut = null;
                for (int i = 0; i < 4; i++) {
                    if (sides[i] <= 0 && penetration < sides[i]) {
                        penetration = sides[i];
                        sideOut = Side.getSide(i);
                    }
                }
                if (sideOut != null) {
                    if (other.movable) {
                        switch (sideOut) {
                            case UP -> {receivedVelocityY += 1; other.receivedVelocityY -= 1;}
                            case DOWN -> {receivedVelocityY -= 1; other.receivedVelocityY += 1;}
                            case LEFT -> {receivedVelocityX += 1; other.receivedVelocityX -= 1;}
                            case RIGHT -> {receivedVelocityX -= 1; other.receivedVelocityX += 1;}
                        }
                    }
                    else
                        solidCollisions[sideOut.num()] = Math.min(penetration, solidCollisions[sideOut.num()]);
                    return true;
                }
            }
        }
        else {
            sides[Side.UP.num()] = (trueY - height / 2) - (other.trueY - other.height / 2);
            sides[Side.DOWN.num()] = (other.trueY + other.height / 2) - (trueY + height / 2);
            sides[Side.LEFT.num()] = (trueX - width / 2) - (other.trueX - other.width / 2);
            sides[Side.RIGHT.num()] = (other.trueX + other.width / 2) - (trueX + width / 2);
            for (int i = 0; i < 4; i++)
                solidCollisions[i] = sides[i] < 0 && sides[i] < solidCollisions[i] ? sides[i] : solidCollisions[i];
        }
        return false;
    }

    public void resetCollisions() {
        for (int i = 0; i < 4; i++) {
            solidCollisions[i] = 0;
        }
        receivedVelocityX = 0;
        receivedVelocityY = 0;
    }

    public void render(Graphics g) {
        Color c = g.getColor();
        double trueX = x + offsetX;
        double trueY = y + offsetY;
        if (solid)
            g.setColor(new Color(1f,0f,0f,.5f));
        else
            g.setColor(new Color(0f,1f,0f,.2f));
        g.fillRect((int)((trueX - width / 2) * wrap.getScale()), (int) ((trueY - height / 2) * wrap.getScale()), (int) (width * wrap.getScale()), (int) (height * wrap.getScale()));
        g.setColor(c);
    }

    public double getSolidCollision(int side) {
        return solidCollisions[side];
    }

    public double getReceivedVelocityX() {
        return receivedVelocityX;
    }

    public double getReceivedVelocityY() {
        return receivedVelocityY;
    }
}
