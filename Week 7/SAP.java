import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.Arrays;


public class SAP {

   private final Digraph G;
   private int minlen;
   private BreadthFirstDirectedPaths vPaths;
   private BreadthFirstDirectedPaths wPaths;
   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G) {
      this.G = G;
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
      this.ancestor(v,w);
      return this.minlen;
   }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w) {
      // idea take the two vertices and do BFS in one then on the other and check for intersections,
      // repeat in all the new vertices until theres an intersection
      // when it is a rooted DAG this should always be the case
      // TODO: improve on this
      this.vPaths = new BreadthFirstDirectedPaths(G,v);
      this.wPaths = new BreadthFirstDirectedPaths(G,w);
      int min = Integer.MAX_VALUE;
      int ans = -1;
      this.minlen = -1;
      for (int i = 0; i < G.V(); i++) {
         if (vPaths.hasPathTo(i) && wPaths.hasPathTo(i)) {
            this.minlen = vPaths.distTo(i) + wPaths.distTo(i);
            if (this.minlen < min) { min = this.minlen; ans = i; }
         }
      }
      return ans;
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w) {
      this.ancestor(v,w);
      return this.minlen;
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
      this.minlen = -1;
      int min = Integer.MAX_VALUE;
      int minans = -1;

      for (int i : v) {
         for (int j : w) {
            int ans = ancestor(i,j);
            if (this.minlen < min) { min = this.minlen; minans = ans; }
         }
      }
      this.minlen = min;
      return minans;
   }

   // do unit testing of this class
   public static void main(String[] args) {

      Integer[] A = {13, 23, 24}; 
      Integer[] B = {16, 6, 17};

      Iterable<Integer> v = Arrays.asList(A);
      Iterable<Integer> w = Arrays.asList(B);

      In in = new In(args[0]);
      Digraph G = new Digraph(in);
      SAP sap = new SAP(G);
      // StdOut.println(G);
      //while (!StdIn.isEmpty()) {
         // int v = StdIn.readInt();
         // int w = StdIn.readInt();
         int ancestor = sap.ancestor(v, w);
         int length   = sap.length(v, w);
         StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
      //}
   }
}