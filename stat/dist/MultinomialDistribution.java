package stat.dist;

import math.DoubleVector;
import math.IntVector;

/**
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20050630
 */
public class MultinomialDistribution {

    private DoubleVector logProbabilities;

    public MultinomialDistribution(int n) {
	assert (n > 0): "Number of classes must be positive: " + n;

	this.logProbabilities = new DoubleVector(n);
	logProbabilities.fill(Math.log(1.0 / n));
    }

    public MultinomialDistribution(DoubleVector p) {
	this.logProbabilities = p.normalize().log();
    }

    public MultinomialDistribution(double[] p) {
	this(new DoubleVector(p));
    }

    public double probability(IntVector x) {
	return Math.exp(logProbability(x));
    }

    public double logProbability(IntVector x) {
	return logProbabilities.dotProduct(x);
    }

}
