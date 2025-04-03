package tboir.hud;

import tboir.engine.Wrap;
import tboir.map.Map;
import tboir.map.Room;
import tboir.tools.Image;

import java.awt.Graphics2D;
import java.util.Arrays;

public class Hud {

    public static final int MAX_HEARTS = 12;

    private final Wrap wrap;
    private final Image[] hearts;
    private final Image[][] minimap;
    private final Image[][] mapIcons;

    public Hud(Wrap wrap) {
        this.wrap = wrap;
        this.hearts = new Image[MAX_HEARTS];
        this.minimap = new Image[Map.MAX_SIZE][Map.MAX_SIZE];
        this.mapIcons = new Image[Map.MAX_SIZE][Map.MAX_SIZE];
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

    public void render(Graphics2D g) {
        for (Image heart : this.hearts) {
            if (heart != null) {
                heart.draw(g);
            }
        }
        for (int i = 0; i < Map.MAX_SIZE; i++) {
            for (int j = 0; j < Map.MAX_SIZE; j++) {
                if (this.mapIcons[i][j] != null) {
                    this.minimap[i][j].draw(g);
                    this.mapIcons[i][j].draw(g);
                }
            }
        }
    }

    public void updateMap(int currentX, int currentY, Room[][] rooms) {
        for (int i = 0; i < Map.MAX_SIZE; i++) {
            Arrays.fill(this.mapIcons[i], null);
            Arrays.fill(this.minimap[i], null);
        }

        int minimapComponentSize = 50;
        int xDistance = (int)(minimapComponentSize - minimapComponentSize / 2.8);
        int yDistance = (int)(minimapComponentSize - minimapComponentSize / 2.5);
        int baseX = 1600;
        int baseY = 0;
        for (int i = 0; i < Map.MAX_SIZE; i++) {
            for (int j = 0; j < Map.MAX_SIZE; j++) {
                RoomState roomState = null;
                if (rooms[i][j] == null) {
                    continue;
                }
                if (i == currentX && j == currentY) {
                    roomState = RoomState.currentlyIn;
                } else if (rooms[i][j].isCompleted()) {
                    roomState = RoomState.completed;
                } else if (rooms[i][j].isExplored()) {
                    roomState = RoomState.explored;
                } else if ((i != 0 && rooms[i - 1][j] != null && rooms[i - 1][j].isExplored()) || (i + 1 != Map.MAX_SIZE && rooms[i + 1][j] != null && rooms[i + 1][j].isExplored()) || (j != 0 && rooms[i][j - 1] != null && rooms[i][j - 1].isExplored()) || (j + 1 != Map.MAX_SIZE && rooms[i][j + 1] != null && rooms[i][j + 1].isExplored())) {
                    roomState = RoomState.explored;
                }
                if (roomState != null) {
                    this.minimap[i][j] = new Image(this.wrap, roomState.ordinal(), 0, "mapIcons" , baseX + (i) * xDistance, baseY + (j) * yDistance, minimapComponentSize, minimapComponentSize);
                    this.mapIcons[i][j] = new Image(this.wrap, rooms[i][j].getType().ordinal(), 1, "mapIcons", baseX + (i) * xDistance, baseY + (j) * yDistance, minimapComponentSize, minimapComponentSize);
                }
            }
        }
    }

    private void addHearts(int column, int row, int index) {
        int size = 70;
        int spacing = size - 10;
        int x = 50 + (index % (12 / 2)) * spacing;
        int y = 50 + (index / 6 * spacing);
        this.hearts[index] = new Image(this.wrap, column, row, "hearts", x, y, size, size);
    }
}
