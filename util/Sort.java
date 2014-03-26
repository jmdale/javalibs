package util;

/**
 * Sorting algorithms.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140325
 */
public class Sort {

    /**
     * Sorts an array of doubles in place using insertion sort. From
     * CLR, p. 3.
     */
    public static void insertionSort(double[] a) {
	if (a.length < 2) {
	    return;
	}

	for (int j = 1; j < a.length; j += 1) {
	    double key = a[j];
	    int i = j - 1;

	    while ((i >= 0) && (a[i] > key)) {
		a[i+1] = a[i];
		i -= 1;
	    }

	    a[i+1] = key;
	}
    }

    /**
     * Sorts an array of doubles in place using selection sort.
     */
    public static void selectionSort(double[] a) {
	for (int i = 0; i < a.length; i += 1) {
	    swap(a, i, argmin(a, i));
	}
    }

    /**
     * Exchanges the ith and jth elements of a.
     */
    private static void swap(double[] a, int i, int j) {
	if (i != j) {
	    double temp = a[i];
	    a[i] = a[j];
	    a[j] = temp;
	}
    }

    /**
     * Returns the index of the smallest element of a, starting from
     * index i.
     */
    private static int argmin(double[] a, int i) {
	double min = Double.POSITIVE_INFINITY;
	int argmin = -1;

	for (int j = i; j < a.length; j += 1) {
	    if (a[j] < min) {
		min = a[j];
		argmin = j;
	    }
	}

	return argmin;
    }

    public static void mergeSort(double[] a) {
	mergeSort(a, 0, a.length - 1);
    }

    /**
     * Sorts an array of doubles in place using merge sort. From CLR,
     * p. 13.
     */
    public static void mergeSort(double[] a, int p, int r) {
	if (p < r) {
	    int q = (p + r) / 2;
	    mergeSort(a, p, q);
	    mergeSort(a, q + 1, r);
	    merge(a, p, q + 1, r);
	}
    }

    private static void merge(double[] a, int p, int q, int r) {

    }

    public static void main(String[] args) {
	double[] a = sample(Integer.parseInt(args[0]));
	println(a);
	insertionSort(a);
	println(a);
	selectionSort(a);
	println(a);
    }

    /**
     * Returns an array of n random numbers in [0, 1).
     */
    private static double[] sample(int n) {
	double[] a = new double[n];
	for (int i = 0; i < n; i += 1) {
	    a[i] = Math.random();
	}
	return a;
    }

    private static void println(double[] a) {
	for (int i = 0; i < a.length; i += 1) {
	    System.out.print(a[i] + "\t");
	}
	System.out.println();
    }

}
