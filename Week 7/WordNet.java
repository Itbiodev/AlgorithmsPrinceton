import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;
import edu.princeton.cs.algs4.ST;

public class WordNet {

   private int N;
   private ST<String,Integer> nounST;
   private ST<Integer,String> synST;
   private Digraph G;
   private SAP sap;
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms){

      if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

      this.nounST = new ST<String,Integer>();
      this.synST = new ST<Integer,String>();

      // deal with synsets
      In input = new In(synsets);
      int V = 0;
      while (input.hasNextLine()) {
         String[] s = input.readLine().split(",");
         int id = Integer.parseInt(s[0]);
         synST.put(id,s[1]);
         String[] nouns = s[1].split(" ");
         for (String t : nouns) 
            nounST.put(t,id);
         V++;
      }
      input.close();

      this.G = new Digraph(V);

      // deal with hypernyms
      input = new In(hypernyms);
      while (input.hasNextLine()) {

         String[] s = input.readLine().split(",");

         for (int i = 0; i < s.length-1; i++) 
            G.addEdge(Integer.parseInt(s[0]),Integer.parseInt(s[i+1]));
      }
      input.close();

      // check if its a DAG, i.e. has no cycles
      DirectedCycle c = new DirectedCycle(G);

      if (c.hasCycle()) throw new IllegalArgumentException();

      // check if its rooted DAG
      boolean isPRoot = false;
      int nRoots = 0; // no. of possible roots
      for (int i = 0; i < G.V(); i++)
            if (G.outdegree(i) == 0 && G.indegree(i) > 0) // a root has no outgoing connections but needs to have incoming ones
               { isPRoot = true; nRoots++;}
      // TODO: Is it necessary to check that is a common ancestor of every node?
      if (nRoots == 0 || nRoots > 1) // there can only be one root
         throw new IllegalArgumentException();

   }
   // returns all WordNet nouns
   public Iterable<String> nouns(){
      return nounST;
   }
   // is the word a WordNet noun?
   public boolean isNoun(String word){
      return nounST.contains(word);
   }

   // distance between nounA and nounB
   public int distance(String nounA, String nounB){
      if (!nounST.contains(nounA) || !nounST.contains(nounB))
         throw new IllegalArgumentException();
      this.sap = new SAP(G);
      return sap.length(nounST.get(nounA),nounST.get(nounB));
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB){
      if (!nounST.contains(nounA) || !nounST.contains(nounB))
         throw new IllegalArgumentException();

      int x = nounST.get(nounA);
      int y = nounST.get(nounB);
      this.sap = new SAP(G);
      int ancestorid = sap.ancestor(x,y);
      return synST.get(ancestorid);
   }

   private class  DirectedCycle  {
      private boolean[] marked;
      private int[] edgeTo;
      private Stack<Integer> cycle;  // vertices on a cycle (if one exists)    
      private boolean[] onStack;      // vertices on recursive call stack     
      
      public DirectedCycle(Digraph G) {
         onStack = new boolean[G.V()];
         edgeTo  = new int[G.V()];
         marked  = new boolean[G.V()];
         for (int v = 0; v < G.V(); v++)
             if (!marked[v]) dfs(G, v);
      }    

      private void dfs(Digraph G, int v) {
         onStack[v] = true;       
         marked[v] = true;       
         for (int w : G.adj(v))
            if (this.hasCycle()) return;
            else if (!marked[w])
            {  edgeTo[w] = v; dfs(G, w);  }          
            else if (onStack[w]) {
               cycle = new Stack<Integer>();
               for (int x = v; x != w; x = edgeTo[x])
                  cycle.push(x);
               cycle.push(w);
               cycle.push(v);
            } 
         onStack[v] = false;
      }

      public boolean hasCycle() 
      { return cycle != null; }    

      // public Iterable<Integer> cycle()
      // { return cycle; }  
   }

   // do unit testing of this class
   public static void main(String[] args){
      WordNet wn = new WordNet("synsets.txt","hypernyms.txt");
      String word = "dating";
      StdOut.println(wn.isNoun(word));
      word = "geological_dating";
      StdOut.println(wn.isNoun(word));
   }
}