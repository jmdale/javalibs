
package stat;

import math.DoubleMatrix;
import math.DoubleVector;
import math.SingularMatrixException;

/**
 * Performs logistic regression using the iteratively reweighted
 * least-squares algorithm.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20060823
 */
public class LogisticRegression {

    /**
     * The algorithm terminates when the relative improvement in
     * log-likelihood drops below EPS.
     */
    private static final double EPS = 1e-8;

    /**
     * The design matrix.
     */
    private DoubleMatrix X;

    /**
     * The output vector.
     */
    private DoubleVector y;

    /**
     * The current parameter estimate.
     */
    private DoubleVector beta;

    /**
     * The current value of the canonical parameter, equal to X *
     * beta.
     */
    private DoubleVector theta;

    /**
     * The current value of the Bernoulli parameter, equal to exp(pi)
     * / (1 + exp(pi)).
     */
    private DoubleVector pi;

    /**
     * The current value of the log-likelihood (ignoring terms
     * constant in pi); equal to y * log(pi) + (1 - y) * log(1 - pi);
     */
    private double ll;

    /**
     * The improvement in the log-likelihood on the last iteration.
     */
    private double deltaLL;


    public LogisticRegression(DoubleMatrix X, DoubleVector y) {

	this.X = X;
	this.y = y;

    }


    public DoubleVector getParameters() {

	return beta.clone();

    }


    public double getLogLikelihood() {

	return ll;

    }


    /**
     * Runs the iteratively reweighted least-squares algorithm,
     * updating beta.
     */
    public void doIRLS() throws IdentifiabilityException {

// 	checkY();
	initialize();

	int iter = 0;

	do {

	    update();
	    checkNaN();

	    iter += 1;
// 	    System.err.println("Parameters after iteration " + iter + ": " + beta);
// 	    System.err.println("Log-likelihood after iteration " + iter + ": " + ll);
// 	    System.err.println("Log-likelihood increase in iteration " + iter + ": " + deltaLL);

	} while (!done());

    }


    /**
     * Tests whether y contains at least one 0, at least one 1, and no
     * values other than 0 or 1; throws IdentifiabilityException if
     * any of these conditions is not met.
     */
//     private void checkY() throws IdentifiabilityException {

// 	boolean has0 = false;
// 	boolean has1 = false;

// 	for (int i = 0; i < y.length(); i += 1) {

// 	    double d = y.get(i);

// 	    if (d == 0) {

// 		has0 = true;

// 	    } else if (d == 1) {

// 		has1 = true;

// 	    } else {

// 		throw new IdentifiabilityException("Illegal value in y: " + d + ".");

// 	    }

// 	}

// 	if (!has0) {

// 	    throw new IdentifiabilityException("y contains only 1s.");

// 	}

// 	if (!has1) {

// 	    throw new IdentifiabilityException("y contains only 0s.");

// 	}

//     }


    private void initialize() {

	this.beta = new DoubleVector(X.columns());
	beta.fill(0);

	this.theta = new DoubleVector(X.rows());
	theta.fill(0);

	this.pi = new DoubleVector(X.rows());
	pi.fill(0.5);

	this.ll = computeLogLikelihood();
	this.deltaLL = Double.NaN;

// 	System.err.println("Initial parameters: " + beta);
// 	System.err.println("Initial log-likelihood: " + ll);

    }


    private void update() throws IdentifiabilityException {

	double llOld = ll;

	DoubleMatrix WX = X.clone();

	/*
	 * Instead of actually forming the diagonal weight matrix W,
	 * then doing a full matrix multiplication by the design
	 * matrix X, we do things much more efficiently, computing WX
	 * by multiplying the ith row of X by the ith element of W.
	 */
	for (int i = 0; i < WX.rows(); i += 1) {

	    double w = pi.get(i) * (1 - pi.get(i));

	    for (int j = 0; j < WX.columns(); j += 1) {

		WX.set(i, j, w * WX.get(i, j));

	    }

	}

	DoubleMatrix tX = X.transpose();
	DoubleMatrix G = tX.multiply(WX);
	DoubleVector d = tX.multiply(y.subtract(pi));

	try {

	    DoubleVector z = G.solve(d);
	    beta = beta.add(z);
	    theta = X.multiply(beta);
	    pi = inverseLogit(theta);
	    ll = computeLogLikelihood();
	    deltaLL = ll - llOld;

	} catch (SingularMatrixException sme) {

	    throw new IdentifiabilityException(sme);

	}

    }


    private void checkNaN() throws IdentifiabilityException {

	if (Double.isNaN(ll)) {

	    throw new IdentifiabilityException("Log-likelihood is NaN.");

	}

	for (int i = 0; i < beta.length(); i += 1) {

	    if (Double.isNaN(beta.get(i))) {

		throw new IdentifiabilityException("beta[" + i + "] is NaN.");

	    }

	}

    }


    private static DoubleVector inverseLogit(DoubleVector theta) {

	DoubleVector pi = new DoubleVector(theta.length());

	for (int i = 0; i < pi.length(); i += 1) {

// 	    double d = Math.exp(theta.get(i));
// 	    pi.set(i, d / (1 + d));

	    double d = Math.exp(-theta.get(i));
	    pi.set(i, 1 / (1 + d));

	}

	return pi;

    }


    private double computeLogLikelihood() {

	double ll = 0;

	for (int i = 0; i < y.length(); i += 1) {

	    double inc = y.get(i) * Math.log(pi.get(i)) + (1 - y.get(i)) * Math.log(1 - pi.get(i));

	    if (Double.isNaN(inc)) {

// 		System.err.println("***** NaN at row " + i + ":");
// 		System.err.println("***** X: " + X.getRow(i));
// 		System.err.println("***** y: " + y.get(i));
// 		System.err.println("***** theta: " + theta.get(i));
// 		System.err.println("***** pi: " + pi.get(i));

	    } else {

		ll += inc;

	    }

	}

	return ll;

    }


    private boolean done() {

	return deltaLL < EPS * Math.abs(ll);

    }

}
