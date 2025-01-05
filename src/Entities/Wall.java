package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Tools.EntityManager;

public class Wall extends Entity{
    public Wall(Wrap wrap, EntityManager entities, double x, double y, int width, int height) {
        super(wrap, entities, EntityType.OBSTACLE, "resource/entities/blank.png", x, y, width, height, 1, 1, 0, 0);
    }
}
