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
public class PointIntegerDistribution extends IntegerDistribution {

    private int location;

    public PointIntegerDistribution(int location) {
	this.location = location;
    }

    public double probability(int x) {
	return (x == location) ? 1 : 0;
    }

    public double distribution(int x) {
	return (x < location) ? 0 : 1;
    }

    public int quantile(double p) {
	return location;
    }

    public int random() {
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
