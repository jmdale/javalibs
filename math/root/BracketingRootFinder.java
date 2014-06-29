package math.root;

import math.Function;

/**
 * Abstract class representing a root-finding method which searches
 * within an interval within which the function crosses zero.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070816
 */
public abstract class BracketingRootFinder extends RootFinder {

    /**
     * Lower bound for the region containing the root.
     */
    protected double min;

    /**
     * Upper bound for the region containing the root.
     */
    protected double max;

    protected BracketingRootFinder(Function f, double min, double max) {
	super(f);

	if (Double.isInfinite(min) || Double.isInfinite(max)) {
	    throw new IllegalArgumentException("Initial root bracket must be bounded: " + min + ", " + max + ".");
	}

	if (max <= min) {
	    throw new IllegalArgumentException("min must be less than max: " + min + ", " + max + ".");
	}

	this.min = min;
	this.max = max;
    }

    /**
     * Adjusts the root bracket defined by the min and max fields so
     * that f(min) and f(max) have opposite signs. Assuming that f is
     * continuous on the interval [min, max], this guarantees that
     * there is a root within the bracket.
     */
    public void bracket() {
	double fMin = f.apply(min);
	double fMax = f.apply(max);
	int i = 0;

	while (fMin * fMax > 0) { // while fMin and fMax have the same sign
	    if (Math.abs(fMin) < Math.abs(fMax)) {
		min -= 1.6 * (max - min);
		fMin = f.apply(min);
	    } else {
		max += 1.6 * (max - min);
		fMax = f.apply(max);
	    }

	    ++i;

	    if (i >= 50) {
		throw new IllegalStateException("Could not find a bracket.");
	    }
	}
    }

}
