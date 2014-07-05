
package stat.dist;

/**
 * Computes the Percentage Points of the Normal Distribution (PPND) to
 * an accuracy of sixteen decimal places using Wichura's algorithm.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20050109
 */
public class PPND16 {

    private static double ZERO = 0.0E0;
    private static double ONE = 1.0E0;
    private static double HALF = ONE / 2.0E0;

    private static double SPLIT1 = 0.425E0;
    private static double SPLIT2 = 5.0E0;
    private static double CONST1 = 0.180625E0;
    private static double CONST2 = 1.6E0;

    /* Coefficients for p close to 1/2. */
    private static double[] A = {
	3.3871328727963666080E0,
	1.3314166789178437745E2,
	1.9715909503065514427E3,
	1.3731693765509461125E4,
	4.5921953931549871457E4,
	6.7265770927008700853E4,
	3.3430575583588128105E4,
	2.5090809287301226727E3
    };

    private static double[] B = {
	1.0E0,
	4.2313330701600911252E1,
	6.8718700749205790830E2,
	5.3941960214247511077E3,
	2.1213794301586595867E4,
	3.9307895800092710610E4,
	2.8729085735721942674E4,
	5.2264952788528545610E3
    };


    /* Coefficients for p close to neither 1/2 nor 0 nor 1. */
    private static double[] C = {
	1.42343711074968357734E0,
	4.63033784615654529590E0,
	5.76949722146069140550E0,
	3.64784832476320460504E0,
	1.27045825245236838258E0,
	2.41780725177450611770E-1,
	2.27238449892691845833E-2,
	7.74545014278341407640E-4
    };

    private static double[] D = {
	1.0E0,
	2.05319162663775882187E0,
	1.67638483018380384940E0,
	6.89767334985100004550E-1,
	1.48103976427480074590E-1,
	1.51986665636164571966E-2,
	5.47593808499534494600E-4,
	1.05075007164441684324E-9
    };

    /* Coefficients for p close to 0 or 1. */
    private static double[] E = {
	6.65790464350110377720E0,
	5.46378491116411436990E0,
	1.78482653991729133580E0,
	2.96560571828504891230E-1,
	2.65321895265761230930E-2,
	1.24266094738807843860E-3,
	2.71155556874348757815E-5,
	2.01033439929228813265E-7
    };

    private static double[] F = {
	1.0E0,
	5.99832206555887937690E-1,
	1.36929880922735805310E-1,
	1.48753612908506148525E-2,
	7.86869131145613259100E-4,
	1.84631831751005468180E-5,
	1.42151175831644588870E-7,
	2.04426310338993978564E-15
    };


    public static double ppnd16(double p) {

	assert ((0 <= p) && (p <= 1)): "p must be between 0 and 1: " + p;

	double q = p - HALF;
	double r, ppnd16;

	if (Math.abs(q) <= SPLIT1) {

	    r = CONST1 - q * q;
	    ppnd16 = q *
		(((((((A[7] * r + A[6]) * r + A[5]) * r + A[4]) * r + A[3]) * r + A[2]) * r + A[1]) * r + A[0]) /
		(((((((B[7] * r + B[6]) * r + B[5]) * r + B[4]) * r + B[3]) * r + B[2]) * r + B[1]) * r + ONE);

	} else {

	    if (q < 0) {

		r = p;

	    } else {

		r = ONE - p;

	    }

	    r = Math.sqrt(-Math.log(r));

	    if (r <= SPLIT2) {

		r = r - CONST2;
		ppnd16 =
		    (((((((C[7] * r + C[6]) * r + C[5]) * r + C[4]) * r + C[3]) * r + C[2]) * r + C[1]) * r + C[0]) /
		    (((((((D[7] * r + D[6]) * r + D[5]) * r + D[4]) * r + D[3]) * r + D[2]) * r + D[1]) * r + ONE);

	    } else {

		r = r - SPLIT2;
		ppnd16 =
		    (((((((E[7] * r + E[6]) * r + E[5]) * r + E[4]) * r + E[3]) * r + E[2]) * r + E[1]) * r + E[0]) /
		    (((((((F[7] * r + F[6]) * r + F[5]) * r + F[4]) * r + F[3]) * r + F[2]) * r + F[1]) * r + ONE);

	    }

	    if (q < 0) {

		ppnd16 = -ppnd16;

	    }

	}

	return ppnd16;

    }

}
