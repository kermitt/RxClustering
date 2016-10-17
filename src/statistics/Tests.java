package statistics;

import java.io.FileNotFoundException;
import java.util.Map;

import healthpath.common.Caller;

public class Tests {
	public static void main(String...strings) {
		try {
			get_ave_test();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
	public static void get_ave_test() throws FileNotFoundException { 
		FindAverages ave = new FindAverages(); 
		Map<Integer, Record> records = ave.readResults("RESULTS.psv");

		//for ( Integer key : records.keySet()) {
		//	Caller.log("key " + key );
		//}
		Integer key = 1;
		Record r = records.get(key);
		//Caller.log(r.show()); 
		
		long t1 = System.currentTimeMillis();
		Record r2 = ave.getPeople(key, r.children);
		long t2 = System.currentTimeMillis() - t1;
		//String results = r2.finish();
		String results = r2.finish_for_JSON();
		Caller.note(results);
		Caller.log("Finished fetching " + r2.donate_count + " records in " + t2 + " milsec " );
	}
}