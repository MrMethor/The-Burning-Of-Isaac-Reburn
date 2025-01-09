package tboir.tools;

import tboir.engine.Wrap;

import java.awt.Graphics;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Color;

public class Button {

    private final Wrap wrap;

    private final Image image;
    private final TextBox label;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private boolean hovered;

    public Button(Wrap wrap, String label, String path, int x, int y, int size) {
        this.wrap = wrap;
        this.x = x;
        this.y = y;
        this.width = size;
        this.height = (int)(size / 480.0 * 100.0);

        Color color = Color.decode("#BBBBBB");
        this.label = new TextBox(wrap, label, x, y, color, "Castellar", Font.BOLD, this.height / 2, this.width, this.height);
        this.image = new Image(wrap, path, x, y, this.width, this.height);
    }

    public void update() {
        boolean isHovered = this.isWithinBounds(this.wrap.getMouseX(), this.wrap.getMouseY());
        if (this.hovered != isHovered) {
            if (isHovered) {
                this.label.changeColor(Color.decode("#CFCFCF"));
            } else {
                this.label.changeColor();
            }
            this.hovered = isHovered;
        }
        if (this.hovered) {
            this.wrap.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    public void render(Graphics g) {
        this.image.draw(g);
        this.label.draw(g);
    }

    public boolean isWithinBounds(double x, double y) {
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height;
    }

    public void changeImage(String path) {
        this.image.changeImage(path);
    }
}