package Math.Equation;

import Math.MathObject;
import Math.Equation.CustomFunction;
import Math.Equation.Expression;
import Math.Exception.NotDefinedException;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * A class that represents an equation in Math. It really is just a collection of Expressions that are equal to
 * each other.
 * 
 * @author Sam Westerman
 * @version 0.67
 * @since 0.1
 */
public class Equation implements MathObject {

    /** This classe's list of expressions that are equal to eachother. */
    protected ArrayList<Expression> expressions;

    /**
     * The default constructor. This just instantiates {@link #expressions} as an empty ArrayList.
     */
    public Equation() {
        expressions = new ArrayList<Expression>();
    }


    /**
     * Adds all of the {@link Expression}s as defined in <code>pExprs</code>.
     * @param pExprs    An Arraylist of {@link Expression}s that will be added to {@link #expressions}.
     * @return This class, with <code>pExprs</code> added.
     */
    public Equation add(ArrayList<Expression> pExprs) {
        expressions.addAll(pExprs);
        return this;
    }

    /**
     * Adds all of the {@link Expression}s in <code>pExprs</code> to {@link #expressions}.
     * @param pExprs    A variable amount of {@link Expression}s that will be added to {@link #expressions}.
     * @return This class, with <code>pExprs</code> added.
     */
    public Equation add(Expression... pExprs) {
        for(Expression expr : pExprs)
            expressions.add(expr);
        return this;
    }

    /**
     * Adds new {@link Expression}s, each instatiated by a paramater in <code>pStrs</code>, to {@link #expressions}.
     * @param pStrs    A variable amount of Strings that will be added to {@link #expressions}.
     * @return This class, with <code>pStrs</code> added.
     */    
    public Equation add(String... pStrs) {
        for(String pStr : pStrs)
            for(String str : pStr.split("=",2)){
                expressions.add(new Expression(str));
            }
        return this;
    }

    /**
     * Returns the {@link #expressions} that this class defines.
     * @return {@link #expressions}
     */
    public ArrayList<Expression> expressions() {
        return expressions;
    }

    /**
     * Gets a string representing the {@link Expression#expression} for each {@link Expression} in {@link #expressions}.
     * @return A string comprised of each expression, with <code> = </code> between each one.
     */
    public String formattedExpressions(){
        String ret = "";
        for(Expression expr : expressions)
            ret += expr.formattedExpression() + " = ";
        return ret.substring(0, ret.length() - (expressions.size() > 0 ? 3 : 0));
    }

    @Override
    public String toString() {
        return "Equation: expressions = " + expressions;
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indent(idtLvl + 1) + "Expressions:";
        for(Expression expr : expressions) {
            //TODO: Update this "= " here to coincide with greater than or less than equations (when introduced).
            ret += "\n" + indent(idtLvl + 2) + "= "+ expr.formattedExpression();
        }
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indent(idtLvl + 1) + "Raw Equation:\n" + indentE(idtLvl + 2) + formattedExpressions() + "\n";
        ret += indent(idtLvl + 1) + "Expressions:";
        for(Expression expr : expressions)
            ret += "\n" + expr.toFullString(idtLvl + 2);
        return ret + "\n" + indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public Equation copy(){
        return new Equation().add(expressions);
    }
}