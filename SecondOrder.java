/**
 * Class defining a variable Function raised to a variable Function
 * @author Sam Barr
 */
public class SecondOrder extends Function
{
    private Function base;
    private Function power;
    
    /**
     * Constructs a SecondOrder object with a given base and power
     * @param Function base
     * @param Function power
     */
    public SecondOrder(Function base, Function power)
    {
        this.base = base;
        this.power = power;
    }
    
    /**
     * Returns the value of the SecondOrder objet at a given x value
     * @param double x
     * @return double
     */
    public double getValue(double x)
    {
        return Math.pow(base.getValue(x), power.getValue(x));
    }
    
    /**
     * Returns the derivative of the Function
     * @return Function
     */
    public Function getDerivative() //d/dx f(x)^g(x) = g(x)*f'(x)*f(x)^(g(x)-1) + g'(x)*ln(f(x))*f(x)^g(x)
    {
        MultipliedTerm first = new MultipliedTerm();
        first.addTerm(power); //g(x)
        first.addTerm(base.getDerivative()); //f'(x)
        Polynomial temp = new Polynomial(-1); //f(x)^(g(x)-1)
        temp.addTerm(power);
        first.addTerm(new SecondOrder(base, temp));
        
        MultipliedTerm second = new MultipliedTerm();
        second.addTerm(this); //f(x)^g(x)
        second.addTerm(power.getDerivative()); //g'(x)
        ChainedTerm temp2 = new ChainedTerm();
        temp2.addTerm(new LogarithmicTerm()); //ln(f(x))
        temp2.addTerm(base);
        second.addTerm(temp2);
        
        Polynomial stuff = new Polynomial();
        stuff.addTerm(first);
        stuff.addTerm(second);
        return stuff;
    }
    
    /**
     * Returns the information of the SecondOrder object as a String
     * @return String
     */
    public String toString()
    {
        return "(" + base + ")^(" + power + ")";
    }
}