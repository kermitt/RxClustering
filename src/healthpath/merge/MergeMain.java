package healthpath.merge;

import healthpath.common.Caller;
import healthpath.common.Library;

public class MergeMain {
	public static void main(String...strings) {
		Merge merge = new Merge();
		
		int depth_limit = 100; 
		String in_file = Library.RESULTS_HOME + "step0_VETTED_22_24_29.csv";

		merge.depthFirst_getRIV_assignComplexity(depth_limit, in_file); 		
		merge.addRiv_toPeople();
		Caller.log( "The end");
	}
}