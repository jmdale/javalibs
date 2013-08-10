package math;

/**
 * Methods related to the gamma function.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20110824
 */
public class GammaFunction {

    /**
     * Computes the coefficients for the Lanczos approximation to the
     * gamma function. This method for computing the coefficients is
     * given in Theorem 1.3.1 of John L. Spouge, "Computation of the
     * Gamma, Digamma, and Trigamma Functions", SIAM Journal on
     * Numerical Analysis, Vol. 31, No. 3 (Jun. 1994), pp. 931-944.
     *
     * The number of coefficients, including the first which is always
     * 1, is equal to Math.floor(a) + 1; thus a determines the number
     * of terms to use in, and thus the accuracy of, the
     * approximation.
     */
    private static double[] lanczosCoeff(double a) {

	assert (a > 1): "a must be greater than 1: " + a + ".";

	int K = (int) Math.floor(a);
	double[] c = new double[K + 1];
	c[0] = 1;

	for (int k = 1; k <= K; k += 1) {
	    c[k] = Math.pow(a - k, k - 0.5) * Math.exp(a - k) / (Lib.factorial(k - 1) * Lib.SQRT_2PI);

	    if ((k % 2) == 0) {
		c[k] = -c[k];
	    }
	}

	return c;
    }

    private static final double LGAMMA_A = 10.5;
    private static final double[] LGAMMA_COEFF = lanczosCoeff(LGAMMA_A);

    /**
     * Computes the natural logarithm of the gamma function. Based on
     * Equation 7 of the Spouge paper. This function has been tested
     * against R.
     */
    public static double lgamma(double z) {
	assert (z > 0): "z must be greater than 0: " + z + ".";

	return (z + 0.5) * Math.log(z + LGAMMA_A) - (z + LGAMMA_A) + Lib.LN_SQRT_2PI + Math.log(lgammaSeries(LGAMMA_COEFF, z)) - Math.log(z);
    }

    /**
     * Computes the series portion of the Lanczos approximation. This
     * corresponds to the right hand side of Equation 8 in the Spouge
     * paper, neglecting the error term.
     */
    private static double lgammaSeries(double[] c, double z) {
	double s = c[0];
	for (int k = 1; k < c.length; ++k) {
	    s += c[k] / (z + k);
	}
	return s;
    }

    public static double gamma(double z) {
	return Math.exp(lgamma(z));
    }

    /**
     * Computes the digamma function, which is the derivative of the
     * lgamma function; or equivalently, the derivative of the gamma
     * function divided by the gamma function. The formula used here
     * is derived from Equation 21 and the derivative of Equation 8 in
     * the Spouge paper. This function has been tested against R.
     */
    public static double digamma(double z) {
	return
	    Math.log(z + LGAMMA_A) - ((LGAMMA_A - 0.5) / (z + LGAMMA_A)) - (1 / z) -
	    (digammaSeries(LGAMMA_COEFF, z) / lgammaSeries(LGAMMA_COEFF, z));
    }

    /**
     * The derivative of Equation 8 in the Spouge paper.
     */
    private static double digammaSeries(double[] c, double z) {
	double s = 0;
	for (int k = 1; k < c.length; ++k) {
	    s += c[k] / square(z + k);
	}
	return s;
    }

    /**
     * Computes the trigamma function, which is the derivative of the
     * digamma function. The formula used here is derived from
     * Equation 22 and the second derivative of Equation 8 in the
     * Spouge paper. This function has been tested against R.
     */
    public static double trigamma(double z) {
	double s0 = lgammaSeries(LGAMMA_COEFF, z);
	double s1 = digammaSeries(LGAMMA_COEFF, z);
	double s2 = trigammaSeries(LGAMMA_COEFF, z);

	return
	    (1 / square(z)) + (1 / (z + LGAMMA_A)) + ((LGAMMA_A - 0.5) / square(z + LGAMMA_A)) +
	    (s2 / s0) - square(s1 / s0);
    }

    /**
     * The second derivative of Equation 8 in the Spouge paper.
     */
    private static double trigammaSeries(double[] c, double z) {
	double s = 0;
	for (int k = 1; k < c.length; ++k) {
	    s += c[k] / cube(z + k);
	}
	return 2 * s;
    }

    private static double square(double x) {
	return x * x;
    }

    private static double cube(double x) {
	return x * x * x;
    }

    /**
     * Computes the lower incomplete gamma function (denoted by
     * lower-case gamma) at arguments a and x, divided by the complete
     * gamma function at argument a. In other words, this is the
     * function denoted by P(a, x) and defined by Equation 6.2.1 of
     * <em>Numerical Recipes in C</em>. The value is computed using
     * the series given in equation 6.2.5 of <em>Numerical Recipes in
     * C</em>.
     *
     * I had initially implemented the series with a fixed stopping
     * criterion of 100 iterations. I found that this implementation
     * was quite unsuitable for the primary purpose for which I had
     * implemented it, namely computing the normal CDF by way of the
     * error function (erf). In particular, the normal CDF computation
     * would break down horribly for |z| > ~3.
     *
     * I have now modified this implementation to use a (term < EPS *
     * sum) stopping criterion. I have tested this function against
     * the corresponding pgamma function in R, and the results appear
     * to be good.
     *
     * This implementation may still be problematic in that it will
     * converge slowly for x > a + 1. One possible way to fix this
     * problem would be to replace this implementation with on based
     * on the series / continued fraction hybrid strategy described in
     * <em>Numerical Recipes in C</em>.
     *
     * @see <a href="http://lib-www.lanl.gov/numerical/bookcpdf/c6-2.pdf">Numerical Recipes in C, Section 6.2</a>
     */
    public static double lowerIncompleteGammaOverGamma(double a, double x) {
	double term = 1 / gamma(a + 1);
	double sum = term;
	double EPS = 0.0000001;
	int n = 1;

	while (true) {
	    term *= x / (a + n);
	    sum += term;

	    if (term < EPS * sum) {
		break;
	    } else {
                ++n;
	    }
	}

	return Math.exp(-x) * Math.pow(x, a) * sum;
    }

    /**
     * Computes the upper incomplete gamma function (denoted by
     * upper-case gamma) at arguments a and x, divided by the complete
     * gamma function at argument a. This is simply one minus the
     * value of lowerIncompleteGammaOverGamma at the same arguments.
     *
     * @see <a href="http://lib-www.lanl.gov/numerical/bookcpdf/c6-2.pdf">Numerical Recipes in C</a>
     * @see <a href="http://mathworld.wolfram.com/IncompleteGammaFunction.html">MathWorld</a>
     */
    public static double upperIncompleteGammaOverGamma(double a, double x) {
	return 1 - lowerIncompleteGammaOverGamma(a, x);
    }

}
