package stat;

import math.DoubleVector;

/**
 * An interface representing a statistic: a function which takes a
 * vector of observations (a sample) and returns a double.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20041114
 */
public interface Statistic {

    public double apply(DoubleVector dv);

}
