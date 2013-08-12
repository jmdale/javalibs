
package math;

import util.Strings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * Useful (static) functions for working with matrices.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20060112
 */
public class Matrices {

    /**
     * Returns a new square matrix whose diagonal entries are the
     * elements of the given vector.
     */
    public static DoubleMatrix diagonal(DoubleVector d) {

	DoubleMatrix result = new DoubleMatrix(d.length);

	for (int i = 0; i < d.length; i += 1) {

	    result.data[i][i] = d.data[i];

	}

	return result;

    }


    /**
     * Returns the n by n identity matrix.
     */
    public static DoubleMatrix identity(int n) {

	DoubleMatrix identity = new DoubleMatrix(n);

	for (int i = 0; i < n; i += 1) {

	    identity.data[i][i] = 1;

	}

	return identity;

    }


    /**
     * Reads the contents of a file as a tab-delimited matrix of
     * doubles. Assumes that the file has no header line.
     */
    public static DoubleMatrix read(String filename) throws IOException {

	return read(filename, false);

    }


    /**
     * Reads the contents of a file as a tab-delimited matrix of
     * doubles.
     */
    public static DoubleMatrix read(String filename, boolean header) throws IOException {

	return read(new File(filename), header);

    }


    /**
     * Reads the contents of a file as a tab-delimited matrix of
     * doubles. Assumes that the file has no header line.
     */
    public static DoubleMatrix read(File file) throws IOException {

	return read(file, false);

    }


    /**
     * Reads the contents of a file as a tab-delimited matrix of
     * doubles.
     */
    public static DoubleMatrix read(File file, boolean header) throws IOException {

	List<DoubleVector> rows = readRows(file, header);
	int columns = countColumns(rows);
	DoubleMatrix dm = new DoubleMatrix(rows.size(), columns);

	int i = 0;

	for (DoubleVector row : rows) {

	    for (int j = 0; j < row.length(); j += 1) {

		dm.data[i][j] = row.data[j];

	    }

	    for (int j = row.length(); j < columns; j += 1) {

		dm.data[i][j] = Double.NaN;

	    }

	    i += 1;

	}

	return dm;

    }


    private static List<DoubleVector> readRows(File file, boolean header) throws IOException {

	List<DoubleVector> rows = new LinkedList<DoubleVector>();
	BufferedReader input = new BufferedReader(new FileReader(file));

	if (header) {

	    input.readLine();

	}

	while (true) {

	    String line = input.readLine();

	    if (line == null) {

		break;

	    }

	    String[] fields = Strings.tokenize(line);
	    DoubleVector row = new DoubleVector(fields.length);

	    for (int i = 0; i < fields.length; i += 1) {

		row.data[i] = Double.parseDouble(fields[i]);

	    }

	    rows.add(row);

	}

	return rows;

    }


    /**
     * Returns the length of the longest vector in the given list.
     */
    private static int countColumns(List<DoubleVector> rows) {

	int columns = -1;

	for (DoubleVector dv : rows) {

	    columns = Math.max(columns, dv.length());

	}

	return columns;

    }


    public static void write(DoubleMatrix d, String filename) throws IOException {

	write(d, new File(filename));

    }


    public static void write(DoubleMatrix d, File file) throws IOException {

	PrintWriter output = new PrintWriter(file);

	for (int i = 0; i < d.rows; i += 1) {

	    output.print(d.data[i][0]);

	    for (int j = 1; j < d.columns; j += 1) {

		output.print("\t" + d.data[i][j]);

	    }

	    output.println();
	    output.flush();

	}

    }

}
