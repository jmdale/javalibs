
package util.fn;

/**
 * A function which takes an int to a boolean.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20030423
 */
public abstract class IntToBoolean {

    public abstract boolean apply(int x);


    public boolean[] map(int[] x) {

	boolean[] b = new boolean[x.length];

	for (int i = 0; i < b.length; i += 1) {

	    b[i] = this.apply(x[i]);

	}

	return b;

    }


    public int[] filter(int[] x) {

	int[] r1 = new int[x.length];
	int j = 0;

	for (int i = 0; i < x.length; i += 1) {

	    if (this.apply(x[i])) {

		r1[j] = x[i];
		j += 1;

	    }

	}

	int[] r2 = new int[j];
	System.arraycopy(r1, 0, r2, 0, j);

	return r2;

    }

}
