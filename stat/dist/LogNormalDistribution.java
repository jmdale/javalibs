package stat.dist;

import math.DoubleVector;

/**
 * A log-normal distribution.
 *
 * @see <a href="http://mathworld.wolfram.com/LogNormalDistribution.html">MathWorld</a>
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class LogNormalDistribution extends DoubleDistribution {

    private static final StandardNormalDistribution STANDARD_NORMAL = new StandardNormalDistribution();

    private double mean;
    private double standardDeviation;
    private double logStandardDeviation;

    /**
     * Creates a new log-normal distribution with the given mean and
     * standard deviation parameters.
     */
    public LogNormalDistribution(double mean, double standardDeviation) {
	this.mean = mean;
	this.standardDeviation = standardDeviation;
	this.logStandardDeviation = Math.log(standardDeviation);
    }

    public String toString() {
	return "LogNormal(" + mean + ", " + standardDeviation + ")";
    }

    public double density(double x) {
	return STANDARD_NORMAL.density((Math.log(x) - mean) / standardDeviation) / (standardDeviation * x);
    }

    public double logDensity(double x) {
	return STANDARD_NORMAL.logDensity((Math.log(x) - mean) / standardDeviation) - (logStandardDeviation + Math.log(x));
    }

    public double distribution(double x) {
	return STANDARD_NORMAL.distribution((Math.log(x) - mean) / standardDeviation);
    }

    public double quantile(double p) {
	return Math.exp(mean + standardDeviation * STANDARD_NORMAL.quantile(p));
    }

    public double random() {
	return Math.exp(mean + standardDeviation * STANDARD_NORMAL.random());
    }

    public double min() {
	return 0;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	return Math.exp(mean + standardDeviation * standardDeviation / 2);
    }

    public double variance() {
	return Math.exp(2 * mean + standardDeviation * standardDeviation) * (Math.exp(standardDeviation * standardDeviation) - 1);
    }

    /**
     * Returns a new LogNormalDistribution estimated from the given
     * samples x using maximum likelihood. The logarithm of each
     * sample is taken; the mean of the distribution is the mean of
     * the log-scaled samples, and the standard deviation of the
     * distribution is the standard deviation of the log-scaled
     * samples.
     */
    public static LogNormalDistribution estimate(DoubleVector x) {
	x = x.log();
	return new LogNormalDistribution(x.mean(), x.standardDeviation());
    }

}
