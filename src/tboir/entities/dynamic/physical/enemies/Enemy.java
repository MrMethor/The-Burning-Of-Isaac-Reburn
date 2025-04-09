package tboir.entities.dynamic.physical.enemies;

import tboir.engine.Wrap;
import tboir.entities.Entity;
import tboir.entities.dynamic.physical.PhysicalEntity;
import tboir.entities.dynamic.physical.PickUp;
import tboir.entities.dynamic.projectiles.Projectile;
import tboir.entities.EntityType;
import tboir.map.Room;
import tboir.tools.Collision;
import tboir.tools.Coords;
import tboir.map.EntityManager;


import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public abstract class Enemy extends PhysicalEntity {

    private double health;
    private int initialWait;

    private int[] shortestPaths;
    private boolean[][] tileObstacleGrid;
    private final ArrayList<Coords> lineOfSight;
    private boolean isDead;

    public Enemy(Wrap wrap, EntityManager entities, EntityType type, double health, String name, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super(wrap, entities, type, name, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.health = health;
        this.initialWait = 20;
        this.lineOfSight = new ArrayList<>();
        this.shortestPaths = new int[Room.TILES_WIDTH * Room.TILES_HEIGHT];
    }

    @Override
    protected void applyCollision(Collision collision) {
        if (this.isDead) {
            return;
        }
        switch (collision.entity().getType()) {
            case WALL -> this.applySolidCollision(collision);
            case PLAYER, ENEMY -> this.applyRelativeCollision(collision);
            case OBSTACLE -> {
                if (!this.canFly()) {
                    this.applySolidCollision(collision);
                }
            }
            case HALF_HEART, FULL_HEART, SOUL_HEART -> {
                if (!this.canFly()) {
                    this.applyRelativeCollision(collision);
                }
            }
            case SPIKE -> {
                if (!this.canFly()) {
                    this.health -= 0.1;
                }
            }
            case FRIENDLY_PROJECTILE -> {
                this.health -= ((Projectile)collision.entity()).getDamage();
                int addVelocityX = 0;
                int addVelocityY = 0;
                switch (collision.side()) {
                    case LEFT -> addVelocityX += 10;
                    case RIGHT -> addVelocityX -= 10;
                    case UP -> addVelocityY += 10;
                    case DOWN -> addVelocityY -= 10;
                }
                this.addVelocity(addVelocityX, addVelocityY);
            }
        }
        if (this.health <= 0) {
            this.isDead = true;
            this.dropRoll();
            this.destroy();
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if (this.getWrap().isHitboxes()) {
            for (Coords coords : this.lineOfSight) {
                Color c = g.getColor();
                g.setColor(new Color(0f, 1f, 0f, .2f));
                g.fillOval(coords.x() - 2, coords.y() - 2, 4, 4);
                g.setColor(c);
            }
        }
    }

    private void dropRoll() {
        Random random = new Random();
        int chance = random.nextInt(10);
        if (chance == 0) {
            addEntity(new PickUp(this.getWrap(), this.getEntities(), EntityType.HALF_HEART, this.getX(), this.getY()));
        }
    }

    protected boolean waitInitially() {
        if (this.initialWait == 0) {
            return false;
        } else {
            this.initialWait--;
        }
        return true;
    }

    public void updateObstacleGrid(boolean[][] tileObstacleGrid) {
        this.tileObstacleGrid = tileObstacleGrid;
    }

    public void updateShortestPath(int[] shortestPaths) {
        this.shortestPaths = shortestPaths;
    }

    public Entity getPlayer() {
        return this.getEntities().getPlayer();
    }

    protected void resetLineOfSight() {
        this.lineOfSight.clear();
    }

    protected void addToLineOfSight(Coords coords) {
        this.lineOfSight.add(coords);
    }

    protected boolean[][] getObstacleGrid() {
        return this.tileObstacleGrid;
    }

    protected int[] getShortestPaths() {
        return this.shortestPaths;
    }
}
