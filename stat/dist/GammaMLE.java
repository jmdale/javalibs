package stat.dist;

import static java.lang.Math.log;
import static math.GammaFunction.digamma;
import static math.GammaFunction.trigamma;

import math.DoubleVector;
import math.DoubleMatrix;

/**
 * Another attempt at ML estimation for the gamma distribution. This
 * time we try Newton-Raphson.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060309
 */
class GammaMLE {

    /**
     * Computes the score vector, the gradient of the log-likelihood.
     */
    private static DoubleVector score(double sumX, double sumLogX, int n, double alpha, double beta) {
	DoubleVector s = new DoubleVector(2);

	s.set(0, sumLogX - n * (log(beta) + digamma(alpha)));
	s.set(1, (sumX / beta - n * alpha) / beta);

	return s;
    }

    /**
     * Computes the information matrix, the negative of the matrix of
     * second derivatives of the log-likelihood.
     */
    private static DoubleMatrix information(double sumX, int n, double alpha, double beta) {
	DoubleMatrix i = new DoubleMatrix(2, 2);

	i.set(0, 0, n * trigamma(alpha));
	i.set(0, 1, n / beta);
	i.set(1, 0, n / beta);
	i.set(1, 1, (2 * sumX / beta - n * alpha) / (beta * beta));

	return i;
    }

    private static DoubleVector solve(DoubleVector x, double alphaInit, double betaInit) {
	double EPS = 1e-10;

	DoubleVector params = new DoubleVector(2);
	params.set(0, alphaInit);
	params.set(1, betaInit);
	int n = x.length();
	double sumX = x.sum();
	double sumLogX = x.log().sum();

	while (true) {
// 	    System.out.println("alpha = " + params.get(0) + ", beta = " + params.get(1));
	    DoubleVector s = score(sumX, sumLogX, n, params.get(0), params.get(1));
	    DoubleMatrix i = information(sumX, n, params.get(0), params.get(1));

	    /*
	     * Stop when the norm of the gradient of the
	     * log-likelihood is sufficiently close to zero.
	     */
	    if (s.norm() < EPS) {
		return params;
	    } else {
		try {
		    params = params.add(i.solve(s));
		} catch (math.SingularMatrixException sme) {
		    System.err.println("Information matrix singular -- cannot continue.");
		    System.exit(1);
		}
	    }
	}
    }

    static GammaDistribution newtonMLE(DoubleVector x) {
	GammaDistribution moment = GammaDistribution.momentEstimate(x);
	DoubleVector params = solve(x, moment.getShape(), moment.getScale());

	return new GammaDistribution(params.get(0), params.get(1));
    }

}
