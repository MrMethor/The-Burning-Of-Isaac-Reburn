package tboir.tools;

import tboir.engine.Wrap;
import tboir.entities.dynamic.DynamicEntity;
import tboir.entities.dynamic.physical.enemies.Enemy;
import tboir.entities.dynamic.physical.Player;
import tboir.entities.dynamic.projectiles.Projectile;
import tboir.entities.Entity;
import tboir.entities.TrapDoor;
import tboir.entities.Wall;
import tboir.enums.EntityType;
import tboir.enums.Side;
import tboir.map.Door;

import java.awt.Graphics;
import java.util.ArrayList;

public class EntityManager {

    private final Wrap wrap;

    private final ArrayList<Entity> entities;
    private final ArrayList<Entity> entitiesToAdd;
    private final ArrayList<Entity> entitiesToRemove;

    public EntityManager(Wrap wrap) {
        this.wrap = wrap;
        this.entities = new ArrayList<>();
        this.entitiesToAdd = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
    }

    public void update() {
        this.entities.removeIf(Entity::isToDestroy);
        this.entities.removeAll(this.entitiesToRemove);
        this.entities.addAll(this.entitiesToAdd);
        this.entitiesToAdd.clear();
        this.entitiesToRemove.clear();
        this.entities.sort((a, b) -> -1 * a.compareTo(b));
        for (Entity entity : this.entities) {
            entity.applyBehavior();
        }
        for (Entity entity : this.entities) {
            if (entity instanceof DynamicEntity) {
                ((DynamicEntity) entity).applyMovement();
            }
        }
        this.handleCollisions();
        for (Entity entity : this.entities) {
            entity.applyCollisions();
        }
        for (Entity entity : this.entities) {
            entity.animate();
        }
        this.wrap.updateEntityCount(this.entities.size());
    }

    private void handleCollisions() {
        for (int i = 0; i < this.entities.size(); i++) {
            for (int j = i; j < this.entities.size(); j++) {
                if (this.entities.get(i) == this.entities.get(j)) {
                    continue;
                }

                double[] sides = new double[4];

                Entity entity1 = this.entities.get(i);
                Entity entity2 = this.entities.get(j);

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
        for (Entity entity : this.entities) {
            if (entity.getType() == EntityType.DOOR) {
                entity.render(g);
            }
        }
    }

    public void renderTiles(Graphics g) {
        for (Entity entity : this.entities) {
            if (entity.getType() == EntityType.OBSTACLE || entity.getType() == EntityType.VISUAL || entity.getType() == EntityType.SPIKE || entity.getType() == EntityType.TRAP_DOOR) {
                entity.render(g);
            }
        }
    }

    public void renderDynamic(Graphics g) {
        for (Entity entity : this.entities) {
            if (entity instanceof DynamicEntity || entity.getType() == EntityType.ENEMY) {
                entity.render(g);
            }
        }
    }

    public void renderItems(Graphics g) {
        for (Entity entity : this.entities) {
            if (entity.getType() == EntityType.ITEM) {
                entity.render(g);
            }
        }
    }

    public void addEntity(Entity entity) {
        this.entitiesToAdd.add(entity);
    }

    public void addPlayer(Player player) {
        if (!this.entities.contains(player)) {
            this.entitiesToAdd.add(player);
        }
    }

    public void removePlayer() {
        this.entities.removeIf(e -> e instanceof Player);
    }

    public void removeProjectiles() {
        this.entities.removeIf(e -> e instanceof Projectile);
    }

    public boolean hasEnemies() {
        for (Entity entity : this.entities) {
            if (entity instanceof Enemy) {
                return true;
            }
        }
        return false;
    }

    public void openDoors() {
        for (Entity entity : this.entities) {
            if (entity instanceof Door) {
                ((Door) entity).openDoor();
            } else if (entity instanceof TrapDoor) {
                ((TrapDoor) entity).openTrapDoor();
            } else if (entity instanceof Wall) {
                this.entitiesToRemove.add(entity);
            }
        }
    }

    // Getter
    public ArrayList<Entity> getEntities() {
        return this.entities;
    }
}
