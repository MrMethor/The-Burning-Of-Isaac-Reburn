import Entities.Item;
import Entities.Player;
import Engine.Wrap;
import Enums.GameState;
import Engine.Component;
import Entities.Entity;

import java.awt.Graphics;
import java.util.ArrayList;

public class Game implements Component {

    private final Wrap wrap;
    private Entity room;
    private final ArrayList<Entity> entities = new ArrayList<>();

    public Game(Wrap wrap) {
        this.wrap = wrap;
        room = new Room(wrap);
        entities.add(new Player(wrap));
        entities.add(new Item(wrap));
    }

    public void update() {
        for (int i = 0; i < wrap.getCommands().size(); i++) {
            switch (wrap.getCommands().get(i).command()) {
                case escape -> wrap.changeState(GameState.PAUSE);
            }
            wrap.getControls().removeCommand(wrap.getCommands().get(i));
        }
        entities.sort((a, b) -> -1 * a.compareTo(b));
        for (Entity entity : this.entities)
            entity.update();
        checkCollision();
    }

    public void render(Graphics g) {
        room.render(g);
        for (Entity entity : entities)
            entity.render(g);
    }

    private void checkCollision() {
        for (Entity entity : entities)
            entity.collision(room);
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i; j < entities.size(); j++) {
                if (i != j)
                    entities.get(i).collision(entities.get(j));
            }
        }
    }
}