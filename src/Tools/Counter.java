package Tools;

public class Counter {

    public static final int DESIRED_UPS = 60;
    public static final int SECOND = 1000000000;
    public static final int MS = SECOND / DESIRED_UPS;

    private int ups;
    private int fps;
    private int upsCounter;
    private int fpsCounter;
    private int elapsedTime;

    public void addFPS() {
        fpsCounter++;
    }

    public void addUPS() {
        upsCounter++;
    }

    public void addTime(long alpha) {
        if (elapsedTime >= SECOND) {
            elapsedTime = 0;
            resetCounter();
            //print();
        }
        elapsedTime += (int)alpha;
    }

    public void resetCounter() {
        ups = upsCounter;
        fps = fpsCounter;
        upsCounter = 0;
        fpsCounter = 0;
    }

    public int getFPS() {
        return fps;
    }

    public int getUPS() {
        return ups;
    }
}
