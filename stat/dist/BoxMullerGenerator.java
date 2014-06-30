package stat.dist;

/**
 * Generates random samples from a standard normal distribution using
 * the Box-Muller method.
 *
 * @see <a href="http://mathworld.wolfram.com/Box-MullerTransformation.html">MathWorld</a>
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20041130
 */
public class BoxMullerGenerator {

    private double x1;
    private double x2;
    private double y1 = Double.NaN;
    private double y2 = Double.NaN;

    public double next() {
	if (Double.isNaN(y2)) {
	    generate();
	    double r = y1;
	    y1 = Double.NaN;
	    return r;
	} else {
	    double r = y2;
	    y2 = Double.NaN;
	    return r;
	}
    }

    private void generate() {
	x1 = Math.random();
	x2 = Math.random();
	double c = Math.sqrt(-2 * Math.log(x1));
	double d = 2 * Math.PI * x2;
	y1 = c * Math.cos(d);
	y2 = c * Math.sin(d);
    }

    public static void main(String[] args) {
	int n = Integer.parseInt(args[0]);
	double x;
	BoxMullerGenerator bmg = new BoxMullerGenerator();

	for (int i = 0; i < n; ++i) {
	    x = bmg.next();
	}
    }

}
