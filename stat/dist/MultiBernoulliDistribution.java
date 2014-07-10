package stat.dist;

/**
 * A Bernoulli distribution extended to multiple classes. This is
 * essentially a single-trial multinomial distribution over the
 * integers {0, ..., n-1}.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class MultiBernoulliDistribution extends IntegerDistribution {

    private double[] p;

    public MultiBernoulliDistribution(int n) {
	assert (n > 0): "Number of classes must be positive: " + n;

	this.p = new double[n];
	java.util.Arrays.fill(p, 1.0 / n);
    }

    public MultiBernoulliDistribution(double[] p) {
	assert (p.length > 0): "Number of classes must be positive: " + p.length;

	this.p = new double[p.length];
	System.arraycopy(p, 0, this.p, 0, p.length);
	stat.Lib.normalize(this.p);
    }

    public String toString() {
	StringBuffer sb = new StringBuffer("MultiBernoulli(" + p[0]);

	for (int i = 1; i < p.length; ++i) {
	    sb.append(", ");
	    sb.append(p[i]);
	}

	sb.append(")");
	return sb.toString();
    }

    public double probability(int x) {
	assert ((0 <= x) && (x < p.length)): "x must be between 0 and " + (p.length - 1) + ": " + x;
	return p[x];
    }

    public double distribution(int x) {
	double dist = 0.0;

	for (int i = 0; i <= x; ++i) {
	    dist += p[i];
	}

	return dist;
    }


    public int quantile(double p) {
	assert ((0 <= p) && (p <= 1)): "p must be between 0 and 1: " + p;

	double sp = 0;

	for (int i = 0; i < this.p.length; ++i) {
	    sp += this.p[i];
	    if (sp > p) {
		return i;
	    }
	}

	// p == 1
	return this.p.length - 1;
    }

    public int random() {
	return stat.Lib.random(p);
    }

    public double min() {
	return 0;
    }

    public double max() {
	return p.length - 1;
    }

    public double mean() {
	double mean = 0;

	for (int i = 0; i < p.length; ++i) {
	    mean += i * p[i];
	}

	return mean;
    }

    public double variance() {
	double mean = mean();
	double variance = 0;

	for (int i = 0; i < p.length; ++i) {
	    double d = i - mean;
	    variance += d * d * p[i];
	}

	return variance;
    }

}
