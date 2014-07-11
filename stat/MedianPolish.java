package stat;

import math.DoubleMatrix;
import math.DoubleVector;

/**
 * Performs median polish on a two-way table. Median polish is a
 * method for computing robust estimates of the parameters in a model
 * of the form
 *
 * x_ij = mu + alpha_i + beta_j + epsilon_ij
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20041217
 */
public class MedianPolish {

    /**
     * The minimum relative change in the sum of absolute residuals
     * needed in each iteration to continue the run.
     */
    private double eps;

    /**
     * The maximum number of iterations.
     */
    private int maxIter;

    /**
     * The matrix being polished. After run() has been called, this
     * matrix will contain the residuals.
     */
    private DoubleMatrix x;

    /**
     * The common effect.
     */
    private double t;

    /**
     * The row effects.
     */
    private DoubleVector r;

    /**
     * The column effects.
     */
    private DoubleVector c;

    /**
     * The number of iterations which have been performed.
     */
    private int iter;

    /**
     * The sum of the absolute values of the residuals after the
     * previous iteration.
     */
    private double oldSum;

    /**
     * The sum of the absolute values of the residuals after the
     * current iteration.
     */
    private double newSum;

    public MedianPolish(DoubleMatrix x, double eps, int maxIter) {
	assert (eps >= 0): "eps < 0: " + eps;
	assert (maxIter > 0): "maxIter < 1: " + maxIter;

	this.eps = eps;
	this.maxIter = maxIter;
	this.x = x.clone();

	this.t = 0;
	this.r = DoubleVector.repeat(0, x.rows());
	this.c = DoubleVector.repeat(0, x.columns());

	this.iter = 0;
	this.oldSum = 0;
	this.newSum = 0;
    }

    public MedianPolish(DoubleMatrix x) {
	this(x, 0.01, 10);
    }

    public MedianPolish(DoubleMatrix x, double eps) {
	this(x, eps, 10);
    }

    public MedianPolish(DoubleMatrix x, int maxIter) {
	this(x, 0.01, maxIter);
    }

    public void run() {
	do {
	    iter();
	} while (!converged());
    }

    private void iter() {
	System.out.print(".");
	shiftRowMedians();
	shiftColumnEffect();
	shiftColumnMedians();
	shiftRowEffect();
	sumResiduals();

	++iter;
    }

    private void shiftRowMedians() {
	DoubleVector rm = rowMedians();

	for (int i = 0; i < x.rows(); ++i) {
	    double m = rm.get(i);
	    for (int j = 0; j < x.columns(); ++j) {
		x.set(i, j, x.get(i, j) - m);
	    }
	}

	r = r.add(rm);
    }

    private DoubleVector rowMedians() {
	DoubleVector dv = new DoubleVector(x.rows());

	for (int i = 0; i < x.rows(); ++i) {
	    dv.set(i, x.getRow(i).median());
	}

	return dv;
    }

    private void shiftColumnMedians() {
	DoubleVector cm = columnMedians();

	for (int j = 0; j < x.columns(); ++j) {
	    double m = cm.get(j);
	    for (int i = 0; i < x.rows(); ++i) {
		x.set(i, j, x.get(i, j) - m);
	    }
	}

	c = c.add(cm);
    }

    private DoubleVector columnMedians() {
	DoubleVector dv = new DoubleVector(x.columns());

	for (int i = 0; i < x.columns(); ++i) {
	    dv.set(i, x.getColumn(i).median());
	}

	return dv;
    }

    private void shiftRowEffect() {
	double rem = r.median();

	t += rem;
	r = r.subtract(rem);
    }

    private void shiftColumnEffect() {
	double cem = c.median();

	t += cem;
	c = c.subtract(cem);
    }

    private void sumResiduals() {
	oldSum = newSum;
	newSum = 0;

	for (int i = 0; i < x.rows(); ++i) {
	    for (int j = 0; j < x.columns(); ++j) {
		newSum += Math.abs(x.get(i, j));
	    }
	}
    }

    private boolean converged() {
	return ((iter == maxIter) ||
		(newSum == 0) ||
		(Math.abs(newSum - oldSum) <= eps * newSum));
    }

    /*
     * Arizona temperature data from Tukey, EDA, p. 333.
     */
    private static DoubleMatrix arizonaTemperatures =
	new DoubleMatrix(new double[][]{
			     {65.2, 90.1, 94.6},
			     {63.4, 88.3, 93.7},
			     {57.0, 82.7, 88.3},
			     {46.1, 70.8, 76.4},
			     {35.8, 58.4, 64.2},
			     {28.4, 52.1, 57.1},
			     {25.3, 49.7, 55.3}
			 });

    /*
     * From http://www.answers.com/topic/median-polish.
     */
    private static DoubleMatrix answersExample =
	new DoubleMatrix(new double[][]{
			     {13, 17, 26, 18, 29},
			     {42, 48, 57, 41, 59},
			     {34, 31, 36, 22, 41}
			 });


    public static void main(String[] args) {
 	MedianPolish mp = new MedianPolish(arizonaTemperatures);
	System.out.println("Original matrix: ");
	mp.x.print();
	mp.run();
	mp.print();

	System.out.println();

 	mp = new MedianPolish(answersExample);
	System.out.println("Original matrix: ");
	mp.x.print();
	mp.run();
	mp.print();
    }

    private void print() {
	System.out.println();
	System.out.println("Overall: " + t);
	System.out.println();
	System.out.println("Row effects: " + r);
	System.out.println();
	System.out.println("Column effects: " + c);
	System.out.println();
	System.out.println("Residuals: ");
	x.print();
	System.out.println();
    }

    private static DoubleMatrix medPolMatrix() {
	double grandMedian = 5.0;
	double[] rowMedians = new double[]{1, 2, 3, 4, 5};
	double[] columnMedians = new double[]{1, 2, 3, 4, 5};
	stat.dist.NormalDistribution nd = new stat.dist.NormalDistribution(0, 0.1);
	double[][] mp = new double[5][5];

	for (int i = 0; i < rowMedians.length; ++i) {
	    for (int j = 0; j < columnMedians.length; ++j) {
		mp[i][j] = grandMedian + rowMedians[i] + columnMedians[j] + nd.random();
	    }
	}

	return new DoubleMatrix(mp);
    }

}
