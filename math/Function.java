package math;

/**
 * A function from doubles to doubles.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070814
 */
public abstract class Function {

    public abstract double apply(double x);

    /**
     * Returns a constant function which returns y when applied to any
     * value.
     */
    public static Function constant(final double y) {
	return new Function() {
		public double apply(double x) {
		    return y;
		}
	    };
    }

    public static final Function ZERO = constant(0);

    public Function add(final Function f) {
	return new Function() {
		public double apply(double x) {
		    return this.apply(x) + f.apply(x);
		}
	    };
    }

    public Function subtract(final Function f) {
	return new Function() {
		public double apply(double x) {
		    return this.apply(x) - f.apply(x);
		}
	    };
    }

    public Function multiply(final Function f) {
	return new Function() {
		public double apply(double x) {
		    return this.apply(x) * f.apply(x);
		}
	    };
    }

    public Function divide(final Function f) {
	return new Function() {
		public double apply(double x) {
		    return this.apply(x) / f.apply(x);
		}
	    };
    }

    /**
     * Computes the integral of this function between a and b,
     * evaluating the function at n points.
     */
    public double integrate(double a, double b, int n) {
	assert (a < b): "a >= b: " + a + ", " + b;
	assert (n > 1): "n < 2: " + n;

	double h = (b - a) / (n - 1);
	double s = (apply(a) + apply(b)) / 2;

	for (int i = 1; i < n - 1; ++i) {
	    s += apply(a + i * h);
	}

	return s * h;
    }

}
