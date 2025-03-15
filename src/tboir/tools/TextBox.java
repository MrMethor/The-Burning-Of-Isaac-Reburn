package tboir.tools;

import tboir.engine.Wrap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.FontMetrics;

public class TextBox {

    private final Wrap wrap;
    private String text;
    private final Font font;
    private Color color;
    private final Color defaultColor;
    private int width;
    private int height;
    private int x;
    private int y;

    public TextBox(Wrap wrap, String text, int x, int y, Color color, String fontFamily, int decor, int size, int width, int height) {
        this.wrap = wrap;
        this.text = text;
        this.x = (int)(x * wrap.getScale());
        this.y = (int)(y * wrap.getScale());
        this.width = (int)(width * wrap.getScale());
        this.height = (int)(height * wrap.getScale());
        this.font = new Font(fontFamily, decor, (int)((double)size * wrap.getScale()));
        this.defaultColor = color;
        this.color = color;
    }

    public TextBox(Wrap wrap, String text, int x, int y, Color color, String fontFamily, int decor, int size) {
        this.wrap = wrap;
        this.text = text;
        this.x = (int)(x * wrap.getScale());
        this.y = (int)(y * wrap.getScale());
        this.font = new Font(fontFamily, decor, (int)((double)size * wrap.getScale()));
        this.defaultColor = color;
        this.color = color;
    }

    public void draw(Graphics g) {
        Color previousColor = g.getColor();
        Font previousFont = g.getFont();

        g.setColor(this.color);
        g.setFont(this.font);
        if (this.height == 0 || this.width == 0) {
            g.drawString(this.text, this.x, this.y);
        } else {
            g.drawString(this.text, this.calculateX(g.getFontMetrics(this.font)), this.calculateY(g.getFontMetrics(this.font)));
        }

        g.setColor(previousColor);
        g.setFont(previousFont);
    }

    public void changeColor(Color c) {
        this.color = c;
    }

    public void changeColor() {
        this.color = this.defaultColor;
    }

    public void changePosition(int x, int y) {
        this.x = (int)(x * this.wrap.getScale());
        this.y = (int)(y * this.wrap.getScale());
    }

    public void changeText(String text) {
        this.text = text;
    }

    private int calculateX(FontMetrics metrics) {
        return this.x + (this.width - metrics.stringWidth(this.text)) / 2;
    }

    private int calculateY(FontMetrics metrics) {
        return this.y + (this.height - metrics.getHeight()) / 2 + metrics.getAscent();
    }
}