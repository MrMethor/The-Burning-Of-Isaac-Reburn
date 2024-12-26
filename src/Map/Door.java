package Map;

import Engine.Wrap;
import Entities.Dynamic.Physical.Player;
import Enums.DoorType;
import Enums.Side;
import Tools.SpriteSheet;

import java.awt.*;

enum DoorState {
    CLOSED,
    OPENED
}

public class Door {

    private Wrap wrap;

    private Side side;
    private double size = 175;
    private int x;
    private int y;

    private SpriteSheet spriteSheet;

    public Door(Wrap wrap, Side side, DoorType type) {
        this.wrap = wrap;
        this.side = side;
        switch (side) {
            case LEFT -> {
                x = 238;
                y = 1080 / 2;
            }
            case RIGHT -> {
                x = 1682;
                y = 1080 / 2;
            }
            case UP -> {
                x = 1920 / 2;
                y = 105;
            }
            case DOWN -> {
                x = 1920 / 2;
                y = 975;
            }
        }
        String typeString = "";
        switch (type) {
            case BASEMENT -> typeString = "basement";
            case DEPTHS -> typeString = "depths";
            case GOLDEN -> typeString = "golden";
        }
        String path = "resource/doors/" + typeString + ".png";
        spriteSheet = new SpriteSheet(wrap, path, x - size / 2, y - size / 2, size, size, 4, 2);
        spriteSheet.swapImage(side.num(), DoorState.CLOSED.ordinal());
    }

    public boolean isCollided(Player player) {
        double[] sides = new double[4];

        sides[Side.UP.num()] = (y - size / 2) - (player.getHitboxY() + player.getHitboxHeight() / 2);
        sides[Side.DOWN.num()] = (player.getHitboxY() - player.getHitboxHeight() / 2) - (y + size / 2);
        sides[Side.LEFT.num()] = (x - size / 2) - (player.getHitboxX() + player.getHitboxWidth() / 2);
        sides[Side.RIGHT.num()] = (player.getHitboxX() - player.getHitboxWidth() / 2) - (x + size / 2);

        return (sides[Side.UP.num()] < 0 && sides[Side.DOWN.num()] < 0) && (sides[Side.LEFT.num()] < 0 && sides[Side.RIGHT.num()] < 0);
    }

    public void render(Graphics g) {
        spriteSheet.draw(g);
        if (wrap.isHitboxes())
            drawHitbox(g);
    }

    private void drawHitbox(Graphics g) {
        Color c = g.getColor();
        g.setColor(new Color(0f,0f,1f,.2f));
        g.fillRect((int)((x - size / 2.0) * wrap.getScale()), (int) ((y - size / 2.0) * wrap.getScale()), (int) (size * wrap.getScale()), (int) (size * wrap.getScale()));
        g.setColor(c);
    }

    public void openDoor() {
        spriteSheet.swapImage(side.num(), DoorState.OPENED.ordinal());
    }
}
