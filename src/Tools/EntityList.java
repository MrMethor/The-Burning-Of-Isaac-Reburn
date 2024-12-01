package Tools;

import Engine.Component;
import Entities.Dynamic.DynamicEntity;
import Entities.Entity;

import java.awt.*;
import java.util.ArrayList;

public class EntityList implements Component {

    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> entitiesToAdd = new ArrayList<>();

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void update() {
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
        entities.removeIf(Entity::isToDestroy);
    }

    public void render(Graphics g) {
        for (Entity entity : entities)
            entity.render(g);
    }

    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
    }
}
