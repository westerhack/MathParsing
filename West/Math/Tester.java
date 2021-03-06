package West.Math;
import West.Math.Equation.*;
import West.Math.Display.*;
import West.Math.Display.GraphGUI.*;
import West.Math.Equation.Function.*;
import West.Math.Set.*;
import West.*;

import java.util.List;
import java.util.ArrayList;
/**
 * A tester class for the whole equation
 * @author Sam Westerman
 * @version 1.1
 * @since 0.1
 */
public class Tester {
    /**
     * The main function for the West.Math.package.
     * @param args The arguemnts passed in - usually through the command line
     * @throws IllegalArgumentException Thrown when the first value isn't equal to "--e" of --f 
     */
    public static void main(String[] args) throws IllegalArgumentException {
        if(args.length == 0){
             args = new String[]{"y={x:n:(n=x＞2)@(–10,10,1)}"};
             // args = new String[]{"y={x | x＜–2 ∨ x＞2 @[–10,10,1]}"};
        }


        if(args.length == 1 && args[0].equals("--g")){
            GraphGUI gg = new GraphGUI();
        } else {
            double PI = Math.PI;

            String indep = "";
            ArrayList<String> dep = new ArrayList<String>();

            GraphComponents.GraphTypes gtype = GraphComponents.GraphTypes.XY;

            int[] winbounds = new int[]{810, 810};
            double[] eqBounds = new double[]{-10, -10, 10, 10};
            double[] step = new double[]{1000};

            int prefix = 0;

            EquationSystem eqsys = new EquationSystem();
            if(args.length == 1) {
                eqsys = new EquationSystem().add(new Equation().add(args[0]));
            } else if(args.length > 1) {
                int i = -1;
                char type = 'e';
                //  if(!args[0].contains("--"))
                    //  throw new IllegalArgumentException("first value has to start with '--'");
                while(i < args.length - 1) { // args.length is String.
                    i++;
                    if(args[i].matches("--f(unc)?")) {type = 'f'; continue;}
                    if(args[i].matches("--e(q)?")) {type = 'e'; continue;}
                    if(args[i].matches("--i(dep)?")) {type = 'i'; continue;}
                    if(args[i].matches("--d(ep)?")) {type = 'd'; continue;}
                    if(args[i].matches("--g(type)?")) {type = 'g'; continue;}
                    if(args[i].matches("--s(tep)?b?(ounds)?")) {type = 's'; continue;}
                    if(args[i].matches("--b(ounds)?")) {type = 'b'; continue;}
                    if(args[i].matches("--[Pp](refix(es)?)?")) {
                        prefix = Character.valueOf(args[i].charAt(2)) == 'p' ? 1: 2;
                        continue;
                    }
                    if (type == 'f') {
                        try {
                            eqsys.add(args[i].split(":")[0], new CustomFunction(args[i].split(":")[1])); // fix me.
                        } catch(NumberFormatException err) {
                            Print.printw("Syntax: FUNCNAME:FUNC.val() (" + args[i] + ")");
                        } catch(ArrayIndexOutOfBoundsException err) {
                            eqsys.add(args[i], new CustomFunction(args[i])); // fix me.
                        }
                    } else if (type == 'e') {
                        eqsys.add(new Equation().add((eqsys.size() == 0 && (args[i].indexOf("=") == -1 || 
                                               args[i].indexOf("=") > 5) ? "y=" : "") + args[i]));
                    } else if (type == 'i'){
                        indep = args[i];
                    } else if (type == 'd'){
                        dep.add(args[i]);
                    } else if (type == 'g'){
                        gtype = args[i].matches("[Pp](olar)?")?
                            GraphComponents.GraphTypes.POLAR :
                            GraphComponents.GraphTypes.XY;
                    } else if (type == 's'){
                        String[] spl = args[i].split(",");
                        assert spl.length == 1 || spl.length == 3 : "Step is 'Step' or 'Min, Max, Step'";
                        try{
                            if(spl.length == 1)
                                step = new double[]{(double)eqsys.evald("b0", new EquationSystem().add("b0="+spl[0]))};
                            else
                                step = new double[]{(double)eqsys.evald("b0", new EquationSystem().add("b0="+spl[0])),
                                                    (double)eqsys.evald("b1", new EquationSystem().add("b1="+spl[1])),
                                                    (double)eqsys.evald("b2", new EquationSystem().add("b2="+spl[2]))};
                        } catch(NumberFormatException exc){
                            System.err.println("Step is 'Step' or 'Min, Max, Step' and all numbers, not '"+args[i]+"'");
                        }
                    } else if (type == 'b'){
                        String[] spl = args[i].split(",");
                        assert spl.length == 4 : "Bounds needs to be 'Min x, Min y, Max x, Max y' and all numbers.";
                        try{
                        eqBounds = new double[]{(double)eqsys.evald("b0",new EquationSystem().add("b0="+spl[0])),
                                                (double)eqsys.evald("b1",new EquationSystem().add("b1="+spl[1])),
                                                (double)eqsys.evald("b2",new EquationSystem().add("b2="+spl[2])),
                                                (double)eqsys.evald("b3",new EquationSystem().add("b3="+spl[3]))};
                        } catch(NumberFormatException exc){
                            System.err.println("Bounds needs to be 'Min x, Min y, Max x, Max y' and all numbers.");
                        }
                    }
                }
            }
            EquationSystem eqsysfinal = eqsys;
            if(dep.isEmpty() && !eqsys.isEmpty()){
                dep = new ArrayList<String>(){{
                    add(eqsysfinal.equations().get(0).subEquations().get(0).get(0).toString());
                    // first equation, first node - which is just (EQUATION) - then first subnode.
                }};
            }
            if(indep.isEmpty()){
                System.out.println(eqsys);
                int pref = prefix;
                dep.forEach(s -> Print.printi("RESULT ("+s+"):", 
                            EquationSystem.appendMetricSuffix((Complex)
                                                              eqsysfinal.eval(s),
                                                              pref)));
            } else {
                String[] depl = new String[dep.size()];
                for(int i = 0; i < dep.size(); i++)
                    depl[i] = dep.get(i);
                eqsys.graph(new GraphComponents(winbounds,
                                                eqBounds,
                                                step,
                                                gtype,
                                                indep,
                                                depl));
            }
        }
    }


}











