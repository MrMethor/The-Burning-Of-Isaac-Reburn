package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Tools.EntityManager;

import java.util.Random;

public class Rock extends Entity {
    public Rock(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.OBSTACLE, "resource/entities/rocks.png", 2, 1, x, y, 130, 130, 0.8, 0.8, 0, 0);
        Random random = new Random();
        swapTexture(random.nextInt(2), 0);
    }
}
