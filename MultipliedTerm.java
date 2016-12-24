import java.util.ArrayList;
/**
 * @author Sam Barr
 */
public class MultipliedTerm extends Function
{
    private ArrayList<Function> terms;
    private double coefficient;
    
    /**
     * Constructs a MultipliedTerm
     */
    public MultipliedTerm()
    {
        this(1);
    }
    
    public MultipliedTerm(double coefficient)
    {
        terms = new ArrayList<Function>();
        this.coefficient = coefficient;
    }
    
    /**
     * Adds a given Function to the MultipliedTerm
     * @param Function eq
     */
    public void addTerm(Function eq)
    {
        if(eq instanceof SimpleTerm)
        {
            SimpleTerm temp = (SimpleTerm) eq;
            coefficient *= temp.getCoefficient();
            terms.add(temp.getFunction());
        }
        else if(eq instanceof PolynomialTerm)
        {
            PolynomialTerm temp = (PolynomialTerm) eq;
            coefficient *= temp.getCoefficient();
            terms.add(new PolynomialTerm(1, temp.getPower()));
        }
        else terms.add(eq);
    }
    
    /**
     * Returns the value of the MultipliedTerm at a given x value
     * @param double x
     * @return double
     */
    public double getValue(double x)
    {
        double total = 1;
        for(int i = 0; i < terms.size(); i++)
        {
            total *= terms.get(i).getValue(x);
        }
        return total * coefficient;
    }
    
    /**
     * Returns the derivative of the MultipliedTerm
     * @return Function     A Polynomial
     */
    public Function getDerivative()
    {
        Polynomial stuff = new Polynomial();
        for(int i = 0; i < terms.size(); i++)
        {
            MultipliedTerm temp = new MultipliedTerm();
            for(int j = 0; j < terms.size(); j++)
            {
                Function t;
                if(j == i) t = terms.get(j).getDerivative();
                else t = terms.get(j);
                temp.addTerm(t);
            }
            stuff.addTerm(temp);
        }
        return new SimpleTerm(coefficient, stuff);
    }
    
    /**
     * Returns true if the MultipliedTerm contians no terms
     * @return boolean
     */
    public boolean isEmpty()
    {
        return terms.size() == 0;
    }
    
    /**
     * Returns the information of the MultipliedTerm as a String
     * @return String
     */
    public String toString()
    {
        String stuff = terms.get(0).toString() + "(";
        for(int i = 1; i < terms.size(); i++)
        {
            stuff += ")" + " * " + "(" + terms.get(i);
        }
        return coefficient + stuff + ")";
    }
}