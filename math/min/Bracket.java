package math.min;

/**
 * A bracket containing a (local) minimum of a function. A bracket for
 * a minimum of a function f consists of a set of three points, a, b,
 * and c, such that a < b < c, f(b) < f(a), and f(b) < f(c).
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070919
 */
class Bracket {

    double a, b, c, fa, fb, fc;

    /**
     * Creates a new bracket given the abscissa points a, b, and c,
     * and the ordinates fa, fb, and fc (the values at a, b, and c of
     * the function being minimized).
     */
    Bracket(double a, double b, double c, double fa, double fb, double fc) {
	if ((a < b) && (b < c)) {
	    this.a = a;
	    this.b = b;
	    this.c = c;
	    this.fa = fa;
	    this.fb = fb;
	    this.fc = fc;
	} else if ((a > b) && (b > c)) {
	    this.a = c;
	    this.b = b;
	    this.c = a;
	    this.fa = fc;
	    this.fb = fb;
	    this.fc = fa;
	} else {
	    throw new IllegalArgumentException(String.format("Abscissa points are not strictly monotonic: %s, %s, %s\n",
							     a, b, c));
	}

	if (!((fb < fa) && (fb < fc))) {
	    throw new IllegalArgumentException(String.format("Middle ordinate is not below outer ordinates: %s, %s, %s\n",
							     fa, fb, fc));
	}
    }

    double min() {
	return b;
    }

    /**
     * Returns the length of this bracket: the difference between the
     * largest and smallest abscissas.
     */
    double length() {
	return c - a;
    }

    /**
     * Returns the "scale" of this bracket, defined as the sum of the
     * absolute values of the outer abscissas.
     */
    double scale() {
	return Math.abs(a) + Math.abs(c);
    }

    public String toString() {
	return String.format("[(%s, %s), (%s, %s), (%s, %s)]", a, fa, b, fb, c, fc);
    }

}
