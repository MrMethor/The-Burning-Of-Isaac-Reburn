public class Counter {

    public final int DESIRED_UPS = 60;
    public final int SECOND = 1000000000;
    public final int MS = SECOND / DESIRED_UPS;

    private int ups;
    private int fps;
    private int upsCounter;
    private int fpsCounter;
    private int elapsedTime;

    public Counter() {
        this.ups = 0;
        this.fps = 0;
        this.upsCounter = 0;
        this.fpsCounter = 0;
        this.elapsedTime = 0;
    }

    public void addFPS() {
        this.fpsCounter++;
    }

    public void addUPS() {
        this.upsCounter++;
    }

    public void addTime(long alpha) {
        if (elapsedTime >= SECOND) {
            elapsedTime = 0;
            resetCounter();
            //print();
        }
        this.elapsedTime += alpha;
    }

    public void resetCounter() {
        this.ups = this.upsCounter;
        this.fps = this.fpsCounter;
        this.upsCounter = 0;
        this.fpsCounter = 0;
    }
    public void print() {
        System.out.println("UPS: " + ups);
        System.out.println("FPS: " + fps);
    }
}
