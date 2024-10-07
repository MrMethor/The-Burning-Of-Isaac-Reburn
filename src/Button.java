import fri.shapesge.FontStyle;
import fri.shapesge.Image;
import fri.shapesge.TextBlock;

public class Button {

    private final Image image;
    private final TextBlock textBlock;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Button(String label, String path, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new Image(path);
        this.image.changePosition(x, y);
        this.image.makeVisible();
        int labelLength = label.length();
        this.textBlock = new TextBlock(label);
        this.textBlock.changePosition(calculateWidth(labelLength), calculateHeight());
        this.textBlock.changeFont("Castellar", FontStyle.BOLD, height / 2);
        this.textBlock.changeColor("#CCCCCC");
        this.textBlock.makeVisible();
    }

    public boolean isPressed(int x, int y) {
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height;
    }

    private int calculateWidth(int labelLength) {
        return (8 + x + width / 3) + (4 - labelLength) * 18;
    }

    private int calculateHeight() {
        return y + height * 2 / 3;
    }

    public void close() {
        this.image.makeInvisible();
        this.textBlock.makeInvisible();
    }
}