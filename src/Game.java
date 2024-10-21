import Engine.Wrap;
import Enums.GameState;
import java.awt.Graphics;

public class Game {

    private final Room room;
    private final Player player;
    private final Wrap wrap;

    public Game(Wrap wrap) {
        this.wrap = wrap;
        player = new Player(wrap);
        room = new Room(wrap);
    }

    public void update() {
        int playerX = 0;
        int playerY = 0;
        boolean fireUp = false, fireDown = false, fireLeft = false, fireRight = false;

        // Actions
        var actions = wrap.getActions();
        for (int i = 0; i < actions.size(); i++) {
            switch (actions.get(i)) {
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

        // Commands
        var commands = wrap.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            switch (commands.get(i).command()) {
                case escape: wrap.changeState(GameState.PAUSE); break;
            }
            wrap.getControls().removeCommand(commands.get(i));
        }

        // Components
        player.firingDirection(new boolean[]{fireUp, fireDown, fireLeft, fireRight});
        player.move(playerX, playerY);
        player.update();
    }

    public void render(Graphics g) {
        room.render(g);
        player.render(g);
    }
}