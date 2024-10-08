import fri.shapesge.Image;
import fri.shapesge.TextBlock;

public class Player {

    private Image texture;
    private double previousX;
    private double previousY;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private double speed;
    private double slideFactor = 0.9;
    private TextBlock debug = new TextBlock("", 50, 50);

    public Player() {
        this.x = 1920 / 2;
        this.y = 1080 / 2;
        this.speed = 0.5;
        this.texture = new Image("resource/player.png");
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
        this.texture.changePosition((int)this.x, (int)this.y);
    }

    public void move(int x, int y) {
        double compensation = 1;
        if (x != 0 && y != 0)
            compensation = 0.7;
        this.velocityX += x * speed * compensation;
        this.velocityY += y * speed * compensation;
    }

    public void render() {
        this.texture.changePosition((int)new Interpolation().interpolate(this.previousX, this.x), (int)new Interpolation().interpolate(this.previousY, this.y));
    }

    public void close(){
        this.texture.makeInvisible();
        this.debug.makeInvisible();
    }

}
