package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Tools.EntityList;

public class Door extends Entity {
    public Door(Wrap wrap, EntityList entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
    }
}
