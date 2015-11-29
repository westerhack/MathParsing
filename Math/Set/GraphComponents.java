package Math.Set;
import Math.Exception.InvalidArgsException;

public class GraphComponents {
    /** 
     * The physical amount of pixels the window is wide and tall.
     * The array will look like the following: <code>(X, Y)</code>
     */
    private int[] winBounds;

    /** 
     * The min and max x/y values used for display.
     * Increasing them incrases the zoom level, and shifting them a certain direction shifts the 
     * "window" that direction.
     * <p>
     * The array will look like the following: <code>(min X, min Y, max X, max Y)</code>.
     */
    private double[] dispBounds;
 
    /** 
     * The amount of lines that will be drawn. "step" is a bit of a misnomer, but I couldnt think of anything better.
     */
    private double step;

    public GraphComponents(){
        this(new int[]{1000, 1000}, new double[]{-10, -10, 10, 10}, 100);
    }
    public GraphComponents(int[] pWinBounds, double[] pDispBounds, double pStep) throws InvalidArgsException{
        if(pWinBounds.length != 2)
            throw new InvalidArgsException("Window Bounds needs to be in format (X, Y)!");
        if(pWinBounds[0] <= 0  || pWinBounds[1] <= 0)
            throw new InvalidArgsException("Both the X and Y components of Window Bounds needs to be positive!");
        if(pDispBounds.length != 4)
            throw new InvalidArgsException("Display Bounds needs to be in format (min X, min Y, max X, max Y)!");
        winBounds = pWinBounds;
        dispBounds = pDispBounds;
        step = pStep;

    }
    public int[] winBounds(){ return winBounds; }
    public double[] dispBounds(){ return dispBounds; }
    public double step(){ return step; }
    public double cStep(){ return (dispBounds[2] - dispBounds[0]) / step;}
    public double[] fix(double x, double y){ return new double[]{
            (x - dispBounds[0]) / (dispBounds[2] - dispBounds[0]) * winBounds[0], 
            (1 - (y - dispBounds[1]) / (dispBounds[3] - dispBounds[1])) * winBounds[1]};

            // (x - dispBounds[0]) / (dispBounds[1] - dispBounds[0]) * winBounds[0], 
            // (1 - (y - dispBounds[2]) / (dispBounds[3] - dispBounds[2])) * winBounds[1]};

    }
    public String toString(){
        return "GraphingComponent: winBounds(" + winBounds[0] + ", " + winBounds[1] + "); dispBounds(" + 
            dispBounds[0] + ", " + dispBounds[1] + ", " + dispBounds[2] + ", " + dispBounds[3] + "); STEP: " + step;
    }
}