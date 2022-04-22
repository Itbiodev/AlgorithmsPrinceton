import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.List;
import java.util.ArrayList;
import edu.princeton.cs.algs4.SET;

public class BoggleSolver {
    private TrieST<Integer> dict;
    private int n,m;
    private final int SIZE = 26;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.) 
    public BoggleSolver(String[] dictionary) {

        this.dict = new TrieST<Integer>();
        Integer val = 0;
        for (String s : dictionary) {
            dict.put(s.replace("QU","Q"),val);
            val++;
        }
	}
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        this.m = board.rows(); this.n = board.cols();
        char[][] onBoard = new char[m][n];
        for (int i = 0; i < this.m; i++)
            for (int j = 0; j < this.n; j++) 
                onBoard[i][j] = board.getLetter(i,j);

        SET<String> validWords = new SET<String>();
        boolean[][] visited = new boolean[m][n];
        String str = "";
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n ; j++) {
                if (this.dict.root.next[onBoard[i][j]-65] != null) {
                    str = str + onBoard[i][j];
                    searchWord(this.dict.root.next[onBoard[i][j]-65], onBoard, i, j, validWords, visited, str);
                    str = "";
                }
            }
        }
        return validWords;
	}
    private void searchWord(Node node, char[][] board,int i, int j, SET<String> valid, boolean[][] visited, String str) {
        // if entire word is in the trie we add it
        if (node.val != null && str.length() > 2)
            valid.add(str.replace("Q","QU"));
        // try to traverse the board
        if (i >= 0 && i < this.m && j >= 0 && j < this.n && !visited[i][j]) {
            visited[i][j] = true;
            for (int k = 0; k < SIZE; k++) {
                // check if the next character is on the trie
                if (node.next[k] != null) {
                    char nextChar = (char)(k+'A');
                    if ( i > 0 && !visited[i-1][j] && board[i-1][j] == nextChar) // up
                        searchWord(node.next[k], board, i-1,j,valid,visited,str+nextChar);
                    if ( i < this.m - 1 && !visited[i+1][j] && board[i+1][j] == nextChar) // down
                        searchWord(node.next[k], board, i+1,j,valid,visited,str+nextChar);
                    if (j > 0 && !visited[i][j-1] && board[i][j-1] == nextChar) // left
                        searchWord(node.next[k], board, i,j-1,valid,visited,str+nextChar);
                    if (j < this.n - 1&& !visited[i][j+1] && board[i][j+1] == nextChar) // right
                        searchWord(node.next[k], board, i,j+1,valid,visited,str+nextChar);
                    if (i > 0 && j < this.n - 1 && !visited[i-1][j+1] && board[i-1][j+1] == nextChar) // 1.5
                        searchWord(node.next[k],board, i-1,j+1,valid,visited,str+nextChar);
                    if (i < this.m - 1 && j < this.n - 1 && !visited[i+1][j+1] && board[i+1][j+1] == nextChar) // 4.5
                        searchWord(node.next[k], board,i+1,j+1,valid,visited,str+nextChar);
                    if (i < this.m - 1 && j > 0 && !visited[i+1][j-1] && board[i+1][j-1] == nextChar) // 7.5
                        searchWord(node.next[k], board, i+1,j-1,valid,visited,str+nextChar);
                    if (i > 0 && j > 0 && !visited[i-1][j-1] && board[i-1][j-1] == nextChar) // 10.5
                        searchWord(node.next[k], board, i-1,j-1,valid,visited,str+nextChar);
                }
            }
            visited[i][j] = false;
        }
    }
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.) 
    public int scoreOf(String word) {
        int[] table = {1,1,2,3,5,11};
        int score = word.length() - 3;
        if (!this.dict.contains(word.replace("QU","Q")) || score < 0) return 0;
        if (score > 5) return table[5];
        return table[score];
	}
    private class Node {
        private final int SIZE = 26;
        public Object val;
        public Node[] next = new Node[this.SIZE];
    }
    private class TrieST<Value> {
        private final int SIZE = 26;
        public Node root;
        private int n; //no. of keys

        public TrieST(){}

        public Value get(String key) {
            if (key == null) throw new IllegalArgumentException();
            Node x = get(root,key,0);
            if (x == null) return null;
            return (Value) x.val;
        }
        public boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException();
            return get(key) != null;
        }
        private Node get(Node x, String key,int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            int c = (int)key.charAt(d)-65;
            return get(x.next[c], key, d+1);
        }
        public void put(String key, Value val) {
            if (key == null) throw new IllegalArgumentException();
            if (val == null) delete(key);
            else root = put(root,key,val,0);
        }
        private Node put(Node x, String key, Value val, int d) {
            // d is how far from the root we have traveled
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (x.val == null) n++; // this guy wasn't here
                x.val = val; 
                return x;
            }
            int c = (int)key.charAt(d)-65;
            x.next[c] = put(x.next[c], key, val, d+1);
            return x;
        }
        public int size() {
            return n;
        }
        public void delete(String key) {
            if (key == null) throw new IllegalArgumentException();
            root = delete(root, key, 0);
        }  
        private Node delete(Node x, String key, int d) {
            if (x == null) throw new IllegalArgumentException();
            if (d == key.length()) {
                if (x.val != null) n--;
                x.val = null;
            }
            else {
                int c = (int)key.charAt(d)-65;
                x.next[c] = delete(x.next[c], key, d+1);
            }

            if (x.val != null) return x;
            for (int c = 0; c < this.SIZE; c++) 
                if (x.next[c] != null)
                    return x;
            return null;
        }
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}