package West.Math.Set.Node;
import West.Math.Set.Collection;
import java.util.ArrayList;
import West.Math.Equation.Equation;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Token;
import West.Math.Exception.NotDefinedException;
import java.util.HashMap;
import West.Math.MathObject;
import java.lang.Comparable;
/**
 * TODO: JAVADOC
 * 
 * @author Sam Westerman
  * @version 0.89
 * @since 0.75
 */
public class EquationNode extends Node<EquationNode.Comparator, EquationNode> implements MathObject{
    private interface TokenObj {
        public boolean checkBounds(Double val1, Double val2);
    }
    public static class Comparator{
        public final String SYMBOL;
        public final TokenObj TOKENOBJ;
        public Comparator(String s, TokenObj t){
            SYMBOL = s;
            TOKENOBJ = t;
        }
        public String toString(){
            return SYMBOL;
        }
        public boolean isBool(){
            return BOOLEANS.containsKey(SYMBOL);
        }
        public boolean isComp(){
            return COMPARATOR.containsKey(SYMBOL);
        }
    }

    public static final HashMap<String, Comparator> COMPARATOR = new HashMap<String, Comparator>()
    {{
       put("<=", new Comparator("<=", (d1, d2) -> d1 <= d2));
       put(">=", new Comparator(">=", (d1, d2) -> d1 >= d2));
       put("!=", new Comparator("!=", (d1, d2) -> !d1.equals(d2)));
       put("<",  new Comparator("<",  (d1, d2) -> d1 < d2));
       put(">",  new Comparator(">",  (d1, d2) -> d1 > d2));
       put("=",  new Comparator("=",  (d1, d2) -> d1.equals(d2)));
       put("≣",  new Comparator("≣",  (d1, d2) -> d1.equals(d2))); //from here down are backups
       put("≠",  new Comparator("≠",  (d1, d2) -> !d1.equals(d2)));
       put("≥",  new Comparator("≥",  (d1, d2) -> d1 >= d2));
       put("≤",  new Comparator("≤",  (d1, d2) -> d1 <= d2));
       // put(null, null);
   }};

   public static boolean test(Double a, Double b){
    return false;
   }
    public static final HashMap<String, Comparator> BOOLEANS = new HashMap<String, Comparator>()
    {{
      put("||", new Comparator("||", (d1, d2) -> d1.equals(1D) || d2.equals(1D)));
      put("&&", new Comparator("&&", (d1, d2) -> d1.equals(1D) && d2.equals(1D)));
      put("^^", new Comparator("^^", (d1, d2) -> d1.equals(1D) ^  d2.equals(1D)));
      put("∨", new Comparator("∨", (d1, d2) -> d1.equals(1D) ||  d2.equals(1D))); //backup, OR
      put("∧", new Comparator("∧", (d1, d2) -> d1.equals(1D) &&  d2.equals(1D))); //backup, AND
      put("⊻", new Comparator("⊻", (d1, d2) -> d1.equals(1D) ^  d2.equals(1D))); //backup, XOR
      put("", new Comparator("", (d1, d2) -> true)); //this one should just say go on to the next one
    }};

    public EquationNode(){
        super();
        setToken(BOOLEANS.get(""));
    }

    public EquationNode(Node pCollection) {
        super();
        elements.add(pCollection);
        setToken(BOOLEANS.get(""));
    }
    public EquationNode(Comparator pComp) {
        setToken(pComp);
    }


    public <C extends Comparable<C>> boolean isInBounds(HashMap<String, Double> vars, String toEval){
        if(token.SYMBOL.isEmpty()){
            assert size() == 1 : toFancyString();
            return ((EquationNode)get(0)).isInBounds(vars, toEval);
        }
        assert size() == 2;
        assert token.isBool() || token.isComp();
        Double d0, d1;
        if(token.isBool()){
            d0 = ((EquationNode)get(0)).isInBounds(vars, toEval) ? 1D : 0D;
            d1 = ((EquationNode)get(1)).isInBounds(vars, toEval) ? 1D : 0D;
        } else {
            d0 = ((TokenNode)getCSD().get(0)).eval((EquationSystem.fromHashMap(vars))).get("**TEMP**"); 
            d1 = ((TokenNode)getCSD().get(1)).eval((EquationSystem.fromHashMap(vars))).get("**TEMP**"); 
            //its **TEMP** because every one TokenNode
            //list starts with an empty function.
        }
        return token.TOKENOBJ.checkBounds(d0, d1);
    }

    public static Comparator getBool(String s){
        return BOOLEANS.get(s);
    }

    public static Comparator getComp(String s){
        return COMPARATOR.get(s);
    }

    public void addED(EquationNode en) { // addD, but stops at a non EquationNode
        assert en != null : "Cannot addDepth null Nodes!";
        if(size() <= 0){
            addE(en);
        } else {
            if(get(-1).get(-1) instanceof TokenNode){
                addE(en);
                while(size() > 1){
                    EquationNode eqn = (EquationNode)pop(0);
                    get(-1).add(eqn);
                }
            }
            else
                ((EquationNode)get(-1)).addED(en);
        }
    }

    public void addBD(EquationNode en) { //addD, but stops @ a non-BOOLEAN EquationNode
        assert en != null : "Cannot addDepth null Nodes!";
        if(size() <= 0)
            addE(en);
        else {
            assert get(-1) instanceof EquationNode;
            if(!((Comparator)get(-1).token()).isBool())
                addE(en);
            else
                ((EquationNode)get(-1)).addBD(en);
        }
    }
    public void addCD(Node en) { //addD, but stops @ a TokenNode
        assert en != null : "Cannot addDepth null Nodes!";
        if(size() <= 0)
            addE(en);
        else {
            if(get(-1) instanceof TokenNode)
                addE(en);
            else
                ((EquationNode)get(-1)).addCD(en);
        }
    }

    public EquationNode getCSD(){ // get topmost token.
        assert size() != 0;
        if(get(-1) instanceof TokenNode)
            return this;
        return ((EquationNode)get(0)).getCSD();
    }


    private static boolean checkForNullEquations(ArrayList<Equation> pEquations){
        for(Equation t : pEquations)
            if(t == null)
                return false;
        return true;
    }

  
    @Override
    public String toString(){
        return "Equaiton"+super.toString();
    }
    @Override
    public EquationNode copy(){
        return (EquationNode)new EquationNode(token).addAllN(elements);
    }

    public EquationNode setToken(String pStr){
        return (EquationNode)super.setToken(BOOLEANS.containsKey(pStr)? BOOLEANS.get(pStr) : COMPARATOR.get(pStr));
    }
  
    @Override
    public EquationNode setToken(Comparator pEquation){
        return (EquationNode)super.setToken(pEquation);
    }
    @Override
    public String toFancyString(){
        return super.toFancyString().replaceFirst("Node", "EquationNode");
    }
    @Override
    public String toFullString(){
        return super.toFullString().replaceFirst("Node", "EquationNode");
    }
 }