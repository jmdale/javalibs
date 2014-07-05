package stat.dist;

import math.DoubleVector;

import java.util.Arrays;

/**
 * A distribution represent by a kernel density.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public class KernelDensity extends DoubleDistribution {

    /**
     * The points at which the density was evaluated.
     */
    double[] x;

    /**
     * The density values.
     */
    double[] y;

    /**
     * The cumulative distribution.
     */
    double[] dist;

    KernelDensity(double[] x, double[] y) {
	this.x = x;
	this.y = y;
    }

    public void print() {
	for (int i = 0; i < x.length; ++i) {
	    System.out.println(x[i] + "\t" + y[i]);
	}
    }

    public double density(double d) {
	if ((d < x[0]) || (d > x[x.length - 1])) {
	    return 0;
	}

	int i = Arrays.binarySearch(this.x, d);

	if (i >= 0) {
	    return y[i];
	} else {
	    i = -(i + 1);
	    return interpolateDensity(d, i - 1);
	}
    }

    /**
     * Linearly interpolates between y[i] and y[i+1] according to d.
     */
    private double interpolateDensity(double d, int i) {
	double dx = (d - x[i]) / (x[i + 1] - x[i]);
	return (1 - dx) * y[i] + dx * y[i + 1];
    }

    public double distribution(double d) {
	if (dist == null) {
	    computeDist();
	}

	if (d < x[0]) {
	    return 0;
	} else if (d > x[x.length - 1]) {
	    return 1;
	}

	int i = Arrays.binarySearch(this.x, d);

	if (i >= 0) {
	    return dist[i];
	} else {
	    i = -(i + 1);
	    return interpolateDistribution(d, i - 1);
	}
    }

    /**
     * Linearly interpolates between dist[i] and dist[i+1] according
     * to d.
     */
    private double interpolateDistribution(double d, int i) {
	double dx = (d - x[i]) / (x[i + 1] - x[i]);
	return (1 - dx) * dist[i] + dx * dist[i + 1];
    }

    private void computeDist() {
	this.dist = new double[x.length];
	dist[0] = y[0];

	for (int i = 1; i < dist.length; ++i) {
	    dist[i] = dist[i - 1] + (x[i] - x[i - 1]) * (y[i] + y[i + 1]) / 2;
	}
    }

    /**
     * Do a lookup in dist and interpolate against x.
     */
    public double quantile(double p) {
	assert ((0 <= p) && (p <= 1)): "p must be in [0, 1]: " + p;

	if (dist == null) {
	    computeDist();
	}

	if (p < dist[0]) {
	    return Double.NEGATIVE_INFINITY;
	} else if (p > dist[dist.length - 1]) {
	    return Double.POSITIVE_INFINITY;
	}

	int i = Arrays.binarySearch(dist, p);

	if (i >= 0) {
	    return x[i];
	} else {
	    i = -(i + 1);
	    return interpolateQuantile(p, i - 1);
	}
    }

    /**
     * Linearly interpolates between x[i] and x[i+1] according to p.
     */
    private double interpolateQuantile(double p, int i) {
	double dp = (p - dist[i]) / (dist[i + 1] - dist[i]);
	return (1 - dp) * x[i] + dp * x[i + 1];
    }

    /**
     * Returns the mode of the estimated kernel density.
     */
    public double mode() {
	double yMax = y[0];
	double xMax = x[0];

	for (int i = 1; i < y.length; ++i) {
	    if (y[i] > yMax) {
		yMax = y[i];
		xMax = x[i];
	    }
	}

	return xMax;
    }

    public double min() {
	return x[0];
    }

    public double max() {
	return x[x.length - 1];
    }

    public double mean() {
	double m = 0;

	for (int i = 1; i < x.length; ++i) {
	    // x * p(x) dx
	    m += average(x[i], x[i-1]) * average(y[i], y[i-1]) * (x[i] - x[i-1]);
	}

	return m;
    }

    public double variance() {
	double m = mean();
	double v = 0;

	for (int i = 1; i < x.length; ++i) {
	    // (x - E[x])^2 * p(x) dx
	    v += square(average(x[i], x[i-1]) - m) * average(y[i], y[i-1]) * (x[i] - x[i-1]);
	}

	return v;
    }

    private static double average(double x, double y) {
	return (x + y) / 2;
    }

    private static double square(double x) {
	return x * x;
    }

    public static KernelDensity estimate(DoubleVector x, int n) {
	return KernelDensityEstimator.estimate(x, n);
    }

    public static void main(String[] args) {
	GammaDistribution gd = new GammaDistribution(4, 0.25);
	DoubleVector x = gd.random(100);
	KernelDensity kd = estimate(x, 100);
    }

}
