package stat;

import stat.dist.DoubleDistribution;
import util.fn.DoubleToDouble;

/**
 * A simple implementation of importance sampling: computes the
 * expectation of a function with respect to one distribution by
 * sampling from a different distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20041210
 */
public class ImportanceSampler {

    /**
     * The function whose expectation will be computed.
     */
    private DoubleToDouble f;

    /**
     * The distribution over which to compute the expectation.
     */
    private DoubleDistribution p;

    /**
     * The distribution from which samples will be drawn.
     */
    private DoubleDistribution q;

    /**
     * The sum of the values of f, weighted by the ratio of the
     * densities of p and q, at the sample points.
     */
    private double s;

    /**
     * The number of samples taken.
     */
    private int n;

    public ImportanceSampler(DoubleToDouble f, DoubleDistribution p, DoubleDistribution q) {
	this.f = f;
	this.p = p;
	this.q = q;
	this.s = 0;
	this.n = 0;
    }

    public double getMean() {
	// Note: if p and q were unnormalized distributions, we would
	// want to divide by the sum of the importance weights instead
	// of the number of samples.
	return s / n;
    }

    public void sample() {
	double r = q.random();
	s += f.apply(r) * p.density(r) / q.density(r);
	n += 1;
    }

    public void sample(int m) {
	for (int i = 0; i < m; ++i) {
	    sample();
	}
    }

}
