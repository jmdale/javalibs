
package math;

/**
 * Performs Gaussian elimination on square matrices, possibly
 * augmented with extra columns.
 *
 * For example, if the matrix is augmented with one extra column,
 * doing Gaussian elimination solves a linear system. If the matrix is
 * augmented with the identity matrix, performing Gaussian elimination
 * transforms the identity matrix into the inverse of the matrix
 * eliminated.
 *
 * If the matrix is square, doing only forward Gaussian elimination
 * while counting row swaps allows the determinant to be computed from
 * the diagonal elements.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20050323
 */
public class GaussianElimination {

    private DoubleMatrix m;

    private int rowSwaps;


    public GaussianElimination(DoubleMatrix m) {

	this.m = m;
	this.rowSwaps = 0;

    }


    /**
     * Perform "forward" elimination, making m upper triangular.
     */
    public void forward() throws SingularMatrixException {

	for (int i = 0; i < m.rows - 1; i += 1) {

	    ensurePivot(i);

	    for (int j = i + 1; j < m.rows; j += 1) {

		eliminate(i, j);

	    }

	}

    }


    /**
     * Perform backsubstitution, making m diagonal (assuming m is
     * already upper triangular).
     */
    public void backward() {

	for (int i = m.rows - 1; i > 0; i -= 1) {

	    for (int j = i - 1; j >= 0; j -= 1) {

		eliminate(i, j);

	    }

	}

    }


    /**
     * Divides each row by the element on the diagonal.
     */
    public void diagonal() {

	for (int i = 0; i < m.rows; i += 1) {

	    double c = m.data[i][i];

	    for (int j = 0; j < m.columns; j += 1) {

		m.data[i][j] /= c;

	    }

	}

    }


    /**
     * Ensures that the pivot element m.data[i][i] is non-zero, by
     * swapping rows if necessary.
     */
    private void ensurePivot(int i) throws SingularMatrixException {

	if (m.data[i][i] == 0) {

	    m.swapRows(i, findPivotRow(i));
	    rowSwaps += 1;

	}

    }


    /**
     * Finds the smallest j > i such that m.data[j][i] != 0. If there
     * is no such j, throws SingularMatrixException.
     */
    private int findPivotRow(int i) throws SingularMatrixException {

	for (int j = i + 1; j < m.rows; j += 1) {

	    if (m.data[j][i] != 0) {

		return j;

	    }

	}

	throw new SingularMatrixException("No pivot at " + i + ".");

    }


    /**
     * Subtracts a multiple of row i from row j so that m.data[j][i]
     * becomes zero.
     */
    private void eliminate(int i, int j) {

	double c = m.data[j][i] / m.data[i][i];

	for (int k = 0; k < m.columns; k += 1) {

	    m.data[j][k] -= c * m.data[i][k];

	}

    }


    public int rowSwaps() {

	return this.rowSwaps;

    }

}
