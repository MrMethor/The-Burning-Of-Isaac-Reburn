package Entities.Dynamic.Physical;

import Engine.Wrap;
import Map.Room;
import Enums.EntityType;
import Tools.EntityList;

public class Bomb extends PhysicalEntity {

    public Bomb(Wrap wrap, EntityList entities, Room room, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, room, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
    }
}
