package stat.dist;

import static math.BetaFunction.incompleteBeta;
import static math.GammaFunction.lgamma;

/**
 * The t distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class TDistribution extends DoubleDistribution {

    /**
     * The number of degrees of freedom.
     */
    private int df;

    /**
     * The constant part of the log density function. This must be
     * recomputed if df changes.
     */
    private double logNorm;

    public TDistribution(int df) {
	assert (df > 0): "df <= 0: " + df;

	this.df = df;
	this.logNorm = logNorm(df);
    }

    public String toString() {
	return "T(" + df + ")";
    }

    public double density(double x) {
	return Math.exp(logDensity(x));
    }

    public double logDensity(double x) {
	return logNorm - Math.log(1 + (x * x) / df) * (df + 1.0) / 2.0;
    }

    /*
     * This has been tested against the t distribution CDF in the JSci
     * package (http://jsci.sourceforge.net/). The results seem to
     * agree quite well up to around 10 decimal places.
     */
    public double distribution(double x) {
	if (x < 0) {
	    return 1 - distribution(-x);
	} else {
	    return 1 - 0.5 * incompleteBeta(0.5 * df, 0.5, df / (df + x * x));
	}
    }

    /*
     * The quantile and random methods are currently inherited from
     * the default methods in DoubleDistribution. The quantile method
     * has been tested and seems to work fine (which should imply that
     * random() will work as well).
     *
     * There are alternate strategies in case problems arise:
     *
     * Applied Statistics Algorithm 109 implements the inverse of the
     * incomplete beta function, which could be used for quantiles.
     *
     * random() could be implemented as z / sqrt(x^2 / df), where z
     * has the standard normal distribution, x^2 has the chi-squared
     * distribution with df degrees of freedom, and df is the number
     * of degrees of freedom of the t distribution we want to sample
     * from. However, the benefit of this would basically depend on
     * the efficiency of generating chi-squared random variates.
     */

    public double min() {
	return Double.NEGATIVE_INFINITY;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	/*
	 * According to Stone, the mean of the t distribution with one
	 * degree of freedom is (infinity - infinity), and therefore
	 * undefined. MathWorld, however, states without qualification
	 * that the mean is 0.
	 */
	if (df == 1) {
	    return Double.NaN;
	} else {
	    return 0;
	}
    }

    public double variance() {
	/*
	 * MathWorld states without qualification that the variance of
	 * the t distribution is (df / (df - 2)). Clearly this is
	 * meaningless when df = 1; if we accept, following Stone,
	 * that the mean is undefined for df = 1, then so is the
	 * variance. When df = 2, this would seem to suggest that the
	 * variance is defined and infinite.
	 */
	if (df == 1) {
	    return Double.NaN;
	} else if (df == 2) {
	    return Double.POSITIVE_INFINITY;
	} else {
	    return df / (df - 2.0);
	}
    }

    private static double logNorm(int df) {
	return lgamma((df + 1.0) / 2.0) - lgamma(df / 2.0) - Math.log(Math.PI * df) / 2;
    }

}
