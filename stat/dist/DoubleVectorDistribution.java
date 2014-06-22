package stat.dist;

import math.DoubleMatrix;
import math.DoubleVector;

/**
 * A multivariate distribution; i.e., a distribution over vectors of
 * doubles.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20050420
 */
public abstract class DoubleVectorDistribution {

    /**
     * Computes the density function of this distribution at a given
     * value.
     */
    public abstract double density(DoubleVector x);

    /**
     * Computes the natural logarithm of the density function of this
     * distribution. The default implementation simply returns the
     * result of calling {@link java.lang.Math#log(double) Math.log}
     * on the value returned by the {@link #density(double) density}
     * method. However, for some distributions, the natural logarithm
     * of the density function can (and should) be computed in a much
     * more efficient manner.
     */
    public double logDensity(DoubleVector x) {
	return Math.log(density(x));
    }

    /**
     * Returns a random vector of doubles sampled from this
     * distribution.
     */
    public abstract DoubleVector random();

    /**
     * Returns the mean vector of this distribution.
     */
    public abstract DoubleVector mean();

    /**
     * Returns the variance-covariance matrix of this distribution.
     */
    public abstract DoubleMatrix covariance();

}
