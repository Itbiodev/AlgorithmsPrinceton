import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class DLListDeque<Item> implements Iterable<Item> {
	
	private Node first;
	private Node last;
    private int N = 0;
    
    private class Node {
    	Item item;
    	Node next;
    	Node prev;
    }
    
	public DLListDeque() {
		first = new Node();
		last  = new Node();
		first.next = last;
		last.prev = first;
	}

	public boolean isEmpty() { return first == null; }

	public int size() { return N; }

	// I'm the head now
	public void addFirst(Item item) {
		if (item == null) 
			throw new IllegalArgumentException();

		Node oldfirst = first;
		Node first = new Node();
		if (isEmpty()) last = first;
		else 		   { first.next = oldfirst; first.prev = null; }
		N++;
		first.item = item;
	}
	// I'm the tail now
	public void addLast(Item item) {

		if (item == null) 
			throw new IllegalArgumentException();

		 Node oldlast = last;
		 Node last = new Node();
		 last.next = null;
		 if (isEmpty()) first = last;
		 else		 	{ oldlast.next = last;  last.prev = oldlast;}
		 last.item = item;
		 N++;
	}

	public Item removeFirst() {

		if (isEmpty()) 
			throw new NoSuchElementException();

		Item item = first.item;
		first = first.next; 
		first.prev = null;
		if(isEmpty()) first = null; // remove?
		N--;
		return item;
	}

	public Item removeLast() {

		if (isEmpty()) 
			throw new NoSuchElementException();

		Item item = last.item;
		last = last.prev;
		last.next = null;
		if(isEmpty()) last = null; // remove?
		N--;
		return item;
	}
	public Iterator<Item> iterator() {
		return new ListIterator();
	}
	public class ListIterator implements Iterator<Item> {

		private Node current = first;

		public boolean hasNext() { return current != null; }
		public Item next() { 
			Item item = current.item;
			current = current.next;
			return item;
		}
		public void remove() {}
	}
	public static void main(String[] args) {
		
		DLListDeque<Character> chars = new DLListDeque<Character>();

		String s = args[0];

		for (int i = 0; i < s.length(); i++)
			chars.addLast(s.charAt(i));
		for (Character i : chars)
			StdOut.print(chars.removeLast());
	}
}

