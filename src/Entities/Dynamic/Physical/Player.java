package Entities.Dynamic.Physical;

import Engine.Wrap;
import Entities.Dynamic.Projectiles.Fireball;
import Enums.EntityType;
import Enums.Side;
import Tools.Collision;
import Tools.EntityList;
import Map.Map;

import java.awt.*;
import java.util.ArrayList;

public class Player extends PhysicalEntity {

    private Map map;

    private int movingX;
    private int movingY;

    private double firingSpeed;
    private double shotSpeed;
    private double shotSize;

    private final ArrayList<Integer> firingOrder = new ArrayList<>();
    private Side facing = null;
    private int firingTime;
    private int fireCounter;

    private int health;
    private int gracePeriod = 60;
    private int gracePeriodCounter = 0;

    public Player(Wrap wrap) {
        super(wrap, null, EntityType.PLAYER, "resource/spriteSheets/character.png", 3, 4, 1920/2.0, 1080/2.0, 150, 150, 0.4, 0.4, 0, 0.3);
        firingSpeed = 5;
        health = 6;
        shotSize = 1;
        shotSpeed = 8;
        firingTime = (int)(60 / firingSpeed);
        fireCounter = firingTime;
    }

    public void referenceEntities(EntityList e) {
        entities = e;
    }

    public void referenceMap(Map m) {
        map = m;
    }

    public boolean hasEntities() {
        return entities != null;
    }

    public void applyBehavior() {

        int playerX = 0;
        int playerY = 0;
        boolean fireUp = false, fireDown = false, fireLeft = false, fireRight = false;

        var actions = wrap.getActions();
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

        for (int i = 0; i < wrap.getCommands().size(); i++) {
            switch (wrap.getCommands().get(i).command()) {
                case space -> map.tryChangeRoom();
                default -> {
                    continue;
                }
            }
            wrap.getControls().removeCommand(wrap.getCommands().get(i));
        }

        facingDirection(new boolean[]{fireUp, fireDown, fireLeft, fireRight});
        fireCheck();
        move(playerX, playerY);
        if (gracePeriodCounter > 0)
            gracePeriodCounter--;
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entityType()) {
            case ROOM, OBSTACLE -> applySolidCollision(collision);
            case ITEM -> applyRelativeCollision(collision);
            case ENEMY -> {
                applyRelativeCollision(collision);
                hit(1);
            }
            case SPIKE -> hit(2);
        }
    }

    public void animate() {
        int numFrame = (int) (animationCounter / (10.0 / speed) / (width / 150.0) % 4);
        int column = numFrame == 3 ? 1 : numFrame;
        int row;
        if (facing != null) {
            row = facing.num();
            if (movingX == 0 && movingY == 0)
                column = 1;
        }
        else if (movingY == -1)
            row = 0;
        else if (movingY == 1)
            row = 1;
        else if (movingX == -1)
            row = 2;
        else if (movingX == 1)
            row = 3;
        else {
            row = 1;
            column = 1;
        }
        swapTexture(column, row);
        animationCounter++;
    }

    public void render(Graphics g) {
        if (gracePeriodCounter != 0 && gracePeriodCounter / 5 % 2 == 1)
            return;
        super.render(g);
    }

    public void changeRoom(Side side) {
        int halfARoomHeight = 680;
        int halfARoomWidth = 1265;
        switch (side) {
            case UP -> y = previousY = y + halfARoomHeight;
            case DOWN -> y = previousY = y - halfARoomHeight;
            case LEFT -> x = previousX = x + halfARoomWidth;
            case RIGHT -> x = previousX = x - halfARoomWidth;
        }
        switch (side) {
            case UP, DOWN -> x = previousX = 1920 / 2.0;
            case LEFT, RIGHT -> y = previousY = 1080 / 2.0 - 40;
        }
        velocityX = 0;
        velocityY = 0;
    }


    // Help methods
    private void hit(int damage) {
        if (gracePeriodCounter == 0) {
            health -= damage;
            gracePeriodCounter = gracePeriod;
        }
    }

    private void facingDirection(boolean[] sides) {
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] && !firingOrder.contains(i))
                firingOrder.add(0, i);
            else if (!sides[i] && firingOrder.contains(i))
                firingOrder.remove(Integer.valueOf(i));
        }
        if (!firingOrder.isEmpty())
            facing = Side.getSide(firingOrder.get(0));
        else
            facing = null;
    }

    private void fireCheck() {
        if (facing != null && fireCounter >= firingTime) {
            fire();
            fireCounter -= firingTime;
        }
        if (fireCounter != firingTime)
            fireCounter++;
    }

    private void fire() {
        int fireX = 0;
        int fireY = 0;
        switch (facing) {
            case UP -> fireY = 1;
            case DOWN -> fireY = -1;
            case LEFT -> fireX = -1;
            case RIGHT -> fireX = 1;
        }
        var rad = Math.atan2(fireY, fireX);
        int angle = (int)(rad * (180 / Math.PI));
        if (angle < 0)
            angle += 360;
        entities.addEntity(new Fireball(wrap, entities, x, y + height / 10, (int)(100 * shotSize), (int)(100 * shotSize), shotSpeed, velocityX, velocityY, angle));
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