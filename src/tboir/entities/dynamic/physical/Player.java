package tboir.entities.dynamic.physical;

import tboir.engine.Wrap;
import tboir.entities.dynamic.projectiles.Fireball;
import tboir.enums.EntityType;
import tboir.enums.Side;
import tboir.tools.Collision;
import tboir.map.Map;
import tboir.hud.Hud;

import java.awt.Graphics;
import java.util.ArrayList;

public class Player extends PhysicalEntity {

    private Map map;
    private final Hud hud;

    private boolean dead;

    private int movingX;
    private int movingY;

    private double damage;
    private double range;
    private double firingSpeed;
    private double shotSpeed;
    private double shotSize;

    private final ArrayList<Integer> firingOrder;
    private Side facing;
    private int firingTime;
    private int fireCounter;

    private int redHearts;
    private int redContainers;
    private int soulHearts;

    public static final int MAX_HEALTH = 24;

    private final int gracePeriod;
    private int gracePeriodCounter;

    public Player(Wrap wrap, Hud hud) {
        super(wrap, null, EntityType.PLAYER, "resource/entities/character.png", 3, 4, 1920 / 2.0, 1080 / 2.0, 150, 150, 0.4, 0.4, 0, 0.3);
        this.hud = hud;
        this.dead = false;
        this.firingSpeed = 2;
        this.redHearts = 0;
        this.redContainers = 0;
        this.soulHearts = 0;
        this.addHealth(6, 0, 0);
        this.shotSize = 1;
        this.shotSpeed = 6;
        this.damage = 1;
        this.range = 1.2;
        this.firingTime = (int)(60 / this.firingSpeed);
        this.fireCounter = this.firingTime;
        this.gracePeriod = 60;
        this.gracePeriodCounter = 0;
        this.facing = null;
        this.firingOrder = new ArrayList<>();
    }

    @Override
    public void applyBehavior() {
        int playerX = 0;
        int playerY = 0;
        boolean fireUp = false;
        boolean fireDown = false;
        boolean fireLeft = false;
        boolean fireRight = false;

        var actions = this.getWrap().getActions();
        for (int i = 0; i < actions.size(); i++) {
            switch (actions.get(i)) {
                case moveUp -> playerY -= 1;
                case moveDown -> playerY += 1;
                case moveRight -> playerX += 1;
                case moveLeft -> playerX -= 1;
                case fireUp -> fireUp = true;
                case fireDown -> fireDown = true;
                case fireLeft -> fireLeft = true;
                case fireRight -> fireRight = true;
            }
        }
        this.facingDirection(new boolean[]{fireUp, fireDown, fireLeft, fireRight});
        this.fireCheck();
        this.move(playerX, playerY);
        if (this.gracePeriodCounter > 0) {
            this.gracePeriodCounter--;
        }
    }

    @Override
    protected void applyCollision(Collision collision) {
        switch (collision.entity().getType()) {
            case WALL, OBSTACLE -> this.applySolidCollision(collision);
            case ITEM -> this.applyRelativeCollision(collision);
            case ENEMY_PROJECTILE -> this.hit(1);
            case ENEMY -> {
                this.applyRelativeCollision(collision);
                this.hit(1);
            }
            case DOOR -> this.map.queueChangeRoom(collision.side());
            case SPIKE -> this.hit(2);
            case TRAP_DOOR -> this.map.tryChangeLevel();
            case FULL_HEART, SOUL_HEART, HALF_HEART -> {
                if (this.consumeHeart(collision.entity().getType())) {
                    collision.entity().destroy();
                    this.hud.updateHearts(this.redHearts, this.redContainers, this.soulHearts);
                } else {
                    this.applyRelativeCollision(collision);
                }
            }
        }
    }

    @Override
    public void animate() {
        int numFrame = (int)(this.getAnimationCounter() / (10.0 / this.getSpeed()) / (this.getWidth() / 150.0) % 4);
        int column = numFrame == 3 ? 1 : numFrame;
        int row;
        if (this.facing != null) {
            row = this.facing.num();
            if (this.movingX == 0 && this.movingY == 0) {
                column = 1;
            }
        } else if (this.movingY == -1) {
            row = 0;
        } else if (this.movingY == 1) {
            row = 1;
        } else if (this.movingX == -1) {
            row = 2;
        } else if (this.movingX == 1) {
            row = 3;
        } else {
            row = 1;
            column = 1;
        }
        this.swapTexture(column, row);
        this.addToAnimationCounter();
    }

    @Override
    public void render(Graphics g) {
        if (this.gracePeriodCounter != 0 && this.gracePeriodCounter / 5 % 2 == 1) {
            return;
        }
        super.render(g);
    }

    public void changeRoom(Side side) {
        int halfARoomHeight = 725;
        int halfARoomWidth = 1290;
        double x = Wrap.CENTER_X;
        double y = Wrap.CENTER_Y - 40;
        switch (side) {
            case UP -> y = this.getY() + halfARoomHeight;
            case DOWN -> y = this.getY() - halfARoomHeight;
            case LEFT -> x = this.getX() + halfARoomWidth;
            case RIGHT -> x = this.getX() - halfARoomWidth;
        }
        this.changePosition(x, y);
        this.resetVelocity();
    }

    public void addHealth(int redHearts, int redContainers, int soulHearts) {
        if (redContainers % 2 != 0) {
            redContainers++;
        }
        this.redContainers += redContainers + redHearts;
        this.redHearts += redHearts;
        this.soulHearts += soulHearts;

        if (this.redHearts > this.redContainers) {
            this.redHearts = this.redContainers;
        }

        if (this.soulHearts + this.redContainers > MAX_HEALTH) {
            int excessHearts = this.soulHearts + this.redContainers - MAX_HEALTH;
            if (this.soulHearts < excessHearts) {
                excessHearts -= this.soulHearts;
                this.soulHearts = 0;
            }
            this.redContainers -= excessHearts;
        }
        this.hud.updateHearts(this.redHearts, this.redContainers, this.soulHearts);
    }

    public void addStats(double damage, double range, double shotSpeed, double fireSpeed, double shotSize, double speed, int size) {
        this.damage += damage;
        this.range += range;
        this.shotSpeed += shotSpeed;
        this.firingSpeed += fireSpeed;
        this.shotSize += shotSize;
        this.addSpeed(speed);
        this.addSize(size);
    }

    public void addSpecial(int id) {
    }

    public void referenceMap(Map m) {
        this.map = m;
    }

    public boolean isDead() {
        return this.dead;
    }

    // Help methods
    private void hit(double damage) {
        if (this.gracePeriodCounter == 0) {
            if (this.soulHearts > 0) {
                this.soulHearts -= damage;
            } else {
                this.redHearts -= damage;
            }

            this.gracePeriodCounter = this.gracePeriod;
        }
        this.hud.updateHearts(this.redHearts, this.redContainers, this.soulHearts);
        if (this.redHearts + this.soulHearts <= 0) {
            this.dead = true;
        }
    }

    private boolean consumeHeart(EntityType type) {
        switch (type) {
            case FULL_HEART -> {
                if (this.redHearts >= this.redContainers) {
                    return false;
                }
                this.redHearts += 2;
                if (this.redHearts > this.redContainers) {
                    this.redContainers--;
                }
                return true;
            }
            case HALF_HEART -> {
                if (this.redHearts >= this.redContainers) {
                    return false;
                }
                this.redHearts++;
                return true;
            }
            case SOUL_HEART -> {
                if (this.soulHearts + this.redContainers >= MAX_HEALTH) {
                    return false;
                }
                this.soulHearts += 2;
                if (this.soulHearts + this.redContainers > MAX_HEALTH) {
                    this.soulHearts--;
                }
                return true;
            }
        }
        return false;
    }

    private void facingDirection(boolean[] sides) {
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] && !this.firingOrder.contains(i)) {
                this.firingOrder.addFirst(i);
            } else if (!sides[i] && this.firingOrder.contains(i)) {
                this.firingOrder.remove(Integer.valueOf(i));
            }
        }
        if (!this.firingOrder.isEmpty()) {
            this.facing = Side.getSide(this.firingOrder.getFirst());
        } else {
            this.facing = null;
        }
    }

    private void fireCheck() {
        if (this.facing != null && this.fireCounter >= this.firingTime) {
            this.fire();
            this.fireCounter -= this.firingTime;
            this.firingTime = (int)(60 / this.firingSpeed);
        }
        if (this.fireCounter != this.firingTime) {
            this.fireCounter++;
        }
    }

    private void fire() {
        int fireX = 0;
        int fireY = 0;
        double x = this.getX();
        double y = this.getY();
        switch (this.facing) {
            case UP -> {
                fireY = 1;
                y -= this.getHeight() / 4;
            }
            case DOWN -> {
                fireY = -1;
                y += this.getHeight() / 4;
            }
            case LEFT -> {
                fireX = -1;
                x -= this.getWidth() / 4;
            }
            case RIGHT -> {
                fireX = 1;
                x += this.getWidth() / 4;
            }
        }
        var rad = Math.atan2(fireY, fireX);
        int angle = (int)(rad * (180 / Math.PI));
        angle = angle < 0 ? angle + 360 : angle;
        this.addEntity(new Fireball(this.getWrap(), this.getEntities(), this.damage, this.range, x, y + 15, (int)(100 * this.shotSize), (int)(100 * this.shotSize), this.shotSpeed, 0, 0, angle));
    }

    private void move(int x, int y) {
        this.movingX = x;
        this.movingY = y;
        double compensation = 1;
        if (x != 0 && y != 0) {
            compensation = 0.7;
        }
        this.addVelocity(x * this.getSpeed() * compensation, y * this.getSpeed() * compensation);
    }
}