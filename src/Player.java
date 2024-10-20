import Tools.Image;
import Tools.Interpolation;
import Tools.Screen;
import Enums.Side;

import java.awt.*;
import java.util.ArrayList;

public class Player {

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
    private Tools.Image texture = new Image("resource/character/character_idle.png", Screen.WIDTH / 2, Screen.HEIGHT / 2, 116, 176);
    private int movingX;
    private int movingY;
    private long animationCounter;

    public void update() {
        previousX = x;
        previousY = y;
        x += velocityX;
        y += velocityY;
        velocityX *= slideFactor;
        velocityY *= slideFactor;
        animate();
    }

    public void render(Graphics g, Interpolation interpolation) {
        int width = 116;
        int height = 176;
        double renderedX = interpolation.interpolate(previousX, x) - (double)width / 2;
        double renderedY = interpolation.interpolate(previousY, y) - (double)height / 2;
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
        long frame = animationCounter / (long)(10 / speed) % 4 + 1;
        String defaultPath = "resource/character/character_";
        if (firing != null) {
            if (movingX == 0 && movingY == 0)
                texture.changeImage(defaultPath + firing.str() + 2 + ".png");
            else
                texture.changeImage(defaultPath + firing.str() + frame + ".png");
        }
        else if (movingY == -1)
            texture.changeImage(defaultPath + "up" + frame + ".png");
        else if (movingY == 1)
            texture.changeImage(defaultPath + "down" + frame + ".png");
        else if (movingX == -1)
            texture.changeImage(defaultPath + "left" + frame + ".png");
        else if (movingX == 1)
            texture.changeImage(defaultPath + "right" + frame + ".png");
        else
            texture.changeImage(defaultPath + "idle.png");
        animationCounter++;
    }

}