
package stat.dist;

/**
 * Computes the Percentage Points of the Normal Distribution (PPND) to
 * an accuracy of seven decimal places using Wichura's algorithm.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20050109
 */
public class PPND7 {

    private static float ZERO = 0.0E0F;
    private static float ONE = 1.0E0F;
    private static float HALF = ONE / 2.0E0F;

    private static float SPLIT1 = 0.425F;
    private static float SPLIT2 = 5.0F;
    private static float CONST1 = 0.180625F;
    private static float CONST2 = 1.6F;

    private static float[] A = new float[4];
    private static float[] B = new float[4];
    private static float[] C = new float[4];
    private static float[] D = new float[3];
    private static float[] E = new float[4];
    private static float[] F = new float[3];

    static {

	/* Coefficients for p close to 1/2. */
	A[0] = 3.3871327179E0F;
	A[1] = 5.0434271938E1F;
	A[2] = 1.5929113202E2F;
	A[3] = 5.9109374720E1F;
	B[1] = 1.7895169469E1F;
	B[2] = 7.8757757664E1F;
	B[3] = 6.7187563600E1F;

	/* Coefficients for p close to neither 1/2 nor 0 nor 1. */
	C[0] = 1.4234372777E0F;
	C[1] = 2.7568153900E0F;
	C[2] = 1.3067284816E0F;
	C[3] = 1.7023821103E-1F;
	D[1] = 7.3700164250E-1F;
	D[2] = 1.2021132975E-1F;

	/* Coefficients for p close to 0 or 1. */
	E[0] = 6.6579051150E0F;
	E[1] = 3.0812263860E0F;
	E[2] = 4.2868294337E-1F;
	E[3] = 1.7337203997E-2F;
	F[1] = 2.4197894225E-1F;
	F[2] = 1.2258202635E-2F;

    };


    public static float ppnd7(float p) {

	assert ((0F <= p) && (p <= 1F)): "p must be between 0 and 1: " + p;

	float q = p - HALF;
	float r, ppnd7;

	if (Math.abs(q) <= SPLIT1) {

	    r = CONST1 - q * q;
	    ppnd7 = q *	(((A[3] * r + A[2]) * r + A[1]) * r + A[0]) / (((B[3] * r + B[2]) * r + B[1]) * r + ONE);

	} else {

	    if (q < 0) {

		r = p;

	    } else {

		r = ONE - p;

	    }

	    r = (float) Math.sqrt(-Math.log(r));

	    if (r <= SPLIT2) {

		r = r - CONST2;
		ppnd7 = (((C[3] * r + C[2]) * r + C[1]) * r + C[0]) / ((D[2] * r + D[1]) * r + ONE);

	    } else {

		r = r - SPLIT2;
		ppnd7 = (((E[3] * r + E[2]) * r + E[1]) * r + E[0]) / ((F[2] * r + F[1]) * r + ONE);

	    }

	    if (q < 0) {

		ppnd7 = -ppnd7;

	    }

	}

	return ppnd7;

    }

}
