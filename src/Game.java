import Engine.Wrap;
import Entities.Dynamic.Physical.Player;
import Map.Map;
import Enums.GameState;
import Engine.Component;

import java.awt.Graphics;

public class Game implements Component {

    private final Wrap wrap;
    private Map map;
    private int currentFloor = 1;
    private Player player;

    public Game(Wrap wrap) {
        this.wrap = wrap;
        player = new Player(wrap);
        map = new Map(wrap, player, currentFloor, true);
    }

    public void update() {
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
    }

    public void render(Graphics g) {
        if (player.hasEntities())
            map.currentRoom().render(g);
    }
}