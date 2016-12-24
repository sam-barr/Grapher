/**
 * @author Sam Barr
 */
public class SimpleTerm extends Function
{
    private double coefficient;
    private Function eq;
    
    /**
     * Constructs a SimpleTerm with a given coefficient and a given Function
     * @param double coefficient
     * @param Function eq
     */
    public SimpleTerm(double coefficient, Function eq)
    {
        this.coefficient = coefficient;
        this.eq = eq;
    }
    
    /**
     * Returns the value of the SimpleTerm at a given x value
     * @param double x
     * @return double
     */
    public double getValue(double x)
    {
        return coefficient * eq.getValue(x);
    }
    
    /**
     * Returns the derivative of the SimpleTerm
     * @return Function     A SimpleTerm
     */
    public Function getDerivative()
    {
        return new SimpleTerm(coefficient, eq.getDerivative());
    }
    
    /**
     * Returns the coefficient of the SimpleTerm
     * @return double
     */
    public double getCoefficient()
    {
        return coefficient;
    }
    
    /**
     * Returns the Function of the SimpleTerm
     * @return Function
     */
    public Function getFunction()
    {
        return eq;
    }
    
    /**
     * Returns the information of the SimpleTerm as a String
     */
    public String toString()
    {
        return coefficient + " * (" + eq + ")";
    }
}