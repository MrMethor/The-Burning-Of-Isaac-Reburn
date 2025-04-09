package tboir.map;

import tboir.engine.Wrap;
import tboir.entities.Entity;
import tboir.entities.EntityType;
import tboir.entities.TrapDoor;
import tboir.entities.Wall;
import tboir.entities.dynamic.DynamicEntity;
import tboir.entities.dynamic.physical.enemies.Enemy;
import tboir.entities.dynamic.physical.Player;
import tboir.entities.dynamic.projectiles.Projectile;
import tboir.engine.Side;
import tboir.tools.Edge;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

public class EntityManager {

    private final Wrap wrap;

    private Player player;
    private final ArrayList<Entity> entities;
    private final ArrayList<Entity> entitiesToAdd;
    private final ArrayList<Entity> entitiesToRemove;

    public static final int OBSTACLE_UPDATE_TIME_MS = 60;
    private int counter;

    public EntityManager(Wrap wrap) {
        this.wrap = wrap;
        this.player = null;
        this.entities = new ArrayList<>();
        this.entitiesToAdd = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
    }

    public void update() {
        this.entities.removeAll(this.entitiesToRemove);
        this.entities.addAll(this.entitiesToAdd);
        this.entitiesToRemove.clear();
        this.entitiesToAdd.clear();
        this.entities.sort((a, b) -> -1 * a.compareTo(b));
        this.updateObstacles();
        this.applyBehaviors(); // everyone
        this.applyMovement(); // DynamicEntity
        this.handleCollisions();
        this.applyCollisions(); // everyone
        this.animate(); // everyone
        this.wrap.updateEntityCount(this.entities.size());
    }

    private void applyBehaviors() {
        for (Entity entity : this.entities) {
            entity.applyBehavior();
        }
    }

    private void applyMovement() {
        for (Entity entity : this.entities) {
            if (entity instanceof DynamicEntity) {
                ((DynamicEntity)entity).applyMovement();
            }
        }
    }

    private void applyCollisions() {
        for (Entity entity : this.entities) {
            entity.applyCollisions();
        }
    }

    private void animate() {
        for (Entity entity : this.entities) {
            entity.animate();
        }
    }

    private void handleCollisions() {
        for (int i = 0; i < this.entities.size(); i++) {
            for (int j = i; j < this.entities.size(); j++) {
                if (this.entities.get(i) != this.entities.get(j)) {
                    this.addCollision(this.entities.get(i), this.entities.get(j));
                }
            }
        }
    }

    private void addCollision(Entity entity1, Entity entity2) {
        double[] sides = this.assignSides(entity1, entity2);
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
            assert sideOut != null;
            entity2.addCollision(Side.getOpposite(sideOut), penetration, entity1);
        }
    }

    private double[] assignSides(Entity entity1, Entity entity2) {
        double[] sides = new double[4];
        sides[Side.UP.num()] = (entity1.getHitboxY() - entity1.getHitboxHeight() / 2) - (entity2.getHitboxY() + entity2.getHitboxHeight() / 2);
        sides[Side.DOWN.num()] = (entity2.getHitboxY() - entity2.getHitboxHeight() / 2) - (entity1.getHitboxY() + entity1.getHitboxHeight() / 2);
        sides[Side.LEFT.num()] = (entity1.getHitboxX() - entity1.getHitboxWidth() / 2) - (entity2.getHitboxX() + entity2.getHitboxWidth() / 2);
        sides[Side.RIGHT.num()] = (entity2.getHitboxX() - entity2.getHitboxWidth() / 2) - (entity1.getHitboxX() + entity1.getHitboxWidth() / 2);
        return sides;

    }

    public boolean checkCollisionWithType(double x, double y, EntityType entityType) {
        for (Entity entity : this.entities) {
            if (entity.getType() == entityType) {
                if (x < entity.getHitboxX() + entity.getHitboxWidth() / 2 && x > entity.getHitboxX() - entity.getHitboxWidth() / 2 && y < entity.getHitboxY() + entity.getHitboxHeight() / 2 && y > entity.getHitboxY() - entity.getHitboxHeight() / 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public void renderBack(Graphics2D g) {
        for (Entity entity : this.entities) {
            if (entity.getType() == EntityType.DOOR || entity.getType() == EntityType.WALL) {
                entity.render(g);
            }
        }
        for (Entity entity : this.entities) {
            if (entity.getType() == EntityType.OBSTACLE || entity.getType() == EntityType.VISUAL || entity.getType() == EntityType.SPIKE || entity.getType() == EntityType.TRAP_DOOR || entity.getType() == EntityType.WEB) {
                entity.render(g);
            }
        }
    }

    public void renderFront(Graphics2D g) {
        for (Entity entity : this.entities) {
            if (entity.getType() == EntityType.ITEM) {
                entity.render(g);
            }
        }
        for (Entity entity : this.entities) {
            if (entity instanceof DynamicEntity || entity.getType() == EntityType.ENEMY) {
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
        this.player = player;
    }

    public void removePlayer() {
        this.entities.removeIf(e -> e instanceof Player);
        this.player = null;
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
                ((Door)entity).openDoor();
            } else if (entity instanceof TrapDoor) {
                ((TrapDoor)entity).openTrapDoor();
            } else if (entity instanceof Wall) {
                this.entitiesToRemove.add(entity);
            }
        }
    }

    public void destroy(Entity entity) {
        this.entitiesToRemove.add(entity);
    }

    private void updateObstacles() {
        if (this.counter == 0) {
            boolean[][] tileObstacleGrid = new boolean[Room.TILES_HEIGHT][Room.TILES_WIDTH];
            for (Entity entity : this.entities) {
                if (entity.getType() == EntityType.OBSTACLE) {
                    tileObstacleGrid[Room.getTileY(entity)][Room.getTileX(entity)] = true;
                }
            }
            for (Entity entity : this.entities) {
                if (entity instanceof Enemy) {
                    ((Enemy)entity).updateObstacleGrid(tileObstacleGrid);
                }
            }
            this.updateShortestPath(tileObstacleGrid);
            this.counter = OBSTACLE_UPDATE_TIME_MS;
        } else {
            this.counter--;
        }
    }

    private void updateShortestPath(boolean[][] tileObstacleGrid) {
        int numOfVertices = Room.TILES_WIDTH * Room.TILES_HEIGHT;
        ArrayList<Edge> edges = new ArrayList<>();
        int[] lastVertices = new int[numOfVertices];
        Integer[] shortestPaths = new Integer[numOfVertices];
        Arrays.fill(lastVertices, Integer.MAX_VALUE);
        Arrays.fill(shortestPaths, Integer.MAX_VALUE);
        int firstVertex = Room.getTileX(this.player) + Room.getTileY(this.player) * Room.TILES_WIDTH;
        if (firstVertex >= numOfVertices) {
            return;
        }
        shortestPaths[firstVertex] = 0;
        for (int i = 0; i < Room.TILES_HEIGHT; i++) {
            for (int j = 0; j < Room.TILES_WIDTH; j++) {
                if (tileObstacleGrid[i][j]) {
                    continue;
                }
                if (j + 1 != Room.TILES_WIDTH && !tileObstacleGrid[i][j + 1]) {
                    edges.add(new Edge(j + i * Room.TILES_WIDTH, (j + i * Room.TILES_WIDTH) + 1));
                }
                if (i + 1 != Room.TILES_HEIGHT && !tileObstacleGrid[i + 1][j]) {
                    edges.add(new Edge(j + i * Room.TILES_WIDTH, (j + i * Room.TILES_WIDTH) + Room.TILES_WIDTH));
                }
            }
        }
        int smallestVertex = this.getSmallestIndex(shortestPaths);
        while (smallestVertex != -1 && shortestPaths[smallestVertex] != Integer.MAX_VALUE) {
            for (Edge edge : edges) {
                if (edge.vertex1() == smallestVertex) {
                    if (shortestPaths[edge.vertex2()] != null && shortestPaths[edge.vertex2()] > shortestPaths[smallestVertex] + 1) {
                        shortestPaths[edge.vertex2()] = shortestPaths[smallestVertex] + 1;
                        lastVertices[edge.vertex2()] = smallestVertex;
                    }
                } else if (edge.vertex2() == smallestVertex) {
                    if (shortestPaths[edge.vertex1()] != null && shortestPaths[edge.vertex1()] > shortestPaths[smallestVertex] + 1) {
                        shortestPaths[edge.vertex1()] = shortestPaths[smallestVertex] + 1;
                        lastVertices[edge.vertex1()] = smallestVertex;
                    }
                }
            }
            shortestPaths[smallestVertex] = null;
            smallestVertex = this.getSmallestIndex(shortestPaths);
        }

        for (Entity entity : this.entities) {
            if (entity instanceof Enemy) {
                ((Enemy)entity).updateShortestPath(lastVertices);
            }
        }
    }

    private Integer getSmallestIndex(Integer[] array) {
        int minValue = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                continue;
            }
            if (array[i] <= minValue) {
                minValue = array[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    // Getter
    public ArrayList<Entity> getEntities() {
        return this.entities;
    }

    public Entity getPlayer() {
        return this.player;
    }
}
