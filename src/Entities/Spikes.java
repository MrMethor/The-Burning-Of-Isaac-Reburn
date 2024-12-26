package Entities;

import Engine.Wrap;
import Enums.EntityType;
import Tools.EntityList;

public class Spikes extends Entity {

    public Spikes(Wrap wrap, EntityList entities, double x, double y) {
        super(wrap, entities, EntityType.SPIKE, "resource/textures/spikes.png", x, y, 120, 120, 0.5, 0.5, 0, 0);
    }
}
