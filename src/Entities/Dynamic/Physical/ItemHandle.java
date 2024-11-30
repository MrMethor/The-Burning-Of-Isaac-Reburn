package Entities.Dynamic.Physical;

import Engine.Wrap;
import Entities.Entity;
import Enums.EntityType;

import java.util.ArrayList;

public class ItemHandle extends PhysicalEntity {
    public ItemHandle(Wrap wrap, ArrayList<Entity> entities, Entity room, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, room, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
    }
}
