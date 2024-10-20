package Tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.FontMetrics;

public class TextBox {

    private String text;
    private Font font;
    private Color color;
    private boolean centered;
    private int width;
    private int height;
    private int x;
    private int y;

    public TextBox(String text, int x, int y, Color color, String fontFamily, int decor, int size, int width, int height) {
        this.text = text;
        this.x = (int) (x * Screen.getScale());
        this.y = (int) (y * Screen.getScale());
        this.width = (int) (width * Screen.getScale());
        this.height = (int) (height * Screen.getScale());
        this.font = new Font(fontFamily, decor, (int)((double)size * Screen.getScale()));
        this.color = color;
    }

    public TextBox(String text, int x, int y, Color color, String fontFamily, int decor, int size) {
        this.text = text;
        this.x = (int) (x * Screen.getScale());
        this.y = (int) (y * Screen.getScale());
        this.width = (int) (width * Screen.getScale());
        this.height = (int) (height * Screen.getScale());
        this.font = new Font(fontFamily, decor, (int)((double)size * Screen.getScale()));
        this.color = color;
    }

    public void draw(Graphics g) {
        Color previousColor = g.getColor();
        Font previousFont = g.getFont();

        g.setColor(color);
        g.setFont(font);
        if (height == 0 || width == 0)
            g.drawString(text, x, y);
        else
            g.drawString(text, calculateX(g.getFontMetrics(font)), calculateY(g.getFontMetrics(font)));

        g.setColor(previousColor);
        g.setFont(previousFont);
    }

    public void changePosition(int x, int y) {
        this.x = (int) (x * Screen.getScale());
        this.y = (int) (y * Screen.getScale());
    }

    private int calculateX(FontMetrics metrics) {
        return x + (width - metrics.stringWidth(text)) / 2;
    }

    private int calculateY(FontMetrics metrics) {
        return y +  (height - metrics.getHeight()) / 2 + metrics.getAscent();
    }
}