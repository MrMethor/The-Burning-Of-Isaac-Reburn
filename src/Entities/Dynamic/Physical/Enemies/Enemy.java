package Entities.Dynamic.Physical.Enemies;

import Engine.Wrap;
import Entities.Dynamic.Physical.PhysicalEntity;
import Entities.Entity;
import Enums.EntityType;

import java.util.ArrayList;

public abstract class Enemy extends PhysicalEntity {

    protected double health = 20;

    public Enemy(Wrap wrap, ArrayList<Entity> entities, Entity room, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, room, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
    }
}
