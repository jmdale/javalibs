package stat.dist;

import math.DoubleMatrix;
import math.DoubleVector;
import math.SingularMatrixException;

/**
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070817
 */
public class MultivariateNormalDistribution extends DoubleVectorDistribution {

    /**
     * The dimensionality.
     */
    private int m;

    /**
     * The mean, a vector of length m.
     */
    private DoubleVector mean;

    /**
     * The variance-covariance matrix, an m-by-m matrix.
     */
    private DoubleMatrix covariance;

    /**
     * The inverse of the covariance matrix.
     */
    private DoubleMatrix inverseCov;

    /**
     * The determinant of the covariance matrix.
     */
    private double detCov;


    public MultivariateNormalDistribution(DoubleVector mean, DoubleMatrix covariance) {
	if (covariance.rows() != covariance.columns()) {
	    throw new IllegalArgumentException("Given covariance matrix is not square: rows = " + covariance.rows() + ", columns = " + covariance.columns() + ".");
	}

	if (mean.length() != covariance.rows()) {
	    throw new IllegalArgumentException("Length of mean vector does not match size of covariance matrix: " + mean.length() + ", " + covariance.rows() + ".");
	}

	this.m = mean.length();
	this.mean = mean.clone();
	this.covariance = covariance.clone();

	try {
	    this.inverseCov = covariance.inverse();
	    this.detCov = covariance.determinant();
	} catch (SingularMatrixException sme) {
	    throw new IllegalArgumentException("Covariance matrix must be non-singular", sme);
	}

// 	System.err.println("NORMAL DIST -- m: " + m);
// 	System.err.println("NORMAL DIST -- mean: " + mean);
// 	System.err.println("NORMAL DIST -- covariance: " + covariance);
// 	System.err.println("NORMAL DIST -- inverse covariance: " + inverseCov);
// 	System.err.println("NORMAL DIST -- determinant of covariance: " + detCov);

    }

    /**
     * Returns [(x - mean)' * inv(covariance) * (x - mean)]^(1/2), the
     * equivalent of a "z-value" for a multivariate normal
     * distribution. In other words, returns the Mahalanobis distance
     * between the given vector and the mean of this distribution.
     */
    public double z(DoubleVector x) {
	DoubleVector v = x.subtract(this.mean);
	double z = Double.NaN;

	try {
	    z = Math.sqrt(v.dotProduct(covariance.solve(v)));
	} catch (SingularMatrixException sme) {
	    throw new ArithmeticException("Something very bad happened -- the covariance matrix is singular!");
	}

	return z;
    }

    public double logDensity(DoubleVector x) {
	return
	    -0.5 * x.subtract(mean).dotProduct(inverseCov.multiply(x.subtract(mean))) -
	    0.5 * m * Math.log(2 * Math.PI) - 0.5 * Math.log(detCov);
    }

    public double density(DoubleVector x) {
	return Math.exp(logDensity(x));
    }

    public DoubleVector random() {
	// Generate a vector r of m independent N(0, 1) variates, then
	// return mean + sqrt(covariance) * r, where sqrt(covariance)
	// refers to the Cholesky factor L such that L * L^T =
	// covariance. See chapter 21 of Jordan's textbook, on
	// sampling.

	throw new UnsupportedOperationException("Not yet implemented.");
    }

    public DoubleVector mean() {
	return mean.clone();
    }

    public DoubleMatrix covariance() {
	return covariance.clone();
    }

    /**
     * Estimates a multivariate normal distribution from a matrix of
     * observations whose rows correspond to independent samples from
     * the distribution being estimated.
     */
    public static MultivariateNormalDistribution estimate(DoubleMatrix x) {
	return new MultivariateNormalDistribution(x.mean(), x.covariance());
    }

}
