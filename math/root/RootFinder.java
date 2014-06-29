package math.root;

import math.Function;

/**
 * Abstract class representing a root-finding method.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070816
 */
public abstract class RootFinder {

    /**
     * The function whose root is sought.
     */
    protected Function f;

    protected RootFinder(Function f) {
	this.f = f;
    }

    public abstract double findRoot(double eps);

    public static double bisect(Function f, double min, double max, double eps) {
	return (new BisectionRootFinder(f, min, max)).findRoot(eps);
    }

    public static double newtonRaphson(Function f, Function df, double init, double eps) {
	return (new NewtonRaphsonRootFinder(f, df, init)).findRoot(eps);
    }

}
