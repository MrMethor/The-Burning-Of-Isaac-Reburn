package Tools;

import Entities.Dynamic.DynamicEntity;
import Entities.Dynamic.Physical.Player;
import Entities.Entity;
import Enums.EntityType;

import java.awt.*;
import java.util.ArrayList;

public class EntityList {

    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> entitiesToAdd = new ArrayList<>();

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void update() {
        entities.removeIf(Entity::isToDestroy);
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        entities.sort((a, b) -> -1 * a.compareTo(b));
        for (Entity entity : entities)
            entity.applyBehavior();
        for (Entity entity : entities) {
            if (entity instanceof DynamicEntity)
                ((DynamicEntity) entity).applyMovement();
        }
        for (Entity entity : entities)
            entity.handleCollisions();
        for (Entity entity : entities)
            entity.animate();
    }

    public void renderTiles(Graphics g) {
        for (Entity entity : entities)
            if (entity.getType() == EntityType.OBSTACLE || entity.getType() == EntityType.VISUAL)
                entity.render(g);
    }

    public void renderDynamic(Graphics g) {
        for (Entity entity : entities)
            if (entity.getType() == EntityType.ENEMY || entity.getType() == EntityType.PLAYER || entity.getType() == EntityType.ITEM)
                entity.render(g);
    }

    public void renderProjectiles(Graphics g) {
        for (Entity entity : entities)
            if (entity.getType() == EntityType.ENEMY_PROJECTILE || entity.getType() == EntityType.FRIENDLY_PROJECTILE)
                entity.render(g);
    }

    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
    }

    public void addPlayer(Player player) {
        if (!entities.contains(player))
            entitiesToAdd.add(player);
    }

    public void removePlayer() {
        entities.removeIf(e -> e instanceof Player);
    }
}
