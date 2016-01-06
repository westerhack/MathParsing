package West.Math.Display;

import West.Math.MathObject;
import West.Math.Set.Collection;

/**
 * Keeps track of different thigns about graphing - like window sizes
 * 
 * @author Sam Westerman
 * @version 0.90
 * @since 0.2
 */
public class GraphComponents implements MathObject {

    /**
     * The physical amount of pixels the window is wide and tall.
     * The array will look like the following: <code>(X, Y)</code>
     */
    protected int[] winBounds;

    /**
     * The min and max x/y values used for display.
     * Increasing them incrases the zoom level, and shifting them a certain direction shifts the 
     * "window" that direction.
     * <p>
     * The array will look like the following: <code>(min X, min Y, max X, max Y)</code>.
     */
    protected double[] dispBounds;
 
    /**
     * The amount of lines that will be drawn. "step" is a bit of a misnomer, but I couldnt think of anything better.
     */
    protected double step;

    protected String indepVar;

    protected String[] depVars;
    /**
     * TODO: JAVADOC
     */
    public GraphComponents() {
        this("x", new String[]{"y"});
    }
    public GraphComponents(String indep, String[] dep){
        this(new int[]{1250, 750}, new double[]{-10, -10, 10, 10}, 250, indep, dep);
    }
    /**
     * TODO: JAVADOC
     */
    public GraphComponents(int[] pWinBounds,
                           double[] pDispBounds,
                           double pStep,
                           String indep,
                           String[] dep)
                            throws IllegalArgumentException{
        if(pWinBounds.length != 2)
            throw new IllegalArgumentException("Cannot instantiate GraphComponents! pWinBounds must be in format (X, Y)!");
        if(pWinBounds[0] <= 0  || pWinBounds[1] <= 0)
            throw new IllegalArgumentException("Cannot instatiate GraphComponents! Both the X and Y components of "+
                                           "pWinBounds need to be positive!");
        if(pDispBounds.length != 4)
            throw new IllegalArgumentException("Cannot instatiate GraphComponents! pDispBounds needs to be in format "+
                                           "(min X, min Y, max X, max Y)!");
        winBounds = pWinBounds;
        dispBounds = pDispBounds;
        step = pStep;
        indepVar = indep;
        assert indepVar != null;
        // if(indepVar == null)
        //     indepVar = "x";
        depVars = dep;
        assert depVars != null;
        // if(depVars == null)
        //     depVars = new String[]{"y"};
        assert indepVar != null;
        assert depVars != null;
        
    }

    /** TODO: JAVADOC */
    public int[] winBounds() { return winBounds; }

    /** TODO: JAVADOC */
    public double[] dispBounds() { return dispBounds; }

    /** TODO: JAVADOC */
    public double step() { return step; }

    /** TODO: JAVADOC */
    public String indepVar() { return indepVar; }

    public String[] depVars() { return depVars; }

    /** TODO: JAVADOC */
    public double cStep() { return (dispBounds[2] - dispBounds[0]) / step;}

    /** TODO: JAVADOC */
    public double[] fix(double x, double y) {
        return fix(x, y, dispBounds[0],  dispBounds[1],  dispBounds[2], dispBounds[3],  winBounds[0],  winBounds[1]);
    }

    /** TODO: JAVADOC */
    public static double[] fix(double x, double y, double minX, double minY, double maxX,
                               double maxY, double winX, double winY) {
            return new double[]{ (x - minX) / (maxX - minX) * winX,
                                 (1 - (y - minY) / (maxY - minY)) * winY};

    }

    @Override
    public String toString() {
        assert winBounds.length == 2;
        assert dispBounds.length == 4;
        return "GraphingComponent: winBounds = [" + winBounds[0] + ", " + winBounds[1] + "], dispBounds = [" + 
            dispBounds[0] + ", " + dispBounds[1] + ", " + dispBounds[2] + ", " + dispBounds[3] + "], step = " + step +
            " (cStep = " + cStep() + "), indepVar = '" + indepVar + "', depVars = " +
            new Collection<String>().addAllE(depVars);
    }

    @Override
    public String toFancyString(int idtLvl) {
        assert winBounds.length == 2;
        assert dispBounds.length == 4;
        String ret = indent(idtLvl) + "GraphComponents:\n";
        ret += indent(idtLvl + 1) + "Window Bounds (X, Y) = [" + winBounds[0] + ", " + winBounds[1] + "]\n";
        ret += indent(idtLvl + 1) + "Display Bounds (x, y, X, Y) = [" + dispBounds[0] + ", " + dispBounds[1] + ", " +
               dispBounds[2] + ", " + dispBounds[3] + "]\n";
        ret += indent(idtLvl + 1) + "Step = " + step + " (cStep = " + cStep() + ")";
        ret += indent(idtLvl + 1) + "Independent Var = " + indepVar;
        ret += indent(idtLvl + 1) + "Dependant Var = " + new Collection<String>().addAllE(depVars);
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        assert winBounds.length == 2;
        assert dispBounds.length == 4;
        String ret = indent(idtLvl) + "GraphComponents:\n";
        ret += indent(idtLvl + 1) + "Window Bounds (X, Y):\n" + indent(idtLvl + 2) + "[" + winBounds[0] + ", " +
               winBounds[1] + "]\n";
        ret += indent(idtLvl + 1) + "Display Bounds (x, y, X, Y):\n" + indent(idtLvl + 2) + "[" + dispBounds[0] + ", " +
               dispBounds[1] + ", " + dispBounds[2] + ", " + dispBounds[3] + "]\n";
        ret += indent(idtLvl + 1) + "Step:\n" + indent(idtLvl + 2) + "\n";
        ret += indent(idtLvl + 2) + "(cStep: " + cStep() + ")";
        ret += indent(idtLvl + 1) + "Independent Var:\n" + indentE(idtLvl + 2) + indepVar;
        ret += indent(idtLvl + 1) + "Dependant Var:\n" + indentE(idtLvl + 2) +
                                    new Collection<String>().addAllE(depVars);
        return ret + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public GraphComponents copy(){
        return new GraphComponents(winBounds, dispBounds, step, indepVar, depVars);
    }

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof GraphComponents))
            return false;
        if(this == pObj)
            return true;
        GraphComponents pgc = (GraphComponents)pObj;
        return winBounds.equals(pgc) &&
               dispBounds.equals(pgc) &&
               step == pgc.step &&
               indepVar.equals(pgc.indepVar) &&
               depVars.equals(pgc.depVars);

    }
}