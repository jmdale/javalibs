package math;

import java.io.Serializable;

/**
 * Basic complex number class.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140518
 */
public class Complex implements Serializable {

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex I = new Complex(0, 1);

    private double real;
    private double imag;

    public Complex(double real, double imag) {
	this.real = real;
	this.imag = imag;
    }

    public static Complex polar(double r, double theta) {
	assert (r >= 0): "r < 0: " + r;

	return new Complex(r * Math.cos(theta), r * Math.sin(theta));
    }

    public String toString() {
	return real + "+" + imag + "i";
    }

    public boolean equals(Complex c) {
	return (this.real == c.real) && (this.imag == c.imag);
    }

    public boolean equals(Object o) {
	return (o instanceof Complex) && this.equals((Complex) o);
    }

    public boolean isNaN() {
	return Double.isNaN(real) || Double.isNaN(imag);
    }

    public boolean isInfinite() {
	return Double.isInfinite(real) || Double.isInfinite(imag);
    }

    public double real() {
	return this.real;
    }

    public double imag() {
	return this.imag;
    }

    public double modulus() {
	return Math.sqrt(this.real * this.real + this.imag * this.imag);
    }

    public double argument() {
	return Math.atan(this.imag / this.real);
    }

    public Complex conjugate() {
	return new Complex(this.real, -this.imag);
    }

    public Complex add(Complex c) {
	return new Complex(this.real + c.real, this.imag + c.imag);
    }

    public Complex subtract(Complex c) {
	return new Complex(this.real - c.real, this.imag - c.imag);
    }

    public Complex multiply(Complex c) {
	return new Complex(this.real * c.real - this.imag * c.imag,
			   this.real * c.imag + this.imag * c.real);
    }

    public Complex divide(Complex c) {
	double d = c.real * c.real + c.imag * c.imag;

	return new Complex((this.real * c.real + this.imag * c.imag) / d,
			   (this.imag * c.real - this.real * c.imag) / d);
    }

    public Complex divide(double d) {
	return new Complex(this.real / d, this.imag / d);
    }

    public Complex square() {
	return new Complex(this.real * this.real - this.imag * this.imag,
			   2 * this.real * this.imag);
    }

    public Complex sqrt() {
	return polar(Math.sqrt(modulus()), argument() / 2);
    }

    public Complex pow(double d) {
	return polar(Math.pow(modulus(), d), d * argument());
    }

    public Complex exp(Complex c) {
	double a = Math.exp(c.real);

	return new Complex(a * Math.cos(c.imag), a * Math.sin(c.imag));
    }

    public Complex log() {
	return new Complex(Math.log(modulus()), argument());
    }

}
