package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Equation.Token.Type;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import java.util.HashMap;
import java.util.Random;

public class InBuiltFunction extends Function {
    public static HashMap<String, InBuiltFunction> FUNCTIONS = new HashMap<String, InBuiltFunction>(){{
        ;
    }};

    public InBuiltFunction(){
        this(null, null, null);
    }

    public InBuiltFunction(String pVal, String pHelp, String pSyntax){
        super(pVal, pHelp, pSyntax);
    }

    public static double exec(String fName, EquationSystem pEq, Node pNode) throws NotDefinedException{
        if(FUNCTIONS.get(fName) == null)
            throw new NotDefinedException("There is no InBuiltFunction '" + fName + "' defined in FUNCTIONS!");
        return FUNCTIONS.get(fName).exec(pEq, pNode);
    }
    @Override
    public double exec(EquationSystem pEq, Node pNode) throws NotDefinedException, InvalidArgsException {
        switch(fName) {
            case "+":
                return pNode.get(0).eval(pEq) + pNode.get(1).eval(pEq);
            case "-":
                return pNode.get(0).eval(pEq) - pNode.get(1).eval(pEq);
            case "*":
                return pNode.get(0).eval(pEq) * pNode.get(1).eval(pEq);
            case "/":
                return pNode.get(0).eval(pEq) / pNode.get(1).eval(pEq);
            case "^":
                return Math.pow(pNode.get(0).eval(pEq), pNode.get(1).eval(pEq)); // not sure this works
            default: 
                double[] args = evalNode(pEq, pNode);
                switch(fName){
                    case "sin":
                        return Math.sin(args[0]);
                    case "cos":
                        return Math.cos(args[0]);
                    case "tan":
                        return Math.tan(args[0]);

                    case "csc":
                        return 1D / Math.sin(args[0]);
                    case "sec":
                        return 1D / Math.cos(args[0]);
                    case "cot":
                        return 1D / Math.tan(args[0]);


                    case "sinh":
                        return Math.sinh(args[0]);
                    case "cosh":
                        return Math.cosh(args[0]);
                    case "tanh":
                        return Math.tanh(args[0]);

                    case "asin":
                        return Math.asin(args[0]);
                    case "acos":
                        return Math.acos(args[0]);
                    case "atan":
                        return Math.atan(args[0]);

                    case "abs":
                        return Math.abs(args[0]);
                    case "ceil":
                        return Math.ceil(args[0]);
                    case "floor":
                        return Math.floor(args[0]);
                    case "hypot":
                        return Math.hypot(args[0], args[1]);
                    case "ln":
                        return Math.log(args[0]);
                    case "log":
                        return Math.log10(args[0]);

                    case "round":
                        return Math.round(args[0]);
                    case "sqrt":
                        return Math.sqrt(args[0]);
                    case "degr":
                        return Math.toDegrees(args[0]);
                    case "radi":
                        return Math.toRadians(args[0]);


                    case "randint": case "randomint": case "ri":
                        if(args.length == 0) return new Random().nextInt(100);
                        if(args.length == 1) return new Random().nextInt((int)args[0]);
                        if(args.length == 2) return new Random().nextInt((int)args[1]) + args[0];
                        Print.printw(fName + " takes 0, 1, or 2 params. Returning 0 instead.");
                        return 0;

                    case "rand": case "random": case "randd": case "randomdouble": case "r":
                        if(args.length == 1) return new Random().nextDouble() * args [0];
                        if(args.length == 2) return (new Random().nextDouble() + args[0]) * args[1];
                        if(args.length == 0) return Math.random();
                        if(args.length != 0)
                            Print.printw(fName + " takes 0, 1, or 2 params. Returning a random num from 0-1 instead.");
                        

                    case "fac":
                        double ret = 1;
                        for(int x = 1; x <= (int)args[0]; x++)
                            ret *= x;
                        return ret;

                    default:
                        throw new NotDefinedException("InBuiltFunction " + this + " doesn't have a defined way to compute it!");
            }
        }
    }
    @Override
    public String toString(){
        return "InBuiltFunction: '" + fName + "'";
    }

    @Override
    public String toFancyString(){
        throw new NotDefinedException();
    }

    @Override
    public String toFullString(){
        throw new NotDefinedException();
    }

}