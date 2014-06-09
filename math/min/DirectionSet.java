package math.min;

import math.DoubleVector;
import math.VectorFunction;

/**
 * Multidimensional function minimization using Powell's direction set
 * method.
 *
 * This is similar to coordinate descent, in that we cycle through a
 * set of direction, doing successive line minimizations along each
 * direction. The difference is that instead of always using the
 * standard basis as our direction set, we update the set after each
 * cycle, removing the first vector in the set and adding in the net
 * direction from the last cycle. In order to keep the direction set
 * linearly independent, we reset it to the standard basis after N+1
 * cycles through the set (for a function of N variables).
 *
 * Further details are in Section 10.5 of <em>Numerical Recipes in
 * C</em>.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070820
 */
class DirectionSet {

    VectorFunction f;
    int N;
    DoubleVector min;
    double fMin;
    double eps;

    DirectionSet(VectorFunction f, DoubleVector start, double eps) {
	this.f = f;
	this.N = start.length();
	this.min = start;
	this.fMin = f.apply(start);
	this.eps = eps;
    }

    DoubleVector findMin() {
	while (true) { // loop until the function stops decreasing

	    DoubleVector[] directions = basisVectors(); // initialize the directions

	    for (int i = 0; i < N; i++) { // do N iterations of the "basic procedure"
		DoubleVector minInit = min;
		double fMinInit = fMin;

		for (int j = 0; j < N; j++) { // minimize along each direction
		    min = Minimize.lineMin(f, min, directions[j], eps);
		    fMin = f.apply(min);
		}

		DoubleVector dirNew = min.subtract(minInit);
		min = Minimize.lineMin(f, min, dirNew, eps);
		fMin = f.apply(min);
		addDirection(directions, dirNew);

		if (Math.abs(fMin - fMinInit) <= Math.abs(eps * fMinInit)) {
		    return min;
		}
	    }
	}
    }

    private DoubleVector[] basisVectors() {
	DoubleVector[] e = new DoubleVector[N];

	for (int i = 0; i < N; i++) {
	    e[i] = new DoubleVector(N);
	    e[i].set(i, 1);
	}

	return e;
    }

    /**
     * Shifts each element of the given direction set forward, and
     * makes the given vector the last direction.
     */
    private static void addDirection(DoubleVector[] directions, DoubleVector dirNew) {
	for (int i = 1; i < directions.length; i++) {
	    directions[i-1] = directions[i];
	}

	directions[directions.length-1] = dirNew;
    }

}
