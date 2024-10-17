import java.awt.Graphics2D;

public class Game {

    private final Player player = new Player();

    public void update(Controls controls) {
        int playerX = 0;
        int playerY = 0;
        boolean[] fire = {false, false, false, false};

        for (int i = 0; i < controls.keyboard().length; i++) {
            switch (controls.keyboard()[i]) {
                case moveUP: playerY -= 1; break;
                case moveDown:  playerY += 1; break;
                case moveRight: playerX += 1; break;
                case moveLeft: playerX -= 1; break;
                case fireUp: fire[Side.UP.num()] = true; break;
                case fireDown: fire[Side.DOWN.num()] = true; break;
                case fireLeft: fire[Side.LEFT.num()] = true; break;
                case fireRight: fire[Side.RIGHT.num()] = true; break;
                case escape:
                    controls.removeCommand(Keyboard.escape);
                    Main.changeState(2);
                    break;
            }
        }
        player.firingDirection(fire);
        player.move(playerX, playerY);
        player.update();
    }

    public void render(Graphics2D g) {
        player.render();
    }
}