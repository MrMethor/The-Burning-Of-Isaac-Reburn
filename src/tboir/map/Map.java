package tboir.map;

import tboir.engine.Wrap;
import tboir.entities.dynamic.physical.Player;
import tboir.engine.Side;
import tboir.hud.Hud;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Map {

    public static final int MAX_SIZE = 9;
    public static final int FOUR_BITS = 16;

    private final Wrap wrap;
    private final Player player;
    private final Hud hud;

    private Side changeRoom;
    private boolean changeLevel;
    private final FloorType floor;

    private final ArrayList<String>[][] roomLayouts;
    private final RoomType[][] mapLayout;
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
        this.roomLayouts = (ArrayList<String>[][])new ArrayList[RoomType.values().length][FOUR_BITS];
        for (int i = 0; i < RoomType.values().length; i++) {
            for (int j = 0; j < FOUR_BITS; j++) {
                this.roomLayouts[i][j] = new ArrayList<>();
            }
        }
        this.mapLayout = new RoomType[MAX_SIZE][MAX_SIZE];
        this.storeRoomOptions();
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

        this.mapLayout[this.currentX][this.currentY] = RoomType.STARTING;

        this.generateGenericRooms();

        if (this.hasGolden) {
            this.generateGoldenRoom();
        }

        this.generateBossRoom();

        this.setupRooms();

        this.setupDoors();

        this.currentRoom().getEntities().addPlayer(this.player);
        this.currentRoom().explored();
        this.player.referenceEntities(this.currentRoom().getEntities());
        this.player.referenceMap(this);
        this.hud.updateMap(this.currentX, this.currentY, this.rooms);
    }

    private void generateGenericRooms() {
        Random rand = new Random();
        while (this.roomCount != 0) {
            int x = rand.nextInt(0, MAX_SIZE);
            int y = rand.nextInt(0, MAX_SIZE);
            if (this.mapLayout[x][y] != null) {
                continue;
            }

            int nearbyRoomsCount = this.roomsNearbyCount(x, y);

            int randomFactor = 0;
            switch (nearbyRoomsCount) {
                case 1 -> randomFactor = 2;
                case 2 -> randomFactor = 4;
                case 3 -> randomFactor = 8;
                case 4 -> randomFactor = 16;
            }

            if (randomFactor != 0 && rand.nextInt(randomFactor) == 0 && this.roomCount > 0) {
                this.mapLayout[x][y] = RoomType.DEFAULT;
                this.roomCount--;
            }
        }
    }

    private void generateGoldenRoom() {
        Random rand = new Random();
        int failState = 10000;
        while (failState != 0) {
            int x = rand.nextInt(0, MAX_SIZE);
            int y = rand.nextInt(0, MAX_SIZE);
            if (this.mapLayout[x][y] != null) {
                continue;
            }

            if (this.roomsNearbyCount(x, y) == 1 && rand.nextInt(4) < 1) {
                this.mapLayout[x][y] = RoomType.GOLDEN;
                return;
            }
            failState--;
        }
        throw new RuntimeException("Golden room generation failed");
    }

    private void generateBossRoom() {
        Random rand = new Random();
        int failState = 10000;
        while (failState != 0) {
            int x = rand.nextInt(0, MAX_SIZE);
            int y = rand.nextInt(0, MAX_SIZE);
            if (this.mapLayout[x][y] != null) {
                continue;
            }

            if (x >= 3 && x <= 6 && y >= 3 && y <= 6) {
                continue;
            }

            if (this.roomsNearbyCount(x, y, RoomType.GOLDEN) == 0 && this.roomsNearbyCount(x, y, RoomType.DEFAULT) == 1 && rand.nextInt(4) < 1) {
                this.mapLayout[x][y] = RoomType.BOSS;
                return;
            }
            failState--;
        }
        throw new RuntimeException("Boss room generation failed");
    }

    private void setupRooms() {
        Random rand = new Random();
        for (int y = 0; y < MAX_SIZE; y++) {
            for (int x = 0; x < MAX_SIZE; x++) {
                if (this.mapLayout[x][y] == null) {
                    continue;
                }
                String roomCode;
                ArrayList<String> fileList = null;
                boolean allGood = true;
                while (fileList == null || fileList.isEmpty()) {
                    roomCode = this.randomizeRoomsNearby(this.roomsNearby(x, y));
                    fileList = this.roomLayouts[this.mapLayout[x][y].ordinal()][Integer.parseInt(roomCode, 2)];
                    if (roomCode.equals("1111") && fileList.isEmpty()) {
                        allGood = false;
                        System.out.println("Error occured while generating a room");
                        break;
                    }
                }
                if (allGood) {
                    String randomFile = fileList.get(rand.nextInt(0, fileList.size()));
                    String path = "resource/roomLayouts/" + this.mapLayout[x][y].getPath() + "/" + randomFile;
                    this.mapLayout[x][y] = this.mapLayout[x][y] == RoomType.STARTING ? RoomType.DEFAULT : this.mapLayout[x][y];
                    this.rooms[x][y] = new Room(this.wrap, path, this.mapLayout[x][y], this.floor);
                } else {
                    this.rooms[x][y] = new Room(this.wrap, "resource/roomLayouts/error.txt", this.mapLayout[x][y], this.floor);
                }

            }
        }
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

    private void storeRoomOptions() {
        for (int i = 0; i < RoomType.values().length; i++) {
            File folder = new File("resource/roomLayouts/" + RoomType.values()[i].getPath());
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles == null) {
                continue;
            }
            for (File file : listOfFiles) {
                try {
                    Scanner scanner = new Scanner(file);
                    this.roomLayouts[i][this.intFromRoomCode(scanner.nextLine())].add(file.getName());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private int intFromRoomCode(String code) {
        int dec = 0;
        if (code.contains("U")) {
            dec += 8;
        }
        if (code.contains("D")) {
            dec += 4;
        }
        if (code.contains("L")) {
            dec += 2;
        }
        if (code.contains("R")) {
            dec += 1;
        }
        return dec;
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

    private int roomsNearbyCount(int x, int y, RoomType type) {
        String roomsNearby = this.roomsNearby(x, y, type);
        return roomsNearby.replace("0", "").length();
    }

    private int roomsNearbyCount(int x, int y) {
        String roomsNearby = this.roomsNearby(x, y);
        return roomsNearby.replace("0", "").length();
    }

    private String roomsNearby(int x, int y) {
        char[] roomsNearby = {'0', '0', '0', '0'};
        if (y - 1 >= 0 && this.mapLayout[x][y - 1] != null) {
            roomsNearby[Side.UP.ordinal()] = '1';
        }
        if (y + 1 < MAX_SIZE && this.mapLayout[x][y + 1] != null) {
            roomsNearby[Side.DOWN.ordinal()] = '1';
        }
        if (x - 1 >= 0 && this.mapLayout[x - 1][y] != null) {
            roomsNearby[Side.LEFT.ordinal()] = '1';
        }
        if (x + 1 < MAX_SIZE && this.mapLayout[x + 1][y] != null) {
            roomsNearby[Side.RIGHT.ordinal()] = '1';
        }
        return new String(roomsNearby);
    }

    private String roomsNearby(int x, int y, RoomType type) {
        char[] roomsNearby = {'0', '0', '0', '0'};
        if (y - 1 >= 0 && this.mapLayout[x][y - 1] != null && this.mapLayout[x][y - 1] == type) {
            roomsNearby[Side.UP.ordinal()] = '1';
        }
        if (y + 1 < MAX_SIZE && this.mapLayout[x][y + 1] != null && this.mapLayout[x][y + 1] == type) {
            roomsNearby[Side.DOWN.ordinal()] = '1';
        }
        if (x - 1 >= 0 && this.mapLayout[x - 1][y] != null && this.mapLayout[x - 1][y] == type) {
            roomsNearby[Side.LEFT.ordinal()] = '1';
        }
        if (x + 1 < MAX_SIZE && this.mapLayout[x + 1][y] != null && this.mapLayout[x + 1][y] == type) {
            roomsNearby[Side.RIGHT.ordinal()] = '1';
        }
        return new String(roomsNearby);
    }

    private String randomizeRoomsNearby(String roomsNearby) {
        Random random = new Random();
        char[] roomsNearbyChar = roomsNearby.toCharArray();
        for (int i = 0; i < roomsNearbyChar.length; i++) {
            if (roomsNearbyChar[i] == '0') {
                roomsNearbyChar[i] = random.nextInt(0, 3) == 0 ? '1' : '0';
            }
        }
        return new String(roomsNearbyChar);
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
