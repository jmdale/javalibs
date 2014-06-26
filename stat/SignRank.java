package stat;

/**
 * The Wilcoxon signed-rank distribution. Adapted from R 1.9.1,
 * src/nmath/signrank.c.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20040904
 */
public class SignRank {

    /**
     * Working space used by csignrank.
     */
    private static double[][] w;

    /**
     * w will always be of size at least (SIGNRANK_MAX + 1) by
     * (SIGNRANK_MAX + 1).
     */
    private static final int SIGNRANK_MAX = 50;

    /**
     * The current allocated size of w is (allocated_n + 1) by
     * (allocated_n + 1).
     */
    private static int allocated_n;

    /**
     * Sets w to null and allocated_n to 0.
     */
    private static void w_free() {
	w = null;
	allocated_n = 0;
    }

    /**
     * If the current allocated size of w (allocated_n) is less than
     * n, frees w and allocates a new w of size at least n.
     */
    private static void w_init_maybe(int n) {
	if ((w != null) && (n > allocated_n)) {
	    w_free();
	}

	if (w == null) {
	    n = Math.max(n, SIGNRANK_MAX);
	    allocated_n = n;
	    w = new double[n + 1][];
	}
    }

    /**
     * Performs some funky voodoo and dumps the results into w.
     */
    private static double csignrank(int k, int n) {
	int u = n * (n + 1) / 2;
	int c = u / 2;

	if ((k < 0) || (k > u)) {
	    return 0;
	}

	if (k > c) {
	    k = u - k;
	}

	if (w[n] == null) {
	    w[n] = new double[c + 1];
	    for (int i = 0; i <= c; ++i) {
		w[n][i] = -1;
	    }
	}

	if (w[n][k] < 0) {
	    if (n == 0) {
		w[n][k] = (k == 0) ? 1 : 0;
	    } else {
		w[n][k] = csignrank(k - n, n - 1) + csignrank(k, n - 1);
	    }
	}

	return w[n][k];
    }

    /**
     * The density function.
     *
     * @param x The sum of the ranks of the positive values in the sample.
     * @param n The sample size.
     * @param give_log Whether or not the natural logarithm of the density should be returned.
     */
    public static double dsignrank(int x, int n, boolean give_log) {
	assert (n > 0):
	    "Sample size n must be positive: " + n + ".";

	if ((x < 0) || (x > (n * (n + 1) / 2))) {
	    return (give_log ? Double.NEGATIVE_INFINITY : 0);
	}

	w_init_maybe(n);
	double log_d = Math.log(csignrank(x, n)) - n * Math.log(2);
	return (give_log ? log_d : Math.exp(log_d));
    }

    /**
     * The cumulative distribution function.
     *
     * @param x The sum of the ranks of the positive values in the sample.
     * @param n The sample size.
     * @param lower_tail Whether the lower or upper tail probability should be returned.
     * @param log_p Whether or not the natural logarithm of the density should be returned.
     */
    public static double psignrank(int x, int n, boolean lower_tail, boolean log_p) {
	assert (n > 0):
	    "Sample size n must be positive: " + n + ".";

	if (x < 0) {
	    return (log_p ? Double.NEGATIVE_INFINITY : 0);
	} else if (x > (n * (n + 1) / 2)) {
	    return (log_p ? 0 : 1);
	}

	w_init_maybe(n);

	double f = Math.exp(-n * Math.log(2));
	double p = 0;

	if (x <= (n * (n + 1) / 4.0)) {
	    for (int i = 0; i <= x; ++i) {
		p += csignrank(i, n) * f;
	    }
	} else {
	    x = n * (n + 1) / 2 - x;
	    for (int i = 0; i < x; ++i) {
		p += csignrank(i, n) * f;
	    }
	    lower_tail = !lower_tail;
	}

	if (lower_tail) {
	    return (log_p ? Math.log(p) : p);
	} else {
	    return (log_p ? Math.log(1 - p) : (1 - p));
	}
    }

    /**
     * The quantile function.
     *
     * @param x The quantile to compute.
     * @param n The sample size.
     * @param lower_tail Whether the given quantile should be taken with respect to the lower or upper tail.
     * @param log_p Whether or not the given quantile is on the natural logarithm scale.
     */
    public static int qsignrank(double p, int n, boolean lower_tail, boolean log_p) {
	assert (n > 0):
	    "Sample size n must be positive: " + n + ".";

	if (log_p) {
	    assert (p <= 0):
		"Log quantile must not be positive: " + p + ".";
	} else {
	    assert ((0 <= p) && (p <= 1)):
		"Quantile must be between 0 and 1: " + p + ".";
	}

	if (log_p) {
	    if (p == Double.NEGATIVE_INFINITY) {
		return 0;
	    } else if (p == 0) {
		return n * (n + 1) / 2;
	    }
	} else {
	    if (p == 0) {
		return 0;
	    } else if (p == 1) {
		return n * (n + 1) / 2;
	    }
	}

	if (log_p || (!lower_tail)) {
	    p = (log_p ? Math.exp(p) : p);
	}

	w_init_maybe(n);

	double f = Math.exp(-n * Math.log(2));
	double P = 0;
	int q = 0;

	/*
	 * This comes from the i386 Linux GCC float.h. I don't have
	 * any idea why it comes into play here, but assuming Java's
	 * double arithmetic is similar, I guess I might as well keep
	 * this.
	 */
	double DBL_EPSILON = 2.2204460492503131e-16;

	if (p <= 0.5) {
	    p = p - 10 * DBL_EPSILON;
	    while (true) {
		P += csignrank(q, n) * f;
		if (P >= p) {
		    break;
		}
		q += 1;
	    }
	} else {
	    p = 1 - p + 10 * DBL_EPSILON;
	    while (true) {
		P += csignrank(q, n) * f;
		if (P > p) {
		    q = n * (n + 1) / 2 - q;
		    break;
		}
		q += 1;
	    }
	}

	return q;
    }

    /**
     * A random variate from the distribution. For each rank i from 1
     * to n, flip a fair coin; return the sum of the ranks for which
     * the coin lands heads.
     *
     * @param n The sample size.
     */
    public static int rsignrank(int n) {
	assert (n > 0):
	    "Sample size n must be positive: " + n + ".";

	if (n == 0) {
	    return 0;
	}

	int r = 0;

	for (int i = 1; i <= n; ++i) {
	    if (Math.random() < 0.5) {
		r += i;
	    }
	}

	return r;
    }

}
