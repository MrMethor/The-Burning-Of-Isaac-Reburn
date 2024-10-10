public class Game {

    private final Player player = new Player();
    private Pause pause = null;
    private boolean exited = false;

    public void update(Controls controls) {
        // Paused
        if (this.pause != null) {
            this.pause.update(controls);
            if (this.pause.resumed()) {
                this.pause.close();
                this.pause = null;
            }
            else if (this.pause.exited()) {
                this.pause.close();
                this.pause = null;
                this.player.close();
                this.exited = true;
            }
            return;
        }

        // Game
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
                    this.pause = new Pause();
                    break;
            }
        }
        this.player.firingDirection(fire);
        this.player.move(playerX, playerY);
        this.player.update();
    }

    public void render() {
        if (this.pause == null)
            this.player.render();
    }

    public boolean exited() {
        return exited;
    }
}