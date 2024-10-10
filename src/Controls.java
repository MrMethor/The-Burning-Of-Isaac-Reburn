import java.util.Arrays;

public class Controls {

    private final Keyboard[] keyboard = new Keyboard[8];
    private final Mouse[] mouse = new Mouse[3];

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

    public void LEFT_clicked(int x, int y) {
        this.mouse[0] = new Mouse(x, y);
    }

    public Keyboard[] keyboard() {
        return keyboard;
    }

    public Mouse[] mouse() {
        return mouse;
    }

    public void W_pressed() {
        addCommand(Keyboard.moveUP);
    }

    public void W_released() {
        removeCommand(Keyboard.moveUP);
    }

    public void S_pressed() {
        addCommand(Keyboard.moveDown);
    }

    public void S_released() {
        removeCommand(Keyboard.moveDown);
    }

    public void D_pressed() {
        addCommand(Keyboard.moveRight);
    }

    public void D_released() {
        removeCommand(Keyboard.moveRight);
    }

    public void A_pressed() {
        addCommand(Keyboard.moveLeft);
    }

    public void A_released() {
        removeCommand(Keyboard.moveLeft);
    }

    public void UP_pressed() {
        addCommand(Keyboard.fireUp);
    }

    public void UP_released() {
        removeCommand(Keyboard.fireUp);
    }

    public void DOWN_pressed() {
        addCommand(Keyboard.fireDown);
    }

    public void DOWN_released() {
        removeCommand(Keyboard.fireDown);
    }

    public void RIGHT_pressed() {
        addCommand(Keyboard.fireRight);
    }

    public void RIGHT_released() {
        removeCommand(Keyboard.fireRight);
    }

    public void LEFT_pressed() {
        addCommand(Keyboard.fireLeft);
    }

    public void LEFT_released() {
        removeCommand(Keyboard.fireLeft);
    }

    public void ESCAPE_pressed() {
        addCommand(Keyboard.escape);
    }
}
