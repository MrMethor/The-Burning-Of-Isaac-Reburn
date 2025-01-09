package tboir.hud;

import tboir.engine.Wrap;
import tboir.tools.SpriteSheet;
import tboir.tools.Image;

import java.awt.Graphics;
import java.util.Arrays;

public class Hud {

    private final Wrap wrap;
    private final SpriteSheet spriteSheet;
    private final Image[] hearts;

    public Hud(Wrap wrap) {
        this.wrap = wrap;
        this.hearts = new Image[12];
        this.spriteSheet = new SpriteSheet(this.wrap, "resource/hud/hearts.png", 0, 0, 48, 32, 3, 2);
    }

    public void updateHearts(int redHearts, int containers, int soulHearts) {
        Arrays.fill(this.hearts, null);

        int fullSoul = soulHearts / 2;
        int fullRed = redHearts / 2;
        int halfSoul = soulHearts % 2;
        int halfRed = redHearts % 2;
        int empty = (containers - redHearts) / 2;

        int c = 0;
        for (int i = 0; i < fullRed; i++, c++) {
            this.addHearts(0, 0, c);
        }
        for (int i = 0; i < halfRed; i++, c++) {
            this.addHearts(1, 0, c);
        }
        for (int i = 0; i < empty; i++, c++) {
            this.addHearts(2, 0, c);
        }
        for (int i = 0; i < fullSoul; i++, c++) {
            this.addHearts(0, 1, c);
        }
        for (int i = 0; i < halfSoul; i++, c++) {
            this.addHearts(1, 1, c);
        }
    }

    public void render(Graphics g) {
        for (Image heart : this.hearts) {
            if (heart != null) {
                heart.draw(g);
            }
        }
    }

    private void addHearts(int column, int row, int index) {
        int size = 70;

        int spacing = size - 10;

        int x = 50 + (index % (12 / 2)) * spacing;
        int y = 50 + (index / 6 * spacing);

        this.hearts[index] = new Image(this.wrap, this.spriteSheet.getImage(column, row), x, y, size, size);
    }
}
