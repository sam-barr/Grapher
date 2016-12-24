/**
 * Class defining a variable with a coefficient raised to a power
 * @author Sam Barr
 */
public class PolynomialTerm extends Function
{
    private double coefficient;
    private double power;
    
    /**
     * Constructs a PolynomialTerm with a coefficient and power of 1
     */
    public PolynomialTerm()
    {
        this(1, 1);
    }
    
    /**
     * Constructs a PolynomialTerm with a given coefficient and a power of 1
     * @param double coefficient
     */
    public PolynomialTerm(double coefficient)
    {
        this(coefficient, 1);
    }
    
    /**
     * Constructs a PolynomialTerm with a given coefficient and power
     * @param double coefficient
     * @param double power
     */
    public PolynomialTerm(double coefficient, double power)
    {
        this.coefficient = coefficient;
        this.power = power;
    }
    
    /**
     * Returns the value of the polynomial at a given x value
     * @param double x
     * @return double
     */
    public double getValue(double x)
    {
        return coefficient * Math.pow(x, power);
    }
    
    /**
     * Returns the derivative of the PolynomialTerm
     * @return Function     a PolynomialTerm
     */
    public Function getDerivative()
    {
        return new PolynomialTerm(coefficient * power, power == 0 ? power : power - 1);
    }
    
    /**
     * Returns the coefficient of the PolynomialTerm
     * @return double
     */
    public double getCoefficient()
    {
        return coefficient;
    }
    
    /**
     * Returns the power of the PolynomialTerm
     * @return double
     */
    public double getPower()
    {
        return power;
    }
    
    /**
     * Returns the information of the PolynomialTerm as a String
     * @return String
     */
    public String toString()
    {
        String co = "";
        if(coefficient == 0) return "0";
        else if(coefficient == -1) co = "-";
        else if(coefficient != 1) co = Double.toString(coefficient);
        String pow = "";
        if(power == 0) return co.equals("") ? "1" : co;
        if(power != 1) pow = "^" + power;
        return co + "x" + pow;
    }
}