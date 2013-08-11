package math;

import java.io.Serializable;
import java.util.Arrays;

/**
 * A vector of booleans.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20050718
 */
public class BooleanVector implements Cloneable, Serializable {

    protected boolean[] data;
    protected int length;

    public BooleanVector(boolean[] data) {
	this.data = data;
	this.length = data.length;
    }

    public BooleanVector(int length, boolean fill) {
	this.data = new boolean[length];
	this.length = length;
	Arrays.fill(this.data, fill);
    }

    public BooleanVector(int length) {
	this(length, false);
    }

    public boolean get(int i) {
	return data[i];
    }

    public void set(int i, boolean b) {
	data[i] = b;
    }

    public void fill(boolean b) {
	Arrays.fill(this.data, b);
    }

    public int length() {
	return length;
    }

    public BooleanVector copy() {
	boolean[] b = new boolean[this.length];
	System.arraycopy(this.data, 0, b, 0, this.length);
	return new BooleanVector(b);
    }

    public Object clone() {
	return copy();
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

    public BooleanVector and(BooleanVector bv) {
	checkLengths(this, bv);

	BooleanVector and = new BooleanVector(this.length);
	for (int i = 0; i < and.length; ++i) {
	    and.data[i] = this.data[i] & bv.data[i];
	}
	return and;
    }

    public BooleanVector or(BooleanVector bv) {
	checkLengths(this, bv);

	BooleanVector or = new BooleanVector(this.length);
	for (int i = 0; i < or.length; ++i) {
	    or.data[i] = this.data[i] | bv.data[i];
	}
	return or;
    }

    public BooleanVector not() {
	BooleanVector not = new BooleanVector(this.length);
	for (int i = 0; i < not.length; ++i) {
	    not.data[i] = !this.data[i];
	}
	return not;
    }

    public BooleanVector subtract(BooleanVector bv) {
	checkLengths(this, bv);

	BooleanVector diff = new BooleanVector(this.length);
	for (int i = 0; i < diff.length; ++i) {
	    diff.data[i] = this.data[i] & (!bv.data[i]);
	}
	return diff;
    }

    public int countTrue() {
	int n = 0;
	for (int i = 0; i < length; ++i) {
	    if (data[i]) {
		++n;
	    }
	}
	return n;
    }

    public boolean some() {
	for (int i = 0; i < length; ++i) {
	    if (data[i]) {
		return true;
	    }
	}
	return false;
    }

    public boolean every() {
	for (int i = 0; i < length; ++i) {
	    if (!data[i]) {
		return false;
	    }
	}
	return true;
    }

    public boolean equals(Object o) {
	return ((o instanceof BooleanVector) && this.equals((BooleanVector) o));
    }

    public boolean equals(BooleanVector bv) {
	return (lengthsEqual(this, bv) && elementsEqual(this, bv));
    }

    public static BooleanVector and(BooleanVector[] bv) {
	assert (bv.length > 0): "bv.length == " + bv.length;

	BooleanVector res = bv[0];
	for (int i = 1; i < bv.length; ++i) {
	    res = res.and(bv[i]);
	}
	return res;
    }

    public static BooleanVector or(BooleanVector[] bv) {
	assert (bv.length > 0): "bv.length == " + bv.length;

	BooleanVector res = bv[0];
	for (int i = 1; i < bv.length; ++i) {
	    res = res.or(bv[i]);
	}
	return res;
    }

    protected static boolean lengthsEqual(BooleanVector bv1, BooleanVector bv2) {
	return (bv1.length == bv2.length);
    }

    protected static boolean elementsEqual(BooleanVector bv1, BooleanVector bv2) {
	for (int i = 0; i < bv1.length; ++i) {
	    if (bv1.data[i] != bv2.data[i]) {
		return false;
	    }
	}
	return true;
    }

    private static void checkLengths(BooleanVector bv1, BooleanVector bv2) {
	assert (bv1.length == bv2.length): "Vectors must be of equal length: " + bv1.length + ", " + bv2.length;
    }

}
