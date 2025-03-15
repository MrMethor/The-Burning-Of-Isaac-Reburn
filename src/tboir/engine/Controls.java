package tboir.engine;

import tboir.enums.Actions;
import tboir.enums.Commands;
import tboir.tools.Coords;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Controls implements MouseListener, KeyListener, MouseMotionListener {

    private final ArrayList<Coords> commands;
    private final ArrayList<Actions> actions;
    private final double[] mouseCoords;
    private final Wrap wrap;

    public Controls(Wrap wrap) {
        this.wrap = wrap;
        this.commands = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.mouseCoords = new double[2];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                this.addAction(Actions.moveUp);
                break;
            case KeyEvent.VK_S:
                this.addAction(Actions.moveDown);
                break;
            case KeyEvent.VK_A:
                this.addAction(Actions.moveLeft);
                break;
            case KeyEvent.VK_D:
                this.addAction(Actions.moveRight);
                break;
            case KeyEvent.VK_UP:
                this.addAction(Actions.fireUp);
                break;
            case KeyEvent.VK_DOWN:
                this.addAction(Actions.fireDown);
                break;
            case KeyEvent.VK_LEFT:
                this.addAction(Actions.fireLeft);
                break;
            case KeyEvent.VK_RIGHT:
                this.addAction(Actions.fireRight);
                break;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                this.addCommand(Commands.escape, 0, 0);
                break;
            case KeyEvent.VK_SPACE:
                this.addCommand(Commands.space, 0, 0);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                this.removeAction(Actions.moveUp);
                break;
            case KeyEvent.VK_S:
                this.removeAction(Actions.moveDown);
                break;
            case KeyEvent.VK_A:
                this.removeAction(Actions.moveLeft);
                break;
            case KeyEvent.VK_D:
                this.removeAction(Actions.moveRight);
                break;
            case KeyEvent.VK_UP:
                this.removeAction(Actions.fireUp);
                break;
            case KeyEvent.VK_DOWN:
                this.removeAction(Actions.fireDown);
                break;
            case KeyEvent.VK_LEFT:
                this.removeAction(Actions.fireLeft);
                break;
            case KeyEvent.VK_RIGHT:
                this.removeAction(Actions.fireRight);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            this.addCommand(Commands.leftClick, (int)((double)e.getX() / this.wrap.getScale()), (int)((double)e.getY() / this.wrap.getScale()));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseCoords[0] = e.getX() / this.wrap.getScale();
        this.mouseCoords[1] = e.getY() / this.wrap.getScale();
    }

    private void addAction(Actions action) {
        if (!this.actions.contains(action)) {
            this.actions.add(action);
        }
    }

    private void removeAction(Actions action) {
        this.actions.remove(action);
    }

    private void addCommand(Commands command, int x, int y) {
        this.commands.add(new Coords(x, y, command));
    }

    public void removeCommand(Coords command) {
        this.commands.remove(command);
    }

    // Getters
    public ArrayList<Coords> getCommands() {
        return this.commands;
    }

    public ArrayList<Actions> getActions() {
        return this.actions;
    }

    public double[] getMouseCoords() {
        return this.mouseCoords;
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
