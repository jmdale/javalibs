package stat.dist;

/**
 * A distribution all of whose mass is location at a single point; it
 * is essentially a Kronecker delta function.
 *
 * (This will be used to specify the distributions of evidence
 * variables in graphical models.)
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class PointDoubleDistribution extends DoubleDistribution {

    private double location;

    public PointDoubleDistribution(double location) {
	this.location = location;
    }

    public double density(double x) {
	return (x == location) ? 1 : 0;
    }

    public double distribution(double x) {
	return (x < location) ? 0 : 1;
    }

    public double quantile(double p) {
	if (p == 0) {
	    return Double.NEGATIVE_INFINITY;
	} else if (p == 1) {
	    return Double.POSITIVE_INFINITY;
	} else {
	    return location;
	}
    }

    public double random() {
	return location;
    }

    public double min() {
	return location;
    }

    public double max() {
	return location;
    }

    public double mean() {
	return location;
    }

    public double variance() {
	return 0;
    }

}
