import java.util.ArrayList;
/** 
 * A class that models an equation, and its behaviour.
 * @author Sam Westerman
 * @version 0.1
 */
public class Equation {
    public static void main(String[] args) {
        // Equation eq = new Equation("1 + b * (2 + 3) + f(x, 4 + 1, a(5)) + 6");
        Equation eq = new Equation("3 * (2 + 1)");
    }

    /** The raw equation, totally untouched. Gets set right when Equation is initialized. */
    public final String RAW_EQ;

    /** An ArrayList of tokens that comprise <code>RAW_EQ</code>. */
    public final ArrayList<Token> TOKENS;

    public final ArrayList<Node> NODES;

    /** 
     * Default constructor. Just passes null to the main constructor.
     */
    public Equation(){
        this(null);
    }
    /** 
     * Main constructor. Takes the parameter <code>pEq</code>, and parses the tokens from it, and generates a "node"
     * model for it.
     * @param pEq       The raw Equation to be parsed.
     */ 
    public Equation(String pEq){
        RAW_EQ = pEq;
        // System.out.println(RAW_EQ);
        TOKENS = parseTokens(RAW_EQ);
        System.out.println(TOKENS);
        NODES = generateNodes(TOKENS);
        System.out.println(NODES);
    }
    private Object[] operDecant (Node n, int pos, ArrayList<Token> pTokens){
        int paren = 0;
        pos++;
        do{
            Token t2 = pTokens.get(pos);
            if(t2.TYPE == Token.Types.RPAR) paren--;
            else if(t2.TYPE == Token.Types.LPAR) paren++;
            else if(t2.TYPE == Token.Types.NUM || t2.TYPE == Token.Types.VAR) n.subNodes.add(new FinalNode(t2));
            else if(t2.TYPE == Token.Types.FUNC || t2.TYPE == Token.Types.GROUP){
                Object[] temp = decant(t2,pos,pTokens);
                pos = (int) temp[0];
                n.subNodes.add((Node)temp[1]);
            }
            else if(t2.TYPE == Token.Types.OPER){
                Object[] temp = decant(t2, pos,pTokens);
                pos = (int) temp[0];
                Node N = n.copy();
                n = new Node(t2, new ArrayList<Node>(){{add(N); add((Node)temp[1]);}});
            }
            pos++;
        } while(paren > 0 && pos < pTokens.size());
        return new Object[]{pos-1, n};
    }


    /** The return type is int, node. I couldn't think of a better way of passing 2 parameters. */
    private Object[] decant (Token t, int pos, ArrayList<Token> pTokens){
        int paren = 0;
        pos++;
        Node n = new Node(t);
        do{
            Token t2 = pTokens.get(pos);
            if(t2.TYPE == Token.Types.RPAR) paren--;
            else if(t2.TYPE == Token.Types.LPAR) paren++;
            else if(t2.TYPE == Token.Types.NUM || t2.TYPE == Token.Types.VAR) n.subNodes.add(new FinalNode(t2));
            else if(t2.TYPE == Token.Types.FUNC || t2.TYPE == Token.Types.GROUP){
                Object[] temp = decant(t2,pos,pTokens);
                pos = (int) temp[0];
                n.subNodes.add((Node)temp[1]);
            }
            else if(t2.TYPE == Token.Types.OPER){
                Object[] temp = operDecant(n.subNodes.get(n.subNodes.size()-1), pos,pTokens);
                pos = (int) temp[0];
                Node N = n.copy();
                n = new Node(t2, new ArrayList<Node>(){{add(N); add((Node)temp[1]);}});
            }
            pos++;
            System.out.println(t2);            
        } while(paren > 0 && pos < pTokens.size());
        return new Object[]{pos-1, n};
    }
    private ArrayList<Node> generateNodes(ArrayList<Token> pTokens){
        ArrayList<Node> nodes = new ArrayList<Node>();
        int pos = 0;
        // Token t;        
        while(pos < pTokens.size()){
            System.out.println(nodes);
            Token t = pTokens.get(pos);
            if(t.TYPE == Token.Types.FUNC || t.TYPE == Token.Types.GROUP){
                if(pTokens.get(pos + 1).TYPE != Token.Types.LPAR)
                    System.err.println("[ERROR]: The Function '" + t +
                                       "' token doesn't have a Left Paren '(' after it.");
                Object[] temp = decant(t,pos,pTokens);
                pos = (int) temp[0];
                nodes.add((Node)temp[1]);
            }
            else if(t.TYPE == Token.Types.VAR || t.TYPE == Token.Types.NUM)
                nodes.add(new FinalNode(t));
            else if(t.TYPE == Token.Types.OPER){
                // nodes.add(new Node(t, new ArrayList<Node>()));
                //     nodes.get(nodes.size()-1).subNodes.add(nodes.get(nodes.size()-2)); 
                //     Token t2 = pTokens.get(pos+1);
                //     if(t2.TYPE == Token.Types.VAR || t2.TYPE == Token.Types.NUM)
                //         nodes.get(nodes.size()-1).subNodes.add(new FinalNode(t2));
                //     else if(t2.TYPE == Token.Types.FUNC || t2.TYPE == Token.Types.GROUP){
                //         pos+=2;
                //         if(pTokens.get(pos + 1).TYPE != Token.Types.LPAR)
                //             System.err.println("[ERROR]: The Function '" + t +
                //                                "' token doesn't have a Left Paren '(' after it.");
                //         Object[] temp = decant(t,pos,pTokens);
                //         pos = (int) temp[0];
                //         // nodes.add((Node)temp[1]);
                //         nodes.get(nodes.size()-1).subNodes.add((Node)temp[1]);
                //     }
                //     nodes.remove(nodes.size()-2);
                //     // nodes.remove(nodes.size()-1);
                // pos++;
            }
            pos++;
        }
        return nodes;
    }
    /** 
     * Generates an ArrayList of tokens that make up the inputted equation. 
     * Note that this removes all whitespace (including spaces) before handling the equation.
     * @param rEq    The equation to be parsed.
     * @return An ArrayList of tokens, each representing a different chunk of the equation. 
     * @see Token.Tokens
     */
    private ArrayList<Token> parseTokens(String rEq){
        rEq = rEq.trim().replaceAll(" ","");
        ArrayList<Token> tokens = new ArrayList<Token>();
        String prev = "";
        char c;
        for(int x = 0; x < rEq.length(); x++) {
            c = rEq.charAt(x);
            if(isAlphaNumP(c)){
                prev += c;
                if(x == rEq.length() - 1)
                    tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));                    
                continue;
            }
            switch(c){
                case '(': // This should never be preceeded by a number.
                    if(!isAlpha(prev))
                        System.err.println("[ERROR] Uh oh! '" + prev +
                        "' isn't Alphabetical, but is succeeded by '('. Continuing anyways.");
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, Token.Types.FUNC));
                    else
                        tokens.add(new Token(prev, Token.Types.GROUP));
                    tokens.add(Token.LPAR);
                    break;
                case ')': 
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(Token.RPAR);
                    break;
                case '-': case '+': case '*': case '/': case '^':
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(new Token(c, Token.Types.OPER));
                    break;
                case ',':
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(new Token(c, Token.Types.DELIM));
                    break;

                default:
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(new Token(c, Token.Types.NULL));
                    break;

            }
            prev = "";
        }
        return tokens;
    }

    /** 
     * Checks if a character is alphanumeric or a period.
     * @param c     The character to test.
     * @return      True if the character is alphanumeric or a period. False otherwise.
     */
    public static boolean isAlphaNumP(char c){
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '.';
    }

    /** 
     * Checks if a string consists only of letters, digits, and / or periods.
     * @param str   The string to test.
     * @return      True if the string consists only of letters, digits, and / or periods. False otherwise.
     */
    public static boolean isAlphaNumP(String str){
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
    public static boolean isAlpha(char c){
        return Character.isAlphabetic(c);
    }

    /** 
     * Checks if a string consists only of letters. 
     * @param str   The string to test.
     * @return      True if the String is only letters. False otherwise.
     */
    public static boolean isAlpha(String str){
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
    public static boolean isNumP(char c){
        return Character.isDigit(c) || c == '.';
    }

    /** 
     * Checks if a string consists only of digits and / or periods. 
     * Please note that this doesn't check to make sure there _are_ digits. So "...." will return true.
     * @param str   The string to test.
     * @return      True if the String is only digits and / or periods. False otherwise.
     */        
    public static boolean isNumP(String str){
        for(char c : str.toCharArray())
            if(!isNumP(c))
                return false;
        return true;
    } 
}

