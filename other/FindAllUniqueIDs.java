package other;

import common.Caller;
import common.PSVReader;
import common.Config;
import java.util.*;

import common.GeneralWriter;
/* 
 * FIND all the unique people IDs 
 * 
 */
public class FindAllUniqueIDs extends PSVReader {
	
	private LinkedHashMap<String, String> features = Config.getFeatures();
	private int total_entries = 0;
	private Map < String, String > people = new HashMap <>(); 

	public void marshal(String filepath) {
		Caller.log("WRITING TO: " + filepath + " with ");
		int row_count = 0;
		GeneralWriter gw = new GeneralWriter(filepath);
		gw.step1_of_2(Config.getHeader());
		for (String personId : people.keySet()) {
			gw.step2_of_2(personId);
		}
		Caller.log("Total row count: " + row_count);
	}
	
	@Override
	public void populate(String entry) {
		try {
			String[] pieces = entry.split(Config.PIPE);
			if (pieces.length == features.size()) {
				String pid = pieces[0]; // the person_id
				if (!people.containsKey(pid)) {
					people.put(pid, entry);
				} else {
					// do nothing
				}
				total_entries++;
			} else {
				Caller.log( pieces.length + "   Skipping " + entry );
			}
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}
	
	/////////////////// MAIN /////////////
	public static void main(String... strings) {
		long t1 = System.currentTimeMillis();
		String path = Config.HOME + Config.step2_rollup;
		FindAllUniqueIDs uniques = new FindAllUniqueIDs();
		int limit = 10000000; // this file actually only has 3.2 million entries
		uniques.read_psv(limit, path);
		uniques.marshal(Config.HOME + "uniques_22_24_29.csv");
		long t2 = System.currentTimeMillis();
		Caller.context_note("The end in " + (t2 - t1) + " milsec ");
		Caller.context_note("Total entries: " + uniques.total_entries);
		Caller.context_note("Total uniques: " + uniques.people.size());
	}
}