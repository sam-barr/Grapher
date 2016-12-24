import java.text.DecimalFormat;
import java.text.ParsePosition;
/**
 * Class providing utility methods for the Function interface
 * @author Sam Barr
 */
public class FunctionUtil
{
    /**
     * Converts a given String into an Function
     * @param String eq
     * @return Function
     * @throws IllegalArgumentException if eq cannot be made into an Function
     */
    public static Function getFunction(String eq)
    {
        while(eq.contains("pi")) eq = eq.substring(0, eq.indexOf("pi")) + Double.toString(Math.PI) + eq.substring(eq.indexOf("pi") + 2);
        while(eq.contains("e")) eq = eq.substring(0, eq.indexOf("e")) + Double.toString(Math.E) + eq.substring(eq.indexOf("e") + 1);
        while(eq.contains(" ")) eq = eq.substring(0, eq.indexOf(" ")) + eq.substring(eq.indexOf(" ") + 1); //remove whitespace
        for(int i = 0; i < eq.length() - 1; i++)
        {
            if(eq.charAt(i) == '-')
            {
                if(i != 0 && eq.charAt(i - 1) == '^') continue;
                eq = eq.substring(0, i) + "+-1*" + eq.substring(i + 1);
                i++;
            }
        }
        try
        {
            return parseFunction(eq);
        }
        catch(Exception e){throw new IllegalArgumentException("Given string is not a valid Function");}
    }

    /**
     * Called by FunctionUtil.getFunction to parse the Function
     * @param String eq
     * @return Function
     * @throws IllegalArgumentException if eq cannot be made into an Function
     */
    private static Function parseFunction(String eq)
    {   
        if(eq.equals("x")) return new PolynomialTerm(); //base case 1
        else if(eq.startsWith("+")) return parseFunction(eq.substring(1));
        Polynomial p = new Polynomial();
        for(int i = 0; i < eq.length(); i++) //separate added terms
        {
            if(isParenthetical(eq, i)) continue;
            else if(eq.charAt(i) == '+')
            {
                p.addTerm(parseFunction(eq.substring(0, i)));
                p.addTerm(parseFunction(eq.substring(i + 1)));
                break;
            }
        }
        if(!p.isEmpty()) return p;
        MultipliedTerm m = new MultipliedTerm();
        for(int i = 0; i < eq.length(); i++) //separate multiplied terms
        {
            if(isParenthetical(eq, i)) continue;
            else if(eq.charAt(i) == '*')
            {
                m.addTerm(parseFunction(eq.substring(0, i)));
                m.addTerm(parseFunction(eq.substring(i + 1)));
            }
        }
        if(!m.isEmpty()) return m;
        for(int i = 0; i < eq.length(); i++)
        {
            if(isParenthetical(eq, i)) continue;
            else if(eq.charAt(i) == '/')
            {
                if(eq.startsWith("dx", i + 1)) continue;
                m.addTerm(parseFunction(eq.substring(0, i)));
                ChainedTerm c = new ChainedTerm();
                c.addTerm(new PolynomialTerm(1, -1));
                c.addTerm(parseFunction(eq.substring(i + 1)));
                m.addTerm(c);
            }
        }
        if(!m.isEmpty()) return m;
        DecimalFormat df = new DecimalFormat(); //evaluating a number
        ParsePosition pos = new ParsePosition(0);
        Number n = df.parse(eq, pos);
        if(eq.contains("^")) //separate power stuff
        {
            for(int i = 0; i < eq.length(); i++)
            {
                if(isParenthetical(eq, i)) continue;
                else if(eq.charAt(i) == '^')
                {
                    ChainedTerm c = new ChainedTerm();
                    boolean onLeft = eq.substring(0, i).contains("x");
                    boolean onRight = eq.substring(i).contains("x");
                    if(onLeft && onRight)
                    {
                        return new SecondOrder(parseFunction(eq.substring(0, i)), parseFunction(eq.substring(i + 1)));
                    }
                    else if(onRight) //x term is on right
                    {
                        c.addTerm(new ExponentialTerm(n.doubleValue()));
                        c.addTerm(parseFunction(eq.substring(i + 1)));
                        return c;
                    }
                    else //x term is on left
                    {
                        ParsePosition powerParse = new ParsePosition(i + 1);
                        double power = df.parse(eq, powerParse).doubleValue();
                        c.addTerm(new PolynomialTerm(1, power));
                        if(n == null)
                        {
                            c.addTerm(parseFunction(eq.substring(0,i)));
                            return c;
                        }
                        else
                        {
                            c.addTerm(parseFunction(eq.substring(pos.getIndex(), i)));
                            return new SimpleTerm(n.doubleValue(), c);
                        }
                    }
                }
            }
        }
        if(!(n == null)) //base case 2
        {
            try
            {
                Function temp = parseFunction(eq.substring(pos.getIndex()));
                return new SimpleTerm(n.doubleValue(), temp);
            }
            catch(IllegalArgumentException e){return new PolynomialTerm(n.doubleValue(), 0);} //base case 2
        }
        for(Function x: TrigTerm.values()) //find trig functions
        {
            if(eq.startsWith(x.toString().substring(0, 4)))
            {
                ChainedTerm c = new ChainedTerm();
                c.addTerm(x);
                c.addTerm(parseFunction(eq.substring(4, eq.lastIndexOf(")"))));
                return c;
            }
        }
        if(eq.startsWith("ln(")) //find logarithmic/ misc functions
        {
            ChainedTerm c = new ChainedTerm();
            c.addTerm(new LogarithmicTerm());
            c.addTerm(parseFunction(eq.substring(3, eq.lastIndexOf(")"))));
            return c;
        }
        else if(eq.startsWith("log("))
        {
            ChainedTerm c = new ChainedTerm();
            c.addTerm(new LogarithmicTerm(10));
            c.addTerm(parseFunction(eq.substring(4, eq.lastIndexOf(")"))));
            return c;
        }
        else if(eq.startsWith("sqrt("))
        {
            ChainedTerm c = new ChainedTerm();
            c.addTerm(new PolynomialTerm(1, .5));
            c.addTerm(parseFunction(eq.substring(5, eq.lastIndexOf(")"))));
            return c;
        }
        else if(eq.startsWith("cbrt("))
        {
            ChainedTerm c = new ChainedTerm();
            c.addTerm(MiscTerm.cbrt);
            c.addTerm(parseFunction(eq.substring(5, eq.lastIndexOf(")"))));
            return c;
        }
        else if(eq.startsWith("abs("))
        {
            ChainedTerm c = new ChainedTerm();
            c.addTerm(MiscTerm.abs);
            c.addTerm(parseFunction(eq.substring(4, eq.lastIndexOf(")"))));
            return c;
        }
        else if(eq.startsWith("sign("))
        {
            ChainedTerm c = new ChainedTerm();
            c.addTerm(MiscTerm.sign);
            c.addTerm(parseFunction(eq.substring(5, eq.lastIndexOf(")"))));
            return c;
        }
        else if(eq.startsWith("d/dx("))
        {
            return parseFunction(eq.substring(5, eq.lastIndexOf(")"))).getDerivative();
        }
        else if(eq.startsWith("(") && eq.endsWith(")")) return parseFunction(eq.substring(1, eq.length() - 1)); //remove outer parentheses
        throw new IllegalArgumentException("Given string is not a valid Function");
    }

    /**
     * Returns true if a given index of a given String is in parenthesis
     * @param String str
     * @param int index
     * @return boolean
     */
    private static boolean isParenthetical(String str, int index)
    {
        int numberOpen = 0;
        int numberClosed = 0;
        for(int i = 0; i < index; i++)
        {
            if(str.charAt(i) == '(') numberOpen++;
            if(str.charAt(i) == ')') numberClosed++;
        }
        return numberOpen > numberClosed;
    }

    /**
     * Returns the midpoint rhiemann sum of a given Function
     * @param Function eq
     * @param double left       Leftmost point
     * @param double right      Rightmost point
     * @param int n     Number of subintervals to add
     * @return double
     */
    public static double midpointSum(Function eq, double left, double right, int n)
    {
        double total = 0;
        double inc = (right - left) / n;
        for(double i = left; i < right; i += inc) total += eq.getValue(i + inc / 2) * inc;
        return total;
    }

    /**
     * Returns the left riemann sum of a given Function
     * @param Function eq
     * @param double left       Leftmost point
     * @param double right      Rightmost point
     * @param int n     Number of subintervals to add
     * @return double
     */
    public static double leftSum(Function eq, double left, double right, int n)
    {
        double total = 0;
        double inc = (right - left) / n;
        for(double i = left; i < right; i += inc) total += eq.getValue(i) * inc;
        return total;
    }

    /**
     * Returns the right riemann sum of a given Function
     * @param Function eq
     * @param double left       Leftmost point
     * @param double right      Rightmost point
     * @param int n     Number of subintervals to add
     * @return double
     */
    public static double rightSum(Function eq, double left, double right, int n)
    {
        double total = 0;
        double inc = (right - left) / n;
        for(double i = left; i < right; i += inc) total += eq.getValue(i + inc) * inc;
        return total;
    }

    /**
     * Returns the trapezoid rule sum of a given Function
     * @param Function eq
     * @param double left       Leftmost point
     * @param double right      Rightmost point
     * @param int n     Number of subintervals to add
     * @return double
     */
    public static double trapezoidSum(Function eq, double left, double right, int n)
    {
        double total = 0;
        double inc = (right - left) / n;
        for(double i = left; i < right; i += inc) total += eq.getValue(i) + eq.getValue(i + inc);
        return inc * total / 2;
    }

    /**
     * Returns the definite integral of a Functionn by finding the number of subintervals required for the left and right rhiemann sum are equal
     * Doesn't work; will most likely run until number of subintervals == Integer.MAX_VALUE
     * @param Function eq
     * @param double left       Leftmost point
     * @param double right      Rightmost point
     * @return double
     */
    public static double definiteIntegral(Function eq, double left, double right)
    {
        int n = 0;
        while(rightSum(eq, left, right, n) != leftSum(eq, left, right, n)) n++;
        return rightSum(eq, left, right, n);
    }
}