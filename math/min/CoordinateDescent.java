package math.min;

import math.DoubleVector;
import math.VectorFunction;

/**
 * Multidimensional function minimization by coordinate descent.
 *
 * To minimize a function of N variables, cycle through the N standard
 * basis vectors e_1, e_2, ..., e_N, performing a line minimization in
 * each direction starting from the current minimum; stop when the
 * relative decrease in the function value is sufficiently small.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070818
 */
class CoordinateDescent {

    VectorFunction f;
    int N;
    DoubleVector min;
    double fMin;
    double eps;

    CoordinateDescent(VectorFunction f, DoubleVector start, double eps) {
	this.f = f;
	this.N = start.length();
	this.min = start;
	this.fMin = f.apply(start);
	this.eps = eps;
    }

    DoubleVector findMin() {
	while (true) {
	    double fMinCur = fMin;

	    for (int i = 0; i < N; i++) {
		DoubleVector dir = basisVector(i);
		min = Minimize.lineMin(f, min, dir, eps);
		fMin = f.apply(min);
	    }

	    if (Math.abs(fMin - fMinCur) <= Math.abs(eps * fMinCur)) {
		return min;
	    }
	}
    }

    private DoubleVector basisVector(int i) {
	DoubleVector e = new DoubleVector(N); // N zeroes
	e.set(i, 1);

	return e;
    }

}
