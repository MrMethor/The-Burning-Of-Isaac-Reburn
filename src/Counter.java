public class Counter {

    public final int DESIRED_UPS = 60;
    public final int SECOND = 1000000000;
    public final int MS = SECOND / DESIRED_UPS;

    private int ups = 0;
    private int fps = 0;
    private int upsCounter = 0;
    private int fpsCounter = 0;
    private int elapsedTime = 0;

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

    public void print() {
        System.out.println("UPS: " + ups);
        System.out.println("FPS: " + fps);
    }
}
