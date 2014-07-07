package stat.dist;

/**
 * A uniform distribution over a range of doubles.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class UniformDoubleDistribution extends DoubleDistribution {

    /**
     * The lower end of the range of this distribution.
     */
    private double low;

    /**
     * The upper end of the range of this distribution.
     */
    private double high;

    public UniformDoubleDistribution(double low, double high) {
	assert (low < high): "Invalid range: [" + low + ", " + high + ")";

	this.low = low;
	this.high = high;
    }

    public UniformDoubleDistribution() {
	this(0, 1);
    }

    public String toString() {
	return "Uniform(" + low + ", " + high + ")";
    }

    public double density(double x) {
	if ((low <= x) && (x < high)) {
	    return 1 / (high - low);
	} else {
	    return 0;
	}
    }

    public double distribution(double x) {
	if (x <= low) {
	    return 0;
	} else if (x >= high) {
	    return 1;
	} else {
	    return (x - low) / (high - low);
	}
    }

    public double quantile(double p) {
	assert ((0 <= p) && (p <= 1)): "p must be in [0, 1]: " + p;
	return low + p * (high - low);
    }

    public double random() {
	return low + Math.random() * (high - low);
    }

    public double min() {
	return low;
    }

    public double max() {
	return high;
    }

    public double mean() {
	return (high + low) / 2;
    }

    public double variance() {
	double d = high - low;
	return (d * d) / 12;
    }

}
