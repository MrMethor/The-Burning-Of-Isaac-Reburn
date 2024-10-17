public class Interpolation {
    
    private static double interpolation;

    public static void setInterpolation(double interp) {
        interpolation = interp;
    }

    public double interpolate(double previous, double current) {
        return previous + (interpolation * (current - previous));
    }

}
