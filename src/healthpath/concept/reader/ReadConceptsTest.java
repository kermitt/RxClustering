package healthpath.concept.reader;

import java.util.Map;

import healthpath.common.Caller;
import healthpath.common.Library;
import healthpath.common.Seen;

public class ReadConceptsTest {
	public static void main(String[] args) {
		read_test();
		concepts_creation_test(); 
	}
	private static void read_test() {
		String in_file = Library.RESULTS_HOME + "step0_VETTED_22_24_29.csv";
		ReadConcepts rc = new ReadConcepts(10, in_file);
		int i = 0;
		boolean isOk = true; 
		for ( String feature : rc.router.keySet()) {
			//Caller.log( ++i + "|" + feature);
			Map <String, Seen > map = rc.router.get(feature);
			for (String payload : map.keySet()) {
			//	Caller.log("\t" + map.get(payload).seen + "|" + payload + "|" + map.get(payload).complexity.size() );
				isOk &= map.get(payload).seen > 0; 
				isOk &= map.get(payload).complexity.size() > 0; 
				i++;	
			}
		}
		isOk &= i > 0; 
		Caller.log(isOk);
	}
	
	private static void concepts_creation_test() {
		String in_file = Library.RESULTS_HOME + "step0_VETTED_22_24_29.csv";
		ReadConcepts rr = new ReadConcepts(100, in_file);
		int inserted_count = rr.populateConcepts(); 
		boolean isOk = inserted_count > 0; 
		Caller.log(isOk, " Inserted " + inserted_count );
	}
	
}
