
package math;

/**
 * Native interfaces for various mathematical functions.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20060108
 */
abstract class JNIMath {

    /**
     * Multiplies the matrices m1 and m2, which have dimensions r1 x
     * c1 and r2 x c2, respectively.
     */
    static native double[][] multiply(double[][] m1, int r1, int c1, double[][] m2, int r2, int c2);


    /**
     * Returns the natural logarithm of the gamma function.
     */
    static native double lgamma(double x);


    /**
     * Returns zero from a native method call.
     */
    static native int zero();


    static {

	System.loadLibrary("jnimath");

    }


    public static void main(String[] args) {

// 	double[][] m1 = new double[][]{{1, 2, 4}, {2, 3, 5}, {3, 5, 7}};
// 	double[][] m2 = new double[][]{{-2, 3, -1}, {0.5, -2.5, 1.5}, {0.5, 0.5, -0.5}};
// 	double[][] id = multiply(m1, 3, 3, m2, 3, 3);

// 	for (int i = 0; i < id.length; i += 1) {

// 	    for (int j = 0; j < id[i].length; j += 1) {

// 		System.out.format("%12f", id[i][j]);

// 	    }

// 	    System.out.println();

// 	}

	int n = Integer.parseInt(args[0]);
	int z = 0;
	long start = System.currentTimeMillis();

	for (int i = 0; i < n; i += 1) {

	    z = zero();

	}

	long end = System.currentTimeMillis();
	double ms = end - start;
	double msPer = ms / n;

	System.out.println(z);
	System.out.println(ms + "\t" + msPer);

    }

}
