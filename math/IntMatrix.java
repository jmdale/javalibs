package math;

import java.io.Serializable;

/**
 * A matrix of ints.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060320
 */
public class IntMatrix implements Cloneable, Serializable {

    int[][] data;
    int rows;
    int columns;

    /**
     * Creates a matrix with the given numbers of rows and
     * columns. All elements are initially zero.
     */
    public IntMatrix(int rows, int columns) {
	this.data = new int[rows][columns];
	this.rows = rows;
	this.columns = columns;
    }

    /**
     * Creates a square matrix with the given number of rows and
     * columns. All elements are initially zero.
     */
    public IntMatrix(int size) {
	this.data = new int[size][size];
	this.rows = size;
	this.columns = size;
    }

    /**
     * Creates a new matrix from the given array. The elements of the
     * array are copied into a new array internally. If the rows of
     * the given array are not all of the same length, they are filled
     * with zeroes up to the length of the longest row.
     */
    public IntMatrix(int[][] data) {
	this(data, true);
    }

    public IntMatrix(int[][] data, boolean copy) {
	this.rows = data.length;

	if (!rowLengthsEqual(data)) {
	    throw new IllegalArgumentException("Lengths of rows in data matrix are not all equal.");
	}

	this.columns = data[0].length;

	if (copy) {
	    this.data = new int[rows][columns];
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
    private static boolean rowLengthsEqual(int[][] data) {
	int len = data[0].length;

	for (int i = 1; i < data.length; ++i) {
	    if (data[i].length != len) {
		return false;
	    }
	}

	return true;
    }

    public IntMatrix clone() {
	IntMatrix clone = new IntMatrix(this.rows, this.columns);
	for (int i = 0; i < this.rows; ++i) {
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
	    System.out.print(data[i][j]);
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
    public int get(int i, int j) {
	return data[i][j];
    }

    /**
     * Returns a vector containing the elements in the ith row of this
     * matrix.
     */
    public IntVector getRow(int i) {
	int[] r = new int[columns];
	for (int j = 0; j < columns; ++j) {
	    r[j] = data[i][j];
	}
	return new IntVector(r);
    }

    /**
     * Returns a vector containing the elements in the jth column of
     * this matrix.
     */
    public IntVector getColumn(int j) {
	int[] c = new int[rows];
	for (int i = 0; i < rows; ++i) {
	    c[i] = data[i][j];
	}
	return new IntVector(c);
    }

    /**
     * Returns an array containing the rows of this matrix as vectors.
     */
    public IntVector[] getRows() {
	IntVector[] iv = new IntVector[this.rows];
	for (int i = 0; i < this.rows; ++i) {
	    iv[i] = getRow(i);
	}
	return iv;
    }

    /**
     * Returns an array containing the columns of this matrix as
     * vectors.
     */
    public IntVector[] getColumns() {
	IntVector[] iv = new IntVector[this.columns];
	for (int j = 0; j < this.columns; ++j) {
	    iv[j] = getColumn(j);
	}
	return iv;
    }

    /**
     * Returns a new matrix containing the rows of this matrix for
     * which the corresponding element in the given BooleanVector is
     * true.
     */
    public IntMatrix selectRows(BooleanVector bv) {
	if (this.rows != bv.length) {
	    throw new IllegalArgumentException("this.rows != bv.length: " + this.rows + ", " + bv.length + ".");
	}

	int[][] sel = new int[bv.countTrue()][columns];
	int i = 0;

	for (int k = 0; k < bv.length; ++k) {
	    if (bv.data[k]) {
		// replace this loop with an arraycopy?
		for (int j = 0; j < columns; ++j) {
		    sel[i][j] = this.data[k][j];
		}
		++i;
	    }
	}

	return new IntMatrix(sel);
    }

    /**
     * Returns a new matrix containing the rows of this matrix whose
     * indices are contained in the given IntVector.
     */
    public IntMatrix selectRows(IntVector iv) {
	int[][] sel = new int[iv.length()][columns];

	for (int i = 0; i < iv.length(); ++i) {
	    int ix = iv.get(i);
	    for (int j = 0; j < columns; ++j) {
		sel[i][j] = this.data[ix][j];
	    }
	}

	return new IntMatrix(sel);
    }

    /**
     * Selects a contiguous sequence of rows from this matrix, from
     * row start (inclusive) to row end (exclusive).
     */
    public IntMatrix selectRows(int start, int end) {
	IntMatrix sel = new IntMatrix(end - start, this.columns);

	for (int i = start; i < end; ++i) {
	    System.arraycopy(this.data[i], 0, sel.data[i - start], 0, this.columns);
	}

	return sel;
    }

    /**
     * Returns a new matrix containing the columns of this matrix for
     * which the corresponding element in the given BooleanVector is
     * true.
     */
    public IntMatrix selectColumns(BooleanVector bv) {

	if (this.columns != bv.length) {

	    throw new IllegalArgumentException("this.columns != bv.length: " + this.columns + ", " + bv.length + ".");

	}

	int[][] sel = new int[rows][bv.countTrue()];
	int j = 0;

	for (int k = 0; k < bv.length; k += 1) {

	    if (bv.data[k]) {

		for (int i = 0; i < rows; i += 1) {

		    sel[i][j] = this.data[i][k];

		}

		j += 1;

	    }

	}

	return new IntMatrix(sel);

    }


    /**
     * Returns a new matrix containing the columns of this matrix
     * whose indices are contained in the given IntVector.
     */
    public IntMatrix selectColumns(IntVector iv) {

	int[][] sel = new int[rows][iv.length()];

	for (int j = 0; j < iv.length(); j += 1) {

	    int jx = iv.get(j);

	    for (int i = 0; i < rows; i += 1) {

		sel[i][j] = this.data[i][jx];

	    }

	}

	return new IntMatrix(sel);

    }


    /**
     * Selects a contiguous sequence of columns from this matrix, from
     * column start (inclusive) to column end (exclusive).
     */
    public IntMatrix selectColumns(int start, int end) {

	IntMatrix sel = new IntMatrix(this.rows, end - start);

	for (int i = 0; i < this.rows; i += 1) {

	    System.arraycopy(this.data[i], start, sel.data[i], 0, end - start);

	}

	return sel;

    }


    /**
     * Sets the entry in the ith row and the jth column of this
     * matrix to the given value.
     */
    public void set(int i, int j, int x) {

	data[i][j] = x;

    }


    /**
     * Sets the elements in the ith row of this matrix to be those in
     * the given vector.
     */
    public void setRow(int i, IntVector iv) {

	if (iv.length != this.columns) {

	    throw new IllegalArgumentException("iv.length != this.columns: " + iv.length + ", " + this.columns + ".");

	}

	for (int j = 0; j < columns; j += 1) {

	    this.data[i][j] = iv.data[j];

	}

    }


    /**
     * Sets the elements in the jth column of this matrix to be those
     * in the given vector.
     */
    public void setColumn(int j, IntVector iv) {

	if (iv.length != this.rows) {

	    throw new IllegalArgumentException("iv.length != this.rows: " + iv.length + ", " + this.rows + ".");

	}

	for (int i = 0; i < rows; i += 1) {

	    this.data[i][j] = iv.data[i];

	}

    }


    /**
     * Sets each element in the ith row of this matrix to be equal to
     * the given value.
     */
    public void fillRow(int i, int x) {

	for (int j = 0; j < columns; j += 1) {

	    this.data[i][j] = x;

	}

    }


    /**
     * Sets each element in the jth column of this matrix to be equal
     * to the given value.
     */
    public void fillColumn(int j, int x) {

	for (int i = 0; i < rows; i += 1) {

	    this.data[i][j] = x;

	}

    }


    /**
     * Returns a new matrix consisting of this matrix with the given
     * vector appended on the right as a column.
     */
    public IntMatrix appendColumn(IntVector iv) {

	if (iv.length != this.rows) {

	    throw new IllegalArgumentException("iv.length != this.rows: " + iv.length + ", " + this.rows + ".");

	}

	IntMatrix im = new IntMatrix(this.rows, this.columns + 1);

	for (int i = 0; i < rows; i += 1) {

	    System.arraycopy(this.data[i], 0, im.data[i], 0, this.columns);
	    im.data[i][this.columns] = iv.data[i];

	}

	return im;

    }


    /**
     * Returns a new matrix containing the columns of this matrix
     * followed by the columns of the given matrix.
     */
    public IntMatrix appendColumns(IntMatrix im) {

	if (im.rows != this.rows) {

	    throw new IllegalArgumentException("im.rows != this.rows: " + im.rows + ", " + this.rows + ".");

	}

	IntMatrix imNew = new IntMatrix(this.rows, this.columns + im.columns);

	for (int i = 0; i < this.rows; i += 1) {

	    System.arraycopy(this.data[i], 0, imNew.data[i], 0, this.columns);
	    System.arraycopy(im.data[i], 0, imNew.data[i], this.columns, im.columns);

	}

	return imNew;

    }


    /**
     * Swaps the ith and jth rows of this matrix, in place.
     */
    public void swapRows(int i, int j) {

	int[] temp = this.data[i];
	this.data[i] = this.data[j];
	this.data[j] = temp;

    }


    /**
     * Multiplies the ith row of this matrix by x.
     */
    public void multiplyRow(int i, int x) {

	for (int j = 0; j < this.columns; j += 1) {

	    this.data[i][j] *= x;

	}

    }


    /**
     * Adds x times the i1th row of this matrix to the i2th row of
     * this matrix.
     */
    public void multiplyRowAndAdd(int i1, int x, int i2) {

	for (int j = 0; j < this.columns; j += 1) {

	    this.data[i2][j] += x * this.data[i1][j];

	}

    }


    /**
     * Adds this matrix and the given matrix, returning the result.
     */
    public IntMatrix add(IntMatrix im) {

	if (im.rows != this.rows) {

	    throw new IllegalArgumentException("im.rows != this.rows: " + im.rows + ", " + this.rows + ".");

	}

	if (im.columns != this.columns) {

	    throw new IllegalArgumentException("im.columns != this.columns: " + im.columns + ", " + this.columns + ".");

	}

	IntMatrix sum = new IntMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		sum.data[i][j] = this.data[i][j] + im.data[i][j];

	    }

	}

	return sum;

    }


    /**
     * Multiplies this matrix by the given matrix on the right,
     * returning the result.
     */
    public IntMatrix multiply(IntMatrix im) {

	if (im.rows != this.columns) {

	    throw new IllegalArgumentException("im.rows != this.columns: " + im.rows + ", " + this.columns + ".");

	}

	return transposingMultiply(this, im);

    }

    /**
     * The naive matrix multiplication algorithm.
     */
    private static IntMatrix naiveMultiply(IntMatrix m1, IntMatrix m2) {

	IntMatrix p = new IntMatrix(m1.rows, m2.columns);

	for (int i = 0; i < p.rows; i += 1) {

	    for (int j = 0; j < p.columns; j += 1) {

		int s = 0;

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
    private static IntMatrix transposingMultiply(IntMatrix m1, IntMatrix m2) {

	m2 = m2.transpose();

	IntMatrix p = new IntMatrix(m1.rows, m2.rows);

	for (int i = 0; i < p.rows; i += 1) {

	    for (int j = 0; j < p.columns; j += 1) {

		int s = 0;

		for (int k = 0; k < m1.columns; k += 1) {

		    s += m1.data[i][k] * m2.data[j][k];

		}

		p.data[i][j] = s;

	    }

	}

	return p;

    }


    /* jniMultiply removed. */


    /**
     * Multiplies this matrix with the given matrix elementwise.
     */
    public IntMatrix multiplyElementwise(IntMatrix im) {

	if ((this.rows != im.rows) || (this.columns != im.columns)) {

	    throw new IllegalArgumentException("Dimensions of matrices are not equal: " + this.rows + "x" + this.columns + ", " + im.rows + "x" + im.columns + ".");

	}

	IntMatrix product = new IntMatrix(this.rows, this.columns);

	for (int i = 0; i < product.rows; i += 1) {

	    for (int j = 0; j < product.columns; j += 1) {

		product.data[i][j] = this.data[i][j] * im.data[i][j];

	    }

	}

	return product;

    }


    /**
     * Returns the transpose of this matrix.
     */
    public IntMatrix transpose() {

	IntMatrix transpose = new IntMatrix(this.columns, this.rows);

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
    public IntVector multiply(IntVector iv) {

	if (iv.length != this.columns) {

	    throw new IllegalArgumentException("Number of columns in matrix must equal length of vector: " + this.columns + ", " + iv.length + ".");

	}

	IntVector result = new IntVector(this.rows);

	for (int i = 0; i < result.length; i += 1) {

	    int sum = 0;

	    for (int j = 0; j < this.columns; j += 1) {

		sum += this.data[i][j] * iv.data[j];

	    }

	    result.data[i] = sum;

	}

	return result;

    }


    /**
     * Adds the given number to each element of this matrix.
     */
    public IntMatrix add(int x) {

	IntMatrix result = new IntMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		result.data[i][j] = this.data[i][j] + x;

	    }

	}

	return result;

    }


    /**
     * Subtracts the given number from each element of this matrix.
     */
    public IntMatrix subtract(int x) {

	IntMatrix result = new IntMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		result.data[i][j] = this.data[i][j] - x;

	    }

	}

	return result;

    }


    /**
     * Multiplies each element of this matrix by the given number.
     */
    public IntMatrix multiply(int x) {

	IntMatrix result = new IntMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		result.data[i][j] = this.data[i][j] * x;

	    }

	}

	return result;

    }


    /**
     * Divides each element of this matrix by the given number.
     */
    public IntMatrix divide(int x) {

	IntMatrix result = new IntMatrix(rows, columns);

	for (int i = 0; i < rows; i += 1) {

	    for (int j = 0; j < columns; j += 1) {

		result.data[i][j] = this.data[i][j] / x;

	    }

	}

	return result;

    }


    /**
     * If this matrix is square, returns a vector containing the
     * diagonal entries of this matrix.
     */
    public IntVector diagonal() {

	if (rows != columns) {

	    throw new IllegalArgumentException("Matrix must be square: " + rows + "x" + columns + ".");

	}
 
	IntVector result = new IntVector(this.rows);

	for (int i = 0; i < result.length; i += 1) {

	    result.data[i] = this.data[i][i];

	}

	return result;

    }


    /**
     * Returns the trace of this matrix, the sum of the diagonal
     * elements. This matrix must be square.
     */
    public int trace() {

	if (rows != columns) {

	    throw new IllegalArgumentException("Matrix must be square: " + rows + ", " + columns + ".");

	}

	int trace = 0;

	for (int i = 0; i < rows; i += 1) {

	    trace += data[i][i];

	}

	return trace;

    }


    /**
     * Returns the maximum element in this matrix.
     */
    public int max() {

	int max = data[0][0];

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
    public int min() {

	int min = data[0][0];

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

	    IntVector ci = getColumn(i);

	    for (int j = i; j < columns; j += 1) {

		IntVector cj = getColumn(j);
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

	    IntVector ci = getColumn(i);

	    for (int j = i; j < columns; j += 1) {

		IntVector cj = getColumn(j);
		double cij = ci.correlation(cj);

		cor.set(i, j, cij);
		cor.set(j, i, cij);

	    }

	}

	return cor;

    }


    /* Removed inverse. */


    /* Removed determinant. */


    /* Removed solve. */


    /**
     * Returns a vector whose ith element is the sum of the ith column
     * of this matrix.
     */
    public IntVector sumColumns() {

	IntVector sc = new IntVector(columns);

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
    public IntVector sumRows() {

	IntVector sr = new IntVector(rows);

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
    public int sum() {
	int s = 0;
	for (int i = 0; i < rows; ++i) {
	    for (int j = 0; j < columns; ++j) {
		s += data[i][j];
	    }
	}
	return s;
    }


    /* Removed entropy. */

}
