import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {

        int i = 0;
        RandomizedQueue<String> things = new RandomizedQueue<String>();

        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()){
            if ( i < k) { // Enqueue the first k items.
                things.enqueue(StdIn.readString());
            } else if (!things.isEmpty() && StdRandom.bernoulli(1/(double) (i+1) )){
                things.dequeue();
                things.enqueue(StdIn.readString());
            } else {
                things.enqueue(StdIn.readString());
            }
            i++;
        }
        // TODO: Print each item from the sequence at most once. 
        for (int j = 0; j < k; j++)
            StdOut.println(things.dequeue());
    }
}
