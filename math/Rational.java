package math;

/**
 * A rational number.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20050519
 */
public class Rational extends Number implements Comparable<Rational> {

    public static final Rational ZERO = new Rational(0, 1, false);
    public static final Rational ONE = new Rational(1, 1, false);

    private int p;
    private int q;

    private Rational(int p, int q, boolean reduce) {
	if (q == 0) {
	    throw new IllegalArgumentException("Denominator must be non-zero: " + q + ".");
	}

	if (q < 0) {
	    p = -p;
	    q = -q;
	}

	this.p = p;
	this.q = q;

	if (reduce) {
	    reduce();
	}
    }

    public Rational(int p, int q) {
	this(p, q, true);
    }

    /**
     * Reduces this rational number to simplest terms, dividing the
     * numerator and denominator by their greatest common denominator.
     */
    private void reduce() {
	if (p == 0) {
	    q = 1;
	} else {
	    int g = Lib.gcd(p, q);

	    p /= g;
	    q /= g;
	}
    }

    public String toString() {
	if (p == 0) {
	    return "0";
	} else if (q == 1) {
	    return Integer.toString(p);
	} else {
	    return p + "/" + q;
	}
    }

    public int compareTo(Rational r) {
	int a = this.p * r.q;
	int b = r.p * this.q;

	if (a < b) {
	    return -1;
	} else if (a > b) {
	    return 1;
	} else {
	    return 0;
	}
    }

    public Rational negate() {
	return new Rational(-p, q, false);
    }

    public Rational inverse() {
	if (p == 0) {
	    throw new IllegalStateException("Can't take inverse of 0/" + q + ".");
	} else {
	    return new Rational(q, p, false);
	}
    }

    public Rational abs() {
	if (p < 0) {
	    return new Rational(-p, q, false);
	} else {
	    return this;
	}
    }

    public Rational add(Rational r) {
	return new Rational(this.p * r.q + r.p * this.q, this.q * r.q);
    }

    public Rational subtract(Rational r) {
	return new Rational(this.p * r.q - r.p * this.q, this.q * r.q);
    }

    public Rational multiply(Rational r) {
	return new Rational(this.p * r.p, this.q * r.q);
    }

    public Rational divide(Rational r) {
	return new Rational(this.p * r.q, this.q * r.p);
    }

    // Methods specified by java.lang.Number

    public int intValue() {
	return p / q;
    }

    public long longValue() {
	return intValue();
    }

    public float floatValue() {
	return ((float) p) / ((float) q);
    }

    public double doubleValue() {
	return ((double) p) / ((double) q);
    }

}
