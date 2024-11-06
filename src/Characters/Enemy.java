package Characters;

import Engine.Wrap;

public class Enemy extends Character {

    public Enemy(Wrap wrap) {
        super(wrap, true);
        changeSize(150, 0.4, 0.35);
        changeHitboxOffset(0, 0.3);
    }

    @Override
    protected void applyBehavior() {
        int playerX = 0;
        int playerY = 0;

        // Actions
        var actions = wrap.getActions();
        for (int i = 0; i < actions.size(); i++) {
            switch (actions.get(i)) {
                case fireUp -> playerY -= 1;
                case fireDown -> playerY += 1;
                case fireRight -> playerX += 1;
                case fireLeft -> playerX -= 1;
            }
        }
        move(playerX, playerY);
    }

    private void move(int x, int y) {
        double compensation = 1;
        if (x != 0 && y != 0)
            compensation = 0.7;
        velocityX += x * speed * compensation;
        velocityY += y * speed * compensation;
    }
}
