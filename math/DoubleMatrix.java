
package math;

import java.io.Serializable;

/**
 * A matrix of doubles.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20070817
 */
public class DoubleMatrix implements Cloneable, Serializable {

    double[][] data;

    int rows;

    int columns;


    /**
     * Creates a matrix with the given numbers of rows and
     * columns. All elements are initially zero.
     */
    public DoubleMatrix(int rows, int columns) {

	this.data = new double[rows][columns];
	this.rows = rows;
	this.columns = columns;

    }

    /**
     * Creates a square matrix with the given number of rows and
     * columns. All elements are initially zero.
     */
    public DoubleMatrix(int size) {

	this.data = new double[size][size];
	this.rows = size;
	this.columns = size;

    }

    /**
     * Creates a new matrix from the given array. The elements of the
     * array are copied into a new array internally. If the rows of
     * the given array are not all of the same length, they are filled
     * with zeroes up to the length of the longest row.
     */
    public DoubleMatrix(double[][] data) {

	this(data, true);

    }

    public DoubleMatrix(double[][] data, boolean copy) {

	this.rows = data.length;

	if (!rowLengthsEqual(data)) {

	    throw new IllegalArgumentException("Lengths of rows in data matrix are not all equal.");

	}

	this.columns = data[0].length;

	if (copy) {

	    this.data = new double[rows][columns];

	    for (int i = 0; i < rows; i += 1) {

		System.arraycopy(data[i], 0, this.data[i], 0, columns);

	    }

	} else {

	    this.data = data;

	}

    }

    /**
     * Returns true if all rows of the given array have the same
     * length; returns false otherwise.
     */
    private static boolean rowLengthsEqual(double[][] data) {

	int len = data[0].length;

	for (int i = 1; i < data.length; i += 1) {

	    if (data[i].length != len) {

		return false;

	    }

	}

	return true;

    }


    public String toString() {

	if (rows == 0) {

	    return "[]";

	} else {

	    StringBuilder sb = new StringBuilder("[");
	    sb.append(rowString(data[0]));

	    for (int i = 1; i < rows; i++) {

		sb.append(", ");
		sb.append(rowString(data[i]));

	    }

	    sb.append("]");

	    return sb.toString();

	}

    }

    private static String rowString(double[] row) {

	if (row.length == 0) {

	    return "[]";

	} else {

	    StringBuilder sb = new StringBuilder("[");
	    sb.append(row[0]);

	    for (int i = 1; i < row.length; i++) {

		sb.append(", ");
		sb.append(row[i]);

	    }

	    sb.append("]");

	    return sb.toString();

	}

    }


    public DoubleMatrix clone() {

	DoubleMatrix clone = new DoubleMatrix(this.rows, this.columns);

	for (int i = 0; i < this.rows; i += 1) {

	    System.arraycopy(this.data[i], 0, clone.data[i], 0, this.columns);

	}

	return clone;

    }


    public void print() {

	for (int i = 0; i < rows; i += 1) {

	    if (i == 0) {

		System.out.print("[");

	    } else {

		System.out.print(" ");

	    }

	    printRow(i);

	    if (i < rows - 1) {

		System.out.println();

	    } else {

		System.out.println("]");

	    }

	}

    }

    private void printRow(int i) {

	for (int j = 0; j < columns; j += 1) {

	    if (j == 0) {

		System.out.print("[");

	    } else {

		System.out.print(" ");

	    }

	    System.out.print((float) data[i][j]);

	}

	System.out.print("]");

    }


    public int rows() {

	return rows;

    }


    public int columns() {

	return columns;

    }


    public boolean isSquare() {

	return (rows == columns);

    }


    /**
     * Returns the entry in the ith row and the jth column of this
     * matrix.
     */
    public double get(int i, int j) {

	return data[i][j];

    }


    /**
     * Returns a vector containing the elements in the ith row of this
     * matrix.
     */
    public DoubleVector getRow(int i) {

	double[] r = new double[columns];

	for (int j = 0; j < columns; j += 1) {

	    r[j] = data[i][j];

	}

	return new DoubleVector(r);

    }


    /**
     * Returns a vector containing the elements in the jth column of
     * this matrix.
     */
    public DoubleVector getColumn(int j) {

	double[] c = new double[rows];

	for (int i = 0; i < rows; i += 1) {

	    c[i] = data[i][j];

	}

	return new DoubleVector(c);

    }


    /**
     * Returns an array containing the rows of this matrix as vectors.
     */
    public DoubleVector[] getRows() {

	DoubleVector[] dv = new DoubleVector[this.rows];

	for (int i = 0; i < this.rows; i += 1) {

	    dv[i] = getRow(i);

	}

	return dv;

    }


    /**
     * Returns an array containing the columns of this matrix as
     * vectors.
     */
    public DoubleVector[] getColumns() {

	DoubleVector[] dv = new DoubleVector[this.columns];

	for (int j = 0; j < this.columns; j += 1) {

	    dv[j] = getColumn(j);

	}

	return dv;

    }


    /**
     * Returns a new matrix containing the rows of this matrix for
     * which the corresponding element in the given BooleanVector is
     * true.
     */
    public DoubleMatrix selectRows(BooleanVector bv) {

	if (this.rows != bv.length) {

	    throw new IllegalArgumentException("this.rows != bv.length: " + this.rows + ", " + bv.length + ".");

	}

	double[][] sel = new double[bv.countTrue()][columns];
	int i = 0;

	for (int k = 0; k < bv.length; k += 1) {

	    if (bv.data[k]) {

		// replace this loop with an arraycopy?
		for (int j = 0; j < columns; j += 1) {

		    sel[i][j] = this.data[k][j];

		}

		i += 1;

	    }

	}

	return new DoubleMatrix(sel);

    }


    /**
     * Returns a new matrix containing the rows of this matrix whose
     * indices are contained in the given IntVector.
     */
    public DoubleMatrix selectRows(IntVector iv) {

	double[][] sel = new double[iv.length()][columns];

	for (int i = 0; i < iv.length(); i += 1) {

	    int ix = iv.get(i);

	    for (int j = 0; j < columns; j += 1) {

		sel[i][j] = this.data[ix][j];

	    }

	}

	return new DoubleMatrix(sel);

    }


    /**
     * Selects a contiguous sequence of rows from this matrix, from
     * row start (inclusive) to row end (exclusive).
     */
    public DoubleMatrix selectRows(int start, int end) {

	DoubleMatrix sel = new DoubleMatrix(end - start, this.columns);

	for (int i = start; i < end; i += 1) {

	    System.arraycopy(this.data[i], 0, sel.data[i - start], 0, this.columns);

	}

	return sel;

    }


    /**
     * Returns a new matrix containing the columns of this matrix for
     * which the corresponding element in the given BooleanVector is
     * true.
     */
    public DoubleMatrix selectColumns(BooleanVector bv) {

	if (this.columns != bv.length) {

	    throw new IllegalArgumentException("this.columns != bv.length: " + this.columns + ", " + bv.length + ".");

	}

	double[][] sel = new double[rows][bv.countTrue()];
	int j = 0;

	for (int k = 0; k < bv.length; k += 1) {

	    if (bv.data[k]) {

		for (int i = 0; i < rows; i += 1) {

		    sel[i][j] = this.data[i][k];

		}

		j += 1;

	    }

	}

	return new DoubleMatrix(sel);

    }


    /**
     * Returns a new matrix containing the columns of this matrix
     * whose indices are contained in the given IntVector.
     */
    public DoubleMatrix selectColumns(IntVector iv) {

	double[][] sel = new double[rows][iv.length()];

	for (int j = 0; j < iv.length(); j += 1) {

	    int jx = iv.get(j);

	    for (int i = 0; i < rows; i += 1) {

		sel[i][j] = this.data[i][jx];

	    }

	}

	return new DoubleMatrix(sel);

    }


    /**
     * Selects a contiguous sequence of columns from this matrix, from
     * column start (inclusive) to column end (exclusive).
     */
    public DoubleMatrix selectColumns(int start, int end) {

	DoubleMatrix sel = new DoubleMatrix(this.rows, end - start);

	for (int i = 0; i < this.rows; i += 1) {

	    System.arraycopy(this.data[i], start, sel.data[i], 0, end - start);

	}

	return sel;

    }


    /**
     * Sets the entry in the ith row and the jth column of this
     * matrix to the given value.
     */
    public void set(int i, int j, double x) {

	data[i][j] = x;

    }


    /**
     * Sets the elements in the ith row of this matrix to be those in
     * the given vector.
     */
    public void setRow(int i, DoubleVector dv) {

	if (dv.length != this.columns) {

	    throw new IllegalArgumentException("dv.length != this.columns: " + dv.length + ", " + this.columns + ".");

	}

	for (int j = 0; j < columns; j += 1) {

	    this.data[i][j] = dv.data[j];

	}

    }


    /**
     * Sets the elements in the jth column of this matrix to be those
     * in the given vector.
     */
    public void setColumn(int j, DoubleVector dv) {

	if (dv.length != this.rows) {

	    throw new IllegalArgumentException("dv.length != this.rows: " + dv.length + ", " + this.rows + ".");

	}

	for (int i = 0; i < rows; i += 1) {

	    this.data[i][j] = dv.data[i];

	}

    }


    /**
     * Sets each element in the ith row of this matrix to be equal to
     * the given value.
     */
    public void fillRow(int i, double d) {

	for (int j = 0; j < columns; j += 1) {

	    this.data[i][j] = d;

	}

    }


    /**
     * Sets each element in the jth column of this matrix to be equal
     * to the given value.
     */
    public void fillColumn(int j, double d) {

	for (int i = 0; i < rows; i += 1) {

	    this.data[i][j] = d;

	}

    }


    /**
     * Returns a new matrix consisting of this matrix with the given
     * vector appended on the right as a column.
     */
    public DoubleMatrix appendColumn(DoubleVector dv) {

	if (dv.length != this.rows) {

	    throw new IllegalArgumentException("dv.length != this.rows: " + dv.length + ", " + this.rows + ".");

	}

	DoubleMatrix dm = new DoubleMatrix(this.rows, this.columns + 1);

	for (int i = 0; i < rows; i += 1) {

	    System.arraycopy(this.data[i], 0, dm.data[i], 0, this.columns);
	    dm.data[i][this.columns] = dv.data[i];

	}

	return dm;

    }


    /**
     * Returns a new matrix containing the columns of this matrix
     * followed by the columns of the given matrix.
     */
    public DoubleMatrix appendColumns(DoubleMatrix dm) {

	if (dm.rows != this.rows) {

	    throw new IllegalArgumentException("dm.rows != this.rows: " + dm.rows + ", " + this.rows + ".");

	}

	DoubleMatrix dmNew = new DoubleMatrix(this.rows, this.columns + dm.columns);

	for (int i = 0; i < this.rows; i += 1) {

	    System.arraycopy(this.data[i], 0, dmNew.data[i], 0, this.columns);
	    System.arraycopy(dm.data[i], 0, dmNew.data[i], this.columns, dm.columns);

	}

	return dmNew;

    }


    /**
     * Swaps the ith and jth rows of this matrix, in place.
     */
    public void swapRows(int i, int j) {

	double[] temp = this.data[i];
	this.data[i] = this.data[j];
	this.data[j] = temp;

    }


    /**
     * Multiplies the ith row of this matrix by d.
     */
    public void multiplyRow(int i, double d) {

	for (int j = 0; j < this.columns; j += 1) {

	    this.data[i][j] *= d;

	}

    }


    /**
     * Adds d times the i1th row of this matrix to the i2th row of
     * this matrix.
     */
    public void multiplyRowAndAdd(int i1, double d, int i2) {

	for (int j = 0; j < this.columns; j += 1) {

	    this.data[i2][j] += d * this.data[i1][j];

	}

    }


    /**
     * Adds this matrix and the given matrix, returning the result.
     */
    public DoubleMatrix add(DoubleMatrix d) {

	if (d.rows != this.rows) {

	    throw new IllegalArgumentException("d.rows != this.rows: " + d.rows + ", " + this.rows + ".");

	}

	if (d.columns != this.columns) {

	    throw new IllegalArgumentException("d.columns != this.columns: " + d.columns + ", " + this.columns + ".");

	}

	DoubleMatrix sum = new DoubleMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		sum.data[i][j] = this.data[i][j] + d.data[i][j];

	    }

	}

	return sum;

    }


    /**
     * Multiplies this matrix by the given matrix on the right,
     * returning the result.
     */
    public DoubleMatrix multiply(DoubleMatrix d) {

	if (d.rows != this.columns) {

	    throw new IllegalArgumentException("d.rows != this.columns: " + d.rows + ", " + this.columns + ".");

	}

	return transposingMultiply(this, d);

    }

    /**
     * The naive matrix multiplication algorithm.
     */
    private static DoubleMatrix naiveMultiply(DoubleMatrix m1, DoubleMatrix m2) {

	DoubleMatrix p = new DoubleMatrix(m1.rows, m2.columns);

	for (int i = 0; i < p.rows; i += 1) {

	    for (int j = 0; j < p.columns; j += 1) {

		double s = 0;

		for (int k = 0; k < m1.columns; k += 1) {

		    s += m1.data[i][k] * m2.data[k][j];

		}

		p.data[i][j] = s;

	    }

	}

	return p;

    }

    /**
     * A variant of the naive algorithm which attempts to improve
     * memory locality by transposing the second matrix. Instead of
     * multiplying elements of a row by elements of a column, we
     * multiply elements of a row by elements of a row. Since the
     * matrices are stored as arrays of rows, this avoids constantly
     * skipping across rows.
     *
     * For small matrices (e.g., less than 100 x 100), this doesn't
     * make a difference, most likely because if the matrices are
     * small enough to fit in the cache, locality is a non-issue. On
     * the other hand, for large matrices, I have observed a speedup
     * of as much as 7-9x (for 1000 x 1000 matrices).
     */
    private static DoubleMatrix transposingMultiply(DoubleMatrix m1, DoubleMatrix m2) {

	m2 = m2.transpose();

	DoubleMatrix p = new DoubleMatrix(m1.rows, m2.rows);

	for (int i = 0; i < p.rows; i += 1) {

	    for (int j = 0; j < p.columns; j += 1) {

		double s = 0;

		for (int k = 0; k < m1.columns; k += 1) {

		    s += m1.data[i][k] * m2.data[j][k];

		}

		p.data[i][j] = s;

	    }

	}

	return p;

    }

    /**
     * The naive matrix multiplication algorithm, implemented in C and
     * accessed via JNI.
     */
    private static DoubleMatrix jniMultiply(DoubleMatrix m1, DoubleMatrix m2) {

	m2 = m2.transpose();

	double[][] p = JNIMath.multiply(m1.data, m1.rows, m1.columns, m2.data, m2.rows, m2.columns);

	return new DoubleMatrix(p, false);

    }


    /**
     * Multiplies this matrix with the given matrix elementwise.
     */
    public DoubleMatrix multiplyElementwise(DoubleMatrix d) {

	if ((this.rows != d.rows) || (this.columns != d.columns)) {

	    throw new IllegalArgumentException("Dimensions of matrices are not equal: " + this.rows + "x" + this.columns + ", " + d.rows + "x" + d.columns + ".");

	}

	DoubleMatrix product = new DoubleMatrix(this.rows, this.columns);

	for (int i = 0; i < product.rows; i += 1) {

	    for (int j = 0; j < product.columns; j += 1) {

		product.data[i][j] = this.data[i][j] * d.data[i][j];

	    }

	}

	return product;

    }


    /**
     * Returns the transpose of this matrix.
     */
    public DoubleMatrix transpose() {

	DoubleMatrix transpose = new DoubleMatrix(this.columns, this.rows);

	for (int i = 0; i < this.rows; i += 1) {

	    for (int j = 0; j < this.columns; j += 1) {

		transpose.data[j][i] = this.data[i][j];

	    }

	}

	return transpose;

    }


    /**
     * Multiples this matrix on the left by the given vector on the
     * right, treating the vector as a column vector.
     */
    public DoubleVector multiply(DoubleVector d) {

	assert (this.columns == d.length): "Number of columns in matrix must equal length of vector: " + this.columns + ", " + d.length + ".";

	    DoubleVector result = new DoubleVector(this.rows);

	    for (int i = 0; i < result.length; i += 1) {

		double sum = 0;

		for (int j = 0; j < this.columns; j += 1) {

		    sum += this.data[i][j] * d.data[j];

		}

		result.data[i] = sum;

	    }

	    return result;

    }


    /**
     * Adds the given number to each element of this matrix.
     */
    public DoubleMatrix add(double d) {

	DoubleMatrix result = new DoubleMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		result.data[i][j] = this.data[i][j] + d;

	    }

	}

	return result;

    }


    /**
     * Subtracts the given number from each element of this matrix.
     */
    public DoubleMatrix subtract(double d) {

	DoubleMatrix result = new DoubleMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		result.data[i][j] = this.data[i][j] - d;

	    }

	}

	return result;

    }


    /**
     * Multiplies each element of this matrix by the given number.
     */
    public DoubleMatrix multiply(double d) {

	DoubleMatrix result = new DoubleMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		result.data[i][j] = this.data[i][j] * d;

	    }

	}

	return result;

    }


    /**
     * Divides each element of this matrix by the given number.
     */
    public DoubleMatrix divide(double d) {

	DoubleMatrix result = new DoubleMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		result.data[i][j] = this.data[i][j] / d;

	    }

	}

	return result;

    }


    /**
     * If this matrix is square, returns a vector containing the
     * diagonal entries of this matrix.
     */
    public DoubleVector diagonal() {

	assert isSquare(): "Matrix must be square: " + this.rows + "x" + this.columns + ".";
 
	DoubleVector result = new DoubleVector(this.rows);

	for (int i = 0; i < result.length; i += 1) {

	    result.data[i] = this.data[i][i];

	}

	return result;

    }


    /**
     * Returns the trace of this matrix, the sum of the diagonal
     * elements. This matrix must be square.
     */
    public double trace() {

	if (!isSquare()) {

	    throw new IllegalArgumentException("Matrix is not square: " + rows + ", " + columns + ".");

	}

	double trace = 0;

	for (int i = 0; i < rows; i += 1) {

	    trace += data[i][i];

	}

	return trace;

    }


    /**
     * Returns the maximum element in this matrix.
     */
    public double max() {

	double max = Double.NEGATIVE_INFINITY;

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		max = Math.max(max, data[i][j]);

	    }

	}

	return max;

    }


    /**
     * Returns the minimum element in this matrix.
     */
    public double min() {

	double min = Double.POSITIVE_INFINITY;

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		min = Math.min(min, data[i][j]);

	    }

	}

	return min;

    }


    /**
     * Returns a vector whose ith entry is the mean of the ith column
     * of this matrix.
     */
    public DoubleVector mean() {

	DoubleVector m = new DoubleVector(columns);

	for (int i = 0; i < columns; i += 1) {

	    m.set(i, getColumn(i).mean());

	}

	return m;

    }


    /**
     * Returns the variance-covariance matrix of the columns of this
     * matrix: the (i, j)th entry of the result matrix is the
     * covariance between the ith and jth columns of this matrix.
     */
    public DoubleMatrix covariance() {

	DoubleMatrix vcov =  new DoubleMatrix(columns, columns);

	for (int i = 0; i < columns; i += 1) {

	    DoubleVector ci = getColumn(i);

	    for (int j = i; j < columns; j += 1) {

		DoubleVector cj = getColumn(j);
		double vij = ci.covariance(cj);

		vcov.set(i, j, vij);
		vcov.set(j, i, vij);

	    }

	}

	return vcov;

    }


    /**
     * Returns a new matrix whose (i, j)th entry is the correlation
     * between the ith and jth columns of this matrix.
     */
    public DoubleMatrix correlation() {

	DoubleMatrix cor =  new DoubleMatrix(columns, columns);

	for (int i = 0; i < columns; i += 1) {

	    DoubleVector ci = getColumn(i);

	    for (int j = i; j < columns; j += 1) {

		DoubleVector cj = getColumn(j);
		double cij = ci.correlation(cj);

		cor.set(i, j, cij);
		cor.set(j, i, cij);

	    }

	}

	return cor;

    }


    /**
     * Returns the inverse of this matrix.
     */
    public DoubleMatrix inverse() throws SingularMatrixException {

	assert isSquare(): "Matrix must be square: " + this.rows + "x" + this.columns + ".";

	    DoubleMatrix m = this.appendColumns(Matrices.identity(this.rows));
	    GaussianElimination ge = new GaussianElimination(m);

	    ge.forward();
	    ge.backward();
	    ge.diagonal();

	    return m.selectColumns(IntVector.sequence(columns, 2 * columns));

    }


    /**
     * Returns the determinant of this matrix.
     *
     * TODO: For small matrices (e.g., less than 7 x 7 or so), it
     * would probably be reasonably efficient, and perhaps more
     * accurate, to compute the determinant directly using the
     * O(n!)-time cofactor expansion.
     */
    public double determinant() throws SingularMatrixException {

	assert isSquare(): "Matrix must be square: " + this.rows + "x" + this.columns + ".";

	    DoubleMatrix m = this.clone();
	    GaussianElimination ge = new GaussianElimination(m);

	    ge.forward();

	    double d = m.diagonal().product();

	    if ((ge.rowSwaps() % 2) == 0) {

		return d;

	    } else {

		return -d;

	    }

    }


    /**
     * Returns the vector x such that this.multiply(x) is equal to the
     * given vector. In other words, solves the system of linear
     * equations defined by this matrix and the given vector.
     *
     * This method works only for square matrices.
     */
    public DoubleVector solve(DoubleVector dv) throws SingularMatrixException {

	if (!isSquare()) {

	    throw new IllegalArgumentException("Matrix is not square: " + this.rows + " x " + this.columns + ".");

	}

	if (this.rows != dv.length()) {

	    throw new IllegalArgumentException("Size of matrix doesn't match length of vector: " + this.rows + ", " + dv.length + ".");

	}

	DoubleMatrix m = this.appendColumn(dv);
	GaussianElimination ge = new GaussianElimination(m);

	ge.forward();
	ge.backward();
	ge.diagonal();

	return m.getColumn(this.columns);

    }


    /**
     * Returns a vector whose ith element is the sum of the ith column
     * of this matrix.
     */
    public DoubleVector sumColumns() {

	DoubleVector sc = new DoubleVector(columns);

	for (int j = 0; j < columns; j += 1) {

	    sc.data[j] = 0;

	    for (int i = 0; i < rows; i += 1) {

		sc.data[j] += this.data[i][j];

	    }

	}

	return sc;

    }


    /**
     * Returns a vector whose ith element is the sum of the ith row of
     * this matrix.
     */
    public DoubleVector sumRows() {

	DoubleVector sr = new DoubleVector(rows);

	for (int i = 0; i < rows; i += 1) {

	    sr.data[i] = 0;

	    for (int j = 0; j < columns; j += 1) {

		sr.data[i] += this.data[i][j];

	    }

	}

	return sr;

    }


    /**
     * Returns the sum of all of the elements of this matrix.
     */
    public double sum() {

	double s = 0;

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		s += data[i][j];

	    }

	}

	return s;

    }


    public DoubleMatrix entropy() {

	DoubleMatrix h = new DoubleMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		if (this.data[i][j] == 0) {

		    h.data[i][j] = 0;

		} else {

		    h.data[i][j] = -this.data[i][j] * Lib.log2(this.data[i][j]);

		}

	    }

	}

	return h;

    }

}
