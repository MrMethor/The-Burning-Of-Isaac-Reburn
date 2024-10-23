package Engine;

public class Interpolation {

    private double interpolation;

    public void setInterpolation(double interpolation) {
        this.interpolation = interpolation;
    }

    public double interpolate(double previous, double current) {
        return previous + (interpolation * (current - previous));
    }
}
