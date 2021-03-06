package util.fn;

/**
 * A function which takes a double to an int.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140404
 */
public abstract class DoubleToInt {

    public abstract int apply(double x);

    public int[] map(double[] x) {
	int[] y = new int[x.length];

	for (int i = 0; i < y.length; i += 1) {
	    y[i] = this.apply(x[i]);
	}

	return y;
    }

}
