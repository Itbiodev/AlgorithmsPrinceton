import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;
import java.util.List;
import java.util.ArrayList;

public class BaseballElimination {
	private Integer n;
	private int[] w;
	private int[] l;
	private int[] r;
	private int[][] g;
	private List<String> teams;
	private double maxFlow;
	private FordFulkerson F;
	private FlowNetwork G;
	private boolean triviallyEliminated;
	private List<String> altR;

	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {

		In file = new In(filename);
		this.n = file.readInt();
		this.teams = new ArrayList<String>();
		this.w = new int[n];
		this.l = new int[n];
		this.r = new int[n];
		this.g = new int[n][n];

		int i = 0;
		while (!file.isEmpty()) {
			// String line = new String(file.readLine());
			// String[] fields = line.split(" ");
			teams.add(file.readString().trim());
			w[i] = Integer.parseInt(file.readString());
			l[i] = Integer.parseInt(file.readString());
			r[i] = Integer.parseInt(file.readString());
			for (int j = 0; j < this.n; j++)
				g[i][j] = Integer.parseInt(file.readString());	
			i++;
		}
		file.close();
	}                    
	// number of teams
	public int numberOfTeams() {
		return this.n;
	}                       
	// all teams
	public Iterable<String> teams() {
		return this.teams;
	}                     
	// number of wins for given team          
	public int wins(String team) {
		int i = teams.indexOf(team);
		if (i == -1)  throw new IllegalArgumentException();
		return w[i];
	}                   
	// number of losses for given team  
	public int losses(String team) {
		int i = teams.indexOf(team);
		if (i == -1)  throw new IllegalArgumentException();
		return l[i];
	}                
	// number of remaining games for given team   
	public int remaining(String team) {
		int i = teams.indexOf(team);
		if (i == -1)  throw new IllegalArgumentException();
		return r[i];
	}   
	// number of remaining games between team1 and team2             
	public int against(String team1, String team2) {
		int i = teams.indexOf(team1);
		int j = teams.indexOf(team2);

		if (i == -1 || j == -1)  throw new IllegalArgumentException();

		return g[i][j];
	}   
	// is given team eliminated?
	public boolean isEliminated(String team) {

		int x = teams.indexOf(team);
		if (x == -1)  throw new IllegalArgumentException();
		// Trivial Elimination
		for (String i : teams) {
			if (wins(team) + remaining(team) < wins(i)) {
				this.triviallyEliminated = true;
				return true;
			}
		}

		// Maxflow

		int noOfMatches = (this.n-1)*(this.n-2)/2; // no. of matches without team x

		int V = 1 + noOfMatches + (this.n-1) + 1;
		this.G = new FlowNetwork(V);
		int[][] pairs = new int[(this.n-1)*(this.n-2)/2][2]; // pairs that don't include x	
		
		int l = 0;
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				if (i != x && j != x) {
					pairs[l][0] = i; pairs[l][1] = j;				
					l++;
				}
			}
		}		
		// link the source to the game vertex with capacity g[i][j]
		for (int i = 0; i < noOfMatches; i++) {
				G.addEdge(new FlowEdge(0,i+1,(double) g[pairs[i][0]][pairs[i][1]]));
		}
		// link game vertex to team vertex with infinite capacity
		for (int i = 0; i < noOfMatches; i++) {
			for (int j = 0; j < 2; j++)
					G.addEdge(new FlowEdge(i+1, noOfMatches + 1 + pairs[i][j], Double.MAX_VALUE));
		}
		// link team vertex to sink with capacity w_i+r_i-w_x
		for (int i = 0; i < n - 1; i++) {
			G.addEdge(new FlowEdge(noOfMatches + 1 +  i,V-1,(double) w[x]+r[x]-w[i]));
		}
		
		this.F = new FordFulkerson(G,0,V-1);
		this.maxFlow = F.value();
		for (FlowEdge E : G.adj(0)) {
			if (E.capacity()-E.flow() > 0)
				return true;
		}

		return false;
	} 
	// subset R of teams that eliminates given team; null if not eliminated            
	public Iterable<String> certificateOfElimination(String team) {

		if (this.teams.indexOf(team) == -1)  throw new IllegalArgumentException();

		List<String> R = new ArrayList<String>();

		if (this.isEliminated(team)) {
			if (!triviallyEliminated) {
				for (String t : this.teams) {
					if (F.inCut((this.n-1)*(this.n-2)/2 + 1 + this.teams.indexOf(t)))
						R.add(t);
				}
				return R;
			}
			else {
				for (String i : teams) {
					if (wins(team) + remaining(team) < wins(i)) {
						R.add(i);
					}
				}
				return R;
			}
		} 
		else
			return null;
	} 
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}