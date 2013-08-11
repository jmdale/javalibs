package math;

import static math.Lib.average;
import static math.Lib.isEven;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A vector of ints.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070819
 */
public class IntVector implements Cloneable, Serializable {

    int[] data;
    int length;

    public IntVector(int length) {
	this.length = length;
	this.data = new int[length];
    }

    public IntVector(int[] data) {
	this.length = data.length;
	this.data = data.clone();
    }

    public IntVector(Collection<Integer> c) {
	this.length = c.size();
	this.data = new int[length];

	int i = 0;
	for (Integer n : c) {
	    this.data[i++] = n;
	}
    }

    public IntVector clone() {
	IntVector clone = null;
	try {
	    clone = (IntVector) super.clone();
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
	    for (int i = 1; i < data.length; ++i) {
		sb.append(' ');
		sb.append(data[i]);
	    }
	}
	sb.append(']');

	return sb.toString();
    }

    public boolean equals(Object o) {
	return (o instanceof IntVector) && this.equals((IntVector) o);
    }

    public boolean equals(IntVector iv) {
	return lengthsEqual(this, iv) && elementsEqual(this, iv);
    }

    public int[] data() {
	return data;
    }

    public int length() {
	return length;
    }

    /**
     * Returns the ith element of this vector.
     */
    public int get(int i) {
	return data[i];
    }

    /**
     * Sets the ith element of this vector to the given value.
     */
    public void set(int i, int x) {
	data[i] = x;
    }

    /**
     * Sets each element of this vector to the given value.
     */
    public void fill(int x) {
	Arrays.fill(data, x);
    }

    /**
     * Returns a new vector obtained by inserting n at the ith
     * position of this vector, shifting elements to the right.
     */
    public IntVector insert(int i, int n) {
	assert ((0 <= i) && (i < length + 1)):
	    "i must be at least zero and less than new length of vector: " + i + ", " + (length + 1) + ".";

	IntVector iv = new IntVector(this.length + 1);

	System.arraycopy(this.data, 0, iv.data, 0, i);
	iv.data[i] = n;
	System.arraycopy(this.data, i, iv.data, i + 1, length - i);

	return iv;
    }

    /**
     * Returns a new vector containing the elements of this vector for
     * which the corresponding element of the given BooleanVector is
     * true.
     */
    public IntVector select(BooleanVector bv) {
	assert (this.length == bv.length): "Vector lengths not equal: " + this.length + ", " + bv.length;

	int n = bv.countTrue();
	int[] sel = new int[n];
	int i = 0;

	for (int j = 0; j < bv.length(); j += 1) {
	    if (bv.data[j]) {
		sel[i] = this.data[j];
		i += 1;
	    }
	}

	return new IntVector(sel);
    }

    /**
     * Returns a new vector containing the elements of this vector
     * indexed by the given vector of integers.
     */
    public IntVector select(IntVector idx) {
	IntVector sel = new IntVector(idx.length);

	for (int i = 0; i < idx.length; ++i) {
	    sel.data[i] = this.data[idx.data[i]];
	}

	return sel;
    }

    /**
     * Returns a subsequence of this vector from start, inclusive, to
     * end, exclusive.
     */
    public IntVector subsequence(int start, int end) {
	assert (start >= 0): "start < 0";
	assert (start < data.length): "start >= length";
	assert (end >= start): "end < start";
	assert (end <= data.length): "end > length";

	int[] dataNew = new int[end - start];
	System.arraycopy(this.data, start, dataNew, 0, dataNew.length);

	return new IntVector(dataNew);
    }

    /**
     * Returns a subsequence of this vector from start, inclusive,
     * through the end of the vector.
     */
    public IntVector subsequence(int start) {
	return subsequence(start, data.length);
    }

    /**
     * Returns a new vector containing the elements of this vector
     * sorted into increasing order.
     */
    public IntVector sort() {
	IntVector sorted = clone();
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

	for (int i = 0; i < this.length; ++i) {
	    indices[i] = new Index(this.data[i], i);
	}

	Arrays.sort(indices, eltComp);

	IntVector order = new IntVector(this.length);

	for (int i = 0; i < order.length; ++i) {
	    order.data[i] = indices[i].idx;
	}

	return order;
    }

    private static class Index {
	int elt;
	int idx;

	Index(int elt, int idx) {
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
    public IntVector reverse() {
	IntVector reversed = new IntVector(length);
	for (int i = 0; i < length; ++i) {
	    reversed.data[i] = this.data[(length - 1) - i];
	}
	return reversed;
    }

    /**
     * Swaps the elements of this vector at the given indices.
     */
    public void swap(int i, int j) {
	if (i != j) {
	    int temp = data[i];
	    data[i] = data[j];
	    data[j] = temp;
	}
    }

    /**
     * Returns a new vector containing the elements of this vector
     * followed by the elements of the given vector.
     */
    public IntVector append(IntVector iv) {
	IntVector app = new IntVector(this.length + iv.length);

	System.arraycopy(this.data, 0, app.data, 0, this.length);
	System.arraycopy(iv.data, 0, app.data, this.length, iv.length);

	return app;
    }

    /**
     * Given a Collection of vectors, append them together in the
     * order in which they are returned by the Collection's iterator;
     * a new vector is created.
     */
    public static IntVector append(Collection<IntVector> c) {
	int length = 0;
	for (IntVector iv : c) {
	    length += iv.length;
	}

	IntVector appended = new IntVector(length);
	int i = 0;

	for (IntVector iv : c) {
	    System.arraycopy(iv.data, 0, appended.data, i, iv.length);
	    i += iv.length;
	}

	return appended;
    }

    public void split(IntVector iv1, IntVector iv2) {
	assert (this.length == (iv1.length + iv2.length));

	System.arraycopy(this.data, 0, iv1.data, 0, iv1.length);
	System.arraycopy(this.data, iv1.length, iv2.data, 0, iv2.length);
    }

    /**
     * Returns the first index at which the given number appears in
     * this vector, starting at the given index, or -1 if the given
     * number does not appear in this vector.
     */
    public int indexOf(int n, int start) {
	assert (start >= 0): "start < 0";
	assert (start < length): "start >= length";

	for (int i = start; i < length; ++i) {
	    if (data[i] == n) {
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
    public int indexOf(int n) {
	return indexOf(n, 0);
    }

    /**
     * Returns true if the given number appears in this vector.
     */
    public boolean contains(int n) {
	return (indexOf(n) != -1);
    }

    /**
     * Returns the sum of the elements in this vector.
     */
    public int sum() {
	int sum = 0;
	for (int i = 0; i < length; ++i) {
	    sum += data[i];
	}
	return sum;
    }

    /**
     * Returns the product of the elements in this vector.
     */
    public int product() {
	int product = 1;
	for (int i = 0; i < length; ++i) {
	    product *= data[i];
	}
	return product;
    }

    /**
     * Returns a new vector obtained by adding the given number to
     * each element of this vector.
     */
    public IntVector add(int n) {
	IntVector iv = new IntVector(length);
	for (int i = 0; i < length; ++i) {
	    iv.data[i] = this.data[i] + n;
	}
	return iv;
    }

    /**
     * Returns a new vector obtained by subtracting the given number
     * from each element of this vector.
     */
    public IntVector subtract(int n) {
	IntVector iv = new IntVector(length);
	for (int i = 0; i < length; ++i) {
	    iv.data[i] = this.data[i] - n;
	}
	return iv;
    }

    /**
     * Returns a new vector obtained by multiplying each element of
     * this vector by the given number.
     */
    public IntVector multiply(int n) {
	IntVector iv = new IntVector(length);
	for (int i = 0; i < length; ++i) {
	    iv.data[i] = this.data[i] * n;
	}
	return iv;
    }

    /**
     * Returns a new vector obtained by dividing each element of this
     * vector by the given number.
     */
    public DoubleVector divide(int n) {
	double d = (double) n;
	DoubleVector dv = new DoubleVector(length);
	for (int i = 0; i < length; ++i) {
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
    public IntVector negate() {
	IntVector iv = new IntVector(length);
	for (int i = 0; i < length; ++i) {
	    iv.data[i] = -this.data[i];
	}
	return iv;
    }

    /**
     * Returns a new vector containing the natural logarithms of the
     * elements in this vector.
     */
    public DoubleVector log() {
	DoubleVector dv = new DoubleVector(length);
	for (int i = 0; i < length; ++i) {
	    dv.data[i] = Math.log(this.data[i]);
	}
	return dv;
    }

    /**
     * Returns a new vector containing the absolute values of the
     * elements in this vector.
     */
    public IntVector abs() {
	IntVector iv = new IntVector(length);
	for (int i = 0; i < length; ++i) {
	    iv.data[i] = Math.abs(this.data[i]);
	}
	return iv;
    }

    /* round method removed. */

    /**
     * Returns a new vector obtained by adding this vector and the
     * given vector elementwise.
     */
    public IntVector add(IntVector iv) {
	assert (lengthsEqual(this, iv)): "Vector lengths not equal";

	IntVector sum = new IntVector(length);
	for (int i = 0; i < length; ++i) {
	    sum.data[i] = this.data[i] + iv.data[i];
	}
	return sum;
    }

    /**
     * Returns a new vector obtained by subtracting the given vector
     * from this vector elementwise.
     */
    public IntVector subtract(IntVector iv) {
	assert (lengthsEqual(this, iv)): "Vector lengths not equal";

	IntVector diff = new IntVector(length);
	for (int i = 0; i < length; ++i) {
	    diff.data[i] = this.data[i] - iv.data[i];
	}
	return diff;
    }

    /**
     * Returns a new vector obtained by multiplying this vector by the
     * given vector elementwise.
     */
    public IntVector multiply(IntVector iv) {
	assert (lengthsEqual(this, iv)): "Vector lengths not equal";

	IntVector prod = new IntVector(length);
	for (int i = 0; i < length; ++i) {
	    prod.data[i] = this.data[i] * iv.data[i];
	}
	return prod;
    }

    /**
     * Returns a new vector obtained by dividing this vector by the
     * given vector elementwise.
     */
    public DoubleVector divide(IntVector iv) {
	assert (lengthsEqual(this, iv)): "Vector lengths not equal";

	DoubleVector quo = new DoubleVector(length);
	for (int i = 0; i < length; ++i) {
	    quo.data[i] = ((double) this.data[i]) / iv.data[i];
	}
	return quo;
    }

    /**
     * Computes the dot product of this vector with the given vector.
     */
    public int dotProduct(IntVector iv) {
	assert (lengthsEqual(this, iv)): "Vector lengths not equal";

	int dotProduct = 0;
	for (int i = 0; i < length; ++i) {
	    dotProduct += data[i] * iv.data[i];
	}
	return dotProduct;
    }

    /* project method removed. */

    /**
     * Returns a new IntVector containing the integers between start,
     * inclusive, and end, exclusive.
     */
    public static IntVector sequence(int start, int end) {
	assert (start < end): "start >= end: " + start + ", " + end;

	int n = end - start;
	IntVector seq = new IntVector(n);
	for (int i = 0; i < n; ++i) {
	    seq.data[i] = start + i;
	}
	return seq;
    }

    /**
     * Returns a new DoubleVector of length n whose elements are all
     * equal to d.
     */
    public static IntVector repeat(int d, int n) {
	IntVector iv = new IntVector(n);
	Arrays.fill(iv.data, d);
	return iv;
    }

    /* multiply(DoubleMatrix) method removed; implement multiply(IntMatrix)?? */

    /* outerProduct(DoubleVector) method removed; implement outerProduct(IntVector) returning IntMatrix?? */

    public double mean() {
	return ((double) sum()) / length();
    }

    /* weightedMean(DoubleVector) method removed. */

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

	int sum = 0;
	int count = 0;

	for (int i = 0; i < data.length; ++i) {
	    if ((from <= data[i]) && (data[i] <= to)) {
		sum += data[i];
		++count;
	    }
	}

	return ((double) sum) / count;
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
	for (int i = 0; i < data.length; ++i) {
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
    public double covariance(IntVector iv) {
	assert (lengthsEqual(this, iv)): "Vector lengths not equal";

	/*
	 * This formula is given in R.A. Fisher, "Statistical Methods
	 * for Research Workers", p. 132 (1958).
	 */
	return (this.dotProduct(iv) - length * this.mean() * iv.mean()) / (length - 1);
    }

    /**
     * Computes the Pearson correlation coefficient r between this
     * vector and the given vector.
     */
    public double correlation(IntVector iv) {
	assert (lengthsEqual(this, iv)): "Vector lengths not equal";

	int n = this.length;
	int s_x = this.sum();
	int s_y = iv.sum();
	int d_xy = n * this.dotProduct(iv) - s_x * s_y;
	int d_x = n * this.dotProduct(this) - s_x * s_x;
	int d_y = n * iv.dotProduct(iv) - s_y * s_y;

	return d_xy / Math.sqrt(d_x * d_y);
    }

    /**
     * Returns a new vector whose ith element is the ith element of
     * this vector minus the mean of this vector.
     */
    public DoubleVector error() {
	return (new DoubleVector(this)).subtract(this.mean());
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
    public int max() {
	int max = data[0];
	for (int i = 1; i < length; ++i) {
	    max = Math.max(max, data[i]);
	}
	return max;
    }

    /**
     * Computes the minimum element of this vector.
     */
    public int min() {
	int min = data[0];
	for (int i = 1; i < length; ++i) {
	    min = Math.min(min, data[i]);
	}
	return min;
    }

    /**
     * Computes the index of the maximum element in this vector.
     */
    public int argmax() {
	int max = data[0];
	int argmax = 0;
	for (int i = 1; i < length; ++i) {
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
	int min = data[0];
	int argmin = 0;
	for (int i = 1; i < length; ++i) {
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
    public int range() {
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
	for (int i = 0; i < length; ++i) {
	    accum += Math.log(data[i]);
	}
	return accum / length();
    }

    /**
     * @see <a href="http://mathworld.wolfram.com/HarmonicMean.html">MathWorld</a>
     */
    public double harmonicMean() {
	double accum = 0;
	for (int i = 0; i < length; ++i) {
	    accum += 1.0 / data[i];
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

    /**
     * Returns the ith order statistic (indexed from 0) of this
     * vector.
     */
    public int orderStatistic(int i) {
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
    private int orderStatistic(int i, int start, int end) {
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
	int x = data[start];
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
    protected static boolean lengthsEqual(IntVector iv1, IntVector iv2) {
	return (iv1.length == iv2.length);
    }

    /**
     * Returns true if the given vectors' elements are pairwise
     * equal. Assumes that the lengths of the vectors are equal.
     */
    protected static boolean elementsEqual(IntVector iv1, IntVector iv2) {
	for (int i = 0; i < iv1.length; ++i) {
	    if (iv1.data[i] != iv2.data[i]) {
		return false;
	    }
	}
	return true;
    }

    /**
     * Returns a vector containing k integers chosen uniformly at
     * random with replacement from {0, ..., n-1}.
     */
    public static IntVector sampleWithReplacement(int k, int n) {
	assert (k > 0): "k < 1: " + k;
	assert (n > 1): "n < 2: " + n;

	IntVector r = new IntVector(k);
	for (int i = 0; i < k; ++i) {
	    r.data[i] = (int) (n * Math.random());
	}
	return r;
    }

    /**
     * Returns a vector containing k integers chosen uniformly at
     * random without replacement from {0, ..., n-1}.
     */
    public static IntVector sampleWithoutReplacement(int k, int n) {
	assert (k > 0): "k < 1: " + k;
	assert (n > 1): "n < 2: " + n;
	assert (k <= n): "k > n: " + k + ", " + n;

	/*
	 * If we wanted to generate M samples without replacement, it
	 * would probably be a bad idea to call this method M times,
	 * since this would reallocate and reinitialize the scratch
	 * array M times.
	 *
	 * One improvement would be to keep a static scratch array
	 * around and reinitialize it with [0..n-1] before each
	 * sample. Even better might be to keep around two arrays: one
	 * would be a pristine scratch array containing [0..n-1], and
	 * the other would be the true scratch array; before each
	 * sample we would quickly "rub off" a copy of the pristine
	 * scratch into the true scratch using System.arraycopy,
	 * rather than looping over the true scratch each time to
	 * initialize it.
	 */

	IntVector scratch = sequence(0, n);
	IntVector r = new IntVector(k);

	for (int i = 0; i < k; ++i) {
	    int j = (int) (n * Math.random()); // could use Random.nextInt instead
	    r.data[i] = scratch.data[j];
	    n -= 1;
	    scratch.data[j] = scratch.data[n];
	}

	return r;
    }

    /**
     * Returns a vector containing k integers chosen uniformly at
     * random from {0, ..., n-1}, with or without replacement.
     */
    public static IntVector sample(int k, int n, boolean withReplacement) {
	return (withReplacement ? sampleWithReplacement(k, n) :	sampleWithoutReplacement(k, n));
    }

}
