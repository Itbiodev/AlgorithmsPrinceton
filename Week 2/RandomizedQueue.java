import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private Item[] a;
	private int N;
	private int head;
	private int tail;

	public RandomizedQueue() {
		a = (Item[]) new Object[1];
		head = 0;
		tail = 0;
		N = 0; // size
	}

	private void resize(int max) {
		Item[] temp = (Item[]) new Object[max];
		for (int i = 0; i < N; i++) 
			temp[i] = a[(head+i)%a.length]; //
		a = temp;
		head = 0;
		tail = N;		
	}

	private void swap(Item[] a, int i, int j) {
		Item t = a[i]; a[i] = a[j]; a[j] = t;
	}
	public boolean isEmpty() { return N == 0; }
	public int size() { return N; }

	public void enqueue(Item item) {
		if (item == null)
			throw new IllegalArgumentException();
		if (N == a.length) resize(2*a.length);
		a[tail++] = item;
        if (tail == a.length) tail = 0;         
		N++;
	}
	public Item dequeue() {
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		int r = StdRandom.uniform(head, head+N); // [a,b)
		Item item = a[r];
        a[r] = a[head];
		a[head] = null;
		N--;
        head++;
		if (head == a.length) head = 0;
	    if (N > 0 && N == a.length/4) resize(a.length/2);
	    return item;
	}
	public Item sample() {
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		return a[StdRandom.uniform(head,head+N)];
	}
	public Iterator<Item> iterator() {
        return new RandomIterator();
    }
    private class RandomIterator implements Iterator<Item> {
        private int size;
        private Item[] copy;

        RandomIterator() {
            size = N;
            copy = (Item[]) new Object[size];

            for (int i = 0; i < size; i++)
                copy[i] = a[i];

        }
        public boolean hasNext()  { return size > 0; }
        public void remove()      {                  } 
        public Item next() { 
           int j = StdRandom.uniform(size);
           Item item = copy[j]; // pick a random item from the array
           copy[j] = copy[size-1];
           copy[size-1] = null; // set the last element to null
           size--;
           return item;
        }
    }
	public static void main(String[] args) {

		RandomizedQueue<Character> chars = new RandomizedQueue<Character>();
		String s = args[0];

		for (int i = 0; i < s.length(); i++)
			chars.enqueue(s.charAt(i));
	}
}
