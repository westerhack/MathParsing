package West.Math.Equation;

import West.Math.MathObject;
import static West.Math.Declare.*;
import West.Math.Exception.NotDefinedException;
import West.Math.Equation.Function.OperationFunction;
import West.Math.Set.Collection;

/**
 * A single item from an equation String.
 * Example: "sin(x+2)" yields the tokens <code>{"sin":FUNC, "(":LPAR, "x":VAR, "+":OPER, "2":NUM, ")":RPAR}</code>.
 * @author Sam Westerman
 * @version 0.75
 * @since 0.1
 */
public class Token implements MathObject {
    /** The String that this class is based upon. */
    protected String val;

    /** The type of token. Used to distinguish between things such as functions, groups, and operations. */
    protected Type type;

    public static enum Type { 
        VAR, FUNC, PAREN, DELIM, COMP, OPER, UNI
    }

    public static final Collection<String> PAREN_L = new Collection<String>()
    {{
        add("(");
        add("[");
        add("{");
    }};

    public static final Collection<String> PAREN_R = new Collection<String>()
    {{
        add(")");
        add("]");
        add("}");
    }};

    public static final Collection<String> DELIM = new Collection<String>()
    {{
        add(",");
        add(":");
        add("|");
    }};
    public Token() {
        this("", Type.VAR);
    }

    public Token(String pVal, Type pType) {
        declP(pVal != null, "Cannot instatiate a Token with a null pVal!");
        declP(pType != null, "Cannot instatiate a Token with a null pType!");
        val = pVal;
        type = pType;
    }

    public Type type() {
        return type;
    }

    public String val() {
        return val;
    }
    
    public boolean isConst() {
        assert type != null;
        return type == Type.VAR;
    }

    public boolean isFunc() {
        assert type != null;
        return type == Type.FUNC;
    }

    public boolean isGroup() {
        assert type != null && val != null;
        return type == Type.FUNC && val.isEmpty();
    }

    public boolean isOper() {
        assert type != null;
        return type == Type.OPER;
    }

    public int priority() {
        assert val != null : "val cannot be null! It was declared in the constructor, and has no way to be changed!";
        if(OperationFunction.OPERATOR.fromString(val) != null){
            assert isOper();
            return OperationFunction.OPERATOR.fromString(val).priority();
        }
        return -1;
    }
    @Override
    public String toString() {
        return "["+val+":"+type.toString().substring(0,1)+"]";
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Token:\n";
        ret += indent(idtLvl + 1) + "Value = " + val + "\n";
        ret += indent(idtLvl + 1) + "Type = " + type;
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Token\n";
        ret += indent(idtLvl + 1) + "Value\n" + indentE(idtLvl + 2) + val + "\n";
        ret += indent(idtLvl + 1) + "Type\n" + indentE(idtLvl + 2) + type + "\n" + indentE(idtLvl + 1) + "\n";
        ret += indentE(idtLvl);
        return ret;
    }

    @Override
    public Token copy(){
        return new Token(val, type);
    }

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof Token))
            return false;
        if(pObj == this)
            return true;
        return val.equals(((Token)pObj).val()) && type == ((Token)pObj).type();
    }
}