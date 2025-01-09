package tboir.engine;

public class Interpolation {

    private double interpolation;

    public void setInterpolation(double interpolation) {
        this.interpolation = interpolation;
    }

    public double interpolate(double previous, double current) {
        return previous + (this.interpolation * (current - previous));
    }
}
