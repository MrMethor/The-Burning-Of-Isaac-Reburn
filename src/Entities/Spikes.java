package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Tools.EntityManager;

public class Spikes extends Entity {

    public Spikes(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities, EntityType.SPIKE, "resource/entities/spikes.png", x, y, 120, 120, 0.5, 0.5, 0, 0);
    }
}
