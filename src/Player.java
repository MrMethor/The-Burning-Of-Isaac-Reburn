import Engine.Wrap;
import Tools.Image;
import Enums.Side;
import Tools.SpriteSheet;

import java.awt.Graphics;
import java.util.ArrayList;

public class Player {

    private final Wrap wrap;

    // Movement
    private double previousX;
    private double previousY;
    private double x = (double)1920 / 2;
    private double y = (double)1080 / 2;
    private double velocityX;
    private double velocityY;
    private final double speed = 1;
    private final double slideFactor = 0.9;

    // Attack
    private final ArrayList<Integer> firingOrder = new ArrayList<>();
    private Side firing = null;

    // Animations
    private SpriteSheet spriteSheet;
    private Image texture;
    private int movingX;
    private int movingY;
    private long animationCounter;

    public Player(Wrap wrap) {
        this.wrap = wrap;
        spriteSheet = new SpriteSheet("resource/character.png", 3, 4);
        texture = new Image(wrap, spriteSheet.getSprite(1, 0), (int)x, (int)y, 150, 150);
    }

    public void update() {
        previousX = x;
        previousY = y;
        x += velocityX;
        y += velocityY;
        velocityX *= slideFactor;
        velocityY *= slideFactor;
        animate();
    }

    public void render(Graphics g) {
        int width = 116;
        int height = 176;
        double renderedX = wrap.interpolate(previousX, x) - (double)width / 2;
        double renderedY = wrap.interpolate(previousY, y) - (double)height / 2;
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
        int numFrame = (int)(animationCounter / (long)(10 / speed) % 4);
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