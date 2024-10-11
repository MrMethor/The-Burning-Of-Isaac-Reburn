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
        image = new Image(path);
        image.changePosition(x, y);
        image.makeVisible();
        int labelLength = label.length();
        textBlock = new TextBlock(label);
        textBlock.changePosition(calculateWidth(labelLength), calculateHeight());
        textBlock.changeFont("Castellar", FontStyle.BOLD, height / 2);
        textBlock.changeColor("#CCCCCC");
        textBlock.makeVisible();
    }

    public boolean isPressed(int x, int y) {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
    }

    public void close() {
        image.makeInvisible();
        textBlock.makeInvisible();
    }

    private int calculateWidth(int labelLength) {
        return (8 + x + width / 3) + (4 - labelLength) * 18;
    }

    private int calculateHeight() {
        return y + height * 2 / 3;
    }

}