package stat.dist;

/**
 * A binomial distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class BinomialDistribution extends IntegerDistribution {

    private int n;
    private double p;

    public BinomialDistribution(int n, double p) {
	if (n < 1) {
	    throw new IllegalArgumentException("n must be positive: " + n + ".");
	}
	if ((p < 0) || (p > 1)) {
	    throw new IllegalArgumentException("p must be in [0, 1]: " + p + ".");
	}

	this.n = n;
	this.p = p;
    }

    public double probability(int x) {
	if ((x < 0) || (x > n)) {
	    return 0;
	} else {
	    return Math.exp(logProbability(x));
	}
    }

    public double logProbability(int x) {
	if ((x < 0) || (x > n)) {
	    return Double.NEGATIVE_INFINITY;
	}

	double pr = n * Math.log(1 - p);
	double logit = Math.log(p / (1 - p));

	for (int i = 1; i <= x; ++i) {
	    pr += Math.log(((double) (n - (i - 1))) / i) + logit;
	}

	return pr;
    }

    public double distribution(int x) {
	if (x < 0) {
	    return 0;
	} else if (x >= n) {
	    return 1;
	} else {
	    double p = 0;

	    for (int i = 0; i <= x; ++i) {
		p += probability(x);
	    }

	    return p;
	}
    }

    public int quantile(double p) {
	throw new UnsupportedOperationException("Not yet implemented.");
    }

    public int random() {
	int r = 0;

	for (int i = 0; i < n; ++i) {
	    if (Math.random() < p) {
		++r;
	    }
	}

	return r;
    }

    public double min() {
	return 0;
    }

    public double max() {
	return n;
    }

    public double mean() {
	return n * p;
    }

    public double variance() {
	return n * p * (1 - p);
    }

}
