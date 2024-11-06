package Characters;

import Engine.Wrap;
import Enums.Side;

public class Player extends Character {

    private int movingX;
    private int movingY;
    private long animationCounter;

    public Player(Wrap wrap) {
        super(wrap, false);
        changeSpriteSheet("resource/character.png", 3, 4, 1, 1);
        changeSize(150, 0.4, 0.35);
        changeHitboxOffset(0, 0.3);
    }

    @Override
    protected void applyBehavior() {
        int playerX = 0;
        int playerY = 0;
        boolean fireUp = false, fireDown = false, fireLeft = false, fireRight = false;

        // Actions
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
        move(playerX, playerY);
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

    private void move(int x, int y) {
        movingX = x;
        movingY = y;
        double compensation = 1;
        if (x != 0 && y != 0)
            compensation = 0.7;
        velocityX += x * speed * compensation;
        velocityY += y * speed * compensation;
    }

    @Override
    protected void animate() {
        if (spriteSheet == null)
            return;
        int numFrame = (int) (animationCounter / (10.0 / speed) / (size / 150.0) % 4);
        int frame = numFrame == 3 ? 1 : numFrame;
        if (facing != null) {
            if (movingX == 0 && movingY == 0)
                texture.changeImage(spriteSheet.getSprite(1, facing.num()));
            else
                texture.changeImage(spriteSheet.getSprite(frame, facing.num()));
        } else if (movingY == -1)
            texture.changeImage(spriteSheet.getSprite(frame, 0));
        else if (movingY == 1)
            texture.changeImage(spriteSheet.getSprite(frame, 1));
        else if (movingX == -1)
            texture.changeImage(spriteSheet.getSprite(frame, 2));
        else if (movingX == 1)
            texture.changeImage(spriteSheet.getSprite(frame, 3));
        else
            texture.changeImage(spriteSheet.getSprite(1, 1));
        animationCounter++;
    }
}