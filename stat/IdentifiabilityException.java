package stat;

/**
 * An exception thrown when a statistical model is found to be
 * unidentifiable.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060722
 */
public class IdentifiabilityException extends Exception {

    public IdentifiabilityException() {
	super();
    }

    public IdentifiabilityException(String message) {
	super(message);
    }

    public IdentifiabilityException(String message, Throwable cause) {
	super(message, cause);
    }

    public IdentifiabilityException(Throwable cause) {
	super(cause);
    }

}
