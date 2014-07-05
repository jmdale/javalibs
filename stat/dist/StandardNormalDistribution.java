package stat.dist;

/**
 * A standard normal distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20080804
 */
public class StandardNormalDistribution extends DoubleDistribution {

    private BoxMullerGenerator bm = new BoxMullerGenerator();

    public String toString() {
	return "Normal(" + 0d + ", " + 1d + ")";
    }

    public double density(double x) {
	return Math.exp(-(x * x) / 2) / math.Lib.SQRT_2PI;
    }

    /**
     * Computes the logarithm of the density function.
     */
    public double logDensity(double x) {
	return -(math.Lib.LN_2PI + (x * x)) / 2;
    }

    /* Constants used in computing distribution function. */
    private static final double a1 = 0.31938153;
    private static final double a2 = -0.356563782;
    private static final double a3 = 1.7814779372;
    private static final double a4 = -1.821255978;
    private static final double a5 = 1.330274429;

    public double distribution(double x) {
	/*
	 * I initially implemented the normal CDF using this call to
	 * the error function:
	 *
	 *     return (1 + math.Lib.erf(x / math.Lib.SQRT2)) / 2;
	 *
	 * However, my implementation of the error function in
	 * math.Lib uses the lowerIncompleteGammaOverGamma function,
	 * which was very broken.
	 *
	 * The implementation below, which comes from Besset's
	 * "Object-Oriented Implementation of Numerical Methods" and
	 * is based on a formula from Abramovitz and Stegun's
	 * "Handbook of Mathematical Functions", appears to be fairly
	 * accurate and stable.
	 */

	if (x > 0) {
	    return 1 - distribution(-x);
	} else {
	    double r = 1 / (1 - 0.2316419 * x);
	    double s = r * (a1 + r * (a2 + r * (a3 + r * (a4 + r * a5))));

	    return s * Math.exp(-0.5 * x * x) / math.Lib.SQRT_2PI;
	}
    }

    /**
     * Computes the quantile function using Wichura's algorithm.
     *
     * @see <a href="http://links.jstor.org/sici?sici=0035-9254%281988%2937%3A3%3C477%3AAA2TPP%3E2.0.CO%3B2-U">Wichura, M.J. Algorithm AS 241: The Percentage Points of the Normal Distribution. Applied Statistics, Vol. 37, No. 3. (1988), pp. 477-484.</a>
     * @see <a href="http://lib.stat.cmu.edu/apstat/241">Applied Statistics Algorithm 241</a>
     */
    public double quantile(double p) {
	return PPND16.ppnd16(p);
    }

    public double random() {
	return bm.next();
    }

    public double min() {
	return Double.NEGATIVE_INFINITY;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	return 0;
    }

    public double standardDeviation() {
	return 1;
    }

    public double variance() {
	return 1;
    }

    public static void main(String[] args) {
	StandardNormalDistribution snd = new StandardNormalDistribution();

	for (int i = -6000; i <= 6000; ++i) {
	    double x = i / 1000.0;

	    System.out.format("%f\t%f\t%f\t%f\n",
			      x, snd.logDensity(x), snd.density(x), Math.exp(snd.logDensity(x)));
	}
    }

}
