/**
 * @author Sam Barr
 */
public class Hyperbola extends Conic
{
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private double a, b, h, k;
    private int orientation;

    /**
     * Constructs a Hyperbola
     * @param double a
     * @param double b
     * @param double h
     * @param double k
     * @param int orientation
     */
    public Hyperbola(double a, double b, double h, double k, int orientation)
    {
        this.a = a;
        this.b = b;
        this.h = h;
        this.k = k;
        if(orientation != VERTICAL && orientation != HORIZONTAL) throw new IllegalArgumentException();
        this.orientation = orientation;
    }

    /**
     * Returns the values of the Hyperbola at a given x value
     * @param double x
     * @return double[]
     */
    public double[] getValues(double x)
    {
        double[] stuff = new double[2];
        if(orientation == VERTICAL)
        {
            double top = Math.sqrt(a * a *(b*b + Math.pow(x - h, 2)) / (b * b));
            double bottom = -top;
            stuff = new double[]{top + k, bottom + k};
        }
        else if(orientation == HORIZONTAL)
        {
            double top = Math.sqrt(b * b * (Math.pow(x-h, 2) - a * a) / a * a);
            double bottom = -top;
            stuff = new double[]{top + k, bottom + k};
        }
        return stuff;
    }
}