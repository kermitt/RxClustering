package statistics;

import java.io.FileNotFoundException;
import java.util.Map;

import healthpath.common.Caller;

public class FindAveragesMain {
	public static void main(String[] args) {
		try {
			FindAverages ave = new FindAverages();
			Map<Integer, Record> records = ave.readResults("RESULTS.psv");

			long t1 = System.currentTimeMillis();

			int records_seen_count = 0; 
			for ( Integer key : records.keySet()) { 
				Caller.log("" +  key );
				
				Record r = records.get(key);
				//Caller.log(r.show()); 
				Record r2 = ave.getPeople(key, r.children);
				//String results = r2.finish();
				String results = r2.finish_for_JSON();
				Caller.note(results);
				
				records_seen_count += r2.donate_count; 
			}
			long t2 = System.currentTimeMillis() - t1;
			Caller.log("Finished fetching " + records_seen_count + " records over " + records.size() + " clusters in " + t2 + " milsec " );
			
			
		} catch (FileNotFoundException ack) {
			ack.printStackTrace();
		}
	}
}
