import Engine.Wrap;
import Engine.Component;
import Tools.Hitbox;
import Tools.Image;
import Enums.Side;
import Tools.SpriteSheet;

import java.awt.Graphics;
import java.util.ArrayList;

public class Player implements Component {

    private final Wrap wrap;

    // Movement
    private double previousX;
    private double previousY;
    private double x = 1920 / 2.0;
    private double y = 1080 / 2.0;
    private double velocityX;
    private double velocityY;
    private final double speed = 1;
    private final double slideFactor = 0.9;
    private final Hitbox hitbox;
    private int size = 150;

    // Attack
    private final ArrayList<Integer> firingOrder = new ArrayList<>();
    private Side firing = null;

    // Animations
    private final SpriteSheet spriteSheet;
    private final Image texture;
    private int movingX;
    private int movingY;
    private long animationCounter;

    public Player(Wrap wrap, ArrayList<Hitbox> hitboxes) {
        this.wrap = wrap;
        spriteSheet = new SpriteSheet("resource/character.png", 3, 4);
        texture = new Image(wrap, spriteSheet.getSprite(1, 0), x, y, size, size);
        hitbox = new Hitbox(wrap, x, y, (int)(0.4 * size), (int)(0.5 * size), true, true);
        hitboxes.add(hitbox);
    }

    public void update() {
        previousX = x;
        previousY = y;
        x += velocityX;
        y += velocityY;
        if (velocityX < 0 && hitbox.getCollision(Side.LEFT.num()) < 0)
            x -= hitbox.getCollision(Side.LEFT.num());
        else if (velocityX > 0 && hitbox.getCollision(Side.RIGHT.num()) < 0)
            x += hitbox.getCollision(Side.RIGHT.num());
        if (velocityY < 0 && hitbox.getCollision(Side.UP.num()) < 0)
            y -= hitbox.getCollision(Side.UP.num());
        else if (velocityY > 0 && hitbox.getCollision(Side.DOWN.num()) < 0)
            y += hitbox.getCollision(Side.DOWN.num());
        velocityX *= slideFactor;
        velocityY *= slideFactor;
        hitbox.move(x, y + size / 4.0);
        animate();
    }

    public void render(Graphics g) {
        double renderedX = wrap.interpolate(previousX, x) - size / 2.0;
        double renderedY = wrap.interpolate(previousY, y) - size / 2.0;
        texture.changePosition((int)renderedX, (int)renderedY);
        texture.draw(g);
    }

    public void move(int x, int y) {
        movingX = x;
        movingY = y;
        double compensation = 1;
        if (x != 0 && y != 0)
            compensation = 0.7;
        velocityX += x * speed * compensation;
        velocityY += y * speed * compensation;
    }

    public void firingDirection(boolean[] sides) {
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] && !firingOrder.contains(i))
                firingOrder.add(0, i);
            else if (!sides[i] && firingOrder.contains(i))
                firingOrder.remove(Integer.valueOf(i));
        }
        if (!firingOrder.isEmpty())
            firing = Side.getSide(firingOrder.get(0));
        else
            firing = null;
    }

    private void animate() {
        int numFrame = (int)(animationCounter / (10.0 / speed) / (size / 150.0) % 4);
        int frame = numFrame == 3 ? 1 : numFrame;
        if (firing != null) {
            if (movingX == 0 && movingY == 0)
                texture.changeImage(spriteSheet.getSprite(1, firing.num()));
            else
                texture.changeImage(spriteSheet.getSprite(frame, firing.num()));
        }
        else if (movingY == -1)
            texture.changeImage(spriteSheet.getSprite(frame, 0));
        else if (movingY == 1)
            texture.changeImage(spriteSheet.getSprite(frame, 1));
        else if (movingX == -1)
            texture.changeImage(spriteSheet.getSprite(frame, 2));
        else if (movingX == 1)
            texture.changeImage(spriteSheet.getSprite(frame, 3));
        else
            texture.changeImage(spriteSheet.getSprite(1, 1));
        animationCounter++;
    }
}