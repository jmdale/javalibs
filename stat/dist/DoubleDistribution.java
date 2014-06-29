package stat.dist;

import math.DoubleMatrix;
import math.DoubleVector;
import math.Function;
import math.root.BisectionRootFinder;

/**
 * A probability distribution over the doubles.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070816
 */
public abstract class DoubleDistribution extends Distribution {

    /**
     * Computes the density function of this distribution at a given
     * value.
     */
    public abstract double density(double x);

    /**
     * Computes the natural logarithm of the density function of this
     * distribution. The default implementation simply returns the
     * result of calling {@link java.lang.Math#log(double) Math.log}
     * on the value returned by the {@link #density(double) density}
     * method. However, for some distributions, the natural logarithm
     * of the density function can (and should) be computed in a much
     * more efficient manner.
     */
    public double logDensity(double x) {
	return Math.log(density(x));
    }

    /**
     * Computes the cumulative distribution function of this
     * distribution, the probability that a random variable with this
     * distribution takes on a value less than or equal to a given
     * value.
     */
    public abstract double distribution(double x);

    /**
     * Computes the quantiles of this distribution. For continuous
     * distributions, the quantile function is the inverse of the
     * {@link #distribution cumulative distribution function}. The
     * default implementation of this method uses a generic root
     * finding method to solve the equation distribution(x) = p for x.
     */
    public double quantile(double p) {
	final double pp = p;

	Function f = new Function() {
		public double apply(double x) {
		    return distribution(x) - pp;
		}
	    };

	double min = this.mean() - 3 * this.standardDeviation();
	double max = this.mean() + 3 * this.standardDeviation();

	BisectionRootFinder brf = new BisectionRootFinder(f, min, max);
	brf.bracket();

	return brf.findRoot(1e-8);
    }

    /**
     * Computes a random variate from this distribution. The default
     * implementation calls {@link java.lang.Math#random()
     * Math.random()} to generate a uniform random variate on [0, 1),
     * then calls the {@link #quantile(double) quantile} method on
     * that value.
     */
    public double random() {
	return quantile(Math.random());
    }

    /**
     * Computes a random sample of the given length from this
     * distribution.
     */
    public DoubleVector random(int n) {
	assert (n > 0): "n must be positive: " + n;

	DoubleVector r = new DoubleVector(n);
	for (int i = 0; i < n; ++i) {
	    r.set(i, random());
	}

	return r;
    }

    /**
     * Computes a random sample from this distribution.
     */
    public DoubleMatrix random(int m, int n) {
	assert ((m > 0) && (n > 0)): "m, n must be positive: " + m + ", " + n;

	DoubleMatrix r = new DoubleMatrix(m, n);
	for (int i = 0; i < m; ++i) {
	    for (int j = 0; j < n; ++j) {
		r.set(i, j, random());
	    }
	}

	return r;
    }

    /**
     * Returns a new distribution of the same type as this one,
     * estimated from the given data: x are observed samples, and w
     * are weights on those samples.
     */
    public DoubleDistribution estimateNew(double[] x, double[] w) {
	throw new UnsupportedOperationException();
    }

}
