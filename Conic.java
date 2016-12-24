/**
 * @author Sam Barr
 */
public abstract class Conic implements Equation
{
    /**
     * Returns the maximum number of values the Conic can have per x value
     * <p>Returns 2 for all conics except Parabola Vertical
     * @return int
     */
    public int getNumValues()
    {
        return 2;
    }
}