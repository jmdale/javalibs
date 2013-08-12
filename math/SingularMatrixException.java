
package math;

/**
 * An exception thrown when a matrix is discovered to be singular
 * (non-invertible).
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20041029
 */
public class SingularMatrixException extends Exception {

    public SingularMatrixException() {

	super();

    }

    public SingularMatrixException(String message) {

	super(message);

    }

    public SingularMatrixException(String message, Throwable cause) {

	super(message, cause);

    }

    public SingularMatrixException(Throwable cause) {

	super(cause);

    }

}
