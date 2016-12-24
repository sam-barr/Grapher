/**
 * Miscelaneous terms
 * @author Sam Barr
 */
public class MiscTerm
{
    public static final Function sign = new Function()
        {
            public double getValue(double x)
            {
                return Math.signum(x);
            }

            public Function getDerivative()
            {
                return new Polynomial(0);
            }

            public String toString()
            {
                return "sign(x)";
            }
        };
    public static final Function cbrt = new Function()
        {
            public double getValue(double x)
            {
                return Math.cbrt(x);
            }

            public Function getDerivative()
            {
                return new PolynomialTerm(1/3, -2/3);
            }

            public String toString()
            {
                return "cbrt(x)";
            }
        };
    public static final Function exp = new Function()
        {
            public double getValue(double x)
            {
                return Math.exp(x);
            }

            public Function getDerivative()
            {
                return exp;
            }
            
            public String toString()
            {
                return "exp(x)";
            }
        };
    public static final Function abs = new Function()
        {
            public double getValue(double x)
            {
                return Math.abs(x);
            }

            public Function getDerivative()
            {
                return sign;
            }
            
            public String toString()
            {
                return "abs(x)";
            }
        };
}