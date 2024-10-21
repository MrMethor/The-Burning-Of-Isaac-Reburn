package Engine;

public class Counter {

    private static final int SECOND_IN_NANO = 1000000000;
    private int timeToElapse;
    private int desiredUPS;

    private int ups;
    private int fps;
    private int upsCounter;
    private int fpsCounter;
    private int elapsedTime;

    public Counter (Integer desiredUPS) {
        this.desiredUPS = desiredUPS;
        timeToElapse = SECOND_IN_NANO / desiredUPS;
    }

    public void addFPS() {
        fpsCounter++;
    }

    public void addUPS() {
        upsCounter++;
    }

    public void addTime(long alpha) {
        if (elapsedTime >= SECOND_IN_NANO) {
            elapsedTime = 0;
            resetCounter();
        }
        elapsedTime += (int)alpha;
    }

    public void resetCounter() {
        timeToElapse = SECOND_IN_NANO / desiredUPS;
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

    public int getTimeToElapse() {
        return timeToElapse;
    }
}
