package mandelbrot;

import java.util.Objects;

/**
 * A class to represent complex numbers and their arithmetic.
 * <p>
 * Complex numbers are immutable.
 */
public class Complex {

    /**
     * The real part of a complex number.
     */
    final double real;

    /**
     * The imaginary part of a complex number.
     */
    final double imaginary;


    /**
     * Creates a complex number given the real and the imaginary components
     *
     * @param real      real component
     * @param imaginary imaginary component
     */
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Zero as a complex number
     */
    static Complex ZERO = new Complex(0, 0);

    /**
     * One as a complex number
     */
    static Complex ONE = new Complex(1, 0);


    /**
     * The complex number whose square is -1
     */
    static Complex I = new Complex(0, 1);

    double getReal() {
        return real;
    }

    double getImaginary() {
        return imaginary;
    }

    /**
     * Creates complex numbers corresponding to rotations
     *
     * @param radians the angle of the rotation (counterclockwise) in radians
     * @return a complex number, whose multiplication corresponds to a rotation by the given angle.
     */
    static Complex rotation(double radians) {
        return new Complex(Math.cos(radians), Math.sin(radians));
    }

    /**
     * Creates a complex number with null imaginary part
     *
     * @param real the real component
     * @return the complex <code>real + 0 i</code>
     */
    public static Complex real(double real) {
        return new Complex(real, 0);
    }

    /**
     * Addition of two complex numbers
     *
     * @param addend a complex
     * @return the complex {@code this + addend}
     */
    public Complex add(Complex addend) {
        return new Complex(this.real + addend.getReal(),
                this.imaginary + addend.getImaginary());
    }

    /**
     * The negation of a complex number
     *
     * @return A complex <code>c</code> such that <code>this + c = 0</code>
     */
    Complex negate() {
        return new Complex(-this.real, -this.imaginary);
    }

    /**
     * The conjugate of a complex number
     *
     * @return A complex <code>c</code> such that <code>this * c = ||this|| ** 2</code>
     */
    Complex conjugate() {
        return new Complex(this.real, -this.imaginary);
    }

    /**
     * Subtraction of two complex numbers
     *
     * @param subtrahend the complex to be subtracted from <code>this</code>
     * @return the complex number <code>this - subtrahend</code>
     */
    Complex subtract(Complex subtrahend) {
        return new Complex(this.real - subtrahend.getReal(), this.imaginary - subtrahend.getImaginary());
    }

    /**
     * Multiplication of two complex numbers
     *
     * @param factor the complex number to multiply to <code>this</code>
     * @return the complex number {@code this * factor}
     */
    Complex multiply(Complex factor) {
        return new Complex(
                this.real * factor.real - this.imaginary * factor.imaginary,
                this.real * factor.imaginary + this.imaginary * factor.real
        );
    }

    /**
     * Squared modulus of a complex number
     *
     * @return <code>||this|| ** 2</code>
     */
    double squaredModulus() {
        return Math.pow(real,2) + Math.pow(imaginary,2);
    }

    /**
     * Modulus (distance to zero) of a complex number
     *
     * @return <code>||this||</code>
     */
    double modulus() {
        return Math.sqrt(squaredModulus());
    }


    /**
     * reciprocal of a complex number
     *
     * @return a complex number <code>c</code> such that <code>this * c = 1</code>
     */
    Complex reciprocal() {
        if (this.equals(ZERO)){
            throw new ArithmeticException("Divide by zero");
        }
        return new Complex(real / (Math.pow(real,2) + Math.pow(imaginary,2)), -imaginary / (Math.pow(real,2) + Math.pow(imaginary,2)));
    }

    /**
     * Division of two complex numbers
     *
     * @param divisor the denominator (a complex number)
     * @return the complex number <code>this / divisor</code>
     */
    Complex divide(Complex divisor) {
        if (divisor.equals(ZERO)){
            throw new ArithmeticException("Divide by zero");
        }
        double factor = 1 / (Math.pow(divisor.getReal(),2) + Math.pow(divisor.getImaginary(),2));
        return new Complex(
                factor * (real * divisor.getReal() + imaginary * divisor.getImaginary()),
                factor * (imaginary * divisor.getReal() - real * divisor.getImaginary())
        );
    }


    /**
     * Integral power of a complex number
     *
     * @param p a non-negative integer
     * @return the complex number <code>this ** p</code>
     */
    Complex pow(int p) {
        if (p == 0)
            return ONE;
        Complex result = this.multiply(this);
        if (p % 2 == 1) {
            result = this.multiply(result.pow((p - 1) / 2));
        } else {
            result = result.pow(p / 2);
        }
        return result;
    }

    /**
     * Scalar multiplication of a complex number
     *
     * @param lambda a scalar number
     * @return the complex number <code>lambda * this</code>
     */
    public Complex scale(double lambda) {
        return new Complex(lambda * real, lambda * imaginary);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Complex complex = (Complex) o;
        return Helpers.doubleCompare(complex.getReal(), real) == 0 &&
                Helpers.doubleCompare(complex.getImaginary(), imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }


    @Override
    public String toString() {
        return "Complex{" +
                "real=" + real +
                ", imaginary=" + imaginary +
                '}';
    }
}
