package math;

/**
 * The fast Fourier transform.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20041211
 */
public class FFT {

    /**
     * Computes the forward discrete Fourier transform of an array of
     * complex numbers.
     */
    public static Complex[] fft(Complex[] a) {
	return transform(a, false);
    }

    public static Complex[] fft(double[] d) {
	Complex[] a = new Complex[d.length];

	for (int i = 0; i < a.length; ++i) {
	    a[i] = new Complex(d[i], 0);
	}

	return fft(a);
    }

    /**
     * Computes the inverse discrete Fourier transform of an array of
     * complex numbers.
     */
    public static Complex[] ifft(Complex[] a) {
	Complex[] y = transform(a, true);
	int n = y.length;

	for (int i = 0; i < n; ++i) {
	    y[i] = y[i].divide(n);
	}

	return y;
    }

    /**
     * Computes the fast Fourier transform of an array of complex
     * numbers. Assumes the that length of the given array is a power
     * of 2. The algorithm is based on the pseudocode in Cormen,
     * Leiserson, and Rivest, "Introduction to Algorithms", p. 788.
     *
     * The inverse argument indicates whether or not to do an inverse
     * transform; note that in the inverse case, normalization is not
     * done here.
     */
    private static Complex[] transform(Complex[] a, boolean inverse) {
	int n = a.length;

	if (n == 1) {
	    return a;
	}

	Complex[] a0 = selectEven(a);
	Complex[] a1 = selectOdd(a);
	Complex[] y0 = transform(a0, inverse);
	Complex[] y1 = transform(a1, inverse);

	/*
	 * The first line, commented out, corresponds to the usage in
	 * CLR, where the principal nth root of unity is used for the
	 * forward transform and its inverse is used for the inverse
	 * transform. However, this gives results that don't match
	 * those of R or Octave: the sign of each imaginary part is
	 * switched. Using the second line fixes this.
	 *
	 * This is probably just a matter of convention; most likely
	 * the difference cancels out when you do a forward and an
	 * inverse transform.
	 */
// 	Complex zn = (inverse ? rootInv(n): root(n));
	Complex zn = (inverse ? root(n): rootInv(n));
	Complex z = Complex.ONE;
	Complex[] y = new Complex[n];

	for (int k = 0; k < n / 2; ++k) {
	    y[k] = y0[k].add(z.multiply(y1[k]));
	    y[k + n/2] = y0[k].subtract(z.multiply(y1[k]));
	    z = z.multiply(zn);
	}

	return y;
    }

    /**
     * Computes the principal complex nth root of unity.
     */
    private static Complex root(int n) {
	double d = (2 * Math.PI) / n;

	return new Complex(Math.cos(d), Math.sin(d));
    }

    /**
     * Computes the inverse of the principal complex nth root of
     * unity.
     */
    private static Complex rootInv(int n) {
	double d = (-2 * Math.PI) / n;

	return new Complex(Math.cos(d), Math.sin(d));
    }

    /**
     * Returns a new array containing the even-indexed elements of the
     * given array.
     */
    private static Complex[] selectEven(Complex[] a) {
	Complex[] y = new Complex[a.length / 2];

	for (int i = 0; i < y.length; ++i) {
	    y[i] = a[2 * i];
	}

	return y;
    }

    /**
     * Returns a new array containing the odd-indexed elements of the
     * given array.
     */
    private static Complex[] selectOdd(Complex[] a) {
	Complex[] y = new Complex[a.length / 2];

	for (int i = 0; i < y.length; ++i) {
	    y[i] = a[2 * i + 1];
	}

	return y;
    }

    public static void main(String[] args) {
	Complex[] a = new Complex[Integer.parseInt(args[0])];

	for (int i = 0; i < a.length; ++i) {
	    a[i] = new Complex(i, 0);
	}

	print(a);
	a = fft(a);
	print(a);
	a = ifft(a);
	print(a);
    }

    private static void print(Complex[] c) {
	System.out.print("[" + c[0]);

	for (int i = 1; i < c.length; ++i) {
	    System.out.print(", " + c[i]);
	}

	System.out.println("]");
    }

}
