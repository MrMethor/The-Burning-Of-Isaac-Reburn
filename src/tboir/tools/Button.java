package tboir.tools;

import tboir.engine.Wrap;

import java.awt.Graphics2D;
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
    private final boolean hoverable;
    private boolean isOn;

    public Button(Wrap wrap, String label, int x, int y, int size, boolean isOn, boolean hoverable) {
        this.wrap = wrap;
        this.x = x;
        this.y = y;
        this.width = size;
        this.height = (int)(size / 480.0 * 100.0);
        this.isOn = isOn;
        this.hoverable = hoverable;
        String texture = this.isOn ? "onButton" : "offButton";

        Color color = Color.decode("#BBBBBB");
        this.label = new TextBox(wrap, label, x, y, color, "Castellar", Font.BOLD, this.height / 2, this.width, this.height);
        this.image = new Image(wrap, texture, x, y, this.width, this.height);
    }

    public void update() {
        boolean isHovered = this.isWithinBounds(this.wrap.getMouseX(), this.wrap.getMouseY());
        if (!this.hoverable) {
            return;
        }
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

    public void render(Graphics2D g) {
        this.image.draw(g);
        this.label.draw(g);
    }

    public boolean isWithinBounds(double x, double y) {
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height;
    }

    public void toggle() {
        this.isOn = !this.isOn;
        String texture = this.isOn ? "onButton" : "offButton";
        this.image.changeImage(texture);
    }

    public void changeLabel(String newLabel) {
        this.label.changeText(newLabel);
    }

    public String getLabel() {
        return this.label.getText();
    }
}