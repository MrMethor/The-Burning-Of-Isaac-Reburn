package Tools;

import Engine.Wrap;
import Entities.Dynamic.DynamicEntity;
import Entities.Dynamic.Physical.Enemies.Enemy;
import Entities.Dynamic.Physical.Player;
import Entities.Dynamic.Projectiles.Projectile;
import Entities.Entity;
import Entities.TrapDoor;
import Entities.Wall;
import Enums.EntityType;
import Enums.Side;
import Map.Door;

import java.awt.*;
import java.util.ArrayList;

public class EntityManager {

    private Wrap wrap;

    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> entitiesToAdd = new ArrayList<>();
    private ArrayList<Entity> entitiesToRemove = new ArrayList<>();

    public EntityManager(Wrap wrap) {
        this.wrap = wrap;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void update() {
        entities.removeIf(Entity::isToDestroy);
        entities.removeAll(entitiesToRemove);
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        entitiesToRemove.clear();
        entities.sort((a, b) -> -1 * a.compareTo(b));
        for (Entity entity : entities)
            entity.applyBehavior();
        for (Entity entity : entities) {
            if (entity instanceof DynamicEntity)
                ((DynamicEntity) entity).applyMovement();
        }
        handleCollisions();
        for (Entity entity : entities)
            entity.applyCollisions();
        for (Entity entity : entities)
            entity.animate();
        wrap.updateEntityCount(entities.size());
    }

    private void handleCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i; j < entities.size(); j++) {
                if (entities.get(i) == entities.get(j))
                    continue;

                double[] sides = new double[4];

                Entity entity1 = entities.get(i);
                Entity entity2 = entities.get(j);

                sides[Side.UP.num()] = (entity1.getHitboxY() - entity1.getHitboxHeight() / 2) - (entity2.getHitboxY() + entity2.getHitboxHeight() / 2);
                sides[Side.DOWN.num()] = (entity2.getHitboxY() - entity2.getHitboxHeight() / 2) - (entity1.getHitboxY() + entity1.getHitboxHeight() / 2);
                sides[Side.LEFT.num()] = (entity1.getHitboxX() - entity1.getHitboxWidth() / 2) - (entity2.getHitboxX() + entity2.getHitboxWidth() / 2);
                sides[Side.RIGHT.num()] = (entity2.getHitboxX() - entity2.getHitboxWidth() / 2) - (entity1.getHitboxX() + entity1.getHitboxWidth() / 2);
                if ((sides[Side.UP.num()] <= 0 && sides[Side.DOWN.num()] <= 0) && (sides[Side.LEFT.num()] <= 0 && sides[Side.RIGHT.num()] <= 0)) {
                    double penetration = -1920;
                    Side sideOut = null;
                    for (int k = 0; k < 4; k++) {
                        if (sides[k] <= 0 && penetration < sides[k]) {
                            penetration = sides[k];
                            sideOut = Side.getSide(k);
                        }
                    }
                    entity1.addCollision(sideOut, penetration, entity2);
                    entity2.addCollision(Side.getOpposite(sideOut), penetration, entity1);
                }
            }
        }
    }

    public void renderDoors(Graphics g) {
        for (Entity entity : entities)
            if (entity.getType() == EntityType.DOOR)
                entity.render(g);
    }

    public void renderTiles(Graphics g) {
        for (Entity entity : entities)
            if (entity.getType() == EntityType.OBSTACLE || entity.getType() == EntityType.VISUAL || entity.getType() == EntityType.SPIKE || entity.getType() == EntityType.TRAP_DOOR)
                entity.render(g);
    }

    public void renderDynamic(Graphics g) {
        for (Entity entity : entities)
            if (entity instanceof DynamicEntity || entity.getType() == EntityType.ENEMY)
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

    public void removeProjectiles() {
        entities.removeIf(e -> e instanceof Projectile);
    }

    public boolean hasEnemies() {
        for (Entity entity : entities) {
            if (entity instanceof Enemy)
                return true;
        }
        return false;
    }

    public void openDoors() {
        for (Entity entity : entities) {
            if (entity instanceof Door)
                ((Door) entity).openDoor();
            else if (entity instanceof TrapDoor)
                ((TrapDoor) entity).openTrapDoor();
            else if (entity instanceof Wall)
                entitiesToRemove.add(entity);
        }
    }
}
