package Math.Exception;

/**
 * An error that is thrown when something is trying to be accessed, but it doesn't exist. Differs from 
 * {@link #NotDefinedException} because this one is for accessing Objects, and the other one is for handling Objects.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class DoesntExistException extends MathException {

    /**
     * The empty constructor. Just passes <code>"Define me!"</code> to
     * {@link #DoesntExistException(String) the main constructor}.
     */
    public DoesntExistException() {
        super();
    }

    // Inherits javadoc from MathException
    public DoesntExistException(String cause) {
        super(cause);
    }
}