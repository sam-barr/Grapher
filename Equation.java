/**
 * @author Sam Barr
 */
public interface Equation
{
    /**
     * Returns the values of the equation at a given x value
     * @param double x
     * @return double[]
     */
    public double[] getValues(double x);
    
    /**
     * Returns the maximum number of values an equation can have per x value
     * @return int
     */
    public int getNumValues();
}