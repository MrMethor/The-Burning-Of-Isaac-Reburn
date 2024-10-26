import Engine.Wrap;
import Enums.GameState;
import Engine.Component;
import Tools.Hitbox;

import java.awt.Graphics;
import java.util.ArrayList;

public class Game implements Component {

    private final Room room;
    private final Player player;
    private final Wrap wrap;
    private final ArrayList<Hitbox> hitboxes;

    public Game(Wrap wrap) {
        this.wrap = wrap;
        hitboxes = new ArrayList<>();
        room = new Room(wrap, hitboxes);
        player = new Player(wrap, hitboxes);
    }

    public void update() {
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

        // Commands
        var commands = wrap.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            switch (commands.get(i).command()) {
                case escape -> wrap.changeState(GameState.PAUSE);
            }
            wrap.getControls().removeCommand(commands.get(i));
        }

        // Components
        player.firingDirection(new boolean[]{fireUp, fireDown, fireLeft, fireRight});
        player.move(playerX, playerY);
        player.update();
        updateHitboxes();
    }

    public void render(Graphics g) {
        room.render(g);
        player.render(g);
        if (wrap.isHitboxes()) {
            for (int i = 0; i < hitboxes.size(); i++)
                hitboxes.get(i).draw(g);
        }
    }

    private void updateHitboxes() {
        for (int i = 0; i < hitboxes.size(); i++)
            hitboxes.get(i).resetCollisions();
        for (int i = 0; i < hitboxes.size(); i++) {
            for (int j = 0; j < hitboxes.size(); j++) {
                if (j == i)
                    continue;
                hitboxes.get(i).collision(hitboxes.get(j));
            }
        }
    }
}