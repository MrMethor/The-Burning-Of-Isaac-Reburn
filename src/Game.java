import Enums.GameState;
import Tools.Controls;
import Tools.Interpolation;

import java.awt.*;

// Holds everything game related
public class Game {

    private final Room room = new Room();
    private final Player player = new Player();

    // Processes input and game components
    public void update(Controls controls) {
        int playerX = 0;
        int playerY = 0;
        boolean fireUp = false, fireDown = false, fireLeft = false, fireRight = false;

        // Tools.Actions
        for (int i = 0; i < controls.actions().size(); i++) {
            switch (controls.actions().get(i)) {
                case moveUp: playerY -= 1; break;
                case moveDown: playerY += 1; break;
                case moveRight: playerX += 1; break;
                case moveLeft: playerX -= 1; break;
                case fireUp: fireUp = true; break;
                case fireDown: fireDown = true; break;
                case fireLeft: fireLeft = true; break;
                case fireRight: fireRight = true; break;
            }
        }

        // Tools.Commands
        for (int i = 0; i < controls.commands().size(); i++) {
            switch (controls.commands().get(i).command()) {
                case escape: Main.changeState(GameState.PAUSE); break;
            }
            controls.removeCommand(controls.commands().get(i));
        }

        // Components
        player.firingDirection(new boolean[]{fireUp, fireDown, fireLeft, fireRight});
        player.move(playerX, playerY);
        player.update();
    }

    // Draws the game graphics
    public void render(Graphics g, Interpolation interpolation) {
        room.render(g, interpolation);
        player.render(g, interpolation);
    }
}