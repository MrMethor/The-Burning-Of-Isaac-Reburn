public class Button {

    //private final Image image;
    //private final TextBlock label;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Button(String label, String path, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        //image = new Image(path);
        //image.changePosition(x, y);
        //image.makeVisible();
        //int labelLength = label.length();
        //this.label = new TextBlock(label);
        //this.label.changePosition(calculateWidth(labelLength), calculateHeight());
        //this.label.changeFont("Castellar", FontStyle.BOLD, height / 2);
        //this.label.changeColor("#CCCCCC");
        //this.label.makeVisible();
    }

    public boolean isPressed(int x, int y) {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
    }

    private int calculateWidth(int labelLength) {
        return (8 + x + width / 3) + (4 - labelLength) * 18;
    }

    private int calculateHeight() {
        return y + height * 2 / 3;
    }

}