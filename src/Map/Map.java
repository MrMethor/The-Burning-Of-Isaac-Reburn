package Map;

// rooms: starter, golden, sacrifice, shop, boss???

import Engine.Wrap;
import Entities.Dynamic.Physical.Player;
import Enums.Side;

import java.util.Random;

public class Map {

    private Wrap wrap;

    private Player player;

    private int size = 9;
    private int roomCount;
    private boolean hasGolden;
    private Room[] rooms = new Room[size * size];

    private int currentX = 5;
    private int currentY = 5;

    public Map(Wrap wrap, Player player, int floor, boolean isGolden) {
        this.wrap = wrap;
        this.player = player;
        this.hasGolden = isGolden;
        generateMap(floor);
    }

    public void generateMap(int floor) {
        Random random = new Random();
        switch(floor) {
            case 3, 4 -> roomCount = 16 + random.nextInt(4);
            case 5, 6 -> roomCount = 32 + random.nextInt(8);
            default -> roomCount = 8 + random.nextInt(1);
        }

        for (int i = 0; i < rooms.length; i++)
            rooms[i] = new Room(wrap, "resource/rooms/starterRoom.txt");

        rooms[currentX + currentY * size] = new Room(wrap, "resource/rooms/starterRoom.txt");
        currentRoom().entities.removePlayer();
        player.referenceEntities(null);
        currentRoom().entities.addPlayer(player);
        player.referenceEntities(currentRoom().entities);
    }

    public void changeRoom(Side side) {
        currentRoom().entities.removePlayer();
        player.referenceEntities(null);
        switch(side) {
            case LEFT -> currentX--;
            case RIGHT -> currentX++;
            case UP -> currentY--;
            case DOWN -> currentY++;
        }
        currentRoom().entities.addPlayer(player);
        player.referenceEntities(currentRoom().entities);
    }

    public Room currentRoom() {
        return rooms[currentX + currentY * size];
    }
    
}
