package tboir.map;

import tboir.engine.Wrap;
import tboir.entities.dynamic.physical.Player;
import tboir.engine.Side;
import tboir.hud.Hud;

import java.util.Random;

public class Map {

    public static final int MAX_SIZE = 9;

    private final Wrap wrap;
    private final Player player;
    private final Hud hud;

    private Side changeRoom;
    private boolean changeLevel;
    private final FloorType floor;

    private final Room[][] rooms;
    private int roomCount;
    private final boolean hasGolden;
    private int currentX;
    private int currentY;

    public Map(Wrap wrap, Player player, int floor, boolean isGolden, Hud hud) {
        this.wrap = wrap;
        this.player = player;
        this.hud = hud;
        this.hasGolden = isGolden;
        this.floor = FloorType.getType(floor);
        this.changeRoom = null;
        this.changeLevel = false;
        this.rooms = new Room[MAX_SIZE][MAX_SIZE];
        this.currentX = 4;
        this.currentY = 4;
        this.generateMap();
    }

    public void update() {
        if (this.changeRoom != null) {
            this.changeRoom(this.changeRoom);
            this.changeRoom = null;
        }
        if (this.player.hasEntities()) {
            this.currentRoom().update();
        }
    }

    private void generateMap() {
        Random random = new Random();
        switch (this.floor) {
            case BASEMENT -> this.roomCount = 8 + random.nextInt(1);
            case CAVES -> this.roomCount = 16 + random.nextInt(4);
            case DEPTHS -> this.roomCount = 32 + random.nextInt(8);
        }

        this.rooms[this.currentX][this.currentY] = new Room(this.wrap, "resource/roomLayouts/starterRoom.txt", RoomType.DEFAULT, this.floor);

        this.generateGenericRooms(this.floor);

        if (this.hasGolden) {
            this.generateGoldenRoom(this.floor);
        }

        this.generateBossRoom(this.floor);

        this.setupDoors();

        this.currentRoom().getEntities().addPlayer(this.player);
        this.currentRoom().explored();
        this.player.referenceEntities(this.currentRoom().getEntities());
        this.player.referenceMap(this);
        this.hud.updateMap(this.currentX, this.currentY, this.rooms);
    }

    private void generateGenericRooms(FloorType floor) {
        Random rand = new Random();
        while (this.roomCount != 0) {
            for (int y = 0; y < MAX_SIZE; y++) {
                for (int x = 0; x < MAX_SIZE; x++) {
                    if (this.rooms[x][y] != null) {
                        continue;
                    }

                    int nearbyRoomsCount = this.roomsNearby(x, y);

                    int randomFactor = 0;
                    switch (nearbyRoomsCount) {
                        case 1 -> randomFactor = 5;
                        case 2 -> randomFactor = 20;
                        case 3 -> randomFactor = 60;
                        case 4 -> randomFactor = 120;
                    }

                    if (randomFactor != 0 && rand.nextInt(randomFactor) == 0 && this.roomCount > 0) {
                        int possibleRooms = 10;
                        this.rooms[x][y] = new Room(this.wrap, "resource/roomLayouts/room" + (rand.nextInt(possibleRooms) + 1) + ".txt", RoomType.DEFAULT, floor);
                        this.roomCount--;
                    }
                }
            }
        }
    }

    private void generateGoldenRoom(FloorType floor) {
        Random rand = new Random();
        int failState = 10;
        while (failState != 0) {
            for (int y = 0; y < MAX_SIZE; y++) {
                for (int x = 0; x < MAX_SIZE; x++) {
                    if (this.rooms[x][y] != null) {
                        continue;
                    }

                    if (this.roomsNearby(x, y) == 1 && rand.nextInt(4) < 1) {
                        this.rooms[x][y] = new Room(this.wrap, "resource/roomLayouts/goldenRoom.txt", RoomType.GOLDEN, floor);
                        return;
                    }
                }
            }
            failState--;
        }
        System.out.println("Golden room generation failed");
    }

    private void generateBossRoom(FloorType floor) {
        Random rand = new Random();
        int failState = 10;
        while (failState != 0) {
            for (int y = 0; y < MAX_SIZE; y++) {
                for (int x = 0; x < MAX_SIZE; x++) {
                    if (this.rooms[x][y] != null) {
                        continue;
                    }

                    if (x >= 3 && x <= 6 && y >= 3 && y <= 6) {
                        continue;
                    }

                    if (this.roomsNearby(x, y, RoomType.GOLDEN) == 0 && this.roomsNearby(x, y, RoomType.DEFAULT) == 1 && rand.nextInt(4) < 1) {
                        this.rooms[x][y] = new Room(this.wrap, "resource/roomLayouts/bossRoom.txt", RoomType.BOSS, floor);
                        return;
                    }
                }
            }
            failState--;
        }
        System.out.println("Boss room generation failed");
    }

    private void setupDoors() {
        for (int y = 0; y < MAX_SIZE; y++) {
            for (int x = 0; x < MAX_SIZE; x++) {

                if (this.rooms[x][y] == null) {
                    continue;
                }

                DoorType up = null;
                DoorType down = null;
                DoorType left = null;
                DoorType right = null;

                if (x - 1 >= 0 && this.rooms[x - 1][y] != null) {
                    left = this.getDoorType(this.rooms[x - 1][y].getType(), this.floor);
                }
                if (x + 1 < MAX_SIZE && this.rooms[x + 1][y] != null) {
                    right = this.getDoorType(this.rooms[x + 1][y].getType(), this.floor);
                }
                if (y - 1 >= 0 && this.rooms[x][y - 1] != null) {
                    up = this.getDoorType(this.rooms[x][y - 1].getType(), this.floor);
                }
                if (y + 1 < MAX_SIZE && this.rooms[x][y + 1] != null) {
                    down = this.getDoorType(this.rooms[x][y + 1].getType(), this.floor);
                }

                this.rooms[x][y].setupDoors(up, down, left, right);
            }
        }
    }

    private DoorType getDoorType(RoomType roomType, FloorType floorType) {
        if (roomType == RoomType.GOLDEN) {
            return DoorType.GOLDEN;
        } else if (roomType == RoomType.BOSS) {
            return DoorType.BOSS;
        }
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

    private int roomsNearby(int x, int y, RoomType type) {
        int roomsNearby = 0;
        if (x - 1 >= 0 && this.rooms[x - 1][y] != null && this.rooms[x - 1][y].getType() == type) {
            roomsNearby++;
        }
        if (x + 1 < MAX_SIZE && this.rooms[x + 1][y] != null && this.rooms[x + 1][y].getType() == type) {
            roomsNearby++;
        }
        if (y - 1 >= 0 && this.rooms[x][y - 1] != null && this.rooms[x][y - 1].getType() == type) {
            roomsNearby++;
        }
        if (y + 1 < MAX_SIZE && this.rooms[x][y + 1] != null && this.rooms[x][y + 1].getType() == type) {
            roomsNearby++;
        }
        return roomsNearby;
    }

    private int roomsNearby(int x, int y) {
        int roomsNearby = 0;
        if (x - 1 >= 0 && this.rooms[x - 1][y] != null) {
            roomsNearby++;
        }
        if (x + 1 < MAX_SIZE && this.rooms[x + 1][y] != null) {
            roomsNearby++;
        }
        if (y - 1 >= 0 && this.rooms[x][y - 1] != null) {
            roomsNearby++;
        }
        if (y + 1 < MAX_SIZE && this.rooms[x][y + 1] != null) {
            roomsNearby++;
        }
        return roomsNearby;
    }

    public void queueChangeRoom(Side side) {
        this.changeRoom = side;
    }

    public void tryChangeLevel() {
        if (!this.currentRoom().isCompleted()) {
            return;
        }
        this.changeLevel = true;
    }

    private void changeRoom(Side side) {
        this.currentRoom().getEntities().removePlayer();
        this.currentRoom().getEntities().removeProjectiles();
        this.player.referenceEntities(null);
        switch (side) {
            case LEFT -> this.currentX--;
            case RIGHT -> this.currentX++;
            case UP -> this.currentY--;
            case DOWN -> this.currentY++;
        }
        this.currentRoom().explored();
        this.currentRoom().getEntities().addPlayer(this.player);
        this.player.referenceEntities(this.currentRoom().getEntities());
        this.player.changeRoom(side);
        this.hud.updateMap(this.currentX, this.currentY, this.rooms);
    }

    public boolean changeLevelRequest() {
        return this.changeLevel;
    }

    //Getter
    public Room currentRoom() {
        return this.rooms[this.currentX][this.currentY];
    }
}
