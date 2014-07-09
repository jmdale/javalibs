package stat.dist;

import math.IntVector;

/**
 * A distribution of vectors of integers.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20050630
 */
public abstract class IntVectorDistribution {

    /**
     * Computes the probability function of this distribution at a
     * given value.
     */
    public abstract double probability(IntVector x);

    /**
     * Computes the natural logarithm of the probability function of
     * this distribution. The default implementation simply returns
     * the result of calling Math.log on the value returned by the
     * probability method. However, for some distributions, the
     * natural logarithm of the density function can (and should) be
     * computed in a much more efficient manner.
     */
    public double logProbability(IntVector x) {
	return Math.log(probability(x));
    }

    /**
     * Computes a random variate from this distribution.
     */
    public abstract IntVector random();

}
