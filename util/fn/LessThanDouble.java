
package util.fn;

/**
 * A function which checks whether an int or a double is less than a
 * double.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20030424
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
