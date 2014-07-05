package stat.dist;

import math.Complex;
import math.DoubleVector;
import math.FFT;

/**
 * Code for estimating a kernel density, adapted from the code for the
 * R density() function.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20041211
 */
class KernelDensityEstimator {

    /**
     * The minimum number of grid points at which to evaluate the
     * density.
     */
    private static final int NMIN = 512;

    /**
     * Call estimate with the default number of grid points.
     */
    static KernelDensity estimate(DoubleVector x) {
	return estimate(x, NMIN);
    }

    /**
     * @param x The locations of the kernels.
     * @param n The number of grid points at which to evaluate the density.
     */
    static KernelDensity estimate(DoubleVector x, int n) {
	/* Number of bandwidths to extend the evaluation past the observed data points. */
	double cut = 3;

	/* Ensure that the number of points is a power of two, in order to make the FFT routine happy. */	 
	if (n > NMIN) {
	    n = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
	} else {
	    n = NMIN;
	}

	double bw = Bandwidth.nrd(x);
	double from = x.min() - cut * bw;
	double to = x.max() + cut * bw;
	double lo = from - 4 * bw;
	double hi = to + 4 * bw;

	double[] y = massdist(x.data(), lo, hi, n);
	double[] kords = kords(lo, hi, n);
	evaluateKernel(kords, bw);

	double[] density = evaluateDensity(y, kords);
	double[] xords = xords(lo, hi, n);

	return new KernelDensity(xords, density);
    }

    private static double[] evaluateDensity(double[] y, double[] kords) {
	Complex[] ytrans = FFT.fft(y);
	Complex[] ktrans = FFT.fft(kords);
	Complex[] conv = new Complex[y.length];

	for (int i = 0; i < conv.length; ++i) {
	    conv[i] = ytrans[i].multiply(ktrans[i].conjugate());
	}

	Complex[] itrans = FFT.ifft(conv);
	double[] d = new double[itrans.length];

	for (int i = 0; i < d.length; ++i) {
	    d[i] = itrans[i].real();
	}

	return d;
    }

    /**
     * Distributes the mass of the observed data points x along a grid
     * of n points between lo and hi.
     */
    private static double[] massdist(double[] x, double lo, double hi, int n) {
	double[] y = new double[2 * n];
	int ixmin = 0;
	int ixmax = n - 2;
	double xmass = 1.0 / x.length;
	double xdelta = (hi - lo) / (n - 1);

	for (int i = 0; i < y.length; ++i) {
	    y[i] = 0;
	}

	for (int i = 0; i < x.length; ++i) {
	    double xpos = (x[i] - lo) / xdelta;
	    int ix = (int) xpos;
	    double fx = xpos - ix;

	    if ((ixmin <= ix) && (ix <= ixmax)) {
		y[ix] += (1 - fx);
		y[ix + 1] += fx;
	    } else if (ix == -1) {
		y[0] += fx;
	    } else if (ix == ixmax + 1) {
		y[ix] += (1 - fx);
	    }
	}

	for (int i = 0; i < n; i += 1) {
	    y[i] *= xmass;
	}

	return y;
    }

    private static double[] kords(double lo, double hi, int n) {
	double[] kords = new double[2 * n];
	double delta = 2 * (hi - lo) / (2 * n - 1);

	for (int i = 0; i < 2 * n; ++i) {
	    kords[i] = i * delta;
	}

	for (int i = n + 1, j = n - 1; i < 2 * n; ++i, --j) {
	    kords[i] = -kords[j];
	}

	return kords;
    }

    private static double[] xords(double lo, double hi, int n) {
	double[] xords = new double[n];
	double delta = (hi - lo) / (n - 1);

	for (int i = 0; i < n; ++i) {
	    xords[i] = lo + i * delta;
	}

	return xords;
    }

    private static void evaluateKernel(double[] kords, double bw) {
	for (int i = 0; i < kords.length; ++i) {
	    kords[i] = gaussian(kords[i], bw);
	}
    }

    private static double gaussian(double x, double bw) {
	return Math.exp(-0.5 * (x * x) / (bw * bw)) / (Math.sqrt(2 * Math.PI) * bw);
    }

}
