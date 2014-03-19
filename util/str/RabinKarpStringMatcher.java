package util.str;

/**
 * An implementation of the Rabin-Karp string matching algorithm. This
 * algorithm is similar to the "naive" algorithm, except that instead
 * of directly comparing the pattern to successive substrings of the
 * text, a "signature" is computed for the pattern and for each
 * successive substring of the text. Matching signatures invoke a full
 * comparison, whereas non-matching signatures signal no match. A key
 * insight is that, although the time needed to compute the signature
 * is proportional to the length of the pattern, "shifting" the
 * signature from one position to the next in the text can be done in
 * constant time.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140318
 */
public class RabinKarpStringMatcher extends StringMatcher {

    /**
     * The "radix" used in the Rabin-Karp algorithm; since we want to
     * be able to match arbitrary strings, this is Character.MAX_VALUE
     * + 1.
     */
    public static final int D = Character.MAX_VALUE + 1;

    /**
     * Q is the largest prime number such that D * Q <
     * Integer.MAX_VALUE.
     */
    public static final int Q = 32749;

    private String pattern;
    private String text;
    private int start;

    /**
     * The length of the pattern.
     */
    private int m;

    /**
     * The length of the text.
     */
    private int n;

    private int h;

    /**
     * The signature of the pattern.
     */
    private int p;

    /**
     * The (current) signature of the text.
     */
    private int t;

    public RabinKarpStringMatcher(String pattern, String text, int start) {
	if (pattern.length() > text.length()) {
	    throw new IllegalArgumentException("Pattern must be no longer than text.");
	}

	if (start < 0) {
	    throw new IllegalArgumentException("Start index must be non-negative.");
	}

	this.pattern = pattern;
	this.text = text;
	this.start = start;
	this.m = pattern.length();
	this.n = text.length();
	this.h = modExp(D, m, Q);
	this.p = 0;
	this.t = 0;

	for (int i = 0; i < m; i += 1) {

	    p = (D * p + pattern.charAt(i)) % Q;
	    t = (D * t + text.charAt(start + i)) % Q;

	}
    }

    public RabinKarpStringMatcher(String pattern, String text) {
	this(pattern, text, 0);
    }

    public int start() {
	return start;
    }

    public void reset() {
	start = 0;
	h = modExp(D, m, Q);
	p = 0;
	t = 0;

	for (int i = 0; i < m; i += 1) {
	    p = (D * p + pattern.charAt(i)) % Q;
	    t = (D * t + text.charAt(start + i)) % Q;
	}
    }

    public int nextMatch() {
	int match = -1;

	for (int i = start; i <= n - m; i += 1) {
	    if ((p == t) && trueMatch(i)) {
		match = i;
		start = i + 1;
	    }

	    if (i < n - m) {
		t = (t + Q - ((text.charAt(i) * h) % Q)) % Q;
		t = (t * D) % Q;
		t = (t + text.charAt(i + m)) % Q;

// 		t = mod((D * (t - text.charAt(i) * h) + text.charAt(i + m)), Q);
	    }

	    if (match > -1) {
		return match;
	    }
	}

	return -1;
    }

    private boolean trueMatch(int i) {
	for (int j = 0; j < m; j += 1) {
	    if (pattern.charAt(j) != text.charAt(i + j)) {
		return false;
	    }
	}
	return true;
    }

    /**
     * Computes a mod q. q is assumed to be positive. In the case
     * where a is positive, this is simply (a % q). In the case where
     * a is negative, (a % q) would be a negative remainder, and so we
     * take q + (a % q).
     */
    private static int mod(int a, int q) {
	if (a >= 0) {
	    return a % q;
	} else {
	    return ((((-a) / q) + 1) * q) + a;
	}
    }

    /**
     * Returns d^(m-1) mod q.
     */
    private static int modExp(int d, int m, int q) {
	int exp = 1;
	for (int i = 1; i < m; i += 1) {
	    exp = (exp * d) % q;
	}
	return exp;
    }
}
