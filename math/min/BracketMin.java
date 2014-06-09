package math.min;

import math.Function;

/**
 * Finds a bracket containing a (local) minimum of a function. Given a
 * function and two initial points, we follow the function downhill
 * until it turns uphill. When that happens, the last two downhill
 * points and the uphill point are a bracket.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070820
 */
class BracketMin {

    Function f;

    double a;
    double fa;
    double b;
    double fb;

    BracketMin(Function f, double a, double b) {
	this.f = f;

	double fa = f.apply(a);
	double fb = f.apply(b);

	if (fb < fa) {
	    this.a = a;
	    this.b = b;
	    this.fa = fa;
	    this.fb = fb;
	} else if (fb > fa) {
	    this.a = b;
	    this.b = a;
	    this.fa = fb;
	    this.fb = fa;
	} else {
	    throw new IllegalArgumentException(String.format("Ordinates are equal at initial abscissas: (%s, %s), (%s, %s)\n",
							     a, fa, b, fb));
	}
    }

    private static final double PHI = (1 + Math.sqrt(5)) / 2;
    private static final int MAX_STEPS = 50;

    Bracket findMin() {
	int iter = 0;

	while (iter < MAX_STEPS) {
	    double c = b + PHI * (b - a);
	    double fc = f.apply(c);

	    if (fc > fb) {
		return new Bracket(a, b, c, fa, fb, fc);
	    } else if (fc < fb) {
		a = b;
		b = c;
		fa = fb;
		fb = fc;

		iter++;
	    } else {
		throw new IllegalStateException(String.format("Ordinates are equal at current abscissas: (%s, %s), (%s, %s)\n",
							      b, fb, c, fc));
	    }
	}

	throw new RuntimeException("Couldn't bracket minimum; function may be decreasing.");
    }

}
