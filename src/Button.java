import Tools.Image;
import Tools.Interpolation;
import Tools.TextBox;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class Button{

    private final Image image;
    private final TextBox label;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Button(String label, String path, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        Color color = Color.decode("#BBBBBB");
        this.label = new TextBox(label, x, y, color, "Castellar", Font.BOLD, height / 2, width, height);
        image = new Image(path, x, y, width, height);
    }

    public void render(Graphics g, Interpolation interpolation) {
        image.draw(g);
        label.draw(g);
    }

    public boolean isPressed(int x, int y) {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
    }
}