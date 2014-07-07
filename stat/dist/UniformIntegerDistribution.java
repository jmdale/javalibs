package stat.dist;

import java.util.Random;

/**
 * A uniform distribution over integers.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class UniformIntegerDistribution extends IntegerDistribution {

    private int low;
    private int high;
    private double p;
    private double logp;
    private Random r;

    public UniformIntegerDistribution(int low, int high) {
	if (low >= high) {
	    throw new IllegalArgumentException("low must be less than high: " + low + ", " + high + ".");
	}

	this.low = low;
	this.high = high;
	this.p = 1.0 / (high - low);
	this.logp = Math.log(this.p);
	this.r = new Random();
    }

    public UniformIntegerDistribution(int n) {
	this(0, n);
    }

    public double probability(int x) {
	return p;
    }

    public double logProbability(int x) {
	return logp;
    }

    public double distribution(int x) {
	if (x < low) {
	    return 0;
	} else if (x < high) {
	    return p * ((x - low) + 1);
	} else {
	    return 1;
	}
    }

    public int quantile(double q) {
	return low + ((int) Math.floor(q / p));
    }

    public int random() {
	return low + r.nextInt(high - low);
    }

    public double min() {
	return low;
    }

    public double max() {
	return high;
    }

    public double mean() {
	return 0.5 * (low + high - 1);
    }

    public double variance() {
	return (high - low - 1) * (high - low + 1) / 12.0;
    }

}
