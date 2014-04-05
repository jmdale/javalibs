
package util.fn;

/**
 * A function which takes a double to a double.
 *
 * @author <a href="mailto:jdale@uclink.berkeley.edu">Joseph Dale</a>
 * @version 20070612
 */
public abstract class DoubleToDouble {

    /**
     * Applies this function to the given double.
     */
    public abstract double apply(double x);


    /**
     * Applies this function to each element of the given array x,
     * returning a new array y such that y[i] = this.apply(x[i]).
     */
    public double[] map(double[] x) {

	double[] y = new double[x.length];

	for (int i = 0; i < y.length; i++) {

	    y[i] = this.apply(x[i]);

	}

	return y;

    }


    /**
     * Applies this function to each element of the given array x,
     * returning a new array y such that y[i] = this.apply(x[i]).
     */
    public double[] map(int[] x) {

	double[] y = new double[x.length];

	for (int i = 0; i < y.length; i++) {

	    y[i] = this.apply(x[i]);

	}

	return y;

    }

}
