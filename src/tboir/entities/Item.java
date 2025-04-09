package tboir.entities;

import tboir.engine.Wrap;
import tboir.entities.dynamic.physical.Player;
import tboir.map.EntityManager;
import tboir.tools.Collision;
import tboir.tools.ItemTemplate;

public class Item extends Entity {

    private final int id;

    public Item(Wrap wrap, EntityManager entities, int id, double x, double y) {
        super(wrap, entities,
                EntityType.ITEM,
                "items",
                wrap.getItemFromRegistry(id).column(),
                wrap.getItemFromRegistry(id).row(),
                x, y,
                120, 120,
                0.2,
                0.2,
                0, 0
        );
        this.id = id;
    }

    @Override
    public void applyBehavior() {
    }

    @Override
    protected void applyCollision(Collision collision) {
        if (collision.entity().getType() == EntityType.PLAYER) {
            this.applyItemEffect();
            this.destroy();
        }
    }

    @Override
    public void animate() {
    }

    private void applyItemEffect() {
        ItemTemplate item = this.getWrap().getItemFromRegistry(this.id);
        Player player = null;
        for (Entity entity : this.getEntities().getEntities()) {
            if (entity.getType() == EntityType.PLAYER) {
                player = (Player)entity;
            }
        }
        player.addHealth(item.redHearts(), item.redContainers(), item.soulHearts());
        player.addStats(item.damage(), item.range(), item.shotSpeed(), item.fireSpeed(), item.shotSize(), item.speed(), item.size());
        if (item.special()) {
            player.addSpecial(this.id);
        }
    }
}
