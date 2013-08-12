
package util.fn;

/**
 * A function which takes a double to a boolean.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20041211
 */
public abstract class DoubleToBoolean {

    /**
     * Applies this function to the given double.
     */
    public abstract boolean apply(double x);


    /**
     * Applies this function to each double in the given array
     * <code>x</code>, returning an array of booleans <code>b</code>
     * such that <code>b[i] = this.apply(x[i])</code>.
     */
    public boolean[] map(double[] x) {

	boolean[] b = new boolean[x.length];

	for (int i = 0; i < b.length; i += 1) {

	    b[i] = this.apply(x[i]);

	}

	return b;

    }


    /**
     * Applies this function to each double in the given array,
     * returning a new array containing those elements for which this
     * function returns true.
     */
    public double[] filter(double[] x) {

	double[] r1 = new double[x.length];
	int j = 0;

	for (int i = 0; i < x.length; i += 1) {

	    if (this.apply(x[i])) {

		r1[j] = x[i];
		j += 1;

	    }

	}

	double[] r2 = new double[j];
	System.arraycopy(r1, 0, r2, 0, j);

	return r2;

    }

}
