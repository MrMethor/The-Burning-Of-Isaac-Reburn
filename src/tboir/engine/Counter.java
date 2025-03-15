package tboir.engine;

public class Counter {

    private static final double SECOND_IN_NANO = 1_000_000_000;
    private double timeToElapse;
    private final int desiredUPS;

    private int ups;
    private int fps;
    private int upsCounter;
    private int fpsCounter;
    private int elapsedTime;

    public Counter (int desiredUPS) {
        this.desiredUPS = desiredUPS;
        this.timeToElapse = SECOND_IN_NANO / desiredUPS;
    }

    public void addUPS() {
        this.upsCounter++;
    }

    public void addFPS() {
        this.fpsCounter++;
    }

    public void addTime(double alpha) {
        if (this.elapsedTime >= SECOND_IN_NANO) {
            this.elapsedTime = 0;
            this.resetCounter();
        }
        this.elapsedTime += (int)alpha;
    }

    public void resetCounter() {
        this.timeToElapse = SECOND_IN_NANO / this.desiredUPS;
        this.ups = this.upsCounter;
        this.fps = this.fpsCounter;
        this.upsCounter = 0;
        this.fpsCounter = 0;
    }

    // Getters
    public int getFPS() {
        return this.fps;
    }

    public int getUPS() {
        return this.ups;
    }

    public double getTimeToElapse() {
        return this.timeToElapse;
    }

    public int getDesiredUPS() {
        return this.desiredUPS;
    }
}
