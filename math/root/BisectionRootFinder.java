package math.root;

import math.Function;

/**
 * Finds a root of a function using the bisection method.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070816
 */
public class BisectionRootFinder extends BracketingRootFinder {

    public BisectionRootFinder(Function f, double min, double max) {
	super(f, min, max);
    }

    /**
     * Finds a root using the bisection method. Assumes that min and
     * max bracket a root. Repeatedly halves the bracket until the
     * size of the bracket is less than eps.
     */
    public double findRoot(double eps) {
	double fMin = f.apply(min);
	double fMax = f.apply(max);

	while ((max - min) / (max + min) > eps) {
	    double mid = (min + max) / 2;
	    double fMid = f.apply(mid);

	    if (fMid * fMin > 0) { // fMid has same sign as fMin
		min = mid;
		fMin = fMid;
	    } else {
		max = mid;
		fMax = fMid;
	    }
	}

	return (min + max) / 2;
    }

    public static void main(String[] args) {
	final double d = Double.parseDouble(args[0]);

	if (Double.isInfinite(d) || Double.isNaN(d) || (d < 0)) {
	    System.err.println("Can't take root of " + d + ",");
	} else if (d == 0) {
	    System.out.println(d);
	} else {
	    Function f = new Function(){ public double apply(double x) { return x * x - d; } };
	    double min = 0;
	    double max = Math.max(d, 1);
	    BisectionRootFinder brf = new BisectionRootFinder(f, min, max);

	    brf.bracket();
	    double root = brf.findRoot(1e-10);
	    System.out.println(root * root);
	}
    }

}
