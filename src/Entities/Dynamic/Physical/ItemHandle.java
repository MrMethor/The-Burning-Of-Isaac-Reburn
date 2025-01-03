package Entities.Dynamic.Physical;

import Engine.Wrap;
import Enums.EntityType;
import Tools.EntityManager;

public class ItemHandle extends PhysicalEntity {
    public ItemHandle(Wrap wrap, EntityManager entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
    }
}
