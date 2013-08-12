
package math;

import static math.Lib.average;
import static math.Lib.isEven;
import static math.Lib.log2;

import util.fn.DoubleToBoolean;
import util.fn.GreaterThanDouble;
import util.fn.LessThanDouble;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A vector of doubles.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20070918
 */
public class DoubleVector implements Cloneable, Serializable {

    int length;

    double[] data;


    public DoubleVector(int length) {

	this.length = length;
	this.data = new double[length];

    }

    public DoubleVector(double[] data) { // varargs?

	this.length = data.length;
	this.data = data.clone();

    }

    public DoubleVector(int[] data) {

	this(util.Arrays.toDoubles(data));

    }

    public DoubleVector(IntVector iv) {

	this(iv.data);

    }

    public DoubleVector(Collection<Double> c) {

	this.length = c.size();
	this.data = new double[length];

	int i = 0;

	for (Double d : c) {

	    this.data[i++] = d;

	}

    }


    public DoubleVector clone() {

	DoubleVector clone = null;

	try {

	    clone = (DoubleVector) super.clone();

	} catch (CloneNotSupportedException cnse) {

	    // can't happen

	}

	clone.data = this.data.clone();

	return clone;

    }


    public String toString() {

	StringBuffer sb = new StringBuffer();

	sb.append('[');

	if (data.length > 0) {

	    sb.append(data[0]);

	    for (int i = 1; i < data.length; i += 1) {

		sb.append(' ');
		sb.append(data[i]);

	    }

	}

	sb.append(']');

	return sb.toString();

    }


    public boolean equals(Object o) {

	return (o instanceof DoubleVector) && this.equals((DoubleVector) o);

    }

    public boolean equals(DoubleVector dv) {

	return lengthsEqual(this, dv) && elementsEqual(this, dv);

    }


    public double[] data() {

	return data;

    }


    public int length() {

	return length;

    }


    /**
     * Returns the ith element of this vector.
     */
    public double get(int i) {

	return data[i];

    }


    /**
     * Sets the ith element of this vector to the given value.
     */
    public void set(int i, double x) {

	data[i] = x;

    }


    /**
     * Sets each element of this vector to the given value.
     */
    public void fill(double x) {

	Arrays.fill(data, x);

    }


    /**
     * Returns a new vector obtained by inserting d at the ith
     * position of this vector, shifting elements to the right.
     */
    public DoubleVector insert(int i, double d) {

	DoubleVector dv = new DoubleVector(this.length + 1);

	System.arraycopy(this.data, 0, dv.data, 0, i);
	dv.data[i] = d;
	System.arraycopy(this.data, i, dv.data, i + 1, length - i);

	return dv;

    }


    /**
     * Returns a new vector containing the elements of this vector for
     * which the corresponding element of the given BooleanVector is
     * true.
     */
    public DoubleVector select(BooleanVector bv) {

	assert (this.length == bv.length): "Vector lengths not equal: " + this.length + ", " + bv.length;

	int n = bv.countTrue();
	double[] sel = new double[n];
	int i = 0;

	for (int j = 0; j < bv.length(); j += 1) {

	    if (bv.data[j]) {

		sel[i] = this.data[j];
		i += 1;

	    }

	}

	return new DoubleVector(sel);

    }


    /**
     * Returns a new vector containing the elements of this vector
     * indexed by the given vector of integers.
     */
    public DoubleVector select(IntVector idx) {

	DoubleVector sel = new DoubleVector(idx.length);

	for (int i = 0; i < idx.length; i += 1) {

	    sel.data[i] = this.data[idx.data[i]];

	}

	return sel;

    }


    /**
     * Returns a subsequence of this vector from start, inclusive, to
     * end, exclusive.
     */
    public DoubleVector subsequence(int start, int end) {

	double[] dataNew = new double[end - start];
	System.arraycopy(this.data, start, dataNew, 0, dataNew.length);

	return new DoubleVector(dataNew);

    }


    /**
     * Returns a subsequence of this vector from start, inclusive,
     * through the end of the vector.
     */
    public DoubleVector subsequence(int start) {

	return subsequence(start, data.length);

    }


    /**
     * Returns a new vector containing the elements of this vector
     * sorted into increasing order.
     */
    public DoubleVector sort() {

	DoubleVector sorted = clone();
	Arrays.sort(sorted.data);

	return sorted;

    }


    /**
     * Returns a vector containing the indices of the order statistics
     * of this vector. In other words, a permutation of the integers
     * 0, ..., (this.length - 1) is returned, such that applying the
     * permutation to this vector would result in the elements of this
     * vector being sorted into ascending order.
     */
    public IntVector order() {

	Index[] indices = new Index[this.length];

	for (int i = 0; i < this.length; i += 1) {

	    indices[i] = new Index(this.data[i], i);

	}

	Arrays.sort(indices, eltComp);

	IntVector order = new IntVector(this.length);

	for (int i = 0; i < order.length; i += 1) {

	    order.data[i] = indices[i].idx;

	}

	return order;

    }

    private static class Index {

	double elt;
	int idx;

	Index(double elt, int idx) {

	    this.elt = elt;
	    this.idx = idx;

	}

    }

    private static final Comparator<Index> eltComp = new Comparator<Index>() {

	public int compare(Index i1, Index i2) {

	    if (i1.elt < i2.elt) {

		return -1;

	    } else if (i1.elt > i2.elt) {

		return 1;

	    } else {

		return 0;

	    }

	}

    };


    /**
     * Returns a new vector containing the elements of this vector in
     * reverse order.
     */
    public DoubleVector reverse() {

	DoubleVector reversed = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    reversed.data[i] = this.data[(length - 1) - i];

	}

	return reversed;

    }


    /**
     * Swaps the elements of this vector at the given indices.
     */
    public void swap(int i, int j) {

	if (i != j) {

	    double temp = data[i];
	    data[i] = data[j];
	    data[j] = temp;

	}

    }


    /**
     * Returns a new vector containing the elements of this vector
     * followed by the elements of the given vector.
     */
    public DoubleVector append(DoubleVector dv) {

	DoubleVector app = new DoubleVector(this.length + dv.length);

	System.arraycopy(this.data, 0, app.data, 0, this.length);
	System.arraycopy(dv.data, 0, app.data, this.length, dv.length);

	return app;

    }


    /**
     * Given a Collection of vectors, append them together in the
     * order in which they are returned by the Collection's iterator;
     * a new vector is created.
     */
    public static DoubleVector append(Collection<DoubleVector> c) {

	int length = 0;

	for (DoubleVector dv : c) {

	    length += dv.length;

	}

	DoubleVector appended = new DoubleVector(length);
	int i = 0;

	for (DoubleVector dv : c) {

	    System.arraycopy(dv.data, 0, appended.data, i, dv.length);
	    i += dv.length;

	}

	return appended;

    }


    public void split(DoubleVector dv1, DoubleVector dv2) {

	System.arraycopy(this.data, 0, dv1.data, 0, dv1.length);
	System.arraycopy(this.data, dv1.length, dv2.data, 0, dv2.length);

    }


    /**
     * Returns the first index at which the given number appears in
     * this vector, starting at the given index, or -1 if the given
     * number does not appear in this vector.
     */
    public int indexOf(double d, int start) {

	for (int i = start; i < length; i++) {

	    if (data[i] == d) {

		return i;

	    }

	}

	return -1;

    }


    /**
     * Returns the first index at which the given number appears in
     * this vector, or -1 if the given number does not appear in this
     * vector.
     */
    public int indexOf(double d) {

	return indexOf(d, 0);

    }


    /**
     * Returns true if the given number appears in this vector.
     */
    public boolean contains(double d) {

	return (indexOf(d) != -1);

    }


    /**
     * Returns the sum of the elements in this vector.
     */
    public double sum() {

	double sum = 0;

	for (int i = 0; i < length; i += 1) {

	    sum += data[i];

	}

	return sum;

    }


    /**
     * Returns the product of the elements in this vector.
     */
    public double product() {

	double product = 1;

	for (int i = 0; i < length; i += 1) {

	    product *= data[i];

	}

	return product;

    }


    /**
     * Returns a new vector obtained by adding the given number to
     * each element of this vector.
     */
    public DoubleVector add(double d) {

	DoubleVector dv = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    dv.data[i] = this.data[i] + d;

	}

	return dv;

    }


    /**
     * Returns a new vector obtained by subtracting the given number
     * from each element of this vector.
     */
    public DoubleVector subtract(double d) {

	DoubleVector dv = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    dv.data[i] = this.data[i] - d;

	}

	return dv;

    }


    /**
     * Returns a new vector obtained by multiplying each element of
     * this vector by the given number.
     */
    public DoubleVector multiply(double d) {

	DoubleVector dv = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    dv.data[i] = this.data[i] * d;

	}

	return dv;

    }


    /**
     * Returns a new vector obtained by dividing each element of this
     * vector by the given number.
     */
    public DoubleVector divide(double d) {

	DoubleVector dv = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    dv.data[i] = this.data[i] / d;

	}

	return dv;

    }


    /**
     * Returns a new vector obtained by dividing each element of this
     * vector by the sum of the elements in this vector.
     */
    public DoubleVector normalize() {

	return this.divide(this.sum());

    }


    /**
     * Returns a new vector containing the additive inverses of the
     * elements in this vector.
     */
    public DoubleVector negate() {

	DoubleVector dv = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    dv.data[i] = -this.data[i];

	}

	return dv;

    }


    /**
     * Returns a new vector containing the natural logarithms of the
     * elements in this vector.
     */
    public DoubleVector log() {

	DoubleVector dv = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    dv.data[i] = Math.log(this.data[i]);

	}

	return dv;

    }


    /**
     * Returns a new vector containing the absolute values of the
     * elements in this vector.
     */
    public DoubleVector abs() {

	DoubleVector dv = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    dv.data[i] = Math.abs(this.data[i]);

	}

	return dv;

    }


    /**
     * Rounds each element of this vector to an int.
     */
    public IntVector round() {

	IntVector rounded = new IntVector(length);

	for (int i = 0; i < length; i += 1) {

	    rounded.data[i] = (int) Math.round(this.data[i]);

	}

	return rounded;

    }


    /**
     * Returns a new vector obtained by adding this vector and the
     * given vector elementwise.
     */
    public DoubleVector add(DoubleVector dv) {

	assert (lengthsEqual(this, dv)): "Vector lengths not equal";

	DoubleVector sum = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    sum.data[i] = this.data[i] + dv.data[i];

	}

	return sum;

    }


    /**
     * Returns a new vector obtained by subtracting the given vector
     * from this vector elementwise.
     */
    public DoubleVector subtract(DoubleVector dv) {

	assert (lengthsEqual(this, dv)): "Vector lengths not equal";

	DoubleVector diff = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    diff.data[i] = this.data[i] - dv.data[i];

	}

	return diff;

    }


    /**
     * Returns a new vector obtained by multiplying this vector by the
     * given vector elementwise.
     */
    public DoubleVector multiply(DoubleVector dv) {

	assert (lengthsEqual(this, dv)): "Vector lengths not equal";

	DoubleVector prod = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    prod.data[i] = this.data[i] * dv.data[i];

	}

	return prod;

    }


    /**
     * Returns a new vector obtained by dividing this vector by the
     * given vector elementwise.
     */
    public DoubleVector divide(DoubleVector dv) {

	assert (lengthsEqual(this, dv)): "Vector lengths not equal";

	DoubleVector quo = new DoubleVector(length);

	for (int i = 0; i < length; i += 1) {

	    quo.data[i] = this.data[i] / dv.data[i];

	}

	return quo;

    }


    /**
     * Computes the dot product of this vector with the given vector.
     */
    public double dotProduct(DoubleVector dv) {

	assert (lengthsEqual(this, dv)): "Vector lengths not equal";

	double dotProduct = 0;

	for (int i = 0; i < length; i += 1) {

	    dotProduct += data[i] * dv.data[i];

	}

	return dotProduct;

    }


    /**
     * Computes the dot product of this vector with the given vector.
     */
    public double dotProduct(IntVector iv) {

	assert (lengthsEqual(this, iv)): "Vector lengths not equal";

	double dotProduct = 0;

	for (int i = 0; i < length; i += 1) {

	    dotProduct += data[i] * iv.data[i];

	}

	return dotProduct;

    }


    /**
     * Returns a new vector which is the projection of this vector
     * onto the given vector.
     */
    public DoubleVector project(DoubleVector dv) {

	assert (lengthsEqual(this, dv)): "Vector lengths not equal";

	return dv.multiply(this.dotProduct(dv) / dv.dotProduct(dv));

    }


    /**
     * Returns a new DoubleVector of n equally-spaced numbers of which
     * the first is start and the last is end.
     */
    public static DoubleVector sequence(double start, double end, int n) {

	double by = (end - start) / (n - 1);
	double[] data = new double[n];

	for (int i = 0; i < n; i += 1) {

	    data[i] = start + i * by;

	}

	return new DoubleVector(data);

    }


    /**
     * Returns a new DoubleVector of length n whose elements are all
     * equal to d.
     */
    public static DoubleVector repeat(double d, int n) {

	DoubleVector dv = new DoubleVector(n);
	Arrays.fill(dv.data, d);

	return dv;

    }


    /**
     * Multiples this vector on the left by the given matrix on the
     * right, treating this vector as a row vector.
     */
    public DoubleVector multiply(DoubleMatrix d) {

	assert (this.length == d.rows):
	    "Length of vector must be equal to number of rows in matrix: " + this.length + ", " + d.rows + ".";

	DoubleVector result = new DoubleVector(d.columns);

	for (int j = 0; j < result.length; j += 1) {

	    result.data[j] = 0;

	    for (int i = 0; i < this.length; i += 1) {

		result.data[j] += this.data[i] * d.data[i][j];

	    }

	}

	return result;

    }


    /**
     * Multiplies this vector as a column by the given vector as a
     * row.
     */
    public DoubleMatrix outerProduct(DoubleVector d) {

	DoubleMatrix result = new DoubleMatrix(this.length, d.length);

	for (int i = 0; i < this.length; i += 1) {

	    for (int j = 0; j < d.length; j += 1) {

		result.data[i][j] = this.data[i] * d.data[j];

	    }

	}

	return result;

    }


    public double mean() {

	return sum() / length();

    }


    public double weightedMean(DoubleVector weights) {

	assert lengthsEqual(this, weights): "Sample and weights not of equal length.";

	return this.dotProduct(weights) / weights.sum();

    }


    /**
     * Computes the trimmed mean of this vector. trim is the fraction
     * of numbers to trim, from both the high and low ends of the
     * vector. For example, if this vector contains 100 elements and
     * trim is 0.05, the largest five and the smallest five numbers
     * will be left out of the mean.
     */
    public double trimmedMean(double trim) {

	int skip = (int) Math.floor(trim * data.length);
	double from = orderStatistic(skip);
	double to = orderStatistic(data.length - skip);

	double sum = 0;
	int count = 0;

	for (int i = 0; i < data.length; i += 1) {

	    if ((from <= data[i]) && (data[i] <= to)) {

		sum += data[i];
		count += 1;

	    }

	}

	return sum / count;

    }


    /**
     * A special case of the trimmed mean, where the top 25% and
     * bottom 25% of elements are trimmed.
     */
    public double midMean() {

	return trimmedMean(0.25);

    }


    /**
     * Computes the variance of the elements of this vector about m
     * (which need not be the mean).
     */
    public double variance(double m) {

	assert (data.length > 1): "Variance is undefined for a vector of length one.";

	double sigma = 0.0;

	for (int i = 0; i < data.length; i += 1) {

	    double d = data[i] - m;
	    sigma += (d * d);

	}

	return sigma / (data.length - 1);

    }


    /**
     * Computes the sample variance.
     */
    public double variance() {

	return variance(mean());

    }


    /**
     * Computes the standard deviation of the elements of this vector
     * about m (which need not be the mean).
     */
    public double standardDeviation(double m) {

	return Math.sqrt(variance(m));

    }


    /**
     * Computes the sample standard deviation.
     */
    public double standardDeviation() {

	return Math.sqrt(variance());

    }


    /**
     * Computes the standard error of the mean.
     */
    public double standardError() {

	return Math.sqrt(variance() / length);

    }


    /**
     * Computes the covariance between this vector and the given
     * vector.
     */
    public double covariance(DoubleVector dv) {

	assert (lengthsEqual(this, dv)): "Vector lengths not equal";

	/*
	 * This formula is given in R.A. Fisher, "Statistical Methods
	 * for Research Workers", p. 132 (1958).
	 */
	return (this.dotProduct(dv) - length * this.mean() * dv.mean()) / (length - 1);

    }


    /**
     * Computes the Pearson correlation coefficient r between this
     * vector and the given vector.
     */
    public double correlation(DoubleVector dv) {

	assert (lengthsEqual(this, dv)): "Vector lengths not equal";

	int n = this.length;
	double s_x = this.sum();
	double s_y = dv.sum();
	double d_xy = n * this.dotProduct(dv) - s_x * s_y;
	double d_x = n * this.dotProduct(this) - s_x * s_x;
	double d_y = n * dv.dotProduct(dv) - s_y * s_y;

	return d_xy / Math.sqrt(d_x * d_y);

    }


    /**
     * Returns a new vector whose ith element is the ith element of
     * this vector minus the mean of this vector.
     */
    public DoubleVector error() {

	return subtract(mean());

    }


    public DoubleVector squaredError() {

	DoubleVector error = error();

	return error.multiply(error);

    }


    public double sumOfSquaredError() {

	return squaredError().sum();

    }


    public DoubleVector absoluteError() {

	return error().abs();

    }


    public double meanAbsoluteDeviation() {

	return absoluteError().mean();

    }


    public double medianAbsoluteDeviation() {

	return absoluteError().median();

    }


    /**
     * Computes the maximum element of this vector.
     */
    public double max() {

	double max = data[0];

	for (int i = 1; i < length; i += 1) {

	    max = Math.max(max, data[i]);

	}

	return max;

    }


    /**
     * Computes the minimum element of this vector.
     */
    public double min() {

	double min = data[0];

	for (int i = 1; i < length; i += 1) {

	    min = Math.min(min, data[i]);

	}

	return min;

    }


    /**
     * Computes the index of the maximum element in this vector.
     */
    public int argmax() {

	double max = data[0];
	int argmax = 0;

	for (int i = 1; i < length; i += 1) {

	    if (data[i] > max) {

		max = data[i];
		argmax = i;

	    }

	}

	return argmax;

    }


    /**
     * Computes the index of the minimum element in this vector.
     */
    public int argmin() {

	double min = data[0];
	int argmin = 0;

	for (int i = 1; i < length; i += 1) {

	    if (data[i] < min) {

		min = data[i];
		argmin = i;

	    }

	}

	return argmin;

    }


    /**
     * The maximum element in this vector minus the minimum element.
     */
    public double range() {

	return max() - min();

    }


    /**
     * The average of the minimum and maximum elements of this vector.
     */
    public double midrange() {

	return average(min(), max());

    }


    /**
     * @see <a href="http://mathworld.wolfram.com/GeometricMean.html">MathWorld</a>
     */
    public double geometricMean() {

	return Math.exp(logGeometricMean());

    }


    public double logGeometricMean() {

	double accum = 0;

	for (int i = 0; i < length; i += 1) {

	    accum += Math.log(data[i]);

	}

	return accum / length();

    }


    /**
     * @see <a href="http://mathworld.wolfram.com/HarmonicMean.html">MathWorld</a>
     */
    public double harmonicMean() {

	double accum = 0;

	for (int i = 0; i < length; i += 1) {

	    accum += 1 / data[i];

	}

	return length() / accum;

    }


    /**
     * Computes the median of this vector.
     */
    public double median() {

	return medianByPartition();

    }


    /**
     * Computes the median of the numbers in this vector using a
     * divide and conquer approach.
     */
    public double medianByPartition() {

	int n = length;

	if (isEven(n)) {

	    return average(orderStatistic(n / 2), orderStatistic((n / 2) - 1));

	} else {

	    return orderStatistic(n / 2);

	}

    }


    public double quantile(double p) {

	int i = (int) (p * length);

	if (i == p * length) {

	    return orderStatistic(i);

	} else {

	    return average(orderStatistic(i), orderStatistic(i + 1));

	}

    }

    public double lowerQuartile() {

	return quantile(0.25);

    }

    public double upperQuartile() {

	return quantile(0.75);

    }

    public double interQuartileRange() {

	return upperQuartile() - lowerQuartile();

    }


    protected static boolean isEven(int n) {

	return ((n % 2) == 0);

    }


    protected static double average(double x, double y) {

	return (x + y) / 2;

    }


    /**
     * Returns the ith order statistic (indexed from 0) of this
     * vector.
     */
    public double orderStatistic(int i) {

	/*
	 * Calls orderStatistic on a copy to avoid changing the order
	 * of the elements in this vector.
	 */
	return clone().orderStatistic(i, 0, length - 1);

    }

    /**
     * From CLR, Chapter 10. This method reorders the elements of this
     * vector.
     */
    private double orderStatistic(int i, int start, int end) {

	if (start == end) {

	    return data[start];

	} else {

	    int split = randomizedPartition(start, end);
	    int diff = (split - start) + 1;

	    if (i < diff) {

		return orderStatistic(i, start, split);

	    } else {

		return orderStatistic(i - diff, split + 1, end);

	    }

	}

    }

    /**
     * Partitions the elements of this vector around an element chosen
     * at random.
     */
    private int randomizedPartition(int start, int end) {

	swap(start, random(start, end + 1));

	return partition(start, end);

    }

    /**
     * Returns a random integer between start, inclusive, and end,
     * exclusive.
     */
    private static int random(int start, int end) {

	return start + ((int) Math.floor(Math.random() * (end - start)));

    }

    /**
     * Partitions the portion of this vector between indices start and
     * end, inclusive, into subsets of elements which are,
     * respectively, less than or equal to and greater than or equal
     * to the element at index start. Returns an index i such that for
     * start <= j < i, data[j] <= data[i]; and for end >= j > i,
     * data[j] >= data[i].
     *
     * From CLR, Chapter 8.
     */
    private int partition(int start, int end) {

	double x = data[start];
	int i = start - 1;
	int j = end + 1;

	while (true) {

	    do { j -= 1; } while (data[j] > x);
	    do { i += 1; } while (data[i] < x);

	    if (i < j) {

		swap(i, j);

	    } else {

		return j;

	    }

	}

    }


    /**
     * Returns true if the given vectors are of equal length.
     */
    protected static boolean lengthsEqual(DoubleVector dv1, DoubleVector dv2) {

	return (dv1.length == dv2.length);

    }


    /**
     * Returns true if the given vectors are of equal length.
     */
    protected static boolean lengthsEqual(DoubleVector dv, IntVector iv) {

	return (dv.length == iv.length);

    }


    /**
     * Returns true if the given vectors' elements are pairwise
     * equal. Assumes that the lengths of the vectors are equal.
     */
    protected static boolean elementsEqual(DoubleVector dv1, DoubleVector dv2) {

	for (int i = 0; i < dv1.length; i += 1) {

	    if (dv1.data[i] != dv2.data[i]) {

		return false;

	    }

	}

	return true;

    }


    /**
     * Returns a new vector containing the elements of this vector
     * which are less than the given number.
     */
    public DoubleVector lessThan(double x) {

	DoubleToBoolean lt = new LessThanDouble.Double(x);

	return new DoubleVector(lt.filter(this.data));

    }


    /**
     * Returns a new vector containing the elements of this vector
     * which are greater than the given number.
     */
    public DoubleVector greaterThan(double x) {

	DoubleToBoolean gt = new GreaterThanDouble.Double(x);

	return new DoubleVector(gt.filter(this.data));

    }


    public DoubleVector entropy() {

	DoubleVector h = new DoubleVector(this.length);

	for (int i = 0; i < this.length; i += 1) {

	    if (this.data[i] == 0) {

		h.data[i] = 0;

	    } else {

		h.data[i] = -this.data[i] * log2(this.data[i]);

	    }

	}

	return h;

    }


    /**
     * Returns the l2-norm of this vector (i.e., its length).
     */
    public double norm() {

	double s = 0;

	for (int i = 0; i < data.length; i += 1) {

	    s += data[i] * data[i];

	}

	return Math.sqrt(s);

    }


    /**
     * Returns the p-norm of this vector (i.e., the pth root of the
     * sum of the pth powers of the absolute values of the elements of
     * the vector).
     */
    public double norm(double p) {

	if (p == Double.POSITIVE_INFINITY) {

	    return this.infinityNorm();

	} else {

	    double s = 0;

	    for (int i = 0; i < data.length; i += 1) {

		s += Math.pow(Math.abs(data[i]), p);

	    }

	    return Math.pow(s, 1 / p);

	}

    }


    /**
     * Returns the infinity-norm of this vector, i.e., the absolute
     * value of the largest component. This is method is not for
     * public consumption; it should be called via
     * norm(Double.POSITIVE_INFINITY).
     */
    protected double infinityNorm() {

	double max = data[0];

	for (int i = 0; i < data.length; i += 1) {

	    if (Math.abs(data[i]) > max) {

		max = Math.abs(data[i]);

	    }

	}

	return max;

    }


    public double distance(DoubleVector dv) {

	assert (lengthsEqual(this, dv)): "Lengths not equal.";

	double s = 0;

	for (int i = 0; i < data.length; i += 1) {

	    double d = this.data[i] - dv.data[i];
	    s += d * d;

	}

	return Math.sqrt(s);

    }

}
