package tboir.entities.dynamic;

import tboir.engine.Wrap;
import tboir.entities.Entity;
import tboir.enums.EntityType;
import tboir.tools.EntityManager;

import java.awt.Graphics;

public abstract class DynamicEntity extends Entity {

    protected double previousX;
    protected double previousY;
    protected double speed;

    public DynamicEntity(Wrap wrap, EntityManager entities, EntityType type, String texturePath, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, texturePath, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = 1;
    }

    public DynamicEntity(Wrap wrap, EntityManager entities, EntityType type, String spriteSheetPath, int column, int row, double x, double y, int width, int height, double widthScale, double heightScale, double offsetX, double offsetY) {
        super (wrap, entities, type, spriteSheetPath, column, row, x, y, width, height, widthScale, heightScale, offsetX, offsetY);
        this.speed = 1;
    }

    protected abstract void calculateMovement();

    @Override
    public void render(Graphics g) {
        double renderedX = this.wrap.interpolate(this.previousX, this.x) - this.width / 2.0;
        double renderedY = this.wrap.interpolate(this.previousY, this.y) - this.height / 2.0;
        this.texture.changePosition((int)renderedX, (int)renderedY);
        super.render(g);
    }

    public void applyMovement() {
        this.previousX = this.x;
        this.previousY = this.y;
        this.calculateMovement();
    }

    // Setup options
    protected void move(double x, double y) {
        this.previousX = this.x;
        this.previousY = this.y;
        this.x = x;
        this.y = y;
    }

    protected void changePosition(double x, double y) {
        this.previousX = x;
        this.previousY = y;
        this.x = x;
        this.y = y;
    }

    protected void changeSpeed(double speed) {
        this.speed = speed;
    }
}
