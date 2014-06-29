package math.root;

import math.Function;

/**
 * The Newton-Raphson root-finding method.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070816
 */
public class NewtonRaphsonRootFinder extends DerivativeRootFinder {

    /**
     * The location at which the search will begin.
     */
    protected double init;

    public NewtonRaphsonRootFinder(Function f, Function df, double init) {
	super(f, df);

	if (Double.isInfinite(init) || Double.isNaN(init)) {
	    throw new IllegalArgumentException("init is infinite or NaN: " + init + ".");
	}

	this.init = init;
    }

    public double findRoot(double eps) {
	System.out.println("a\tf(a)\tf'(a)\tf(a)/f'(a)\ta-f(a)/f'(a)");

	double a = init;

	while (true) {
	    double fa = f.apply(a);
	    if (fa == 0) {
		return a;
	    }

	    double dfa = df.apply(a);
	    if (dfa == 0) {
		throw new IllegalStateException("f'(a) = 0 at a = " + a + ".");
	    }

	    double q = fa / dfa;

	    System.out.format("%e\t%e\t%e\t%e\t%e\n", a, fa, dfa, q, a - q);

	    if (Math.abs(q) < eps) {
		return a - q;
	    } else {
		a -= q;
	    }
	}
    }

    public static void main(String[] args) {
	Function f = new Function(){ public double apply(double x) { return Math.log(x) + 1; } };
	Function df = new Function(){ public double apply(double x) { return 1 / x; } };

	double root = RootFinder.newtonRaphson(f, df, 0.1, 1e-10);

	System.out.println();
	System.out.println("root = " + root);
	System.out.println("f(root) = " + f.apply(root));
    }

}
