/**
 * @author Sam Barr
 */
public abstract class Function implements Equation
{
    /**
     * Returns the values of the equation at a given x value
     * @param double x
     * @return double[]
     */
    public double[] getValues(double x)
    {
        return new double[]{getValue(x)};
    }
    
    /**
     * Returns the maximum number of values the Function can have per x value
     * <p>Returns one for all Functions
     * @return int
     */
    public int getNumValues()
    {
        return 1;
    }

    /**
     * Returns the value of the Function at a given value
     * @param double x
     * @return double
     */
    abstract double getValue(double x);

    /**
     * Returns the derivative of the Function
     * @return Expression
     */
    abstract Function getDerivative();
}