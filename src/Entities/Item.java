package Entities;

import Engine.Wrap;
import Entities.Dynamic.Physical.Player;
import Enums.EntityType;
import Tools.Collision;
import Tools.EntityManager;
import Tools.ItemTemplate;

public class Item extends Entity{

    int id;

    public Item(Wrap wrap, EntityManager entities, int id, String texturePath, double x, double y) {
        super(wrap, entities, EntityType.ITEM, "resource/items/" + texturePath + ".png", x, y, 100, 100, 0.2, 0.2, 0, 0);
        this.id = id;
    }

    protected void applyCollision(Collision collision) {
        if (collision.entity().getType() == EntityType.PLAYER) {
            applyItemEffect();
            destroy();
        }
    }

    private void applyItemEffect(){
        ItemTemplate item = wrap.getItemFromRegistry(id);
        Player player = null;
        for (Entity entity : entities.getEntities()){
            if (entity.getType() == EntityType.PLAYER)
                player = (Player) entity;
        }
        player.addHealth(item.redHearts(), item.redContainers(), item.soulHearts());
        player.addStats(item.damage(), item.range(), item.shotSpeed(), item.fireSpeed(), item.shotSize(), item.speed(), item.size());
        if (item.special())
            player.addSpecial(id);
    }
}
