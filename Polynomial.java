import java.util.ArrayList;
/**
 * Class defining the sum of a list of functions
 * @author Sam Barr
 */
public class Polynomial extends Function
{
    private ArrayList<Function> terms;
    private double constant;

    /**
     * Constructs a Polynomial
     */
    public Polynomial()
    {
        this(0);
    }

    /**
     * Constructs a Polynomial with a given constant
     * @param constant
     */
    public Polynomial(double constant)
    {
        terms = new ArrayList<Function>();
        this.constant = constant;
    }

    /**
     * Adds a given Function to the Polynomial
     * @param Function eq
     */
    public void addTerm(Function eq)
    {
        terms.add(eq);
    }

    /**
     * Returns the derivative of the Polynomial
     * @return Function     A Polynomial
     */
    public Function getDerivative()
    {
        Polynomial temp = new Polynomial();
        for(int i = 0; i < terms.size(); i++)temp.addTerm(terms.get(i).getDerivative());
        return temp;
    }

    /**
     * Returns the value of the Polynomial at a given x value
     * @param double x
     * @return double
     */
    public double getValue(double x)
    {
        double total = 0;
        for(int i = 0; i < terms.size(); i++) total += terms.get(i).getValue(x);
        return total + constant;
    }

    /**
     * Returns true if the Polynomial contains no terms
     * @return boolean
     */
    public boolean isEmpty()
    {
        return terms.size() == 0;
    }

    /**
     * Returns the information of the Polynomial as a String
     * @return String
     */
    public String toString()
    {
        String stuff = "";
        for(int i = 0; i < terms.size(); i++) stuff += terms.get(i) + " + ";
        return stuff + (constant == 0 ? "" : constant);
    }
}