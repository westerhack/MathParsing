package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Exception.TypeMisMatchException;
import Math.Exception.NotDefinedException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that models an expression, and its behaviour.
 * @author Sam Westerman
 * @version 0.2
 */

public class Expression implements MathObject {

    /** The raw expression. */
    protected String expression;

    /**
     * The Node representing the whole expression. 
     * @see   Node#generateNodes(ArrayList)
     */
    protected Node node;
    /**
     * The default constructor for the Expression class. Just passes an empty String, and Node to the 
     * {@link #Expression(String,Node) main expression constructor}.
     */
    public Expression() {
        this("", new Node());
    }



    /**
     * The Object - only constructor for the expression class. Just passes <code>pObj</code>, and a Node based off
     * <code>pObj</code>, {@link #Expression(String,Node) main expression constructor}.
     * @param pObj       An Object that this class will be modeled after. 
     */ 
    public Expression(Object pObj) {
        this("" + pObj, Node.generateNodes(Expression.parseTokens("" + pObj)));
    }

    /**
     * The Node-only for the expression class. Just passes a String created based on Node's subnodes to the
     * {@link #Expression(String,Node) main expression constructor}.
     * @param pNode        The Node that the expression class will be modeled after.
     */ 
    public Expression(Node pNode) {
        this(pNode.genEqString(), pNode);
    }


    /**
     * The main constructor for the expression class. Just sets {@link #expression} and {@link #node} to their
     * respective constructors
     * @param pEq       The String representation of the expression. Is only ever used to identify individual expressions.
     * @param pNode        The Node that the entire expression is based off of.
     */ 
    public Expression(String pEq,
                      Node pNode) {
        expression = pEq.trim().replaceAll(" ","");
        node = pNode;
    }


    /**
     * Returns {@link #node} - the {@link Node} that represents this class's {@link #expression}.
     * @return {@link #node} - the {@link Node} that represents this class's {@link #expression}.
     */
    public Node node(){
        return node;
    }

    /**
     * Returns {@link #expression} - the String that this class is modeled after.
     * @return {@link #expression} - the String that this class is modeled after.
     */
    public String expression(){
        return expression;
    }

    /**
     * Generates {@link #node} using {@link #expression}.
     */
    public void genNode() {
        genNode(expression);
    }

    /**
     * Generates {@link #node} using pEq.
     * @param pEq       The expression that this class's node will be based off of.
     */
    public void genNode(String pEq) {
        node = Node.generateNodes(Expression.parseTokens(pEq));
    }

    /**
     * Fixes any terms that might be misleading to the compiler. For example, <code>sinx</code> will become
     * <code>sin(x)</code>. Note: To not have it do any fixing, put a "@" at the beginning of the input String
     * @param pEq            The expression to be corrected.
     * @return A corrected version of the expression.
     */
    public static String fixExpression(String pEq) {
        if(pEq.charAt(0) == '@')
            return pEq.substring(1);
        if(pEq.indexOf("=")!=-1)
            pEq = pEq.split("=")[1];
        String[] trigf = new String[]{"sec", "csc", "cot", "sinh", "cosh", "tanh", "sin", "cos", "tan"};
        for(String trig : trigf) {
            pEq = pEq.replaceAll("()" + trig + "(?!h)([A-za-z]+)","$1" + trig + "($2)");
        }

        pEq = pEq.replaceAll("\\-\\(", "-1*(");
        pEq = pEq.replaceAll("([\\d.])+(\\(|(?:[A-Za-z]+))", "$1*$2");
        return pEq;
    }

    /**
     * Generates an ArrayList of tokens that represent rEq.
     * Note that this removes all whitespace (including spaces) before handling the expression.
     * @param rEq    The expression to be parsed.
     * @return An ArrayList of tokens, each representing a different chunk of the expression. 
     * @see Token
     */
    public static ArrayList<Token> parseTokens(String rEq) throws TypeMisMatchException{
        rEq = fixExpression(rEq.trim().replaceAll(" ",""));
        ArrayList<Token> tokens = new ArrayList<Token>();
        String prev = "";
        char c;
        for(int x = 0; x < rEq.length(); x++) {
            c = rEq.charAt(x);
            if(prev.length() > 0 && prev.charAt(0) == '\'') {
                prev += c;
                if(c == '\'') {
                    tokens.add(new Token(prev.substring(1, prev.length() -1), Token.Type.ARGS));
                    prev = "";
                }
                continue;
            }
            if(prev.length() > 0 && prev.charAt(0) == '\'' && c == '\'') {
                prev = prev.substring(1);
                tokens.add(new Token(prev, Token.Type.VAR));
                prev = "";
                continue;
            }
            if(isAlphaNumPQ(c)) {
                prev += c;
                if(x == rEq.length() - 1)
                    tokens.add(new Token(prev, isNumP(prev) ? Token.Type.NUM : Token.Type.VAR));                    
                continue;
            }
            switch(c) {
                case '(': // This should never be preceeded by a number.
                    if(!isAlpha(prev)) {
                        throw new TypeMisMatchException("'" + prev + "'isn't alphabetical, but a group / function was" +
                            " attempted to be made because it is succeeded by a '('");
                    } if(prev.length() != 0) {
                        tokens.add(new Token(prev, Token.Type.FUNC));
                    } else {
                        tokens.add(new Token(prev, Token.Type.GROUP));
                    }
                    tokens.add(new Token("(",Token.Type.LPAR));
                    break;
                case ')': 
                    if(prev.length() != 0) {
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Type.NUM : Token.Type.VAR));
                    }
                    tokens.add(new Token(")",Token.Type.RPAR));
                    break;
                case '-': case '+': case '*': case '/': case '^':
                    if(prev.length() != 0) {
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Type.NUM : Token.Type.VAR));
                    }
                    tokens.add(new Token(c, Token.Type.OPER));
                    break;
                case ',':
                    if(prev.length() != 0) {
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Type.NUM : Token.Type.VAR));
                    }
                    tokens.add(new Token(c, Token.Type.DELIM));
                    break;

                default:
                    if(prev.length() != 0) {
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Type.NUM : Token.Type.VAR));
                    }
                    tokens.add(new Token(c, Token.Type.NULL));
                    break;

            }
            prev = "";
        }
        return tokens;
    }


    /**
     * Checks if a character is alphanumeric, period, or a single quote (').
     * @param c     The character to test.
     * @return      True if the character is alphanumeric, period, or a single quote ('). False otherwise.
     */
    protected static boolean isAlphaNumPQ(char c) {
        return isAlphaNumP(c) || c == '\'';
    }

    /**
     * Checks if a character is alphanumeric or a period.
     * @param c     The character to test.
     * @return      True if the character is alphanumeric or a period. False otherwise.
     */
    protected static boolean isAlphaNumP(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '.';
    }

    /**
     * Checks if a String consists only of letters, digits, and / or periods.
     * @param str   The String to test.
     * @return      True if the String consists only of letters, digits, and / or periods. False otherwise.
     */
    protected static boolean isAlphaNumP(String str) {
        for(char c : str.toCharArray())
            if(!isAlphaNumP(c))
                return false;
        return true; //note also uses '.'
    }

    /**
     * Checks if a character is a letter.
     * @param c     The character to test.
     * @return      True if the character is a letter. False otherwise.
     */
    protected static boolean isAlpha(char c) {
        return Character.isAlphabetic(c);
    }

    /**
     * Checks if a String consists only of letters. 
     * @param str   The String to test.
     * @return      True if the String is only letters. False otherwise.
     */
    protected static boolean isAlpha(String str) {
        for(char c : str.toCharArray())
            if(!isAlpha(c))
                return false;
        return true;
    }
   /**
     * Checks if a character is a digit or period.
     * @param c     The character to test.
     * @return      True if the character is a digit or period. False otherwise.
     */
    protected static boolean isNumP(char c) {
        return Character.isDigit(c) || c == '.';
    }

    /**
     * Checks if a String consists only of digits and / or periods. 
     * Please note that this doesn't check to make sure there _are_ digits. So "...." will return true.
     * @param str   The String to test.
     * @return      True if the String is only digits and / or periods. False otherwise.
     */        
    protected static boolean isNumP(String str) {
        for(char c : str.toCharArray())
            if(!isNumP(c))
                return false;
        return true;
    } 

    @Override
    public String toString() {
        return expression == null ? "Null Expression" : 
               expression.length() == 0 ? "Empty Expression" : 
               expression.replaceAll("(\\+|\\-|\\*|/|\\^|,)", " $1 ");
    }

    @Override
    public String toFancyString(int idtLvl) {
        return indent(idtLvl) + expression == null ? "Null Expression" : 
               expression.length() == 0 ? "Empty Expression" : 
               toString();
    }

    @Override
    public String toFullString(int idtLvl) {
        return indent(idtLvl) + "Expression:\n" + indent(idtLvl + 1) + "RawEq\n" + indent(idtLvl + 2) 
                            + expression + "\n" + indent(idtLvl + 1) + "Nodes\n" + node.toFullString(idtLvl + 2);
    }

    @Override
    public Expression copy(){
        return new Expression(expression, node);
    }
}