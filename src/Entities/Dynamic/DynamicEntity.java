package Entities.Dynamic;

import Engine.Wrap;
import Entities.Entity;
import Enums.EntityType;
import Tools.EntityManager;

import java.awt.*;

public abstract class DynamicEntity extends Entity {

    protected double previousX;
    protected double previousY;
    protected double speed;

    public DynamicEntity(Wrap wrap, EntityManager entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        speed = 1;
    }

    public DynamicEntity(Wrap wrap, EntityManager entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        speed = 1;
    }

    // Overridable methods
    //====================================
    protected void calculateMovement() {}

    public void render(Graphics g) {
        double renderedX = wrap.interpolate(previousX, x) - width / 2.0;
        double renderedY = wrap.interpolate(previousY, y) - height / 2.0;
        texture.changePosition((int)renderedX, (int)renderedY);
        super.render(g);
    }
    //==================================

    public void applyMovement() {
        previousX = x;
        previousY = y;
        calculateMovement();
    }

    // Setup options
    protected void move(double x, double y) {
        previousX = this.x;
        previousY = this.y;
        this.x = x;
        this.y = y;
    }

    protected void changePosition(double x, double y) {
        previousX = x;
        previousY = y;
        this.x = x;
        this.y = y;
    }

    protected void changeSpeed(double speed) {
        this.speed = speed;
    }
}
