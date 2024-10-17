import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class Controls implements KeyListener {

    private final Mouse[] mouse = new Mouse[3];
    private final Keyboard[] keyboard = new Keyboard[8];

    public Controls() {
        Arrays.fill(keyboard, Keyboard.None);
        Arrays.fill(mouse, null);
    }

    private void addCommand(Keyboard command) {
        for (Keyboard value : keyboard) {
            if (value == command)
                return;
        }
        for (int i = 0; i < keyboard.length; i++) {
            if (keyboard[i] == Keyboard.None) {
                keyboard[i] = command;
                break;
            }
        }
    }

    public void removeCommand(Keyboard command) {
        int place = -1;
        for (int i = 0; i < keyboard.length; i++) {
            if (keyboard[i] == command) {
                keyboard[i] = Keyboard.None;
                place = i;
                break;
            }
        }
        if (place == -1 || place == keyboard.length - 1) {
            for (; place < keyboard.length; place++)
                keyboard[place] = keyboard[place + 1];
        }
    }

    public Mouse[] mouse() {
        return mouse;
    }

    public Keyboard[] keyboard() {
        return keyboard;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
