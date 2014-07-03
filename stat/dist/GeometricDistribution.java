package stat.dist;

import math.IntVector;

/**
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060221
 */
public class GeometricDistribution extends IntegerDistribution {

    /**
     * The parameter of the distribution. Following the model of the
     * waiting time until success under repeated trials of a Bernoulli
     * distribution, this parameter represents the probability of
     * success on a single trial.
     */
    private double p;

    public GeometricDistribution(double p) {
	if ((p < 0.0) || (p > 1.0)) {
	    throw new IllegalArgumentException("Parameter must be between zero and one, inclusive.");
	} else {
	    this.p = p;
	}
    }

    public double probability(int x) {
	if (x <= 0) {
	    return 0.0;
	} else {
	    return p * Math.pow(1 - p, x - 1);
	}
    }

    public double logProbability(int x) {
	if (x <= 0) {
	    return Double.NEGATIVE_INFINITY;
	} else {
	    return Math.log(p) + (x - 1) * Math.log(1 - p);
	}
    }

    public double distribution(int x) {
	return 1 - Math.pow(1 - p, x);
    }

    public int quantile(double q) {
	return (int) Math.ceil(Math.log(1 - q) / Math.log(1 - p));
    }

    public int random() {
	// May be faster to use quantile(Math.random()).

	int n = 0;

	do {
	    ++n;
	} while (Math.random() >= p);

	return n;
    }

    public double min() {
	return 1;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	return 1 / p;
    }

    public double variance() {
	return (1 - p) / (p * p);
    }

    /**
     * Given a sample, assumed to be independent and identically
     * distributed according to a geometric distribution, returns a
     * new GeometricDistribution representing the maximum likelihood
     * estimate of the distribution of the observations. The MLE of
     * the parameter p is the inverse of the sample average of the
     * observations.
     */
    public static GeometricDistribution estimate(IntVector x) {
	return new GeometricDistribution(1 / x.mean());
    }

}
