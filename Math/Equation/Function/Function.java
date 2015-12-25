package Math.Equation.Function;

import Math.MathObject;
import Math.Equation.EquationSystem;
import Math.Equation.Node;
import static Math.Declare.*;
import Math.Exception.NotDefinedException;

/**
 * A class that simulates both any kind of operation and any fuction in Math.
 * Simply put, anything that isn't a number or variable should be a function somehow.
 * For example, in <code>f(x)</code>, this class would represent f.
 * 
 * @author Sam Westerman
 * @version 0.72
 * @since 0.1
 */
public abstract class Function implements MathObject {

    /**
     * A String that holds either the function name ({@link InBuiltFunction}) or the file name ({@link CustomFunction}).
     */
    protected String name;

    /**
     * The Help text for this function
     */
    protected String help;

    /**
     * The Syntax text for this function.
     */
    protected String syntax;

    /**
     * The default constructor for the Function class. Instatiates {@link #name}, {@link #help}, and {@link #syntax} as
     * empty strings.
     */
    public Function() {
        this("");
    }

    /**
     * Instantiates the Function with the name of <code>pName</code>, and <code>""</code> for help and syntax.
     * @param pName         The name of this function.
     */
    public Function(String pName) {
        this(pName, "", "");
    }
    
    /**
     * The main cosntructor for the Function class. All it does is instantiates name, help, and syntax.
     * @param pName         The name of the function - serves as an identifier for {@link InBuiltFunction}s, and as the
     *                      filename for {@link CustomFunction}s. Cannot be null.
     * @param pHelp         The help string for the function. Cannot be null.
     * @param pSyntax       The syntax string for the function. Cannot be null.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public Function(String pName,
                    String pHelp,
                    String pSyntax) throws IllegalArgumentException{
        declP(pName != null, "Cannot instantiate a function with a null name! Try an empty name String instead.");
        declP(pHelp != null, "Cannot instantiate a function with a null help! Try an empty help String instead.");
        declP(pSyntax != null, "Cannot instantiate a function with a null syntax! Try an empty syntax String instead.");
        name = pName;
        help = pHelp;
        syntax = pSyntax;
    }

    /**
     * Returns this class's {@link #name}.
     * @return this class's {@link #name}.
     */
    public final String name() {
        return name;
    }

    /**
     * Returns the "help" value for this function.
     * @return A String representing the "help" value.
     */
    public String help() {
        return help;
    }

    /**
     * Returns the "help" value for this function.
     * @return A String representing the "syntax" value.
     */
    public String syntax() {
        return syntax;
    }

    /**
     * This thing takes a {@link Node} (usually the node from {@link #exec(EquationSystem,Node) exec}), and returns an
     * array of the numerical values of each {@link Node#subNodes() subNode}.
     * @param pEqSys        The {@link EquationSystem} that will be used when evaluating <code>pNode</code>.
     * @param pNode         The node to be evaluated.
     * @return An array of doubles, with each position corresponding to the value of each Node of that position in 
     *         {@link Node#subNodes() pNode's subNodes()}.
     */
    protected double[] evalNode(final EquationSystem pEqSys,
                                Node pNode) {
        double[] ret = new double[pNode.size()];
        for(int i = 0; i < ret.length; i++) {
            ret[i] = pNode.subNodes().get(i).eval(pEqSys);
        }
        return ret;

    }

    /**
     * Takes the parameter {@link Node} (and {@link EquationSystem}, performs whatever this function is defined to do,
     * and returns the result.
     * @param pEqSys        An {@link EquationSystem} that contains all relevant information about
     *                      {@link Equation Equations} and {@link Function Functions} is stored.
     * @param pNode         The {@link Node} that is going to be solved.
     * @return A double representing the value of <code>pNode</code>, when solved for with <code>pEqSys</code>.
     * @throws NotDefinedException    Thrown when the function is defined, but how to execute it isn't.
     * @throws IllegalArgumentException   Thrown when the function required parameters, and the ones passed aren't right.
     */
    public abstract double exec(final EquationSystem pEqSys,
                                Node pNode) throws
                                    NotDefinedException,
                                    IllegalArgumentException;

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof  Function))
            return false;
        if(this == (Function)pObj)
            return true;
        return name.equals(((Function)pObj).name()) &&
               help.equals(((Function)pObj).help()) &&
               syntax.equals(((Function)pObj).syntax());
    }

    /**
     * Gets the inverse of this function - that is, what function should be done to undo this one. <br>The inverse of 
     * <code>+</code> is <code>-</code>, and the inverse of <code>cos</code> is <code>arccos</code>.
     * @return The inverse of this function.
     * @throws NotDefinedException  Thrown when the inverse hasn't been defined yet, or there is no known
     * @deprecated Not defined yet, will be in the future.
     */
    public Function inverse() throws NotDefinedException{
        throw new NotDefinedException();
    }


}