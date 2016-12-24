/**
 * @author Sam Barr
 */
public class Ellipse extends Conic
{
    private double xAxis, yAxis, h, k;

    /**
     * Constructs an Ellipse
     * @param double xAxis
     * @param double yAxis
     * @param double h
     * @param double k
     */
    public Ellipse(double xAxis, double yAxis, double h, double k)
    {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.h = h;
        this.k = k;
    }

    /**
     * Returns the values of the Ellipse at a given x value
     * @param double x
     * @return double[]
     */
    public double[] getValues(double x)
    {
        double top = Math.sqrt(Math.pow(yAxis, 2) * (1 - Math.pow(x - h, 2) / Math.pow(xAxis, 2)));
        double bottom = -top;
        return new double[]{top + k, bottom + k};
    }
}