package tboir.entities.dynamic.physical;

import tboir.engine.Wrap;
import tboir.entities.dynamic.projectiles.Fireball;
import tboir.enums.EntityType;
import tboir.enums.Side;
import tboir.tools.Collision;
import tboir.tools.EntityManager;
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
        super(wrap, null, EntityType.PLAYER, "resource/entities/character.png", 3, 4, 1920/2.0, 1080/2.0, 150, 150, 0.4, 0.4, 0, 0.3);
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
        boolean fireUp = false, fireDown = false, fireLeft = false, fireRight = false;

        var actions = this.wrap.getActions();
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
        facingDirection(new boolean[]{fireUp, fireDown, fireLeft, fireRight});
        fireCheck();
        move(playerX, playerY);
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
            case DOOR -> map.queueChangeRoom(collision.side());
            case SPIKE -> this.hit(2);
            case TRAP_DOOR -> map.tryChangeLevel();
            case FULL_HEART, SOUL_HEART, HALF_HEART -> {
                if (this.consumeHeart(collision.entity().getType())){
                    collision.entity().destroy();
                    hud.updateHearts(redHearts, redContainers, soulHearts);
                }
                else
                    this.applyRelativeCollision(collision);
            }
        }
    }

    @Override
    public void animate() {
        int numFrame = (int) (this.animationCounter / (10.0 / this.speed) / (this.width / 150.0) % 4);
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
        this.animationCounter++;
    }

    @Override
    public void render(Graphics g) {
        if (this.gracePeriodCounter != 0 && this.gracePeriodCounter / 5 % 2 == 1)
            return;
        super.render(g);
    }

    public void changeRoom(Side side) {
        int halfARoomHeight = 725;
        int halfARoomWidth = 1290;
        switch (side) {
            case UP -> this.y = this.previousY = this.y + halfARoomHeight;
            case DOWN -> this.y = this.previousY = this.y - halfARoomHeight;
            case LEFT -> this.x = this.previousX = this.x + halfARoomWidth;
            case RIGHT -> this.x = this.previousX = this.x - halfARoomWidth;
        }
        switch (side) {
            case UP, DOWN -> this.x = this.previousX = 1920 / 2.0;
            case LEFT, RIGHT -> this.y = this.previousY = 1080 / 2.0 - 40;
        }
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public void resetPosition() {
        this.x = this.previousX = 1920 / 2.0;
        this.y = this.previousY = 1080 / 2.0;
    }

    public void addHealth(int redHearts, int redContainers, int soulHearts) {
        if (redContainers % 2 != 0)
            redContainers++;
        this.redContainers += redContainers + redHearts;
        this.redHearts += redHearts;
        this.soulHearts += soulHearts;

        if (this.redHearts > this.redContainers)
            this.redHearts = this.redContainers;

        if (this.soulHearts + this.redContainers > MAX_HEALTH) {
            int excessHearts = this.soulHearts + this.redContainers - MAX_HEALTH;
            if (this.soulHearts < excessHearts){
                excessHearts -= this.soulHearts;
                this.soulHearts = 0;
            }
            this.redContainers -= excessHearts;
        }
        hud.updateHearts(this.redHearts, this.redContainers, this.soulHearts);
    }

    public void addStats(double damage, double range, double shotSpeed, double fireSpeed, double shotSize, double speed, int size) {
        this.damage += damage;
        this.range += range;
        this.shotSpeed += shotSpeed;
        this.firingSpeed += fireSpeed;
        this.shotSize += shotSize;
        this.speed += speed;
        this.width += size;
        this.height += size;
    }

    public void addSpecial(int id) {

    }

    public void referenceEntities(EntityManager e) {
        this.entities = e;
    }

    public void referenceMap(Map m) {
        this.map = m;
    }

    // Getters
    public boolean hasEntities() {
        return this.entities != null;
    }

    public boolean isDead() {
        return this.dead;
    }

    // Help methods
    private void hit(double damage) {
        if (gracePeriodCounter == 0) {
            if (soulHearts > 0)
                soulHearts -= damage;
            else
                redHearts -= damage;

            gracePeriodCounter = gracePeriod;
        }
        hud.updateHearts(redHearts, redContainers, soulHearts);
        if (redHearts + soulHearts <= 0)
            dead = true;
    }

    private boolean consumeHeart(EntityType type) {
        switch (type) {
            case FULL_HEART -> {
                if (redHearts >= redContainers)
                    return false;
                redHearts += 2;
                if (redHearts > redContainers)
                    redContainers--;
                return true;
            }
            case HALF_HEART -> {
                if (redHearts >= redContainers)
                    return false;
                redHearts++;
                return true;
            }
            case SOUL_HEART -> {
                if (soulHearts + redContainers >= MAX_HEALTH)
                    return false;
                soulHearts += 2;
                if (soulHearts + redContainers > MAX_HEALTH)
                    soulHearts--;
                return true;
            }
        }
        return false;
    }

    private void facingDirection(boolean[] sides) {
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] && !firingOrder.contains(i))
                firingOrder.addFirst(i);
            else if (!sides[i] && firingOrder.contains(i))
                firingOrder.remove(Integer.valueOf(i));
        }
        if (!firingOrder.isEmpty())
            facing = Side.getSide(firingOrder.getFirst());
        else
            facing = null;
    }

    private void fireCheck() {
        if (facing != null && fireCounter >= firingTime) {
            fire();
            fireCounter -= firingTime;
            firingTime = (int)(60 / firingSpeed);
        }
        if (fireCounter != firingTime)
            fireCounter++;
    }

    private void fire() {
        int fireX = 0;
        int fireY = 0;
        double x = this.x;
        double y = this.y;
        switch (facing) {
            case UP -> {
                fireY = 1;
                y -= height / 4;
            }
            case DOWN -> {
                fireY = -1;
                y += height / 4;
            }
            case LEFT -> {
                fireX = -1;
                x -= width / 4;
            }
            case RIGHT -> {
                fireX = 1;
                x += width / 4;
            }
        }
        var rad = Math.atan2(fireY, fireX);
        int angle = (int)(rad * (180 / Math.PI));
        angle = angle < 0 ? angle + 360 : angle;
        entities.addEntity(new Fireball(wrap, entities, damage, range, x, y + 15, (int)(100 * shotSize), (int)(100 * shotSize), shotSpeed, velocityX, velocityY, angle));
    }

    private void move(int x, int y) {
        movingX = x;
        movingY = y;
        double compensation = 1;
        if (x != 0 && y != 0)
            compensation = 0.7;
        velocityX += x * speed * compensation;
        velocityY += y * speed * compensation;
    }
}