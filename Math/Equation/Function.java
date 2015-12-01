package Math.Equation;

import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

/**
 * A class that simulates both any kind of non-simple operation and fuctions in Math.
 * Simply, anything that isn't <code>+ - * / ^</code> should be defined using this.
 * For example, in <code>f(x)</code>, this class would represent f.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public abstract class Function {

    /**
     * A String that holds either the function name ({@link InBuiltFunction}) or the file name ({@link CustomFunction}).
     * OMFG I LOVE THE PUN HERE L0L. "fName" as in "function name". But also "fName" as in "fileName"
     */
    public String fName;

    /**
     * The default constructor for the Function class. Passes null for the pName to the other Constructor.
     */
    public Function() {
        this("");
    }

    /**
     * The main cosntructor for the Function class. All it does is instantiates fName.
     * @param pName         The name of the file which stores the code for how to execute the custom function.
     */
    public Function(String pName) {
        fName = pName;
    }

    /**
     * Gets a String representation of the function. In reality, just returns its name, because a String repr of the 
     * code is wayyy too hard.
     */
    public abstract String toString();

    /**
     * This thing takes a node (usually the node from {@link #exec(Factors,Node) exec}), and returns an array of the 
     * numerical values of each subnode.
     * @param pFactors          The factors that will be used when evaluating pNode.
     * @param pNode             The node to be evaluated.
     * @return An array of doubles, with each position corresponding to the value of each Node of that position in 
     *         {@link Node#subNodes pNode's subNodes}.
     */
    protected double[] evalNode(Equation pEq, Node pNode){
        double[] ret = new double[pNode.size()];
        for(int i = 0; i < ret.length; i++) ret[i] = pEq.eval(pNode.subNodes.get(i));
        return ret;

    }
    /**
     * Takes the different parameter nodes, does whatever operations it was programmed to do, and spits a result back.
     * @param pFactors      A factor class that contains all relevant information about variables / functions.
     *                      This is where variable values and function definitions are stored.
     * @param pNode         The Node that is going to be solved.
     * @return A double representing the value of pNode, when solved for with pFactors.
     * @throws NotDefinedException    Thrown when the function is defined, but how to execute it isn't.
     * @throws InvalidArgsException    Thrown when the function required parameters, and the ones passed aren't right.
     */
    public abstract double exec(Equation pEq, Node pNode) throws NotDefinedException, InvalidArgsException;
}