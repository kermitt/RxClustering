package kmeans;

public class ClustererMain {
	public static void main(String...strings) { 
		Clusterer c = new Clusterer(); 
		
		int select_limit = 10000000; //select * from PERSON_RIV limit foo
		int recurse_limit = 20;// K-Shift-Means iterations
		
		c.doClustering(select_limit, recurse_limit );
	}
}
