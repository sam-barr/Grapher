import java.util.ArrayList;
/**
 * Class defining terms within terms
 * @author Sam Barr
 */
public class ChainedTerm extends Function
{
    private ArrayList<Function> terms;

    /**
     * Constructs a ChainedTerm
     */
    public ChainedTerm()
    {
        terms = new ArrayList<Function>();
    }

    /**
     * Adds a given Function to the ChainedTerm
     * Added Function will be innermost term
     * @param Function eq
     */
    public void addTerm(Function eq)
    {
        terms.add(eq);
    }

    /**
     * Adds a given Function to the ChainedTerm at a given index
     * @param Function eq
     * @param int index
     */
    public void addTerm(Function eq, int index)
    {
        terms.add(index, eq);
    }

    /**
     * Returns the value of the ChainedTerm at a given x value
     * @param double x
     * @return double
     */
    public double getValue(double x)
    {
        double total = x;
        for(int i = terms.size() - 1; i >= 0; i--)
        {
            total = terms.get(i).getValue(total);
        }
        return total;
    }

    /**
     * Returns the derivative of the ChainedTerm
     * @return Function     A MultipliedTerm
     */
    public Function getDerivative()
    {
        MultipliedTerm stuff = new MultipliedTerm();
        for(int i = terms.size() - 1; i >= 0; i--)
        {
            ChainedTerm c = new ChainedTerm();
            c.addTerm(terms.get(i).getDerivative());
            for(int j = i + 1; j < terms.size(); j++)
            {
                c.addTerm(terms.get(j));
            }
            stuff.addTerm(c);
        }
        return stuff;
    }

    /**
     * Returns true if the ChainedTerm contains no terms
     * @return boolean
     */
    public boolean isEmpty()
    {
        return terms.size() == 0;
    }

    /**
     * Returns the information of the ChainedTerm as a String
     * @return String
     */
    public String toString()
    {
        String stuff = terms.get(0).toString();
        for(int i = 1; i < terms.size(); i++)
        {
            for(int j = 0; j < stuff.length(); j++) if(stuff.charAt(j) == 'x') stuff = stuff.substring(0, j) + "@" + stuff.substring(j + 1);
            for(int j = 0; j < stuff.length(); j++) if(stuff.charAt(j) == '@') stuff = stuff.substring(0, j) + "(" + terms.get(i) + ")" + stuff.substring(j + 1);
        }
        return stuff;
    }
}