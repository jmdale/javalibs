package stat.dist;

import math.DoubleVector;

/**
 * Functions for computing bandwidths to use in kernel density
 * estimates.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20041211
 */
class Bandwidth {

    /**
     * R's bw.nrd function.
     */
    static double nrd(DoubleVector x) {
	return 1.06 * Math.min(x.standardDeviation(), x.interQuartileRange() / 1.34) / Math.pow(x.length(), 0.2);
    }

}
