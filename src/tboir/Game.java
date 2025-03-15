package tboir;

import tboir.engine.Wrap;
import tboir.entities.dynamic.physical.Player;
import tboir.map.Map;
import tboir.enums.GameState;
import tboir.hud.Hud;

import java.awt.Graphics;

public class Game {

    private final Wrap wrap;
    private Map map;
    private final Player player;
    private final Hud hud;
    private int currentFloor;

    public Game(Wrap wrap) {
        this.wrap = wrap;
        this.currentFloor = 1;
        this.hud = new Hud(this.wrap);
        this.player = new Player(this.wrap, this.hud);
        this.map = new Map(this.wrap, this.player, this.currentFloor, true);
    }

    public void update() {
        if (this.map.changeLevelRequest()) {
            this.changeLevel();
        }
        for (int i = 0; i < this.wrap.getCommands().size(); i++) {
            switch (this.wrap.getCommands().get(i).command()) {
                case escape -> this.wrap.changeState(GameState.PAUSE);
                default -> {
                    continue;
                }
            }
            this.wrap.getControls().removeCommand(this.wrap.getCommands().get(i));
        }
        this.map.update();
        if (this.player.isDead()) {
            this.wrap.changeState(GameState.DEATH);
        }
    }

    public void render(Graphics g) {
        if (this.player.hasEntities()) {
            this.map.currentRoom().render(g);
        }
        this.hud.render(g);
    }

    private void changeLevel() {
        this.currentFloor++;
        this.map = new Map(this.wrap, this.player, this.currentFloor, true);
        this.player.resetPosition();
    }
}