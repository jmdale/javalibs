package stat.dist;

/**
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class StandardLogisticDistribution extends DoubleDistribution {

    public double density(double x) {
	double dist = distribution(x);
	return dist * (1 - dist);
    }

    public double distribution(double x) {
	return 1 / (1 + Math.exp(-x));
    }

    public double quantile(double p) {
	assert ((0 <= p) && (p <= 1)): "p must be in [0, 1]: " + p;
	return math.Lib.logit(p);
    }

    public double min() {
	return Double.NEGATIVE_INFINITY;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	return Double.NaN;
    }

    public double variance() {
	return Double.NaN;
    }

}
