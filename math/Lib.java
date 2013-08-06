package math;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Mathematical constants and functions.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20080804
 */
public class Lib {

    /**
     * The square root of 2.
     */
    public static final double SQRT2 = Math.sqrt(2);

    /**
     * The natural logarithm of 2.
     */
    public static final double LN_2 = Math.log(2);

    /**
     * The square root of 2 * pi.
     */
    public static final double SQRT_2PI = Math.sqrt(2 * Math.PI);

    /**
     * The natural logarithm of 2 * pi.
     */
    public static final double LN_2PI = Math.log(2 * Math.PI);

    /**
     * The natural logarithm of the square root of 2 * pi.
     */
    public static final double LN_SQRT_2PI = Math.log(SQRT_2PI);

    /**
     * The Euler-Mascheroni Gamma constant. From
     * http://mathworld.wolfram.com/Euler-MascheroniConstant.html.
     */
    public static final double EULER_GAMMA = 0.5772156649015329;



    /**
     * Kronecker delta function.
     */
    public static int delta(int x, int y) {
	return (x == y) ? 1 : 0;
    }

    /**
     * Tests whether a given int is even.
     *
     * @return <code>true</code> if and only if <code>n</code> is even.
     */
    public static boolean isEven(int n) {
	return (n % 2) == 0;
    }

    /**
     * Computes the average of two doubles.
     *
     * @return <code>(x + y) / 2</code>
     */
    public static double average(double x, double y) {
	return (x + y) / 2;
    }

    /**
     * Computes the logarithm of a double, to the base 2.
     */
    public static double log2(double d) {
	return Math.log(d) / LN_2;
    }

    /**
     * Returns the greatest common denominator of two integers.
     */
    public static int gcd(int x, int y) {
	if (x < 0) x = -x;
	if (y < 0) y = -y;

	if (x > y) {
	    return gcdInner(x, y);
	} else {
	    return gcdInner(y, x);
	}
    }

    /**
     * Returns the greatest common divisor of two positive integers x
     * and y, where x is at least y.
     */
    private static int gcdInner(int x, int y) {
	int r = x % y;
	return (r == 0) ? y : gcdInner(y, r);
    }

    /**
     * Computes the factorial.
     */
    public static long factorial(int n) {
	assert (n >= 0):
	    "Argument to factorial must be non-negative.";

	long accum = 1;
	for (int i = 1; i <= n; ++i) {
	    accum *= i;
	}
	return accum;
    }

    /**
     * Returns the natural logarithm of the factorial of n, computed
     * by summing the logs of the integers from 1 to n.
     */
    public static double logFactorial(int n) {
	double accum = 0;
	for (int i = 1; i <= n; ++i) {
	    accum += Math.log(i);
	}
	return accum;
    }

    /**
     * Computes the factorial of n using Stirling's approximation:
     *
     *   n! = n^n * e^-n * sqrt(2 * pi * n)
     */
    public static long stirlingFactorial(int n) {
	return (long) Math.exp(stirlingLogFactorial(n));
    }

    /**
     * Computes the natural logarithm of the factorial of n using
     * Stirling's approximation:
     *
     *   ln(n!) = n * ln(n) - n + ln(2 * pi * n) / 2
     */
    public static double stirlingLogFactorial(int n) {
	return n * Math.log(n) - n + Math.log(2 * Math.PI * n) / 2;
    }

    /**
     * Computes the "falling factorial" function:
     *
     *   f(n, k) = n * (n - 1) * ... * (n - k + 1)
     *
     * @see <a href="http://mathworld.wolfram.com/FallingFactorial.html">MathWorld</a>
     */
    public static long fallingFactorial(int n, int k) {
	if ((n < 0) || (k < 0)) {
	    throw new IllegalArgumentException("n and k must be non-negative: " + n + ", " + k + ".");
	}

	if (k == 0) {
	    return 0;
	} else if (n == 0) {
	    return 1;
	} else if (n < k) {
	    return 0;
	} else {
	    int p = 1;
	    for (int i = n; i > n - k; i -= 1) {
		p *= i;
	    }
	    return p;
	}
    }

    /**
     * Computes n choose k.
     */
    public static long choose(int n, int k) {
	/*
	 * Computing choose using factorials blows up due to
	 * overflow. We compute it using a recurrence instead.
	 */
	long accum = 1; // choose(n, 0)

	for (int i = 1; i <= k; i += 1) {
	    accum = (accum * ((n - i) + 1)) / i;
	}

	return accum;
    }

    /**
     * Computes the natural logarithm of n choose k by summing the
     * logs of (n - k + i) / i for i from 1 to k (inclusive).
     */
    public static double logChoose(int n, int k) {
	assert (n >= 0): "n must be non-negative: " + n;
	assert (k >= 0): "k must be non-negative: " + k;
	assert (n >= k): "n must be at least k: " + n + ", " + k;

	double accum = 0;

	for (int i = 1; i <= k; i += 1) {
	    accum += Math.log(((n - k) + i) / i);
	}

	return accum;
    }

    /**
     * Computes x^y.
     */
    public static long pow(long x, long y) {
	if (y < 0) {
	    throw new IllegalArgumentException("y must be non-negative: " + y + ".");
	} else if (y == 0) {
	    return 1;
	} else {
	    long pow = 1;

	    for (int i = 1; i <= y; i += 1) {

		pow *= x;

	    }

	    return pow;
	}
    }

    /**
     * Computes all permutations of the integers [1..n], returning
     * them as a List of IntVector.
     *
     * TODO: It should be possible to compute this iteratively.
     */
    public static List<IntVector> permute(int n) {
	List<IntVector> permutations = new LinkedList<IntVector>();
	List<IntVector> permutePred = permute(n - 1);

	for (int i = 0; i < n; i += 1) {
	    for (IntVector perm : permutePred) {
		permutations.add(perm.insert(i, n));
	    }
	}

	return permutations;
    }

    /**
     * Computes erf, the "error function". erf(z) is equivalent to the
     * lower incomplete gamma function at 1/2 and z^2 divided by the
     * gamma function at 1/2.
     *
     * @see <a href="http://mathworld.wolfram.com/Erf.html">MathWorld</a>
     * @see <a href="http://lib-www.lanl.gov/numerical/bookcpdf/c6-2.pdf">Numerical Recipes in C, Section 6.2</a>
     */
    public static double erf(double z) {
	double g = GammaFunction.lowerIncompleteGammaOverGamma(0.5, z * z);
	return (z < 0) ? (-g) : g;
    }

    private static double normalCDF(double x) {
	return (1 + erf(x / SQRT2)) / 2;
    }

    /**
     * Computes erfc, the "complementary error function". erfc(z) is
     * simply 1 - erf(z). Alternatively, erfc could be computed in
     * terms of the upper incomplete gamma function.
     *
     * @see <a href="http://mathworld.wolfram.com/Erfc.html">MathWorld</a>
     * @see <a href="http://lib-www.lanl.gov/numerical/bookcpdf/c6-2.pdf">Numerical Recipes in C, Section 6.2</a>
     */
    public static double erfc(double z) {
	return 1 - erf(z);
    }

    /**
     * Computes the binary entropy of a double:
     *
     * H(p) = - p * log2(p) - (1 - p) * log2(1 - p)
     */
    public static double entropy(double p) {
	assert ((0 <= p) && (p <= 1)): "p must be between 0 and 1, inclusive: " + p;

	if ((p == 0) || (p == 1)) {
	    return 0;
	} else {
	    return -(p * log2(p) + (1 - p) * log2(1 - p));
	}
    }

    /**
     * Computes the entropy of an array of doubles (frequency
     * values). Logarithms to the base 2 are used.
     *
     * @see <a href="http://mathworld.wolfram.com/Entropy.html">MathWorld</a>
     */
    public static double entropy(double[] p) {
	double h = 0.0;

	for (int i = 0; i < p.length; i += 1) {
	    if (p[i] != 0.0) {
		h -= (p[i] * log2(p[i]));
	    }
	}

	return h;
    }

    /**
     * Computes the relative entropy (or Kullback-Leibler distance) of
     * the probability distributions represented by two frequency
     * vectors.
     *
     * @see <a href="http://mathworld.wolfram.com/RelativeEntropy.html">MathWorld</a>
     */
    public static double relativeEntropy(double[] f1, double[] f2) {
	if (f1.length != f2.length) {
	    throw new IllegalArgumentException("Frequency arrays must be of equal length.");
	}

	double h = 0;

	for (int i = 0; i < f1.length; i += 1) {
	    if (f1[i] == 0.0) {
		continue;
	    } else if (f2[i] == 0.0) {
		h += Double.POSITIVE_INFINITY;
	    } else {
		h += (f1[i] * log2(f1[i] / f2[i]));
	    }
	}

	return h;
    }

    /**
     * Computes the logit function.
     *
     * @see <a href="http://mathworld.wolfram.com/LogitTransformation.html">MathWorld</a>
     */
    public static double logit(double p) {
	assert ((0 <= p) && (p <= 1)): "p must be between 0 and 1, inclusive: " + p;

	return Math.log(p / (1 - p));
    }


    /**
     * Computes the arithmetic-geometric mean (AGM) of two
     * numbers. The AGM is computed by repeatedly replacing the two
     * numbers by their arithmetic and geometric means, until the
     * numbers converge.
     */
    public static double arithmeticGeometricMean(double x, double y) {
	if ((x <= 0) || (y <= 0)) {
	    throw new IllegalArgumentException("Can't take arithmetic-geometric mean of a negative number: " + x + ", " + y + ".");
	}

	double EPS = 1e-9;
	double a, g;

	do {
	    a = (x + y) / 2;
	    g = Math.sqrt(x * y);
	    x = a;
	    y = g;
	} while (Math.abs(a - g) > g * EPS);

	return (a + g) / 2;
    }

}
