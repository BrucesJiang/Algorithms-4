/***************************************************
 *  Compilation:  javac Interval1D.java
 *  Execution:    java Interval1D
 *  Dependencies: StdOut.java
 *
 *  1-dimensional interval data type
 *
 **************************************************/

package fundamentals.dataAbstraction.api;

import util.api.In;
import util.api.StdOut;

import java.util.Arrays;
import java.util.Comparator;

/**
 *  The {@code Interval1D} class represents a one-dimensional interval.
 *  The interval is <em>closed</em>—it contains both endpoints.
 *  Intervals are immutable: their values cannot be changed after they are created.
 *  The class {@code Interval1D} includes methods for checking whether
 *  an interval contains a point and determining whether two intervals intersect.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/12oop">Section 1.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @auther Bruce Jiang
 */
public class Interval1D {

    /**
     * Compares two intervals by min endpoint
     */
    public static final Comparator<Interval1D> MIN_ENDPOINT_ORDER = new MinEndpointComparator();

    /**
     * Compares two intervals by max endpoint
     */
    public static final Comparator<Interval1D> MAX_ENDPOINT_ORDER = new MaxEndpoingComparator();

    /**
     * Compares two intervals by length
     */
    public static final Comparator<Interval1D> LENGTH_ORDER = new LengthComparator();


    private final double min;
    private final double max;


    /**
     * Initializes a closed interval [min, max]
     * @param min the smaller endpoint
     * @param max the larger endpoint
     * @throws IllegalArgumentException if the min endpoint is greater than the max endpoing
     * @throws IllegalArgumentException if ether {@code min} or {@code max}
     *          is {@code Double.NaN} , {@code Double.POSITIVE_INFINITY} or
     *          {@code Double.NEGATIVE_INFINITY}
     */
    public Interval1D(double min, double max){
        if(Double.isInfinite(min) || Double.isInfinite(max)){
            throw new IllegalArgumentException("Endpoints must be finite");
        }
        if(Double.isNaN(min) || Double.isNaN(max)){
            throw new IllegalArgumentException("Endpoints cannot be NaN");
        }

        //convert -0.0 to +0.0
        if(min == 0.0) min = 0.0;
        if(max == 0.0) max = 0.0;

        if(min <= max){
            this.min = min;
            this.max = max;
        }else throw new IllegalArgumentException("Illegal interval");
    }

    /**
     * Returns the min endpoint of this interval
     *
     * @return the min endpoint of this interval
     */
    public double min(){
        return min;
    }

    /**
     * Returns the max endpoint of this interval
     *
     * @return the max endpoint of this interval
     */
    public double max(){
        return max;
    }

    /**
     *  Returns true if this interval intersects the specified interval
     *
     * @param that the other interval
     * @return {@code true} if this interval intersects the argument interval
     *          {@code false} otherwise
     */
    public boolean intersects(Interval1D that){
        if(this.max < that.min) return false;
        if(that.max < this.min) return false;
        return true;
    }

    /**
     * Returns true if this interval contains this specified value
     *
     * @param x the specified value
     * @return {@code true} if this interval contains this specified value
     *           {@code false} otherwise
     */
    public boolean contains(double x){
        return (this.min < x) && (x < this.max);
    }

    /**
     * Returns the length of this interval
     *
     * @return the length of this interval (max - min)
     */
    public double length(){
        return max - min;
    }

    /**
     * Returns a string representation of this interval
     *
     * @return a string representation of this interval in the form [min, max]
     */
    public String toString(){
        return "[ " + min + ", " + max + "]";
    }


    /**
     * Compares this interval to the specified interval
     *
     * @param other the specified interval
     * @return {@code true} if this interval equals to the specified interval
     *           {@code false} otherwise
     */
    public boolean equals(Object other){
        if(this == other) return true;
        if(other == null) return false;
        if(this.getClass() != other.getClass()) return false;
        Interval1D that = (Interval1D)other;
        return (this.min == that.min)&&(this.max == that.max);
    }

    /**
     * Returns an integer hash code for this interval
     *
     * @return an integer hash code for this interval
     */
    public int hashCode(){
        int hash1 = ((Double)min).hashCode();
        int hash2 = ((Double)max).hashCode();
        return 31*hash1 + hash2;
    }

    // ascending order of min endpoint, breaking ties by max endpoint
    private static class MinEndpointComparator implements Comparator<Interval1D>{

        @Override
        public int compare(Interval1D o1, Interval1D o2) {
            if       (o1.max < o2.max)  return -1;
            else if  (o1.max > o2.max)  return +1;
            else if  (o1.min > o2.min)  return -1;
            else if  (o1.min < o2.min)  return +1;
            else                         return 0;
        }
    }

    // ascending order of max endpoint, breaking ties by min endpoint
    private static class MaxEndpoingComparator implements Comparator<Interval1D>{

        @Override
        public int compare(Interval1D o1, Interval1D o2) {
            if        (o1.max < o2.max)  return -1;
            else if  (o1.max > o2.max) return +1;
            else if  (o1.min < o1.min) return -1;
            else if  (o1.min > o2.min) return +1;
            else                        return 0;
        }
    }

    // ascending order of length
    private static class LengthComparator implements Comparator<Interval1D>{

        @Override
        public int compare(Interval1D o1, Interval1D o2) {
            double alen = o1.length();
            double blen = o2.length();

            if    (alen > blen)   return +1;
            else if (alen < blen) return -1;
            else                  return 0;
        }
    }

    /**
     * Unit test the {@code Interval1D} data type
     * @param args the command-line arguments
     */
    public static void main(String[] args){
        Interval1D[] intervals = new Interval1D[4];
        intervals[0] = new Interval1D(15.0, 33.0);
        intervals[1] = new Interval1D(45.0, 60.0);
        intervals[2] = new Interval1D(20.0, 70.0);
        intervals[3] = new Interval1D(46.0, 55.0);

        StdOut.println("Unsorted");
        for (int i = 0; i < intervals.length; i++)
            StdOut.println(intervals[i]);
        StdOut.println();

        StdOut.println("Sort by min endpoint");
        Arrays.sort(intervals, Interval1D.MIN_ENDPOINT_ORDER);
        for (int i = 0; i < intervals.length; i++)
            StdOut.println(intervals[i]);
        StdOut.println();

        StdOut.println("Sort by max endpoint");
        Arrays.sort(intervals, Interval1D.MAX_ENDPOINT_ORDER);
        for (int i = 0; i < intervals.length; i++)
            StdOut.println(intervals[i]);
        StdOut.println();

        StdOut.println("Sort by length");
        Arrays.sort(intervals, Interval1D.LENGTH_ORDER);
        for (int i = 0; i < intervals.length; i++)
            StdOut.println(intervals[i]);
        StdOut.println();
    }
}