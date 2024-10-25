package Tools;

import Engine.Wrap;
import Enums.Side;

import java.awt.*;

public class Hitbox {

    private Wrap wrap;

    private double x;
    private double y;
    private double width;
    private double height;
    private boolean isSolid;
    private boolean movable;

    private double[] collisions = {1, 1, 1, 1};

    public Hitbox(Wrap wrap, double x, double y, int width, int height, boolean solid, boolean movable) {
        this.wrap = wrap;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isSolid = solid;
        this.movable = movable;
    }

    public void move(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void collision(Hitbox other) {
        if (!movable)
            return;
        double[] sides = new double[4];
        if (other.isSolid()) {
            sides[Side.UP.num()] = -((other.getY() + other.getHeight() / 2) - (y - height / 2));
            sides[Side.DOWN.num()] = -((y + height / 2) - (other.getY() - other.getHeight() / 2));
            sides[Side.LEFT.num()] = -((other.getX() + other.getWidth() / 2) - (x - width / 2));
            sides[Side.RIGHT.num()] = -((x + width / 2) - (other.getX() - other.getWidth() / 2));
            if (sides[Side.UP.num()] <= 0 && sides[Side.DOWN.num()] <= 0 && sides[Side.LEFT.num()] <= 0 & sides[Side.RIGHT.num()] <= 0) {
                double biggest = -10000000;
                int smallestIndex = -1;
                for (int i = 0; i < 4; i++) {
                    if (sides[i] <= 0 && biggest < sides[i]) {
                        biggest = sides[i];
                        smallestIndex = i;
                    }
                }
                if (smallestIndex >= 0)
                    collisions[smallestIndex] = biggest <= 0 && biggest <= collisions[smallestIndex] ? biggest : collisions[smallestIndex];
            }
        }
        else {
            sides[Side.UP.num()] = (y - height / 2) - (other.getY() - other.getHeight() / 2);
            sides[Side.DOWN.num()] = (other.getY() + other.getHeight() / 2) - (y + height / 2);
            sides[Side.LEFT.num()] = (x - width / 2) - (other.getX() - other.getWidth() / 2);
            sides[Side.RIGHT.num()] = (other.getX() + other.getWidth() / 2) - (x + width / 2);
            for (int i = 0; i < 4; i++)
                collisions[i] = sides[i] <= 0 && sides[i] <= collisions[i] ? sides[i] : collisions[i];
        }
    }

    public void resetCollisions() {
        for (int i = 0; i < 4; i++)
            collisions[i] = 1;
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        if (isSolid)
            g.setColor(new Color(1f,0f,0f,.5f));
        else
            g.setColor(new Color(0f,1f,0f,.2f));
        g.fillRect((int)((x - width / 2) * wrap.getScale()), (int) ((y - height / 2) * wrap.getScale()), (int) (width * wrap.getScale()), (int) (height * wrap.getScale()));
        g.setColor(c);
    }

    public double getCollision(int side) {
        return collisions[side];
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isSolid() {
        return isSolid;
    }
}
