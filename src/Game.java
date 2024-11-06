import Characters.Character;
import Characters.Enemy;
import Characters.Player;
import Engine.Wrap;
import Enums.GameState;
import Engine.Component;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

public class Game implements Component {

    private final Room room;
    private final Wrap wrap;
    private final ArrayList<Character> characters = new ArrayList<>();

    public Game(Wrap wrap) {
        this.wrap = wrap;
        room = new Room(wrap);
        characters.add(new Player(wrap));
        characters.add(new Enemy(wrap));
    }

    public void update() {
        var commands = wrap.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            switch (commands.get(i).command()) {
                case escape -> wrap.changeState(GameState.PAUSE);
            }
            wrap.getControls().removeCommand(commands.get(i));
        }
        characters.sort((a, b) -> -1 * a.compareTo(b));
        for (Character npc : characters)
            npc.update();
        updateHitboxes();
    }

    public void render(Graphics g) {
        room.render(g);
        for (Character npc : characters)
            npc.render(g);

        if (wrap.isHitboxes()) {
            room.getHitbox().render(g);
            for (Character character : characters)
                character.getHitbox().render(g);
        }
    }

    private void updateHitboxes() {
        for (Character character : characters)
            character.getHitbox().resetCollisions();
        for (Character character : characters)
            character.getHitbox().collision(room.getHitbox());
        for (int i = 0; i < characters.size(); i++) {
            for (int j = i; j < characters.size(); j++) {
                if (i == j)
                    continue;
                characters.get(i).getHitbox().collision(characters.get(j).getHitbox());
            }
        }
    }
}