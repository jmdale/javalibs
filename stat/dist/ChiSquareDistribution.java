package stat.dist;

import static math.GammaFunction.lgamma;

/**
 * The chi-square distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class ChiSquareDistribution extends DoubleDistribution {

    /**
     * The number of degrees of freedom.
     */
    private int df;

    /**
     * The constant part of the log density function. This must be
     * recomputed if df changes.
     */
    private double logNorm;

    public ChiSquareDistribution(int df) {
	assert (df > 0): "df <= 0: " + df;

	this.df = df;
	this.logNorm = df * Math.log(2) / 2 + lgamma(df / 2);
    }

    public String toString() {
	return "X^2(" + df + ")";
    }

    public double density(double x) {
	return Math.exp(logDensity(x));
    }

    public double logDensity(double x) {
	return (0.5 * df - 1) * Math.log(x) - x / 2 - logNorm;
    }

    public double distribution(double x) {
	throw new UnsupportedOperationException();
    }

    public double min() {
	return 0;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	return df;
    }

    public double variance() {
	return 2 * df;
    }

}
