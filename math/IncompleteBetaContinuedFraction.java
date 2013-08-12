
package math;

/**
 * Continued fraction used in computing the incomplete beta
 * function. This formula is given in Equations 6.4.5 and 6.4.6 of
 * <em>Numerical Recipes in C</em>.
 *
 * @see <a href="http://lib-www.lanl.gov/numerical/bookcpdf/c6-4.pdf">Numerical Recipes in C, Section 6.4</a>
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20050328
 */
public class IncompleteBetaContinuedFraction extends ContinuedFraction {

    private double a;

    private double b;

    private double x;


    public IncompleteBetaContinuedFraction(double a, double b, double x) {

	assert (a > 0): "a must be greater than 0: " + a + ".";
	assert (b > 0): "b must be greater than 0: " + b + ".";
	assert ((0 <= x) && (x <= 1)): "x must be in [0, 1]: " + x + ".";

	this.a = a;
	this.b = b;
	this.x = x;

    }


    public double initialTerm() {

	return 0;

    }

    public double nextUpper() {

	if (n == 1) {

	    return 1;

	} else {

	    int i = n - 1;

	    if ((i % 2) == 0) {

		int m = i / 2;

		return ((m * (b - m) * x) / ((a + 2 * m) * (a + 2 * m - 1)));

	    } else {

		int m = (i - 1) / 2;

		return -(((a + m) * (a + b + m) * x) / ((a + 2 * m) * (a + 2 * m + 1)));

	    }

	}

    }

    public double nextLower() {

	return 1;

    }


    public static double eval(double a, double b, double x) {

	IncompleteBetaContinuedFraction ibcf = new IncompleteBetaContinuedFraction(a, b, x);

	return ibcf.evalWallis(100, 1e-7);

    }

}
