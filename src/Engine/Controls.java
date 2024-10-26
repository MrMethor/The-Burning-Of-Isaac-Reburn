package Engine;

import Enums.Actions;
import Enums.Commands;
import Tools.Coords;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Controls implements MouseListener, KeyListener {

    private final ArrayList<Coords> commands = new ArrayList<>();
    private final ArrayList<Actions> actions = new ArrayList<>();
    private final Wrap wrap;

    public Controls(Wrap wrap) {
        this.wrap = wrap;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: addAction(Actions.moveUp); break;
            case KeyEvent.VK_S: addAction(Actions.moveDown); break;
            case KeyEvent.VK_A: addAction(Actions.moveLeft); break;
            case KeyEvent.VK_D: addAction(Actions.moveRight); break;
            case KeyEvent.VK_UP: addAction(Actions.fireUp); break;
            case KeyEvent.VK_DOWN: addAction(Actions.fireDown); break;
            case KeyEvent.VK_LEFT: addAction(Actions.fireLeft); break;
            case KeyEvent.VK_RIGHT: addAction(Actions.fireRight); break;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: addCommand(Commands.escape, 0, 0); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: removeAction(Actions.moveUp); break;
            case KeyEvent.VK_S: removeAction(Actions.moveDown); break;
            case KeyEvent.VK_A: removeAction(Actions.moveLeft); break;
            case KeyEvent.VK_D: removeAction(Actions.moveRight); break;
            case KeyEvent.VK_UP: removeAction(Actions.fireUp); break;
            case KeyEvent.VK_DOWN: removeAction(Actions.fireDown); break;
            case KeyEvent.VK_LEFT: removeAction(Actions.fireLeft); break;
            case KeyEvent.VK_RIGHT: removeAction(Actions.fireRight); break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1: addCommand(Commands.leftClick, (int)((double)e.getX() / wrap.getScale()), (int)((double)e.getY() / wrap.getScale())); break;
        }
    }

    private void addAction(Actions action) {
        if (!actions.contains(action))
            actions.add(action);
    }

    private void removeAction(Actions action) {
        actions.remove(action);
    }

    private void addCommand(Commands command, int x, int y) {
        commands.add(new Coords(x, y, command));
    }

    public void removeCommand(Coords command) {
        commands.remove(command);
    }

    // Getters
    public ArrayList<Coords> getCommands() {
        return commands;
    }

    public ArrayList<Actions> getActions() {
        return actions;
    }

    // Unused
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
