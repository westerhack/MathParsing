package Math;

import Math.Exception.*;
import Math.Equation.*;

/**
 * A tester class for the whole equation
 * @author Sam Westerman
 * @version 0.6
 * @since 0.1
 */
public class Tester {

    /**
     * The main function for the Math package.
     * @param args The arguemnts passed in - usually through the command line
     * @throws NotDefinedException Thrown when the first value isn't equal to "--e" of --f 
     */
    public static void main(String[] args) throws NotDefinedException {
        EquationSystem eqsys = new EquationSystem();
        if(args.length == 0) {
            eqsys.add("y = graph('eq:x','eq:cosx')");
            eqsys.add("graph", new CustomFunction("graph"));
            // eqsys.add("y = 1 + sin(x) + c");
            // eqsys.add("x = (9.45 + alpha ^ theta) / 2");
            // eqsys.add("theta = 2 * c + alpha");
            // eqsys.add("alpha = 4 + c");
            // eqsys.add("c = 0 - 9");
            // 1 + sin((9.45 + (4 - 9) ^ (2 * - 9 + 4 - 9))/2) - 9
        } else {
            eqsys = new EquationSystem();
            if(args.length == 1) {
                eqsys = new EquationSystem().add(new Equation().add(args[0]));
            } else if(args.length > 1) {
                int i = -1;
                char type = ' ';
                if(!args[0].equals("--f") && !args[0].equals("--e"))
                    throw new NotDefinedException("first value has to be --f, or --e");
                while(i < args.length - 1) { //args.length is String.
                    i++;
                    if(args[i].equals("--f")) {type = 'f'; continue;}
                    if(args[i].equals("--e")) {type = 'e'; continue;}
                    if (type == 'f') {
                        try {
                            eqsys.add(args[i].split(":")[0], new CustomFunction(args[i].split(":")[1])); //fix me.
                        } catch(NumberFormatException err) {
                            Print.printw("Syntax: FUNCNAME:FUNC.val() (" + args[i] + ")");
                        } catch(ArrayIndexOutOfBoundsException err) {
                            Print.printw("Syntax: FUNCNAME:FUNC.val() (" + args[i] + ")");
                        }
                    } else if (type == 'e') {
                        eqsys.add(new Equation().add(args[i]));
                    }
                }
            }
        }
        Print.print(eqsys.toFancyString(1));
        Print.printi("RESULT:", eqsys.eval("y"));
    }


}