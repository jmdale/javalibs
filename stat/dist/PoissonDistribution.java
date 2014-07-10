package stat.dist;

/**
 * A Poisson distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class PoissonDistribution extends IntegerDistribution {

    /**
     * The mean of this distribution.
     */
    private double lambda;

    /**
     * The natural logarithm of the mean of this distribution.
     */
    private double logLambda;

    /**
     * An exponential distribution whose rate parameter is equal to
     * lambda. Used in sampling random numbers from this distribution.
     */
    private ExponentialDistribution expDist;

    public PoissonDistribution(double lambda) {
	if (lambda <= 0) {
	    throw new IllegalArgumentException("lambda must be positive: " + lambda + ".");
	}

	this.lambda = lambda;
	this.logLambda = Math.log(lambda);
	this.expDist = new ExponentialDistribution(1 / lambda);
    }

    public double probability(int x) {
	return Math.exp(logProbability(x));
    }

    public double logProbability(int x) {
	return x * logLambda - lambda - math.GammaFunction.lgamma(x + 1);
    }

    public double distribution(int x) {
	if (x < 0) {
	    return 0;
	}

	double t = Math.exp(-lambda);
	double s = t;

	for (int i = 1; i <= x; ++i) {
	    t *= lambda / i;
	    s += t;
	}

	return s;
    }

    public int quantile(double p) {
	int i = 0;
	double t = Math.exp(-lambda);
	double s = t;

	while (s < p) {
	    i += 1;
	    t *= lambda / i;
	    s += t;
	}

	return i;
    }

    public int random() {
	double t = 0;
	int i = 0;

	while (t <= 1) {
	    t += expDist.random();
	    i += 1;
	}

	return i - 1;
    }

    public double min() {
	return 0;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	return lambda;
    }

    public double variance() {
	return lambda;
    }

}
