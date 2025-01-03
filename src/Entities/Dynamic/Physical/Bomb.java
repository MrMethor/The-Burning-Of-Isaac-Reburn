package Entities.Dynamic.Physical;

import Engine.Wrap;
import Enums.EntityType;
import Tools.EntityManager;

public class Bomb extends PhysicalEntity {

    public Bomb(Wrap wrap, EntityManager entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
    }
}
