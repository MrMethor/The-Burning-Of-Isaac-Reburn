package tboir.tools;

import tboir.engine.Wrap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.FontMetrics;

public class TextBox {

    private final Wrap wrap;
    private String text;
    private int size;
    private final String fontFamily;
    private final int decor;
    private Color color;
    private final Color defaultColor;
    private int width;
    private int height;
    private int x;
    private int y;

    public TextBox(Wrap wrap, String text, int x, int y, Color color, String fontFamily, int decor, int size, int width, int height) {
        this.wrap = wrap;
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.defaultColor = color;
        this.color = color;
        this.size = size;
        this.fontFamily = fontFamily;
        this.decor = decor;
    }

    public TextBox(Wrap wrap, String text, int x, int y, Color color, String fontFamily, int decor, int size) {
        this.wrap = wrap;
        this.text = text;
        this.x = x;
        this.y = y;
        this.defaultColor = color;
        this.color = color;
        this.size = size;
        this.fontFamily = fontFamily;
        this.decor = decor;

    }

    public void draw(Graphics2D g) {
        Color previousColor = g.getColor();
        Font previousFont = g.getFont();

        Font font = new Font(this.fontFamily, this.decor, (int)(this.size * this.wrap.getScale()));
        Font fontCheck = new Font(this.fontFamily, this.decor, this.size);
        g.setColor(this.color);
        g.setFont(font);
        if (this.height == 0 || this.width == 0) {
            g.drawString(this.text, (int)(this.x * this.wrap.getScale()), (int)(this.y * this.wrap.getScale()));
        } else {
            g.drawString(this.text, (int)((this.calculateX(g.getFontMetrics(fontCheck))) * this.wrap.getScale()), (int)(this.calculateY(g.getFontMetrics(fontCheck)) * this.wrap.getScale()));
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
        this.x = x;
        this.y = y;
    }

    public void changeSize(int size) {
        this.size = size;
    }

    public void changeText(String text) {
        this.text = text;
    }

    private int calculateX(FontMetrics metrics) {
        return this.x + ((this.width - metrics.stringWidth(this.text)) / 2);
    }

    private int calculateY(FontMetrics metrics) {
        return this.y + ((this.height - metrics.getHeight()) / 2) + metrics.getAscent();
    }

    public String getText() {
        return this.text;
    }
}