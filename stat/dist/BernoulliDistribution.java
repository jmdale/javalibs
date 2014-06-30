package stat.dist;

/**
 * A Bernoulli distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class BernoulliDistribution extends IntegerDistribution {

    private double p;

    public BernoulliDistribution(double p) {
	if ((p < 0) || (p > 1)) {
	    throw new IllegalArgumentException("p must be in [0, 1]: " + p + ".");
	}

	this.p = p;
    }

    public String toString() {
	return "Bernoulli(" + p + ")";
    }

    public double probability(int x) {
	if (x == 1) {
	    return p;
	} else if (x == 0) {
	    return 1 - p;
	} else {
	    return 0;
	}
    }

    public double distribution(int x) {
	if (x < 0) {
	    return 0;
	} else if (x == 0) {
	    return 1 - p;
	} else {
	    return 1;
	} 
    }

    public int quantile(double p) {
	if ((p <= 0) || (p > 1)) {
	    throw new IllegalArgumentException("p must be in (0, 1]: " + p + ".");
	}

	if (p <= 1 - this.p) {
	    return 0;
	} else {
	    return 1;
	}
    }

    public int random() {
	return (Math.random() < p) ? 1 : 0;
    }

    public double min() {
	return 0;
    }

    public double max() {
	return 1;
    }

    public double mean() {
	return p;
    }

    public double variance() {
	return p * (1 - p);
    }

}
