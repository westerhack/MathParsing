package Math.Equation.Exception;
public class InvalidArgsException extends MathException {
    public InvalidArgsException(){
        super();
    }
    public InvalidArgsException(String cause){
        super(cause);
    }
}