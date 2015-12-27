package West.Math.Equation.Function;

import West.Math.MathObject;
import West.Math.Print;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Node;
import West.Math.Exception.NotDefinedException;

import java.util.HashMap;
import java.util.Random;

/**
 * A class that represents an operation in mathametics. It acts very similar to an {@link InBuiltFunction}.
 * 
 * @author Sam Westerman
 * @version 0.75
 * @since 0.1
  * @see <a href="https://en.wikipedia.org/wiki/Operation_(mathematics)">Operation</a>
 */
public class OperationFunction extends InBuiltFunction {


    public static enum OPERATOR  {
        ADDITION("+", 0),           // Algebra "addition"
        SUBTRACTION("-", 0),        // Algebra "subtraction"
        MULTIPLICATION("*", 0),     // Algebra "multiplication"
        DIVISION("/", 0),           // Algebra "division"
        POWER("^", 0),              // Algebra "power of"

        ST_IN("∈"),                 // Set Theory "in"
        ST_NIN("∉"),                // Set Theory "not in"
        ST_PSUB("⊂"),               // Set Theory "proper subset"
        ST_NPSUB("⊄"),              // Set Theory "not proper subset"
        ST_SUB("⊆"),                // Set Theory "subset"
        ST_NSUB("⊈"),               // Set Theory "not subset"
        ST_PSUP("⊂"),               // Set Theory "proper supset"
        ST_NPSUP("⊄"),              // Set Theory "not proper supset"
        ST_SUP("⊆"),                // Set Theory "supset"
        ST_NSUP("⊈"),               // Set Theory "not supset"
        ST_NOT("¬");                // Set Theory "not"

        private String notation;
        private int priority;
        private OPERATOR (String pNot){
            this(pNot, -1);
        }
        private OPERATOR (String pNot, int pPriority){
            notation = pNot;
            priority = pPriority;
        }

        public String notation(){
            return notation;
        }
        public int priority(){
            return priority;
        }
        public String toString(){
            return notation() + " | " + priority;
        }
        public static OPERATOR fromString(String pNot){
            if (pNot != null)
                for (OPERATOR o : OPERATOR.values())
                    if (pNot.equalsIgnoreCase(o.notation()))
                        return o;
            return null;
        }
    }
    /**
     * Default constructor. Instatiated {@link #name}, {@link #help}, and {@link #syntax} as empty strings.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public OperationFunction() throws NotDefinedException{
        this("", "", "");
    }

    /**
     * Main constructor. Takes a name, a help string, and a syntax string.
     * @param pOper     The symbol of the operator, like <code>+, -, *, /, ^</code>.
     * @param pHelp     The "help" text that will be displayed when the {@link #help()} function is called.
     * @param pSyntax   The "syntax" text that will be displayed when the {@link #syntax()} function is called.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public OperationFunction(String pOper,
                             String pHelp,
                             String pSyntax) throws IllegalArgumentException{
        super(pOper, pHelp, pSyntax);
    }

    @Override
    public double exec(final EquationSystem pEqSys,
                       Node pNode) throws 
                           NotDefinedException,
                           IllegalArgumentException {
        assert pNode.subNodes().size() != 0 : "Node size cannot be 0!";
        assert name.equals("+") || name.equals("-") || name.equals("*") || name.equals("/") || name.equals("^");
        double ret = pNode.get(0).eval(pEqSys);
        switch(name) {
            case "+":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret += pNode.get(i).eval(pEqSys);
                }
                break;
            case "-":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret -= pNode.get(i).eval(pEqSys);
                }
                break;
            case "*":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret *= pNode.get(i).eval(pEqSys);
                }
                break;
            case "/":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret /= pNode.get(i).eval(pEqSys);
                }
                break;
            case "^":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret = Math.pow(ret, pNode.get(1).eval(pEqSys)); // not sure this works
                }
                break;
            default:
                Print.printw("No known way to evaluate '" + this + "'");
        }
        return ret;
    }

    @Override
    public String toString() {
        return "OperationFunction '" + name + "'";
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "OperationFunction '" + name + "':\n";
        ret += indent(idtLvl + 1) + "Help = " + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax = " + syntax + "";
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "OperationFunction:\n";
        ret += indent(idtLvl + 1) + "Name:\n" + indentE(idtLvl + 2) + name + "\n";
        ret += indent(idtLvl + 1) + "Help:\n" + indentE(idtLvl + 2) + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax:\n" + indentE(idtLvl + 2) + syntax;
        return ret + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public OperationFunction copy(){
        return new OperationFunction(name, help, syntax);
    }
}