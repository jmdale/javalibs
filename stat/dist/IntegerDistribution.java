package stat.dist;

import math.IntVector;

/**
 * A probability distribution over the integers.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20041110
 */
public abstract class IntegerDistribution extends Distribution {

    /**
     * Computes the probability function of this distribution at a
     * given value.
     */
    public abstract double probability(int x);

    /**
     * Computes the natural logarithm of the probability function of
     * this distribution. The default implementation simply returns
     * the result of calling Math.log on the value returned by the
     * probability method. However, for some distributions, the
     * natural logarithm of the density function can (and should) be
     * computed in a much more efficient manner.
     */
    public double logProbability(int x) {
	return Math.log(probability(x));
    }

    /**
     * Computes the cumulative distribution function of this
     * distribution, the probability that a random variable with this
     * distribution takes on a value less than or equal to a given
     * value.
     */
    public abstract double distribution(int x);

    /**
     * Computes the quantile function of this distribution. For a
     * discrete distribution, the quantile function is defined as
     * follows: If distribution(x) == p, then quantile(p) ==
     * x. Otherwise, if distribution(x - 1) < p and distribution(x) >
     * p, then quantile(p) == x.
     *
     * An alternative way of phrasing this (I think) is that
     * quantile(p) is the smallest x such that distribution(x) >= p.
     */
    public abstract int quantile(double p);

    /**
     * Computes a random variate from this distribution.
     */
    public abstract int random();

    /**
     * Computes a random sample of the given length from this
     * distribution.
     */
    public IntVector random(int n) {
	assert (n > 0): "n must be positive: " + n;

	IntVector r = new IntVector(n);
	for (int i = 0; i < n; i += 1) {
	    r.set(i, random());
	}

	return r;
    }

}
