public class Game {

    private final Player player;
    private Pause pause = null;
    private boolean exited = false;

    public Game() {
        this.player = new Player();
    }

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
        for (int i = 0; i < controls.getKeyboard().length; i++) {
            switch (controls.getKeyboard()[i]) {
                case moveUP:
                    playerY += -1;
                    break;
                case moveDown:
                    playerY += 1;
                    break;
                case moveRight:
                    playerX += 1;
                    break;
                case moveLeft:
                    playerX += -1;
                    break;
                case escape:
                    controls.removeCommand(Keyboard.escape);
                    this.pause = new Pause();
                    break;
            }
        }
        player.move(playerX, playerY);
        player.update();
    }

    public boolean exited() {
        return exited;
    }
}