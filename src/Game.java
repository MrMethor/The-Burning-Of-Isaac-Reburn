import Engine.Wrap;
import Entities.Dynamic.Physical.Player;
import Map.Map;
import Enums.GameState;
import Engine.Component;
import Hud.Hud;

import java.awt.Graphics;

public class Game implements Component {

    private final Wrap wrap;
    private Map map;
    private Player player;
    private Hud hud;
    private int currentFloor = 1;
    private boolean isGoldenRoom = true;

    public Game(Wrap wrap) {
        this.wrap = wrap;
        hud = new Hud(wrap);
        player = new Player(wrap, hud);
        map = new Map(wrap, player, currentFloor, isGoldenRoom);
    }

    public void update() {
        if (map.changeLevelRequest())
            changeLevel();
        for (int i = 0; i < wrap.getCommands().size(); i++) {
            switch (wrap.getCommands().get(i).command()) {
                case escape -> wrap.changeState(GameState.PAUSE);
                default -> {
                    continue;
                }
            }
            wrap.getControls().removeCommand(wrap.getCommands().get(i));
        }
        map.update();
        if (player.isDead())
            wrap.changeState(GameState.DEATH);
    }

    public void render(Graphics g) {
        if (player.hasEntities())
            map.currentRoom().render(g);
        hud.render(g);
    }

    private void changeLevel() {
        currentFloor++;
        map = new Map(wrap, player, currentFloor, isGoldenRoom);
        player.resetPosition();
    }
}