import Engine.Wrap;
import Entities.Dynamic.Physical.Player;
import Map.Room;
import Enums.GameState;
import Engine.Component;
import Tools.EntityList;

import java.awt.Graphics;

public class Game implements Component {

    private final Wrap wrap;
    private Room room;
    private Player player;

    public Game(Wrap wrap) {
        this.wrap = wrap;
        EntityList entities = new EntityList();
        room = new Room(wrap, entities);
        player = new Player(wrap, entities, room);
        entities.addEntity(player);
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