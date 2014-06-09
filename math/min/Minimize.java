package math.min;

import math.DoubleVector;
import math.Function;
import math.VectorFunction;

/**
 * Static functions providing a procedural interface to minimization
 * routines.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070919
 */
public class Minimize {

    /**
     * Bracket a minimum of the function f, by going downhill from
     * initial points a and b.
     */
    static Bracket bracket(Function f, double a, double b) {
	return (new BracketMin(f, a, b)).findMin();
    }

    /**
     * Use golden section search to bracket a minimum of the function
     * f with relative precision eps, starting with the initial
     * bracket br.
     */
    static Bracket goldenSectionBracket(Function f, Bracket br, double eps) {
	return (new GoldenSectionSearch(f, br, eps)).findMin();
    }

    /**
     * Use golden section search to find a minimum of the function f
     * with relative precision eps, after finding an initial bracket
     * from points a and b.
     */
    public static double goldenSectionMin(Function f, double a, double b, double eps) {
	return goldenSectionBracket(f, bracket(f, a, b), eps).min();
    }

    /**
     * Finds a minimum of the function f along the direction q,
     * starting from point p, by performing a one-dimensional
     * minimization of the function F(x) = f(p + x * q) (x \in R).
     */
    public static DoubleVector lineMin(final VectorFunction f,
				       final DoubleVector p, final DoubleVector q,
				       double eps) {
	Function F = new Function() {
		public double apply(double x) {
		    return f.apply(p.add(q.multiply(x)));
		}
	    };

	double F0 = F.apply(0);
	double F1 = F.apply(1);

	if ((F0 == F1) || (Math.abs(F1 - F0) < eps * Math.abs(F0))) {
	    return p;
	} else {
	    double xMin = goldenSectionMin(F, 0, 1, eps);
	    return p.add(q.multiply(xMin));
	}
    }

    /**
     * Finds a minimum of the function f by coordinate descent.
     */
    public static DoubleVector coordDescentMin(VectorFunction f, DoubleVector start, double eps) {
	return (new CoordinateDescent(f, start, eps)).findMin();
    }

    /**
     * Finds a minimum of the function f by the direction set method.
     */
    public static DoubleVector directionSetMin(VectorFunction f, DoubleVector start, double eps) {
	return (new DirectionSet(f, start, eps)).findMin();
    }

}
