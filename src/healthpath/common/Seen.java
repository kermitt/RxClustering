package healthpath.common;

import java.util.HashSet;
import java.util.Set;

public class Seen {
	public int seen = 1;
	public String riv = "";
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<String> complexity = new HashSet();
	
	public Seen(){}
	public Seen(int observations, String riv) {
		this.seen = observations;
		this.riv = riv;
	}
	// 'complexity' as seen from the Concepts' POV looking _at_ the Persons
	public void maybeAddComplexity(String personId) {
		if ( complexity.add(personId)) { 
			// do nothing : this person's complexity has already been added 
		} else {
			complexity.add(personId); 
		}
	}
}
