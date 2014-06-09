package math.min;

import math.Function;

/**
 * One-dimensional function minimization using golden section search,
 * as described in Section 10.1 of <em>Numerical Recipes in C</em>.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070817
 */
class GoldenSectionSearch {

    Function f;
    Bracket br;
    double eps;

    GoldenSectionSearch(Function f, Bracket br, double eps) {
	this.f = f;
	this.br = br;
	this.eps = eps;
    }

    Bracket findMin() {
	while (!done()) {
	    /*
	     * The next abscissa to test is found by mirroring the
	     * middle abscissa, b, about the midpoint of the
	     * bracket. The formula a + c - b works regardless of
	     * which interval -- (a, b) or (b, c) -- is smaller.
	     */
	    double d = br.a + br.c - br.b;

	    /*
	     * Check whether the new abscissa coincides with one of
	     * the existing abscissa. If this is the case, it signals
	     * that the abscissas have been squeezed together as much
	     * as possible, given the constraints of the floating
	     * point number system. As a result, we can return
	     * directly.
	     */
	    if ((d == br.a) || (d == br.b) || (d == br.c)) {
		return br;
	    }

	    double fd = f.apply(d);

	    if ((fd == br.fa) || (fd == br.fb) || (fd == br.fc)) {
		return br;
	    }

	    /*
	     * The outer if-else here checks which interval within the
	     * bracket -- (a, b) or (b, c) -- is smaller. The inner
	     * if-elses check whether the function value at the new
	     * abscissa is lower than the current minimum. The bracket
	     * is updated accordingly.
	     */
	    if (br.b - br.a < br.c - br.b) {
		if (fd < br.fb) {
		    br = new Bracket(br.b, d, br.c,
				     br.fb, fd, br.fc);
		} else {
		    br = new Bracket(br.a, br.b, d,
				     br.fa, br.fb, fd);
		}
	    } else {
		if (fd < br.fb) {
		    br = new Bracket(br.a, d, br.b,
				     br.fa, fd, br.fb);
		} else {
		    br = new Bracket(d, br.b, br.c,
				     fd, br.fb, br.fc);
		}
	    }
	}

	return br;
    }

    private boolean done() {
	return br.length() <= eps * br.scale();
    }

}
