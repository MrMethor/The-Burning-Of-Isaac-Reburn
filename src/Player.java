import fri.shapesge.Image;

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
    private final Image texture = new Image("resource/character/character_idle.png");
    private int movingX;
    private int movingY;
    private long animationCounter;

    public Player() {
        this.texture.changePosition((int)this.x, (int)this.y);
        this.texture.makeVisible();
    }

    public void update() {
        this.previousX = this.x;
        this.previousY = this.y;
        this.x += this.velocityX;
        this.y += this.velocityY;
        this.velocityX *= slideFactor;
        this.velocityY *= slideFactor;
        animate();
    }

    public void move(int x, int y) {
        this.movingX = x;
        this.movingY = y;
        double compensation = 1;
        if (x != 0 && y != 0)
            compensation = 0.7;
        this.velocityX += x * speed * compensation;
        this.velocityY += y * speed * compensation;
    }

    public void firingDirection(boolean[] sides) {
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] && !this.firingOrder.contains(i))
                this.firingOrder.add(0, i);
            else if (!sides[i] && firingOrder.contains(i)) {
                this.firingOrder.remove(Integer.valueOf(i));
            }
        }
        if (!firingOrder.isEmpty())
            this.firing = Side.getSide(firingOrder.get(0));
        else
            this.firing = null;
    }

    public void render() {
        int width = 116;
        int height = 176;
        double renderedX = new Interpolation().interpolate(this.previousX, this.x) - (double)width / 2;
        double renderedY = new Interpolation().interpolate(this.previousY, this.y) - (double)height / 2;
        this.texture.changePosition((int)renderedX, (int)renderedY);
    }

    public void close() {
        this.texture.makeInvisible();
    }

    private void animate() {
        long frame = animationCounter / (long)(10 / this.speed) % 4 + 1;
        if (this.firing != null) {
            if (this.movingX == 0 && this.movingY == 0)
                this.texture.changeImage("resource/character/character_" + this.firing.str() + 2 + ".png");
            else
                this.texture.changeImage("resource/character/character_" + this.firing.str() + frame + ".png");
        }
        else if (this.movingY == -1)
            this.texture.changeImage("resource/character/character_up" + frame + ".png");
        else if (this.movingY == 1)
            this.texture.changeImage("resource/character/character_down" + frame + ".png");
        else if (this.movingX == -1)
            this.texture.changeImage("resource/character/character_left" + frame + ".png");
        else if (this.movingX == 1)
            this.texture.changeImage("resource/character/character_right" + frame + ".png");
        else
            this.texture.changeImage("resource/character/character_idle.png");

        this.animationCounter++;
    }

}
