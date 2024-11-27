package Tools;

import Engine.Wrap;

import java.awt.*;

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
        this.label = new TextBox(wrap, label, x, y, color, "Castellar", Font.BOLD, height / 2, width, height);
        image = new Image(wrap, path, x, y, width, height);
    }

    public void update() {
        boolean isHovered = isWithinBounds(wrap.getMouseX(), wrap.getMouseY());
        if (hovered != isHovered) {
            if (isHovered)
                label.changeColor(Color.decode("#CFCFCF"));
            else
                label.changeColor();
            hovered = isHovered;
        }
        if (hovered)
            wrap.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void render(Graphics g) {
        image.draw(g);
        label.draw(g);
    }

    public boolean isWithinBounds(double x, double y) {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
    }

    public void changeImage(String path) {
        image.changeImage(path);
    }
}