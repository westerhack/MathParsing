package Math.Exception;

/**
 * Thrown when the arguments to a function are not correct for one reason or another
 * 
 * @author Sam Westerman
 * @version 0.67
 * @since 0.1
 * @deprecated didn't realize IllegalArgumentException existed
 */
@Deprecated
public class InvalidArgsException extends MathException {

    /**
     * Default Constructor. Just calls <code>super()</code>.
     * {@link #InvalidArgsException(String) the main constructor}.
     */
    public InvalidArgsException() {
        super();
    }

    // Inherits javadoc from MathException
    public InvalidArgsException(String cause) {
        super(cause);
    }
}