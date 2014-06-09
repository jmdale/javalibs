package math.min;

import math.DoubleMatrix;
import math.DoubleVector;
import math.Function;
import math.VectorFunction;

/**
 * Methods for testing minimization code.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070919
 */
public class TestMin {

    public static void main(String[] args) {

// 	test1();
// 	System.out.println("------------------------------------------------------------");
// 	test2();
// 	System.out.println("------------------------------------------------------------");
// 	test3();
// 	System.out.println("------------------------------------------------------------");
// 	test4();
// 	System.out.println("------------------------------------------------------------");
// 	test5();
// 	System.out.println("------------------------------------------------------------");
// 	test6();
// 	System.out.println("------------------------------------------------------------");
// 	test7();
// 	System.out.println("------------------------------------------------------------");
	test8();

    }

    /**
     * Tests (one-dimensional) golden section search by finding the
     * minimum of the gamma function on R+.
     */
    private static void test1() {
	Function f = new Function() {
		public double apply(double x) {
		    return math.GammaFunction.gamma(x);
		}
	    };

	double expected = 1.461632144968362341262659542325721328468196204006446351295988409;
	double actual = Minimize.goldenSectionMin(f, 0.1, 0.2, 1e-8);
	double absRelError = Math.abs((expected - actual) / actual);

	System.out.println("Minimum of gamma function:");
	System.out.println("Expected " + expected);
	System.out.println("Got " + actual);
	System.out.println("Absolute relative error: " + absRelError);
    }

    /**
     * Tests multidimensional line minimization by finding the minimum
     * of the paraboloid z = (x-2)^2 + (y-2)^2, starting at (0, 0), in
     * the direction of the vector (0.5, 0.5). (This happens to be the
     * global minimum of that surface.)
     */
    private static void test2() {
	VectorFunction f = new VectorFunction() {
		public double apply(DoubleVector x) {
		    // z = (x-2)^2 + (y-2)^2 ; minimum at (2, 2)
		    return (x.get(0) - 2) * (x.get(0) - 2) + (x.get(1) - 2) * (x.get(1) - 2);
		}
	    };

	DoubleVector p = new DoubleVector(new double[]{0, 0});
	DoubleVector q = new DoubleVector(new double[]{0.5, 0.5});

	DoubleVector expected = new DoubleVector(new double[]{2, 2});
	DoubleVector actual = Minimize.lineMin(f, p, q, 1e-8);
	double absRelError = Math.abs(expected.subtract(actual).norm() / actual.norm());

	System.out.println("Minimum of paraboloid z = (x-2)^2 + (y-2)^2:");
	System.out.println("Expected " + expected);
	System.out.println("Got " + actual);
	System.out.println("Absolute relative error: " + absRelError);
    }

    /**
     * Tests multidimensional line minimization by finding the minimum
     * of the paraboloid z = (x-2)^2 + (y-2)^2, starting at (0, 1), in
     * the direction of the vector (0.5, 0.5). (The correct location
     * can be verified using the method of Lagrange multipliers.)
     */
    private static void test3() {
	VectorFunction f = new VectorFunction() {
		public double apply(DoubleVector x) {
		    // z = (x-2)^2 + (y-2)^2 ; minimum at (2, 2)
		    return (x.get(0) - 2) * (x.get(0) - 2) + (x.get(1) - 2) * (x.get(1) - 2);
		}
	    };

	DoubleVector p = new DoubleVector(new double[]{0, 1});
	DoubleVector q = new DoubleVector(new double[]{0.5, 0.5});

	DoubleVector expected = new DoubleVector(new double[]{1.5, 2.5});
	DoubleVector actual = Minimize.lineMin(f, p, q, 1e-8);
	double absRelError = Math.abs(expected.subtract(actual).norm() / actual.norm());

	System.out.println("Minimum of paraboloid z = (x-2)^2 + (y-2)^2:");
	System.out.println("Expected " + expected);
	System.out.println("Got " + actual);
	System.out.println("Absolute relative error: " + absRelError);
    }

    private static void test4() {
	VectorFunction f = new VectorFunction() {
		public double apply(DoubleVector x) {
		    // z = (x-2)^2 + (y-2)^2 + 1 ; minimum at (2, 2)
		    return (x.get(0) - 2) * (x.get(0) - 2) + (x.get(1) - 2) * (x.get(1) - 2) + 1;
		}
	    };

	java.util.Random r = new java.util.Random();
	DoubleVector start = new DoubleVector(new double[]{r.nextGaussian(), r.nextGaussian()});

	System.out.println("start: " + start);

	DoubleVector expected = new DoubleVector(new double[]{2, 2});
	DoubleVector actual = Minimize.coordDescentMin(f, start, 1e-8);
	double absRelError = Math.abs(expected.subtract(actual).norm() / actual.norm());

	System.out.println("Minimum of paraboloid z = (x-2)^2 + (y-2)^2 + 1:");
	System.out.println("Expected " + expected);
	System.out.println("Got " + actual);
	System.out.println("Absolute relative error: " + absRelError);
    }

    private static void test5() {
	VectorFunction f = new VectorFunction() {
		public double apply(DoubleVector x) {
		    // z = 2*(x-2)^2 + 3*(y-2)^2 + 1 ; minimum at (2, 2)
		    return 2 * (x.get(0) - 2) * (x.get(0) - 2) + 3 * (x.get(1) - 2) * (x.get(1) - 2) + 1;
		}
	    };

	java.util.Random r = new java.util.Random();
	DoubleVector start = new DoubleVector(new double[]{r.nextGaussian(), r.nextGaussian()});

	System.out.println("start: " + start);

	DoubleVector expected = new DoubleVector(new double[]{2, 2});
	DoubleVector actual = Minimize.coordDescentMin(f, start, 1e-8);
	double absRelError = Math.abs(expected.subtract(actual).norm() / actual.norm());

	System.out.println("Minimum of paraboloid z = 2*(x-2)^2 + 3*(y-2)^2 + 1:");
	System.out.println("Expected " + expected);
	System.out.println("Got " + actual);
	System.out.println("Absolute relative error: " + absRelError);
    }

    private static void test6() {
	DoubleVector mean = new DoubleVector(new double[]{4, -1, 3});
	DoubleMatrix cov = new DoubleMatrix(new double[][]{{3.5, 3,    2},
							   {3,   7.25, 5},
							   {2,   5,    4}});

	final stat.dist.MultivariateNormalDistribution mnd =
	    new stat.dist.MultivariateNormalDistribution(mean, cov);

	VectorFunction f = new VectorFunction() {
		public double apply(DoubleVector x) {
		    return -mnd.density(x);
		}
	    };

	java.util.Random r = new java.util.Random();
	DoubleVector start = new DoubleVector(new double[]{r.nextGaussian(),
							   r.nextGaussian(),
							   r.nextGaussian()});

	System.out.println("start: " + start);

	DoubleVector expected = mean;
	DoubleVector actual = Minimize.coordDescentMin(f, start, 1e-8);
	double absRelError = Math.abs(expected.subtract(actual).norm() / actual.norm());

	System.out.println("Minimum of negative multivariate normal density:");
	System.out.println("Expected " + expected);
	System.out.println("Got " + actual);
	System.out.println("Absolute relative error: " + absRelError);
    }

    private static void test7() {
	DoubleVector mean = new DoubleVector(new double[]{4, -1, 3});
	DoubleMatrix cov = new DoubleMatrix(new double[][]{{3.5, 3,    2},
							   {3,   7.25, 5},
							   {2,   5,    4}});

	final stat.dist.MultivariateNormalDistribution mnd =
	    new stat.dist.MultivariateNormalDistribution(mean, cov);

	VectorFunction f = new VectorFunction() {
		public double apply(DoubleVector x) {
		    return -mnd.density(x);
		}
	    };

	java.util.Random r = new java.util.Random();
	DoubleVector start = new DoubleVector(new double[]{r.nextGaussian(),
							   r.nextGaussian(),
							   r.nextGaussian()});

	System.out.println("start: " + start);

	DoubleVector expected = mean;
	DoubleVector actual = Minimize.directionSetMin(f, start, 1e-8);
	double absRelError = Math.abs(expected.subtract(actual).norm() / actual.norm());

	System.out.println("Minimum of negative multivariate normal density:");
	System.out.println("Expected " + expected);
	System.out.println("Got " + actual);
	System.out.println("Absolute relative error: " + absRelError);
    }

    private static void test8() {
	java.util.Random r = new java.util.Random();

	for (int i = 0; i < 10; i++) {
	    double[][] x = new double[3][3];

	    x[0][0] = 3 * r.nextGaussian();
	    x[0][1] = 3 * r.nextGaussian();
	    x[0][2] = 3 * r.nextGaussian();
	    x[1][1] = 3 * r.nextGaussian();
	    x[1][2] = 3 * r.nextGaussian();
	    x[2][2] = 3 * r.nextGaussian();

	    DoubleMatrix xMat = new DoubleMatrix(x);
	    DoubleMatrix xxt = xMat.multiply(xMat.transpose());

	    System.out.println(xxt);

	    try {
		System.err.println(xxt.determinant());
	    } catch (math.SingularMatrixException sme) {
		sme.printStackTrace();
	    }

	    DoubleVector m = new DoubleVector(new double[]{3 * r.nextGaussian(), 3 * r.nextGaussian(), 3 * r.nextGaussian()});

	    final stat.dist.MultivariateNormalDistribution mnd =
		new stat.dist.MultivariateNormalDistribution(m, xxt);

	    VectorFunction f = new VectorFunction() {
		    public double apply(DoubleVector x) {
			return -mnd.density(x);
		    }
		};

	    DoubleVector start = m.add(xMat.multiply(new DoubleVector(new double[]{3 * r.nextGaussian(),
										   3 * r.nextGaussian(),
										   3 * r.nextGaussian()})));

	    System.out.println("start: " + start);

	    DoubleVector expected = m;
	    DoubleVector actual = Minimize.directionSetMin(f, start, 1e-8);
	    double absRelError = Math.abs(expected.subtract(actual).norm() / expected.norm());

	    System.out.println("Minimum of negative multivariate normal density:");
	    System.out.println("Expected " + expected);
	    System.out.println("Expected f = " + f.apply(expected));
	    System.out.println("Got " + actual);
	    System.out.println("Got f = " + f.apply(actual));
	    System.out.println("Absolute relative error: " + absRelError);

	    System.out.println("============================================================");
	}
    }

}
