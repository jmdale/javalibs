package stat;

/**
 * Miscellaneous utility functions for doing probability and
 * statistics calculations. Ideally, this will be used as a temporary
 * way-station for these functions on their way to more appropriate
 * locations as (non-static) methods in relevant classes.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20050420
 */
public class Lib {

    /**
     * Divides each element of the given array by the sum of the
     * elements of the array, thus "normalizing" the array so that its
     * elements sum to 1.
     *
     * Should be moved into DoubleVector.
     */
    public static void normalize(double[] p) {
	double sp = 0;

	for (int i = 0; i < p.length; ++i) {
	    sp += p[i];
	}

	for (int i = 0; i < p.length; ++i) {
	    p[i] /= sp;
	}
    }

    /**
     * Returns a random integer from {0, ..., n-1} according to the n
     * probabilities in the given array.
     *
     * Should belong to the MultiBernoulliDistribution?
     */
    public static int random(double[] p) {
	double rand = Math.random();
	double accum = 0;

	for (int i = 0; i < p.length; ++i) {
	    accum += p[i];
	    if (rand < accum) {
		return i;
	    }
	}

	throw new IllegalStateException("accum did not exceed rand: " + accum + ", " + rand + "; probabilities may not be properly normalized.");
    }

    /**
     * Returns true if the given number is a valid probability, i.e.,
     * a number between 0 and 1, inclusive.
     */
    public static boolean isProbability(double p) {
	return ((0 <= p) && (p <= 1));
    }

    /**
     * Returns true if the given number is a valid log-probability,
     * i.e., a non-positive number.
     */
    public static boolean isLogProbability(double logP) {
	return (logP <= 0);
    }

}
