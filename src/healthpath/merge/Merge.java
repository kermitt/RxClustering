package healthpath.merge;

import java.util.HashMap;
import java.util.Map;

import healthpath.common.Caller;
import healthpath.common.Library;
import healthpath.common.Seen;
import healthpath.common.helpers.Concept;
import healthpath.common.helpers.Person;
import healthpath.common.helpers.SQLBridge;

public class Merge {
	public Map<String, Person> people;
	public Map<String, Map<String,Concept>> lookup = new HashMap<>();
	
	private double[] getRivFromConcept(String feature, String payload ) {  
		if ( ! lookup.containsKey(feature)) {
			Map < String, Concept > payloads = new HashMap<>();
			lookup.put(feature, payloads);
		} 
		if ( ! lookup.get(feature).containsKey(payload)) { 
			Concept concept = SQLBridge.getConcept(feature, payload);	
			lookup.get(feature).put(payload, concept);
		}
		return lookup.get(feature).get(payload).riv;
	}
	
	
	
	public void addRiv_toPeople() { 
		int updating = 0; 
		for ( String person_id : people.keySet() ) { 
			Person p = people.get(person_id);
			for ( String feature : p.claimline.thing_count.keySet()) { 
				for ( String payload : p.claimline.thing_count.get(feature).keySet()) {
					Seen seen = p.claimline.thing_count.get(feature).get(payload); 
					
					double[] riv = getRivFromConcept(feature, payload); 
					String riv_as_string = Library._join(riv);
				//	Caller.log( person_id + "   " + seen.seen + "   f " + feature + " payload: " + payload + "  RIV " + riv_as_string  );
					p.donateRiv(seen.seen, riv);
				}
			}
			
			SQLBridge.addRIVtoPerson(person_id, p.riv);

			if ( updating % 5000 == 0 ) { 
				Caller.log("Passing RIV update " + updating );
			}
			updating++; 
		}
		Caller.log("Finished updating (" + updating + ")");
	}
	
	public void depthFirst_getRIV_assignComplexity(int depth_limit, String in_file) {
		ReadPeopleMerge rpm = new ReadPeopleMerge(depth_limit, in_file);
		people = rpm.these_people;
	}
}
