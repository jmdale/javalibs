package util.fn;

/**
 * A function which checks whether an int or a double is less than a
 * double.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140404
 */
public class LessThanDouble {

    public static class Int extends IntToBoolean {

	/**
	 * The double to compare.
	 */
	private double d;

	public Int(double d) {
	    this.d = d;
	}

	public boolean apply(int x) {
	    return x < d;
	}

    }

    public static class Double extends DoubleToBoolean {

	/**
	 * The double to compare.
	 */
	private double d;

	public Double(double d) {
	    this.d = d;
	}

	public boolean apply(double x) {
	    return x < d;
	}

    }

}
