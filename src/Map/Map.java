package Map;

import Engine.Wrap;
import Entities.Dynamic.Physical.Player;
import Enums.DoorType;
import Enums.FloorType;
import Enums.RoomType;
import Enums.Side;

import java.util.Random;

public class Map {

    private Wrap wrap;
    private Player player;

    private Side changeRoom = null;

    private final int POSSIBLE_ROOMS = 2;
    private final int POSSIBLE_GOLDEN_ROOMS = 2;

    private FloorType floor;

    private int size = 9;
    private Room[][] rooms = new Room[size][size];
    private int roomCount;
    private boolean hasGolden;
    private int currentX = 5;
    private int currentY = 5;

    public Map(Wrap wrap, Player player, int floor, boolean isGolden) {
        this.wrap = wrap;
        this.player = player;
        this.hasGolden = isGolden;
        this.floor = FloorType.getType(floor);
        generateMap();
    }

    public void update() {
        if (changeRoom != null) {
            changeRoom(changeRoom);
            changeRoom = null;
        }
        if (player.hasEntities())
            currentRoom().update();
    }

    public void generateMap() {
        Random random = new Random();
        switch(floor) {
            case BASEMENT -> roomCount = 8 + random.nextInt(1);
            case CAVES -> roomCount = 16 + random.nextInt(4);
            case DEPTHS -> roomCount = 32 + random.nextInt(8);
        }

        rooms[currentX][currentY] = new Room(wrap, "resource/rooms/starterRoom.txt", RoomType.DEFAULT, floor);

        generateGenericRooms(floor);

        if (hasGolden)
            generateGoldenRoom(floor);

        setupDoors();

        currentRoom().getEntities().addPlayer(player);
        player.referenceEntities(currentRoom().getEntities());
        player.referenceMap(this);
    }

    private void generateGenericRooms(FloorType floor) {
        Random rand = new Random();
        while (roomCount != 0) {
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (rooms[x][y] != null)
                        continue;

                    int nearbyRoomsCount = nearbyRoomsCount(x, y);

                    int randomFactor = 0;
                    switch (nearbyRoomsCount) {
                        case 1 -> randomFactor = 5;
                        case 2 -> randomFactor = 20;
                        case 3 -> randomFactor = 80;
                        case 4 -> randomFactor = 160;
                    }

                    if (randomFactor != 0 && rand.nextInt(randomFactor) == 0 && roomCount > 0) {
                        rooms[x][y] = new Room(wrap, "resource/rooms/room" + (rand.nextInt(POSSIBLE_ROOMS - 1) + 1) + ".txt", RoomType.DEFAULT, floor);
                        roomCount--;
                    }
                }
            }
        }
    }

    private void generateGoldenRoom(FloorType floor) {
        Random rand = new Random();
        int failState = 10;
        while (failState != 0) {
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (rooms[x][y] != null)
                        continue;

                    int nearbyRoomsCount = nearbyRoomsCount(x, y);

                    if (nearbyRoomsCount == 1 && rand.nextInt(4) < 1) {
                        rooms[x][y] = new Room(wrap, "resource/rooms/goldenRoom" + (rand.nextInt(POSSIBLE_GOLDEN_ROOMS) + 1) + ".txt", RoomType.GOLDEN, floor);
                        return;
                    }
                }
            }
            failState--;
        }
    }

    private void setupDoors() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (rooms[x][y] == null)
                    continue;

                DoorType up = null, down = null, left = null, right = null;

                if (x - 1 >= 0 && rooms[x - 1][y] != null)
                    left = getDoorType(rooms[x - 1][y].getType(), floor);
                if (x + 1 < size && rooms[x + 1][y] != null)
                    right = getDoorType(rooms[x + 1][y].getType(), floor);
                if (y - 1 >= 0 && rooms[x][y - 1] != null)
                    up = getDoorType(rooms[x][y - 1].getType(), floor);
                if (y + 1 < size && rooms[x][y + 1] != null)
                    down = getDoorType(rooms[x][y + 1].getType(), floor);

                rooms[x][y].setupDoors(up, down, left, right);
            }
        }
    }

    private DoorType getDoorType(RoomType roomType, FloorType floorType) {
        if (roomType == RoomType.GOLDEN)
            return DoorType.GOLDEN;
        switch (floorType) {
            case BASEMENT, CAVES -> {
                return DoorType.BASEMENT;
            }
            case DEPTHS -> {
                return DoorType.DEPTHS;
            }
        }
        return DoorType.BASEMENT;
    }

    private int nearbyRoomsCount(int x, int y) {
        int nearRoomCount = 0;
        if (x - 1 >= 0 && rooms[x - 1][y] != null)
            nearRoomCount++;
        if (x + 1 < size && rooms[x + 1][y] != null)
            nearRoomCount++;
        if (y - 1 >= 0 && rooms[x][y - 1] != null)
            nearRoomCount++;
        if (y + 1 < size && rooms[x][y + 1] != null)
            nearRoomCount++;
        return nearRoomCount;
    }

    public void tryChangeRoom() {
        for (int i = 0; i < 4; i++) {
            if (currentRoom().getDoors()[i] != null && currentRoom().getDoors()[i].isCollided(player))
                changeRoom = Side.getSide(i);
        }
    }

    private void changeRoom(Side side) {
        if (!currentRoom().isCompleted())
            return;
        currentRoom().getEntities().removePlayer();
        currentRoom().getEntities().removeProjectiles();
        player.referenceEntities(null);
        switch(side) {
            case LEFT -> currentX--;
            case RIGHT -> currentX++;
            case UP -> currentY--;
            case DOWN -> currentY++;
        }
        currentRoom().getEntities().addPlayer(player);
        player.referenceEntities(currentRoom().getEntities());
        player.changeRoom(side);
    }

    public Room currentRoom() {
        return rooms[currentX][currentY];
    }
}
