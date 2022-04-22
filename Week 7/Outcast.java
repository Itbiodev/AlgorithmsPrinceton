import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	private WordNet wn;

	public Outcast(WordNet wordnet){
		this.wn = wordnet;
	}

	public int[] outcast(String[] nouns){
		int max = 0;
		int[] outcasts = new int[nouns.length];
		String outcast = null;
		//for (String s : nouns ) {
		//	int sum = 0;
		//	for (String t: nouns) {
		//		if (s != t)
		//			sum += wn.distance(s,t);
		//	}
		//	
		//	if (sum > max)
		//	{ max = sum; outcast = s; }
		//}
		for (int i = 0; i < nouns.length ; i++) {
			int sum = 0;
			for (String s : nouns) {
				if (s != nouns[i])
					sum += wn.distance(s,nouns[i]);
			}
			outcasts[i] = sum;
		}
		return outcasts;
	}

	public static void main(String[] args){
   	   	WordNet wordnet = new WordNet(args[0], args[1]);
    	Outcast outcast = new Outcast(wordnet);
    	for (int t = 2; t < args.length; t++) {
        	In in = new In(args[t]);
        	String[] nouns = in.readAllStrings();
        	//StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        	for (int i : outcast.outcast(nouns)) {
        		StdOut.println(i);
        	}
    	}	
   }
}