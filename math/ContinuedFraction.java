
package math;

/**
 * An abstract class representing a continued fraction. Subclasses
 * implement "generator" methods which return the upper and lower
 * terms of the continued fraction. These methods are called by the
 * methods in this class, which implement general procedures for
 * evaluating continued fractions.
 *
 * @see <a href="http://lib-www.lanl.gov/numerical/bookcpdf/c5-2.pdf">Numerical Recipes in C, Section 5.2</a>
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20050328
 */
public abstract class ContinuedFraction {

    /**
     * Index of the next terms which are to be returned by the
     * nextUpper and nextLower methods.
     */
    protected int n;

    /**
     * Returns the initial term in the continued fraction; this is the
     * part preceding the actual fraction terms. NR calls this b_0.
     */
    public abstract double initialTerm();

    /**
     * Returns the next "upper" term in the continued fraction; this
     * is the numerator portion of each fraction. NR calls these a_i,
     * i = 1, 2, ...
     */
    public abstract double nextUpper();

    /**
     * Returns the next "lower" term in the continued fraction; this
     * is the denominator portion of each fraction. NR calls these
     * b_i, i = 1, 2, ...
     */
    public abstract double nextLower();


    /**
     * Evaluates this continued fraction using the Wallis method with
     * normalization, as described on pg. 170 of <em>Numerical Recipes
     * in C<em>. Evaluating continues until maxIter iterations of the
     * recurrence updates are performed, or when the relative change
     * in the value is less than eps.
     */
    public double evalWallis(int maxIter, double eps) {

	double A0 = 1, B0 = 0, f0 = A0 / B0;
	double A1 = initialTerm(), B1 = 1, f1 = A1 / B1;
	n = 1;

	while (n <= maxIter) {

	    // Get the next terms in the fraction.
	    double a = nextUpper(), b = nextLower();
	    // Compute the next terms in the recurrence.
	    double A2 = b * A1 + a * A0, B2 = b * B1 + a * B0, f2 = A2 / B2;

	    A0 = A1; B0 = B1; f0 = f1;
	    A1 = A2; B1 = B2; f1 = f2;
	    n += 1;

	    // Check whether the relative change in the value in this
	    // iteration is less than eps.
	    if (Math.abs(f1 - f0) < eps * Math.abs(f0)) {

		return f1;

	    }

	    // Normalize the recurrence terms.
	    if (B1 != 0) {

		A0 /= B1; B0 /= B1;
		A1 /= B1; B1 /= B1;

	    }

	}

	return f1;

    }


    public static void main(String[] args) {

	/* Test continued fraction evaluation using the tangent function. */
	for (int i = 1; i <= 999; i += 1) {

	    double x = ((Math.PI * i) / 1000) - (Math.PI / 2);
	    TangentContinuedFraction tcf = new TangentContinuedFraction(x);
	    double tanx = tcf.evalWallis(100, 1e-5);
	    System.out.format("%f\t%f\n", x, tanx);

	}

    }


    /**
     * A ContinuedFraction class which computes the value of the
     * tangent function. Used to test methods for evaluating continued
     * fractions.
     */
    static class TangentContinuedFraction extends ContinuedFraction {

	/**
	 * The argument to tan.
	 */
	private double x;

	TangentContinuedFraction(double x) {

	    this.x = x;

	}

	public double initialTerm() {

	    return 0;

	}

	public double nextUpper() {

	    return (n == 1) ? x : (-(x*x));

	}

	public double nextLower() {

	    return 2 * n - 1;

	}

    }

}
