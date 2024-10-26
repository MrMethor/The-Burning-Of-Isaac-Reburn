package Tools;

import Engine.Wrap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class Button {

    private final Image image;
    private final TextBox label;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Button(Wrap wrap, String label, String path, int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.width = size;
        this.height = (int)(size / 480.0 * 100.0);

        Color color = Color.decode("#BBBBBB");
        this.label = new TextBox(wrap, label, x, y, color, "Castellar", Font.BOLD, height / 2, width, height);
        image = new Image(wrap, path, x, y, width, height);
    }

    public void render(Graphics g) {
        image.draw(g);
        label.draw(g);
    }

    public boolean isPressed(int x, int y) {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
    }

    public void changeImage(String path) {
        image.changeImage(path);
    }
}