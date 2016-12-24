/**
 * @author Sam Barr
 */
public class LogarithmicTerm extends Function
{
    private double base;
    
    /**
     * Constructs a LogarithmicTerm with base e
     */
    public LogarithmicTerm()
    {
        this(Math.E);
    }
    
    /**
     * Constructs a LogarithmicTerm with a given base
     * @param double base
     */
    public LogarithmicTerm(double base)
    {
        this.base = base;
    }
    
    /**
     * Returns the value of the LogarithimcTerm at a given x value
     * @param double x
     * @return double
     */
    public double getValue(double x)
    {
        return log(base, x);
    }
    
    /**
     * Returns the derivative of the LogarithmicTerm
     * @return Function     A SimpleTerm
     */
    public Function getDerivative()
    {
        return new SimpleTerm(log(base, Math.E), new PolynomialTerm(1, -1));
    }
    
    /**
     * Returns the log of a given x value with a given base
     * @param double x
     * @param double base
     * @return double
     */
    private static double log(double base, double x)
    {
        return Math.log(x) / Math.log(base);
    }
    
    /**
     * Returns the information of the LogarithmicTerm as a String
     * @return String
     */
    public String toString()
    {
        return "log(" + base + ", x)";
    }
}