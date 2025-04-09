package tboir.entities.dynamic.physical.enemies;

import tboir.engine.Wrap;
import tboir.entities.EntityType;
import tboir.map.Room;
import tboir.tools.Coords;
import tboir.map.EntityManager;

public class Body extends Enemy {

    private double movingX;
    private double movingY;
    private final double pointDistance;

    public Body(Wrap wrap, EntityManager entities, double x, double y) {
        super(wrap, entities,
                EntityType.ENEMY,
                5,
                "body",
                x, y,
                100, 100,
                0.5, 0.5,
                0, 0
        );
        this.changeSpeed(.5);
        this.pointDistance = 20;
    }

    @Override
    public void applyBehavior() {
        if (this.waitInitially() || this.getPlayer() == null) {
            return;
        }

        this.resetLineOfSight();

        double targetX = this.getPlayer().getHitboxX() - this.getHitboxX();
        double targetY = this.getPlayer().getHitboxY() - this.getHitboxY();

        double distance = (Math.abs(targetX) + Math.abs(targetY));

        double vectorX = (targetX / distance) * this.pointDistance;
        double vectorY = (targetY / distance) * this.pointDistance;

        double x = this.getHitboxX();
        double y = this.getHitboxY();
        boolean hasLineOfSight;
        while (true) {
            this.addToLineOfSight(new Coords((int)x, (int)y, null));
            if (this.getEntities().checkCollisionWithType(x, y, EntityType.PLAYER)) {
                hasLineOfSight = true;
                break;
            } else if (this.getEntities().checkCollisionWithType(x, y, EntityType.OBSTACLE) || this.getEntities().checkCollisionWithType(x, y, EntityType.WALL)) {
                hasLineOfSight = false;
                break;
            }
            x += vectorX;
            y += vectorY;
        }
        if (!hasLineOfSight) {
            int nextTile = this.getShortestPaths()[Room.getTileX(this) + Room.getTileY(this) * Room.TILES_WIDTH];
            targetX = (Room.getTileCenterX(nextTile % Room.TILES_WIDTH) - this.getHitboxX());
            targetY = (Room.getTileCenterY(nextTile / Room.TILES_WIDTH) - this.getHitboxY());
            if (nextTile == Integer.MAX_VALUE) {
                this.movingX = 0;
                this.movingY = 0;
                return;
            }
            distance = (Math.abs(targetX) + Math.abs(targetY));
        }
        this.movingX = targetX / distance;
        this.movingY = targetY / distance;
        this.addVelocity(this.movingX * this.getSpeed(), this.movingY * this.getSpeed());
    }

    @Override
    public void animate() {
        this.getAnimation().mirrorHorizontally(false);
        if (Math.abs(this.movingX) > Math.abs(this.movingY)) {
            this.getAnimation().changeRow(1);
            if (this.movingX < 0) {
                this.getAnimation().mirrorHorizontally(true);
            }
        } else {
            this.getAnimation().changeRow(0);
        }
        if (this.movingX == 0 && this.movingY == 0) {
            this.getAnimation().changeAnimationFrame(0);
        } else if (this.getWrap().isTimeToAnimate(6)) {
            this.getAnimation().incrementAndReset(9);
        }
    }
}
