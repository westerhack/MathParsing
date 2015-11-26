package Math.Equation.CustomFunctions;

import Math.Equation.Function;
import Math.Equation.Factors;
import Math.Equation.Node;
import Math.Equation.CustomFunction;


import Math.Equation.Exception.InvalidArgsException;
import Math.Equation.Exception.NotDefinedException;

public class summation extends CustomFunction{
    public static String help = "Add up numbers from START to END with step STEP";
    public static String args = "([START], END, [STEP])";

    /** 
     * Summation from START to END, with step STEP
     * Params: ([START],END,[STEP])
     * if START is omitted, then 0 is used in its place
     * if STEP is omitted, 1 is used in its place
     * @param pFactors      The factors
     * @return The summation of the numbers defined by pNode.
     */
    @Override
    public double exec(Factors pFactors, Node pNode) throws NotDefinedException, InvalidArgsException {
        double[] vals = evalNode(pFactors, pNode);
        if(vals.length == 0 || vals.length > 3)
            throw new InvalidArgsException("ERROR when parsing summation. Syntax: " + args);
        if(vals.length == 1) { vals = new double[]{0,vals[0],1};}
        if(vals.length == 2) { vals = new double[]{vals[0],vals[1],1};}
        double ret = 0;
        for(double x = vals[0]; x <= vals [1]; x+= vals[2]) ret += x; 
        return ret;
    }
}