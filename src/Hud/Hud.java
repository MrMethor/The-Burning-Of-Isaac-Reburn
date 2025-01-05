package Hud;

import Engine.Wrap;
import Tools.SpriteSheet;
import Tools.Image;

import java.awt.Graphics;
import java.util.Arrays;

public class Hud {

    private Wrap wrap;
    private SpriteSheet spriteSheet;
    Image[] hearts = new Image[12];

    public Hud(Wrap wrap) {
        this.wrap = wrap;
        spriteSheet = new SpriteSheet(wrap, "resource/hud/hearts.png", 0, 0, 48, 32, 3, 2);
    }

    public void updateHearts(int redHearts, int containers, int soulHearts) {
        Arrays.fill(hearts, null);

        int fullSoul = soulHearts / 2;
        int fullRed = redHearts / 2;
        int halfSoul = soulHearts % 2;
        int halfRed = redHearts % 2;
        int empty = (containers - redHearts) / 2;

        int c = 0;
        for (int i = 0; i < fullRed; i++, c++)
            addHearts(0, 0, c);
        for (int i = 0; i < halfRed; i++, c++)
            addHearts(1, 0, c);
        for (int i = 0; i < empty; i++, c++)
            addHearts(2, 0, c);
        for (int i = 0; i < fullSoul; i++, c++)
            addHearts(0, 1, c);
        for (int i = 0; i < halfSoul; i++, c++)
            addHearts(1, 1, c);
    }

    public void render(Graphics g) {
        for (Image heart : hearts) {
            if (heart != null)
                heart.draw(g);
        }
    }

    private void addHearts(int column, int row, int index) {
        int size = 70;

        int spacing = size - 10;

        int x = 50 + (index % (12 / 2)) * spacing;
        int y = 50 + (index / 6 * spacing);

        hearts[index] = new Image(wrap, spriteSheet.getImage(column, row), x, y, size, size);
    }
}
