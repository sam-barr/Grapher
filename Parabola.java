/**
 * @author Sam Barr
 */
public class Parabola extends Conic
{
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private double a;
    private double h;
    private double k;
    private int orientation;

    /**
     * Constructs a Parabola object
     * @param double a
     * @param double h
     * @param double k
     * @param int orientation
     */
    public Parabola(double a, double h, double k, int orientation)
    {
        this.a = a;
        this.h = h;
        this.k = k;
        if(orientation != VERTICAL && orientation != HORIZONTAL) throw new IllegalArgumentException();
        this.orientation = orientation;
    }

    /**
     * Returns the values of the Parabola at a given x value
     * @param double x
     * @return double[]
     */
    public double[] getValues(double x)
    {
        double[] stuff = new double[getNumValues()];
        if(orientation == VERTICAL)
        {
            stuff[0] = Math.pow(x - h, 2) / (4 * a) + k;
        }
        else if(orientation == HORIZONTAL)
        {
            stuff[0] = Math.sqrt(4 * a * (x - h)) + k;
            stuff[1] = -Math.sqrt(4 * a * (x - h)) + k;
        }
        return stuff;
    }

    /**
     * Returns the maximum number of y values per x values
     * @return int
     */
    public int getNumValues()
    {
        if(orientation == VERTICAL) return 1;
        else return 2;
    }
}