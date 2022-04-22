import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
	private static final int SIZE = 256;
	public static void encode() {
		int[] alphabet = new int[SIZE];
		for (int i = 0; i < SIZE; i++)
			alphabet[i] = i;

		while (!BinaryStdIn.isEmpty()) {

			char c = BinaryStdIn.readChar();

			int index = 0;
			while (alphabet[index] != c) index++;

			int t = alphabet[index];
			for (int i = index; i > 0; i--) 
				alphabet[i] = alphabet[i-1];
			
			alphabet[0] = t;
			BinaryStdOut.write(index,8);
		}
		BinaryStdOut.close();
	}
	public static void decode() {
		int[] alphabet = new int[SIZE];
		for (int i = 0; i < SIZE; i++)
			alphabet[i] = i;

		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar();
			int t = alphabet[c];
			for (int i = c; i > 0; i--)
				alphabet[i] = alphabet[i-1];
			alphabet[0] = t;
			BinaryStdOut.write(t,8);
		}
		BinaryStdOut.close();
	}
	public static void main(String[] args) {	
		if (args[0].equals("-"))
			encode();		
		else if (args[0].equals("+"))
			decode();
	}
}
