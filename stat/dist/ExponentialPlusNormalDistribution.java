package stat.dist;

import math.DoubleVector;

/**
 * The distribution of a random variable S = X + Y, where X is
 * distributed exponentially and Y is distributed normally. This class
 * was written to assist with implementing the RMA background
 * subtraction method.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class ExponentialPlusNormalDistribution extends DoubleDistribution {

    private static final StandardNormalDistribution SND = new StandardNormalDistribution();

    /**
     * The mean of the normal distribution.
     */
    private double mu;

    /**
     * The standard deviation of the normal distribution.
     */
    private double sigma;

    /**
     * The rate parameter of the exponential distribution.
     */
    private double alpha;

    /**
     * Creates a new ExponentialPlusNormalDistribution with the given
     * mean, standard deviation, and rate parameters.
     */
    public ExponentialPlusNormalDistribution(double mu, double sigma, double alpha) {
	assert (sigma > 0): "sigma <= 0: " + sigma;
	assert (alpha > 0): "alpha <= 0: " + alpha;

	this.mu = mu;
	this.sigma = sigma;
	this.alpha = alpha;
    }

    public String toString() {
	return "ExponentialPlusNormal(" + mu + ", " + sigma + ", " + alpha + ")";
    }

    public double density(double s) {
	double p = alpha * Math.exp(-alpha * (s - mu) + 0.5 * sigma * sigma * alpha * alpha);
	double z = (s - mu - sigma * sigma * alpha) / sigma;

	return p * SND.distribution(z);
    }

    /**
     * Computes the expected value of the exponential random variable
     * X, conditional on the given observation of S = X + Y.
     */
    public double expectedExponential(double s) {
	double a = s - mu - sigma * sigma * alpha;
	double z = a / sigma;

	/*
	 * This could probably be sped up. My current implementation
	 * of the standard normal distribution function is basically a
	 * polynomial multiplied by a standard normal density. So in
	 * this expression, we could factor out the densities
	 * (including two calls to Math.exp()!!) and just compute the
	 * polynomial.
	 */
	return a + sigma * SND.density(z) / SND.distribution(z);
    }

    /**
     * Computes the expected value of the normal random variable Y,
     * conditional on the given observation of S = X + Y.
     */
    public double expectedNormal(double s) {
	/*
	 * I have independently derived E[Y|S=s] and E[X|S=s] to
	 * verify that the relation E[Y|S=s] + E[X|S=s] = s holds.
	 */
	return s - expectedExponential(s);
    }

    public double distribution(double s) {
	throw new UnsupportedOperationException("Not yet implemented.");
    }

    public double quantile(double p) {
	throw new UnsupportedOperationException("Not yet implemented.");
    }

    public double min() {
	return Double.NEGATIVE_INFINITY;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	throw new UnsupportedOperationException("Not yet implemented.");
    }

    public double variance() {
	throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Estimates an ExponentialPlusNormalDistribution from x. A
     * KernelDensity is first estimated from x. The mode of this
     * kernel density is taken as the mean of the normal part of the
     * new distribution. The standard deviation of the normal part is
     * estimated as the root-mean-square error of the observations in
     * x which are below the mean. The scale of the new distribution
     * is estimated by fitting an exponential distribution to the
     * observations in x which are above the mean.
     */
    public static ExponentialPlusNormalDistribution estimate(DoubleVector x) {
	// In the R code, they actually take the mode once, then take
	// the mode of the lower tail to get the final estimate of mu.
	KernelDensity kd = KernelDensityEstimator.estimate(x, 16384);
	double mu = kd.mode();

	DoubleVector lowerTail = x.lessThan(mu);
	DoubleVector upperTail = x.greaterThan(mu);

	// The sqrt(2) / 0.85 here is in the original code from the R
	// affy package. I'm not sure why; maybe because we got the SD
	// from the lower tail only (i.e. half the data)?
	double sigma = lowerTail.standardDeviation(mu) * Math.sqrt(2) / 0.85;

	// As far as I can tell from the R code, this is a rate, not a
	// scale. I would know for sure if I could remember the
	// details of the derivation, but I can't.
	double alpha = 1 / upperTail.subtract(mu).mean();

	return new ExponentialPlusNormalDistribution(mu, sigma, alpha);
    }

}
