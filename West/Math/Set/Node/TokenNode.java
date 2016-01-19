package West.Math.Set.Node;
import West.Math.Set.Collection;
import java.util.ArrayList;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Function.Function;
import West.Math.Equation.Token;
import West.Math.MathObject;
import West.Math.Equation.Equation;
import java.util.HashMap;
/**
* TODO: JAVADOC
* 
* @author Sam Westerman
* @version 0.85
* @since 0.75
*/
public class TokenNode extends Node<Token, TokenNode> implements MathObject {

    public TokenNode(){
        super(new Token("",Token.Type.FUNC));

    }

    public TokenNode(Token pToken) {
        super(pToken);
    }
    public TokenNode(TokenNode pTokenNode) {
        super(pTokenNode.token);
    }

    protected Object[] condeseNodes(ArrayList<Token> pTokens) {
        int pos = 0;
        TokenNode node = copy();
        while(pos < pTokens.size()) {
            Token t = pTokens.get(pos);
            if(t.isConst() || t.isBinOper() || t.isUNL() || t.isUNR())
                node.add(new TokenNode(t));
            else if(t.isFunc() || t.isDelim()) {
                Collection<Token> passTokens = new Collection<Token>();
                int paren = 0;
                int x = pos + 1;
                do{
                    if(Token.PAREN_L.contains(pTokens.get(x).val())) paren++;
                    if(Token.PAREN_R.contains(pTokens.get(x).val())) paren--;
                    if(t.isDelim() && Token.DELIM.contains(pTokens.get(x).val()) && paren == 0) break;
                    x++;
                } while((t.isDelim() ? 0 <= paren : 0 < paren) && x < pTokens.size());
                for(Token tk : pTokens.subList(pos + 1, x))
                    passTokens.add(tk);
                if(passTokens.get(0).isParen())
                    passTokens.add(1, new Token("", Token.Type.DELIM));
                Object[] temp = new TokenNode(t).condeseNodes(passTokens);
                pos += (int)temp[0];
                node.add((TokenNode)temp[1]);
            } 
            pos++;
        }
        return new Object[]{pos, node};
    }


    private int priority(){
        if(token.isConst())
            return Function.DEFAULT_PRIORITY + 1;
        if(token.isDelim())
            return -1;
        assert token.isFunc();
        Function i = Function.get(token.val());
        return i == null ? Function.DEFAULT_PRIORITY : i.priority();
    }
    private static int firstHighPriority(Collection<TokenNode> peles){
        int pos = 0, priority = Function.DEFAULT_PRIORITY;
        for(int i = 0; i < peles.size(); i++){
            if(peles.get(i).priority() <= priority){ //i swapped them
                priority = peles.get(i).priority();
                pos = i;
            }
        }
        return priority == -1 ? -1 : pos;
    }
    private static TokenNode condense(final Collection<TokenNode> peles){
        System.out.println("peles:"+peles);
        if(peles.size() == 0)
            return null;
        if(peles.size() == 1)
            if(peles.get(0).size() == 0){
                System.out.println("returning0:"+peles.get(0));
                return peles.get(0);
            }
            else if(peles.get(0).size() != 0)
                return new TokenNode(peles.get(0)){{
                    add(condense(new Collection<TokenNode>().addAllE(peles.get(0).elements)).removeExtraFuncs());
                    System.out.println("removed:"+condense(new Collection<TokenNode>().addAllE(peles.get(0).elements)).removeExtraFuncs());
                    // forEach(this::addE);
                    // System.out.println("--elements--::"+peles);
                    // elements.forEach(System.out::println);
                    // System.out.println("--end--::"+peles);
                    // System.out.println("size!=0::  "+this+"$$"+elements+"  ::  "+peles+"::");
                    System.out.println("returning1:"+this);
                }};
        int fhp = firstHighPriority(peles);
        if(fhp == -1)
            return new TokenNode(new Token()){{
                // System.out.println("--aadding---");
                // for(TokenNode e : peles)
                //     System.out.println("->>"+condense(new Collection<TokenNode>().addE(e)));
                peles.forEach(e -> add(condense(new Collection<TokenNode>().addE(e))));
                // System.out.println("--fhpelements--::"+peles);
                // for(int i = 0; i < elements.size(); i++)
                //     System.out.println(i+": "+elements.get(i));
                // System.out.println(elements+"::"+peles);
                // System.out.println("--peles--::"+peles);
                // for(int i = 0; i < peles.size(); i++)
                //     System.out.println(i+": "+peles.get(i));
                // System.out.println(peles+"::"+peles);
                // System.out.println("---------");
                System.out.println("returning2:"+this);
            }};
        TokenNode u = condense(new Collection.Builder<TokenNode>().add(peles.get(fhp)).build());
        TokenNode s = condense(new Collection.Builder<TokenNode>().addAll(peles.subList(0, fhp)).build());
        TokenNode e = condense(new Collection.Builder<TokenNode>().addAll(peles.subList(fhp + 1)).build()).removeExtraFuncs();
        // System.out.println("u4:  "+u+"  ::  "+peles);
        // System.out.println("s:  "+s+"  ::  "+peles);
        System.out.println("e:  "+e+"  ::  "+peles);
        if(s != null)
            u.add(s);
        if(e != null)
            u.add(e);
        System.out.println("returning3:"+u);
        // System.out.println("u8:  "+u+"  ::  "+peles);
        return u;
    }


    public TokenNode removeExtraFuncs(){
        if(isFinal())
            return this;
        TokenNode ret = new TokenNode(this);
        for(Node<?, ?> n : elements){
            TokenNode tn = (TokenNode)n;
            if(!tn.isFinal())
            System.out.println(tn+":"+(tn.size() == 1) + ":"+ tn.get(0).token.isGroup());
            System.out.println(tn.toFancyString());
            if(tn.size() == 1 && tn.get(0).token.isGroup())
                ret.add(new TokenNode(tn){{
                    tn.get(0).forEach(e -> this.add(((TokenNode)e).removeExtraFuncs()));
                }});
            else
                ret.add(tn.removeExtraFuncs());
        }
        System.out.println("ret::"+ret);
        return ret;
    }

    public boolean add(TokenNode tn){
        assert tn != null : "attempting to add null to '"+toFullString()+"'";
        return super.add(tn);
    }
    public static TokenNode generateMasterNode(ArrayList<Token> pTokens) {
        TokenNode tn = (TokenNode)new TokenNode().condeseNodes(pTokens)[1]; //just to make it easier to read.
        tn = condense(new Collection.Builder<TokenNode>().addAll(tn.elements).build());
        return tn.removeExtraFuncs();
    }

    @Override
    public void addD(int i, Node<?, ?> n) {
        assert n != null : "Cannot addDepth null Nodes!";
        if(i <= 0 || size() <= 0){
            add(n);
        } else {
            get(size() - 1).addD(i - 1, n);
        }
    }

    private void addD(TokenNode n){
        super.addD(depth(), n);
    }

    public TokenNode getD(int depth){
        return (TokenNode) super.getD(depth);
    }

    private void setD(int depth, TokenNode n){
        super.setD(depth, -1, n);
    }

    @Override
    public TokenNode get(int pPos){
        return (TokenNode)super.get(pPos);
    }

    @Override
    public TokenNode setToken(Token pToken){
        assert pToken != null;
        token = pToken;
        return this;
    }

    public HashMap<String, Double> eval(final EquationSystem pEqSys){
        return eval(new HashMap<String, Double>(), pEqSys);
    }

    private HashMap<String, Double> appendHashMap(HashMap<String, Double> a, Object b){
        if(b != null)
            a.putAll((HashMap<String, Double>)b);
        return a;
    }
    private HashMap<String, Double> appendHashMap(HashMap<String, Double> a, String b, Double c){
        a.put(b,c);
        return a;
    }


    public TokenNode findExpr(String val){
        if(toExprString().equals(val))
            return this;
        if(true)return this;
        assert size() > 0;
        for(int i = 0; i < size(); i++){
            TokenNode n = get(i);
            if(n != null)
                if(n.toExprString().equals(val))
                    return this;
                else
                    return n;
        }
        return null;
    }
    /**
     * TODO: JAVADOC
     * returns Double.NaN if the result isnt in bounds
     */
    public HashMap<String, Double> eval(HashMap<String, Double> pVars, final EquationSystem pEqSys){
        assert pEqSys != null : "Cannot evaluate a null EquationSystem!";
        String val = toExprString();
        if(pVars.containsKey(val)) //if the value is already in pVars, then just take a shortcut.
            return pVars;
        for(Equation eq : pEqSys.equations()){ //if the current val is on the left hand side, then use that
            if(((TokenNode)eq.subEquations().getSD(1).get(0)).toExprString().equals(val)){
                TokenNode tkn = (((TokenNode)eq.subEquations().getSD(1).get(-1)));
                pVars = appendHashMap(pVars, tkn.eval(pVars, pEqSys));
                appendHashMap(pVars,val, pVars.get(tkn.toString()));
                return pVars;
            }
        }
        if(token.isFunc() || token.isDelim()){
            if(pEqSys.functions().containsKey(token.val())) // if it is a function
                return pEqSys.functions().get(token.val()).exec(pVars, pEqSys, this); //no work
            return Function.exec(pVars, token.val(), pEqSys, this); //no else needed
        } else if(isFinal()){
            val = token.val();
            Double d;
            try{
                return appendHashMap(pVars, val, Double.parseDouble(val));
            } catch(NumberFormatException err){ 
                switch(val) {
                    case "e":
                        return appendHashMap(pVars, val, Math.E);
                    case "k": //Coloumn's constant
                        return appendHashMap(pVars, val, 8.9875517873681764E9);
                    case "pi": case "π": case "Π":
                        return appendHashMap(pVars, val, Math.PI);
                    case "NaN": case "nan":
                        return appendHashMap(pVars, val, Double.NaN);
                    case "rand": case "random":
                        return appendHashMap(pVars, val, Math.random()); 

                    // X / 2
                    case "½": return appendHashMap(pVars, val, 1/2D);

                    // X / 3
                    case "⅓": return appendHashMap(pVars, val, 1/3D);
                    case "⅔": return appendHashMap(pVars, val, 2/3D);

                    // X / 4
                    case "¼": return appendHashMap(pVars, val, 1/4D);
                    case "¾": return appendHashMap(pVars, val, 3/4D);
                    
                    // X / 5
                    case "⅕": return appendHashMap(pVars, val, 1/5D);
                    case "⅖": return appendHashMap(pVars, val, 2/5D);
                    case "⅗": return appendHashMap(pVars, val, 3/5D);
                    case "⅘": return appendHashMap(pVars, val, 4/5D);

                    // X / 6
                    case "⅙": return appendHashMap(pVars, val, 1/6D);
                    case "⅚": return appendHashMap(pVars, val, 5/6D);

                    // X / 7
                    case "⅐": return appendHashMap(pVars, val, 1/7D);

                    // X / 8
                    case "⅛": return appendHashMap(pVars, val, 1/8D);
                    case "⅜": return appendHashMap(pVars, val, 3/8D);
                    case "⅝": return appendHashMap(pVars, val, 5/8D);
                    case "⅞": return appendHashMap(pVars, val, 7/8D);

                    //X / 9
                    case "⅑": return appendHashMap(pVars, val, 1/9D);

                    //X / 10
                    case "⅒": return appendHashMap(pVars, val, 1/10D);

                    default:
                        System.err.println("Variable '" + val + "' doesn't exist; returning NaN instead");
                        return appendHashMap(pVars, val, Double.NaN);
                }
            }
        } else
            throw new UnsupportedOperationException("This shouldn't happen! There is no way to evaluate node: " +
                                                    token.val());
    }

    //here down are from EquationNode
    public <C extends Comparable<C>> boolean isInBounds(HashMap<String, Double> vars, String toEval){
        if(token.val().isEmpty()){
            assert size() == 1 : toFancyString();
            return get(0).isInBounds(vars, toEval);
        }
        assert size() == 2;
        Double d0, d1;
        d0 = get(0).eval((EquationSystem.fromHashMap(vars))).get(get(0).toString()); 
        d1 = get(1).eval((EquationSystem.fromHashMap(vars))).get(get(0).toString()); 
        return Function.get(toString()).funcObj().exec(new Double[]{d0, d1}) == 1;
    }

    public String toExprString(){
        String ret = "";
        if(size() == 0){
            assert token.isConst() ? isFinal() : true;
            return token.val();
        }
        if((token.isFunc() && !token.isBinOper()) || token.isDelim()){
            ret += token.val() + "(";
            for(Node n : this)
                ret += ((TokenNode)n).toExprString() + ", ";
            return (size() == 0 ? ret : ret.substring(0, ret.length() - 2)) + ")";
        } else if(token.isBinOper()){
            if(size() == 1) // TODO: FIX THIS
                ret += token.val() + " " + ((TokenNode)get(0)).toExprString();
            else{
                for(Node n : this){
                    ret += ((TokenNode)n).toExprString();
                    ret += " " + (token == null ? "" : token.val()) + " ";
                }
                return ret.substring(0, ret.length() >=2 ? ret.length() - 2 : ret.length());
            }
        }
        else if(token.isConst())
            return token.val();
        else
            assert false;
        return null;
    }

    @Override
    public TokenNode copy(){
        assert elements != null;
        return (TokenNode)new TokenNode(token).setElements(elements);
    }

    @Override
    public String toString(){
        return toExprString().replaceAll("^\\((.*)\\)$", "$1");
    }

    @Override
    public String toFancyString(){
        return super.toFancyString().replaceFirst("Node", "TokenNode");
    }
    @Override
    public String toFullString(){
        return super.toFullString().replaceFirst("Node", "TokenNode");
    }
}
