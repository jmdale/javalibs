package stat.dist;

import static math.GammaFunction.digamma;
import static math.GammaFunction.lgamma;
import static math.GammaFunction.lowerIncompleteGammaOverGamma;

import math.DoubleVector;
import math.Function;
import math.root.BisectionRootFinder;
import math.root.RootFinder;

/**
 * The gamma distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class GammaDistribution extends DoubleDistribution {

    private double shape;
    private double scale;

    /**
     * A normalization factor used in computing the logDensity
     * method. Since this factor involves the gamma function, caching
     * it ought to make density computations more efficient. However,
     * it would need to be recomputed if shape or scale were modified.
     */
    private double ldnorm;

    /**
     * Creates a new GammaDistribution with the given shape and scale
     * parameters.
     */
    public GammaDistribution(double shape, double scale) {
	assert (scale > 0): "Scale parameter must be greater than zero: " + scale;
	assert (shape > 0): "Shape parameter must be greater than zero: " + shape;

	this.shape = shape;
	this.scale = scale;
	this.ldnorm = Math.log(scale) + lgamma(shape);
    }

    public String toString() {
	return "Gamma(" + shape + ", " + scale + ")";
    }

    public double getShape() {
	return shape;
    }

    public double getScale() {
	return scale;
    }

    public double density(double x) {
	return Math.exp(logDensity(x));
    }

    public double logDensity(double x) {
	if (x <= 0) {
	    return Double.NEGATIVE_INFINITY;
	} else {
	    return (shape - 1) * Math.log(x / scale) - (x / scale) - ldnorm;
	}
    }

    public double distribution(double x) {
	if (x <= 0) {
	    return 0;
	} else {
	    return lowerIncompleteGammaOverGamma(shape, x / scale);
	}
    }

    // Copied from DoubleDistribution; changed min and max.
    public double quantile(double p) {
	final double pp = p;

	Function f = new Function() {
		public double apply(double x) {
		    return distribution(x) - pp;
		}
	    };

	double min = 0;
	double max = mean() + 100 * standardDeviation();

	BisectionRootFinder brf = new BisectionRootFinder(f, min, max);

	return brf.findRoot(1e-10);
    }

    public double min() {
	return 0;
    }

    public double max() {
	return Double.POSITIVE_INFINITY;
    }

    public double mean() {
	return shape * scale;
    }

    public double variance() {
	return shape * scale * scale;
    }

    public GammaDistribution estimateNew(double[] x, double[] w) {
	double sum = 0;
	double weight = 0;

	for (int i = 0; i < x.length; ++i) {
	    sum += w[i] * x[i];
	    weight += w[i];
	}

	double mean = sum / weight;
	sum = 0;

	for (int i = 0; i < x.length; ++i) {
	    sum += w[i] * (x[i] - mean) * (x[i] - mean);
	}

	double variance = sum / weight;
	double scale = variance / mean;
	double shape = mean * mean / variance;

	return new GammaDistribution(shape, scale);
    }

    public static GammaDistribution momentEstimate(DoubleVector x) {
	double mean = x.mean();
	double variance = x.variance();
	double scale = variance / mean;
	double shape = mean * mean / variance;

	return new GammaDistribution(shape, scale);
    }

    /**
     * Computes the maximum likelihood parameter estimates for the
     * given sample.
     *
     * The method is as follows. Let L denote the log-likelihood
     * function, and A and B the shape and scale parameters. We find
     * dL/dB, set equal to 0, and solve for B in terms of A. We then
     * substitute this expression for B into dL/dA. We set this
     * expression for dL/dA equal to 0 and solve for A numerically,
     * which then gives us B.
     *
     * For some reason this seems a bit ad hoc to me. It would be nice
     * to do a gradient ascent, so that we're optimizing both
     * variables at once. The difficulty is the step size. Perhaps we
     * should start with some arbitrary but decently large step size,
     * then make it smaller if following the gradient with that step
     * size overshoots the maximum (i.e., gives a decrease in the
     * log-likelihood). Of course, then we have to rethink the
     * stopping criterion, because if we make the step size too small
     * then we can be making small changes in the log-likelihood even
     * if we're nowhere near the maximum. Perhaps an alternative
     * stopping criterion would be that the step size has to go below
     * a certain epsilon in order to increase the log-likelihood.
     */
    public static GammaDistribution maximumLikelihoodEstimate(DoubleVector x) {
        GammaDistribution momentEst = momentEstimate(x);
        final double m1 = Math.log(x.mean());
        final double m2 = x.log().mean();

	/* dL/dA in terms of A. */
        Function f = new Function() {
                public double apply(double a) {
                    return digamma(a) - Math.log(a) + m1 - m2;
                }
            };

        double shape = RootFinder.bisect(f, momentEst.shape / 10, momentEst.shape * 10, 1e-10);
        double scale = x.mean() / shape;

        return new GammaDistribution(shape, scale);
    }

    /**
     * Computes the log-likelihood of the parameters of this
     * distribution with respect to the given sample (assumed i.i.d.).
     */
    public double logLikelihood(DoubleVector x) {
	return (shape - 1) * x.log().sum() - x.sum() / scale - x.length() * shape * Math.log(scale) - x.length() * lgamma(shape);
    }

    public static void main(String[] args) {
	double alpha = Double.parseDouble(args[0]);
	double beta = Double.parseDouble(args[1]);
	int n = Integer.parseInt(args[2]);

	GammaDistribution gd = new GammaDistribution(alpha, beta);
	System.out.println("True distribution:");
	System.out.println("\t" + gd);

	DoubleVector x = gd.random(n);

	System.out.println("ll = " + gd.logLikelihood(x));

	GammaDistribution gdMoment = momentEstimate(x);
	System.out.println("Moment estimate:");
	System.out.println("\t" + gdMoment);
	System.out.println("\tll = " + gdMoment.logLikelihood(x));

	GammaDistribution gdML = maximumLikelihoodEstimate(x);
	System.out.println("ML estimate (univariate bisection solver):");
	System.out.println("\t" + gdML);
	System.out.println("\tll = " + gdML.logLikelihood(x));

	GammaDistribution gdNewtonML = GammaMLE.newtonMLE(x);
	System.out.println("ML estimate (bivariate Newton solver):");
	System.out.println("\t" + gdNewtonML);
	System.out.println("\tll = " + gdNewtonML.logLikelihood(x));
    }

}
