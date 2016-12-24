/**
 * @author Sam Barr
 */
public class ExponentialTerm extends Function
{
    private double base;
    
    /**
     * Constructs an ExponentialTerm with base e
     */
    public ExponentialTerm()
    {
        this(Math.E);
    }
    
    /**
     * Constructs an ExponentialTerm with a given base
     * @param double base
     */
    public ExponentialTerm(double base)
    {
        this.base = base;
    }
    
    /**
     * Returns the value of the ExponentialTerm at a given x value
     * @param double x
     * @return double
     */
    public double getValue(double x)
    {
        return Math.pow(base, x);
    }
    
    /**
     * Returns the derivative of the ExponentialTerm
     * @return Function     A SimpleTerm
     */
    public Function getDerivative()
    {
        return new SimpleTerm(Math.log(base), new ExponentialTerm(base));
    }
    
    /**
     * Returns the information of the ExponentialTerm as a String
     * @return String
     */
    public String toString()
    {
        return base + "^x";
    }
}