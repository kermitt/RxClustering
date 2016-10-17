package healthpath.concept.reader;

import healthpath.common.Caller;
import healthpath.common.Library;

public class ReadConceptsMain {
	public static void main(String...strings) { 
		long t1 = System.currentTimeMillis(); 
		String in_file = Library.RESULTS_HOME + "step0_VETTED_22_24_29.csv";
		ReadConcepts rc = new ReadConcepts(10000000, in_file);
		rc.populateConcepts(); 
		long t2 = System.currentTimeMillis() - t1; 
		Caller.log("The end ( milsec " + t2 + ")");
	}
}
