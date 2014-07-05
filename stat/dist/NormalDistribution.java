package stat.dist;

import math.DoubleVector;

/**
 * A normal distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class NormalDistribution extends DoubleDistribution {

    private static final StandardNormalDistribution STANDARD_NORMAL = new StandardNormalDistribution();

    private double mean;
    private double standardDeviation;
    private double logStandardDeviation;

    /**
     * Creates a new normal distribution with the given mean and
     * standard deviation.
     */
    public NormalDistribution(double mean, double standardDeviation) {
	assert (standardDeviation > 0):
	    "Standard deviation must be positive: " + standardDeviation + ".";

	this.mean = mean;
	this.standardDeviation = standardDeviation;
	this.logStandardDeviation = Math.log(standardDeviation);
    }

    public String toString() {
	return "Normal(" + mean + ", " + standardDeviation + ")";
    }

    /**
     * Computes the Z-value of a given value x with respect to this
     * distribution. In other words, converts the value of x under
     * this distribution to the corresponding value z under a standard
     * normal distribution.
     */
    public double standardize(double x) {
	return (x - mean) / standardDeviation;
    }

    public double density(double x) {
	return STANDARD_NORMAL.density(standardize(x)) / standardDeviation;
    }

    public double logDensity(double x) {
	return STANDARD_NORMAL.logDensity(standardize(x)) - logStandardDeviation;
    }

    /**
     * Computes the distribution function using a formula based on the
     * {@link math.Lib#erf(double) error function}.
     *
     * @see <a href="http://mathworld.wolfram.com/NormalDistribution.html">MathWorld</a>
     */
    public double distribution(double x) {
	return STANDARD_NORMAL.distribution(standardize(x));
    }

    public double quantile(double p) {
	return mean + standardDeviation * STANDARD_NORMAL.quantile(p);
    }

    public double random() {
	return mean + standardDeviation * STANDARD_NORMAL.random();
    }

    public double min() {
	return Double.NEGATIVE_INFINITY;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	return mean;
    }

    public double standardDeviation() {
	return standardDeviation;
    }

    public double variance() {
	return standardDeviation * standardDeviation;
    }

    public NormalDistribution estimateNew(double[] x, double[] w) {
	return estimate(x, w);
    }

    /**
     * Estimates a NormalDistribution from the given samples using
     * maximum likelihood: the mean of the distribution is the sample
     * mean, and the standard deviation of the distribution is the
     * sample standard deviation. (Strictly speaking, we don't use
     * maximum likelihood; the sample variance calculated in the
     * DoubleVector class has N-1 rather than N (the number of data
     * points) in the denominator. Using N would be true maximum
     * likelihood, but standard practice seems to be to use N-1, which
     * gives an unbiased estimate of the variance.)
     */
    public static NormalDistribution estimate(DoubleVector x) {
	return new NormalDistribution(x.mean(), x.standardDeviation());
    }

    /**
     * Do a weighted ML estimate; x are the data, w are the weights.
     */
    public static NormalDistribution estimate(double[] x, double[] w) {
	double sum = 0;
	double norm = 0;

	for (int i = 0; i < x.length; ++i) {
	    sum += w[i] * x[i];
	    norm += w[i];
	}

	double mean = sum / norm;

	sum = 0;

	for (int i = 0; i < x.length; ++i) {
	    double err = x[i] - mean;
	    sum += w[i] * err * err;
	}

	double stdDev = Math.sqrt(sum / norm);

	return new NormalDistribution(mean, stdDev);
    }

}
