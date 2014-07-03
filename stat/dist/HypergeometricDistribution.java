package stat.dist;

/**
 * The hypergeometric distribution. This distribution has three
 * parameters: n1, n2, and n. If n balls are chosen without
 * replacement from an urn containing n1 white balls and n2 black
 * balls, the hypergeometric distribution gives the probability that y
 * out of the n balls are white. The probability is positive when
 * max(0, n - n2) <= y <= min(n, n1) and is zero otherwise.
 *
 * @see <a href="http://mathworld.wolfram.com/HypergeometricDistribution.html">Mathworld</a>
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class HypergeometricDistribution extends IntegerDistribution {

    private int n1;
    private int n2;
    private int n;

    /**
     * The smallest integer for which this distribution has positive
     * probability.
     */
    private int minValue;

    /**
     * The largest integer for which this distribution has positive
     * probability.
     */
    private int maxValue;

    /**
     * The natural logarithm of the probability at minValue.
     */
    private double logMinProb;

    public HypergeometricDistribution(int n1, int n2, int n) {
	assert (n1 > 0): "n1 must be positive: " + n1;
	assert (n2 > 0): "n2 must be positive: " + n2;
	assert (n > 0): "n must be positive: " + n;
	assert (n <= n1 + n2): "n must be no more than n1 + n2: " + n + ", " + (n1 + n2);

	this.n1 = n1;
	this.n2 = n2;
	this.n = n;
	this.minValue = Math.max(0, n - n2);
	this.maxValue = Math.min(n, n1);
	this.logMinProb = calculateLogMinProb();
    }

    private double calculateLogMinProb() {
	if (minValue == 0) {
	    return sumOfLogs(n2 - n + 1, n2) - sumOfLogs(n1 + n2 - n + 1, n1 + n2);
	} else {
	    return sumOfLogs(n - n2 + 1, n1) - sumOfLogs(n + 1, n1 + n2);
	}
    }

    /**
     * Returns the sum of the natural logarithms of the integers
     * betwen start and end, inclusive.
     */
    private double sumOfLogs(int start, int end) {
	double sum = 0;
	for (int i = start; i <= end; ++i) {
	    sum += Math.log(i);
	}

	return sum;
    }

    public double probability(int x) {
	return Math.exp(logProbability(x));
    }

    public double logProbability(int x) {
	if ((x < minValue) || (x > maxValue)) {
	    return Double.NEGATIVE_INFINITY;
	} else {
	    double logProb = logMinProb;
	    for (int y = minValue + 1; y <= x; ++y) {
		logProb += Math.log((n1 - y + 1) * (n - y + 1));
		logProb -= Math.log(y * (n2 - n + y));
	    }

	    return logProb;
	}
    }

    public double distribution(int x) {
	if (x < minValue) {
	    return 0;
	} else if (x > maxValue) {
	    return 1;
	} else {
	    double logProb = logMinProb;
	    double dist = Math.exp(logProb);

	    for (int y = minValue + 1; y <= x; ++y) {
		logProb += Math.log((n1 - y + 1) * (n - y + 1));
		logProb -= Math.log(y * (n2 - n + y));
		dist += Math.exp(logProb);
	    }

	    return dist;
	}
    }

    public int quantile(double p) {
	throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Computes a random variate from this distribution by sampling n
     * integers without replacement from {0, ..., (n1 + n2) - 1}. The
     * random variate is equal to the number of sampled integers which
     * are less than n1.
     */
    public int random() {
	int[] sample = math.IntVector.sampleWithoutReplacement(n, n1 + n2).data();
	int r = 0;

	for (int i = 0; i < sample.length; ++i) {
	    if (sample[i] < n1) {
		++r;
	    }
	}

	return r;
    }

    public double min() {
	return Math.max(0, n - n2);
    }

    public double max() {
	return Math.min(n, n1);
    }

    public double mean() {
	double pi = ((double) n1) / (n1 + n2);
	return n * pi;
    }

    public double variance() {
	double pi = ((double) n1) / (n1 + n2);
	int N = n1 + n2;

	return (n * pi * (1 - pi) * (N - n)) / (N - 1);
    }

    public static void main(String[] args) {
	HypergeometricDistribution hd = new HypergeometricDistribution(Integer.parseInt(args[0]),
								       Integer.parseInt(args[1]),
								       Integer.parseInt(args[2]));

	for (int i = hd.minValue - 1; i <= hd.maxValue + 1; ++i) {
	    System.out.println(i + "\t" + hd.distribution(i));
	}
    }

}
