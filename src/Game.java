import Engine.Wrap;
import Entities.Entity;
import Entities.Dynamic.Physical.Player;
import Entities.Room;
import Enums.GameState;
import Engine.Component;

import java.awt.Graphics;
import java.util.ArrayList;

public class Game implements Component {

    private final Wrap wrap;
    private Room room;
    private Player player;

    public Game(Wrap wrap) {
        this.wrap = wrap;
        var entities = new ArrayList<Entity>();
        room = new Room(wrap, entities);
        player = new Player(wrap, entities, room);
        entities.add(player);
    }

    public void update() {
        for (int i = 0; i < wrap.getCommands().size(); i++) {
            switch (wrap.getCommands().get(i).command()) {
                case escape -> wrap.changeState(GameState.PAUSE);
            }
            wrap.getControls().removeCommand(wrap.getCommands().get(i));
        }
        room.update();
    }

    public void render(Graphics g) {
        room.render(g);
    }
}