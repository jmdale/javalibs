package math.min;

import math.DoubleVector;
import math.Function;
import math.VectorFunctional;

/**
 * Code for function minimization using only gradients, without
 * function evaluations.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20071022
 */
public class GradMin {

    /**
     * Finds a bracket containing a root of f.
     */
    public static double[] bracketRoot(Function f) {
	return bracketRoot(f, 0, 1, f.apply(0), f.apply(1));
    }

    /**
     * Finds a bracket containing a root of f. The initial bracket
     * [lo, hi] (with function values fLo and fHi) is expanded until
     * the endpoints have opposite sign.
     */
    private static double[] bracketRoot(Function f, double lo, double hi, double fLo, double fHi) {
	dump("bracket", lo, hi, fLo, fHi);

	if (fLo * fHi <= 0) { // either fLo or fHi is 0, or fLo and fHi differ in sign
	    return new double[]{lo, hi};
	} else {
	    double next = hi + 2 * (hi - lo);
	    double fNext = f.apply(next);

	    return bracketRoot(f, hi, next, fHi, fNext);
	}
    }

    /**
     * Finds a root of f, to a relative precision of eps. A bracket
     * containing the root is returned.
     */
    public static double[] bisectRoot(Function f, double eps) {
	double[] bracket = bracketRoot(f);
	return bisectRoot(f, bracket[0], bracket[1], f.apply(bracket[0]), f.apply(bracket[1]), eps);
    }

    /**
     * Finds a root of f, to a relative precision of eps. [lo, hi] is
     * a bracket containing the root; fLo and fHi are the function
     * values at the endpoints of the bracket. If the bracket is small
     * enough, it is returned; otherwise, the bracket is cut in half
     * and we recur on the half whose endpoints have opposite sign.
     */
    private static double[] bisectRoot(Function f, double lo, double hi, double fLo, double fHi, double eps) {
	dump("bisect", lo, hi, fLo, fHi);

	if (hi - lo <= eps * (1 + Math.min(Math.abs(lo), Math.abs(hi)))) {
	    return new double[]{lo, hi};
	} else {
	    double mid = (lo + hi) / 2;
	    double fMid = f.apply(mid);

	    if (fLo * fMid <= 0) {
		return bisectRoot(f, lo, mid, fLo, fMid, eps);
	    } else {
		return bisectRoot(f, mid, hi, fMid, fHi, eps);
	    }
	}
    }

    /**
     * Starting at point p, finds the minimum in direction q of the
     * function whose gradient is grad, to precision eps. We use the
     * fact that at the desired point, the gradient will be
     * perpendicular to the direction in which we're searching.
     */
    public static DoubleVector lineMin(final VectorFunctional grad, final DoubleVector p, final DoubleVector q, double eps) {
	Function f = new Function() {
		public double apply(double x) {
		    DoubleVector v = p.add(q.multiply(x));
		    return q.dotProduct(grad.apply(v));
		}
	    };

	double[] root = bisectRoot(f, eps);
	double x0 = (root[0] + root[1]) / 2;

	return p.add(q.multiply(x0));
    }

    /**
     * Starting at point p, finds the minimum of the function whose
     * gradient is grad, to precision eps. This is done by gradient
     * descent: we repeatedly perform line minimization in the
     * direction of the gradient starting from the current point.
     */
    public static DoubleVector gradMin(VectorFunctional grad, DoubleVector p, double eps) {
	int iters = 0;

	while (true) {
	    DoubleVector gradP = grad.apply(p);

	    System.err.println("p:\t" + p);
	    System.err.println("grad:\t" + gradP);
	    System.err.println("norm:\t" + gradP.norm());

	    DoubleVector pNew = lineMin(grad, p, gradP, eps);
	    System.err.println("delta:\t" + (pNew.subtract(p).norm() / p.norm()));
	    iters++;

	    if ((pNew.subtract(p).norm() <= p.norm() * eps) || (iters >= 20)) {
		return p;
	    } else {
		p = pNew;
	    }
	}
    }

    /**
     * Starting at point p, finds the minimum of the function whose
     * gradient is grad, to precision eps. This is done by the
     * conjugate gradient method: we repeatedly perform line
     * minimization in a sequence of conjugate directions, starting
     * with the gradient at the initial point.
     */
//     public static DoubleVector conjGradMin(VectorFunctional grad, DoubleVector p, double eps) {

// 	int iters = 0;
// 	DoubleVector g = grad.apply(p);
// 	DoubleVector h = g;

// 	while (true) {

// 	    System.err.println("p:\t" + p);
// 	    System.err.println("g:\t" + g);
// 	    System.err.println("h:\t" + h);

// 	    DoubleVector pNew = lineMin(grad, p, h, eps);
// 	    System.err.println("delta:\t" + (pNew.subtract(p).norm() / p.norm()));
// 	    iters++;

// 	    if ((pNew.subtract(p).norm() <= p.norm() * eps) || (iters >= 20)) {

// 		return p;

// 	    } else {

// 		DoubleVector gNew = grad.apply(pNew);
// 		double gamma = gNew.dotProduct(gNew.subtract(g)) / g.dotProduct(g);
// 		DoubleVector hNew = gNew.add(h.multiply(gamma));

// 		p = pNew;
// 		g = gNew;
// 		h = hNew;

// 	    }

// 	}

//     }

    private static void dump(String method, double... x) {
	System.out.print(method + ":");

	for (double xx : x) {
	    System.out.print("\t" + xx);
	}

	System.out.println();
    }

    public static void main(String[] args) {
	main2(args);
    }

    private static void main1(String[] args) {
	Function f = new Function() {
		public double apply(double x) {
		    return Math.pow(x - Math.PI, 3);
		}
	    };

// 	double[] bracket = bracketRoot(f, 0, 1, f.apply(0), f.apply(1));
// 	double[] bracket = bracketRoot(f);
	double[] bracket = bisectRoot(f, Double.parseDouble(args[0]));

	dump("result", bracket[0], bracket[1], f.apply(bracket[0]), f.apply(bracket[1]));

	System.out.println("true: " + Math.PI);
    }

    private static void main2(String[] args) {
	VectorFunctional vf = new VectorFunctional() {
		public DoubleVector apply(DoubleVector v) {
		    double x = v.get(0);
		    double y = v.get(1);
		    double ex = Math.exp(-0.5 * (9 * x * x + 16 * y * y));

		    return new DoubleVector(new double[]{9 * x * ex / Math.sqrt(2 * Math.PI),
							 16 * y * ex / Math.sqrt(2 * Math.PI)});
		}
	    };

// 	DoubleVector p = new DoubleVector(new double[]{3, 3});
// 	DoubleVector q = new DoubleVector(new double[]{-1, -0.5});

// 	DoubleVector min = lineMin(vf, p, q, 1e-10);

// 	System.out.println(min);

// 	System.out.println(conjGradMin(vf, new DoubleVector(new double[]{4, 3}), 1e-5));

    }

}
