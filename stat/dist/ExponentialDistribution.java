package stat.dist;

import math.DoubleVector;

/**
 * An exponential distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class ExponentialDistribution extends DoubleDistribution {

    private double scale;
    private double logScale;

    /**
     * Creates a new exponential distribution with the given scale
     * parameter.
     */
    public ExponentialDistribution(double scale) {
	if (scale <= 0) {
	    throw new IllegalArgumentException("scale must be positive: " + scale + ".");
	}

	this.scale = scale;
	this.logScale = Math.log(scale);
    }

    /**
     * Creates a new exponential distribution with scale 1.
     */
    public ExponentialDistribution() {
	this.scale = 1;
	this.logScale = 0;
    }

    /**
     * @return The scale parameter of this distribution.
     */
    public double scale() {
	return scale;
    }

    public double density(double x) {
	return Math.exp(-x / scale) / scale;
    }

    public double logDensity(double x) {
	return -(x / scale + logScale);
    }

    public double distribution(double x) {
	return 1 - Math.exp(-x / scale);
    }

    public double quantile(double p) {
	if ((p < 0) || (p > 1)) {
	    throw new IllegalArgumentException("p must be in [0, 1]: " + p + ".");
	}

	return -scale * Math.log(1 - p);
    }

    public double min() {
	return 0;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	return scale;
    }

    public double variance() {
	return scale * scale;
    }

    public double standardDeviation() {
	return scale;
    }

    /**
     * Estimates an exponential distribution from x using maximum
     * likelihood: the scale parameter of the estimated distribution
     * is the mean of the observed data.
     */
    public static ExponentialDistribution estimate(DoubleVector x) {
	return new ExponentialDistribution(x.mean());
    }

}
