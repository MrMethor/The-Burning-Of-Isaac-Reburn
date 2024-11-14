package Entities;

import Engine.Wrap;
import Enums.EntityType;

public class Item extends Entity {

    public Item(Wrap wrap) {
        super(wrap, EntityType.ITEM);
        changeSize(150, 150, 0.4, 0.35);
        changeHitboxOffset(0, 0.3);
    }
}
