package step2_people;

import common.Caller;
import common.Config;
import common.PSVReader;
import common.Seen;

public class _ReadConcept extends PSVReader {
/* 
	public static void main(String... strings) {
		long t1 = System.currentTimeMillis();
		_ReadConcept rc = new _ReadConcept();
		String fullpath = Config.fullPath + Config.step1_concepts;
		rc.read_psv(50000, fullpath); // it is currently about 5k 
		long t2 = System.currentTimeMillis();
		Caller.context_note("The end in " + (t2 - t1) + " milsec ");
		rc.test_concepts_loaded(); 
	}

	public void test_concepts_loaded() {

		for (String feature : router.keySet()) {
			java.util.Map<String, Seen> seen = router.get(feature);
			Caller.note(feature + " size " + seen.size());

			for (String payload : seen.keySet()) {
				Seen s = seen.get(payload);
				//Caller.log(payload + " |" + s.riv);
			}
		}
	}
 */
	@Override
	public void populate(String entry) {
		try {
			String[] pieces = entry.split(Config.PIPE);
			if (pieces.length == 4) {
				int observations = Integer.parseInt(pieces[0]);
				String feature = pieces[1];
				String payload = pieces[2];
				String riv = pieces[3];
				Seen seen = new Seen(observations, riv);
				router.get(feature).put(payload, seen);
			} else {
				Caller.note("[step2_people.ReadConcept.java]Skipping " + entry);
			}
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}
	
}