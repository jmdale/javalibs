package math.root;

import math.Function;

/**
 * A root-finding method which uses the derivative of the function
 * whose root is sought (for example, the Newton-Raphson method).
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070816
 */
public abstract class DerivativeRootFinder extends RootFinder {

    /**
     * The derivative of the function whose root is sought.
     */
    protected Function df;

    protected DerivativeRootFinder(Function f, Function df) {
	super(f);
	this.df = df;
    }

}
