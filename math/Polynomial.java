package math;

import java.util.Arrays;

/**
 * A polynomial with double coefficients.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20041210
 */
public class Polynomial extends Function {

    public double[] coeff;

    public Polynomial(double[] coeff) {
	this.coeff = coeff;
    }

    /**
     * Evaluates this polynomial using Horner's method.
     */
    public double apply(double x) {
	double result = coeff[coeff.length - 1];

	for (int i = coeff.length - 2; i >= 0; --i) {
	    result *= x;
	    result += coeff[i];
	}

	return result;
    }

    /**
     * Adds the given polynomial to this polynomial.
     */
    public Polynomial add(Polynomial p) {
	double[] a, b;

	if (this.coeff.length < p.coeff.length) {
	    a = zeroExtend(this.coeff, p.coeff.length);
	    b = p.coeff;
	} else {
	    a = this.coeff;
	    b = zeroExtend(p.coeff, this.coeff.length);
	}

	double[] c = new double[a.length];

	for (int i = 0; i < c.length; ++i) {
	    c[i] = a[i] + b[i];
	}

	return new Polynomial(c);
    }

    /**
     * Subtracts the given polynomial from this polynomial.
     */
    public Polynomial subtract(Polynomial p) {
	double[] a, b;

	if (this.coeff.length < p.coeff.length) {
	    a = zeroExtend(this.coeff, p.coeff.length);
	    b = p.coeff;
	} else {
	    a = this.coeff;
	    b = zeroExtend(p.coeff, this.coeff.length);
	}

	double[] c = new double[a.length];

	for (int i = 0; i < c.length; ++i) {
	    c[i] = a[i] - b[i];
	}

	return new Polynomial(c);
    }

    /**
     * Multiplies this polynomial by the given polynomial.
     */
    public Polynomial multiply(Polynomial p) {
	int d = this.coeff.length + p.coeff.length - 1;

	double[] a = zeroExtend(this.coeff, d);
	double[] b = zeroExtend(p.coeff, d);
	double[] c = new double[d];

	for (int k = 0; k < c.length; ++k) {
	    for (int i = 0, j = k; i <= k && j >= 0; ++i, --j) {
		c[k] += a[i] * b[j];
	    }
	}

	return new Polynomial(c);
    }

    /**
     * Returns a new polynomial which is the derivative of this
     * polynomial.
     */
    public Polynomial derivative() {
	double[] dCoeff = new double[this.coeff.length - 1];

	for (int i = 1; i < this.coeff.length; ++i) {
	    dCoeff[i-1] = i * this.coeff[i];
	}

	return new Polynomial(dCoeff);
    }

    /**
     * Returns a new array of the given length, containing the
     * elements of the given array followed by zeroes.
     */
    private static double[] zeroExtend(double[] a, int n) {
	double[] b = new double[n];

	System.arraycopy(a, 0, b, 0, a.length);
	Arrays.fill(b, a.length, b.length, 0);

	return b;
    }

}
