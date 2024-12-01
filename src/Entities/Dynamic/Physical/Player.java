package Entities.Dynamic.Physical;

import Engine.Wrap;
import Entities.Dynamic.Projectiles.Fireball;
import Map.Room;
import Enums.EntityType;
import Enums.Side;
import Tools.Collision;
import Tools.EntityList;

import java.util.ArrayList;

public class Player extends PhysicalEntity {

    private int movingX;
    private int movingY;

    private double firingSpeed;
    private double shotSpeed;

    private final ArrayList<Integer> firingOrder = new ArrayList<>();
    private Side facing = null;
    private int firingTime;
    private int fireCounter;

    public Player(Wrap wrap, EntityList entities, Room room) {
        super(wrap, entities, room, EntityType.PLAYER, "resource/character.png", 3, 4, 1920/2.0, 1080/2.0, 150, 150, 0.5, 0.5, 0, 0.3);
        firingSpeed = 2;
        shotSpeed = 8;
        firingTime = (int)(60 / firingSpeed);
        fireCounter = firingTime;
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

        facingDirection(new boolean[]{fireUp, fireDown, fireLeft, fireRight});
        fireCheck();
        move(playerX, playerY);
    }

    protected void applyCollision(Collision collision) {
        switch (collision.entityType()) {
            case ROOM -> applySolidCollision(collision);
            case ITEM, ENEMY -> applyRelativeCollision(collision);
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


    // Help methods
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
        entities.addEntity(new Fireball(wrap, entities, room, x, y, 100, 100, shotSpeed, velocityX, velocityY, angle));
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