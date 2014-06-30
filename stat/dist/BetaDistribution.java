package stat.dist;

/**
 * The beta distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class BetaDistribution extends DoubleDistribution {

    private double alpha;
    private double beta;
    private double norm;
    private double logNorm;

    public BetaDistribution(double alpha, double beta) {
	if (alpha <= 0) {
	    throw new IllegalArgumentException("alpha parameter must be positive: " + alpha + ".");
	}
	if (beta <= 0) {
	    throw new IllegalArgumentException("beta parameter must be positive: " + beta + ".");
	}

	this.alpha = alpha;
	this.beta = beta;
	this.norm = math.BetaFunction.beta(alpha, beta);
	this.logNorm = math.BetaFunction.logBeta(alpha, beta);
    }

    public double logDensity(double x) {
	return (alpha - 1) * Math.log(x) + (beta - 1) * Math.log(1 - x) - logNorm;
    }

    public double density(double x) {
	return Math.pow(x, alpha - 1) * Math.pow(1 - x, beta - 1) / norm;
    }

    public double distribution(double x) {
	return math.BetaFunction.incompleteBeta(alpha, beta, x);
    }

    public double min() {
	return 0;
    }

    public double max() {
	return 1;
    }

    public double mean() {
	return alpha / (alpha + beta);
    }

    public double variance() {
	return (alpha * beta) / ((alpha + beta) * (alpha + beta) * (alpha + beta + 1));
    }

}
