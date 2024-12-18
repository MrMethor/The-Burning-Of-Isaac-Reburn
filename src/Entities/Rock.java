package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Tools.EntityList;

public class Rock extends Entity {
    public Rock(Wrap wrap, EntityList entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
    }
}
