package stat.dist;

import java.io.Serializable;

/**
 * A probability distribution.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20060214
 */
public abstract class Distribution implements Serializable {

    /**
     * Returns the minimum value that a random variable with this
     * distribution can have.
     */
    public abstract double min();

    /**
     * Returns the maximum value that a random variable with this
     * distribution can have.
     */
    public abstract double max();

    /**
     * Computes the mean of this distribution.
     */
    public abstract double mean();


    /**
     * Computes the variance of this distribution.
     */
    public abstract double variance();

    /**
     * Computes the standard deviation of this distribution, which is
     * the square root of its variance.
     */
    public double standardDeviation() {
	return Math.sqrt(variance());
    }

    /**
     * Computes the coefficient of variation of this distribution,
     * which is its standard deviation divided by its mean.
     */
    public double coefficientOfVariation() {
	return standardDeviation() / mean();
    }

}
