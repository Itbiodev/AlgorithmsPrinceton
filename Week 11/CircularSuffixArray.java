
import java.util.Arrays;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private int length;
    private int[] index;
    private class CircularSuffix implements Comparable<CircularSuffix> {
        private String s;
        private int offset;
        public CircularSuffix(String s, int offset) {
            this.s = s;
            this.offset = offset;
        }
        private char charAt(int pos) {
            return s.charAt((offset + pos) % s.length());
        }
        public int compareTo(CircularSuffix other) {
            if (this == other) return 0;
            for (int i = 0; i < length; i++) {
                if (this.charAt(i) > other.charAt(i)) return 1;
                if (this.charAt(i) < other.charAt(i)) return -1;
            }
            return 0;
        }
        public String toString() {
            return s.substring(offset, s.length()) + s.substring(0, offset);
        }
    }
    public CircularSuffixArray(String s) {
        if (s == null) throw new java.lang.NullPointerException();
        // circular suffix array of s
        length = s.length();
        CircularSuffix[] array = new CircularSuffix[length];
        for (int i = 0; i < length; i++) {
            array[i] = new CircularSuffix(s, i);
        }
        Arrays.sort(array);
        index = new int[length];
        for (int i = 0; i < length; i++) {
            index[i] = array[i].offset;
        }
    }
    public int length() {
        return length;
    }
    public int index(int i) {
        if (i < 0 || i >= length()) throw 
            new java.lang.IndexOutOfBoundsException();
        return index[i];
    }
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("XYUDFSF");
        StdOut.println(csa.index(0));
    }
}
