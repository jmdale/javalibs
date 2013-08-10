package math;

import static math.GammaFunction.lgamma;

/**
 * Methods related to the beta function.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20050328
 */
public class BetaFunction {

    /**
     * Computes the natural logarithm of the beta function.
     */
    public static double logBeta(double w, double z) {
	return lgamma(w) + lgamma(z) - lgamma(w + z);
    }

    /**
     * Computes the beta function.
     */
    public static double beta(double w, double z) {
	return Math.exp(logBeta(w, z));
    }

    /**
     * Computes the incomplete beta function.
     *
     * @see <a href="http://lib-www.lanl.gov/numerical/bookcpdf/c6-4.pdf">Numerical Recipes in C, Section 6.4</a>
     */
    public static double incompleteBeta(double a, double b, double x) {
	assert (a > 0): "a must be greater than 0: " + a + ".";
	assert (b > 0): "b must be greater than 0: " + b + ".";
	assert ((0 <= x) && (x <= 1)): "x must be in [0, 1]: " + x + ".";

	if (x > ((a + 1) / (a + b + 2))) {
	    // NR Equation 6.4.3
	    return 1 - incompleteBeta(b, a, 1 - x);
	} else {
	    double c = Math.exp(lgamma(a + b) - lgamma(a) - lgamma(b) + a * Math.log(x) + b * Math.log(1 - x));
	    double cf = IncompleteBetaContinuedFraction.eval(a, b, x);

	    return c * cf / a;
	}
    }

}
