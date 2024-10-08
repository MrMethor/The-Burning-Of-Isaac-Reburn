import fri.shapesge.Image;
import fri.shapesge.TextBlock;

public class Player {

    // Texture
    private Image texture;
    private final int width = 116;
    private final int height = 176;

    // Movement
    private double previousX;
    private double previousY;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private double speed;
    private double slideFactor = 0.9;

    // Animations
    private int movingX;
    private int movingY;
    private long animationCounter;

    // Debug
    private TextBlock debug = new TextBlock("", 50, 50);

    public Player() {
        this.x = 1920 / 2;
        this.y = 1080 / 2;
        this.speed = 1;
        this.texture = new Image("resource/character/character_idle.png");
        this.texture.changePosition((int)this.x, (int)this.y);
        this.texture.makeVisible();
        this.debug.changeColor("white");
        this.debug.makeVisible();
    }

    public void update() {
        this.debug.changeText("x: " + x + "\ny: " + y);
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

    public void render() {
        double renderedX = new Interpolation().interpolate(this.previousX, this.x) - (double)width / 2;
        double renderedY = new Interpolation().interpolate(this.previousY, this.y) - (double)height / 2;
        this.texture.changePosition((int)renderedX, (int)renderedY);
    }

    public void close(){
        this.texture.makeInvisible();
        this.debug.makeInvisible();
    }

    private void animate() {
        long frame = animationCounter / (long)(10 / this.speed) % 4 + 1;
        if (this.movingY == -1)
            this.texture.changeImage("resource/character/character_back" + frame + ".png");
        else if (this.movingY == 1)
            this.texture.changeImage("resource/character/character_front" + frame + ".png");
        else if (this.movingX == -1)
            this.texture.changeImage("resource/character/character_left" + frame + ".png");
        else if (this.movingX == 1)
            this.texture.changeImage("resource/character/character_right" + frame + ".png");
        else
            this.texture.changeImage("resource/character/character_idle.png");

        this.animationCounter++;
    }

}
