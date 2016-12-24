/**
 * Class defining trigonometric terms
 * @author Sam Barr
 */
public class TrigTerm
{
    public static final Function sin = new Function()
    {
        public double getValue(double x)
        {
            return Math.sin(x);
        }
        
        public Function getDerivative()
        {
            return cos;
        }
        
        public String toString()
        {
            return "sin(x)";
        }
    };
    public static final Function cos = new Function()
    {
        public double getValue(double x)
        {
            return Math.cos(x);
        }
        
        public Function getDerivative()
        {
            return new SimpleTerm(-1, sin);
        }
        
        public String toString()
        {
            return "cos(x)";
        }
    };
    public static final Function tan = new Function()
    {
        public double getValue(double x)
        {
            return Math.tan(x);
        }
        
        public Function getDerivative()
        {
            MultipliedTerm stuff = new MultipliedTerm();
            stuff.addTerm(sec);
            stuff.addTerm(sec);
            return stuff;
        }
        
        public String toString()
        {
            return "tan(x)";
        }
    };
    public static final Function sec = new Function()
    {
        public double getValue(double x)
        {
            return 1 / Math.cos(x);
        }
        
        public Function getDerivative()
        {
            MultipliedTerm stuff = new MultipliedTerm();
            stuff.addTerm(tan);
            stuff.addTerm(sec);
            return stuff;
        }
        
        public String toString()
        {
            return "sec(x)";
        }
    };
    public static final Function csc = new Function()
    {
        public double getValue(double x)
        {
            return 1 / Math.sin(x);
        }
        
        public Function getDerivative()
        {
            MultipliedTerm stuff = new MultipliedTerm(-1);
            stuff.addTerm(cot);
            stuff.addTerm(csc);
            return stuff;
        }
        
        public String toString()
        {
            return "csc(x)";
        }
    };
    public static final Function cot = new Function()
    {
        public double getValue(double x)
        {
            return 1 / Math.tan(x);
        }
        
        public Function getDerivative()
        {
            MultipliedTerm stuff = new MultipliedTerm(-1);
            stuff.addTerm(csc);
            stuff.addTerm(csc);
            return stuff;
        }
        
        public String toString()
        {
            return "cot(x)";
        }
    };
    
    public static Function[] values()
    {
        return new Function[]{sin, cos, tan, sec, csc, cot};
    }
}