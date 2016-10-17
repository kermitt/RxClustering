package step2_people;

import java.util.HashMap;
import java.util.Map;

public class _Person {
	public boolean SKEW_RIV_BASED_ON_TIME = false; // Maybe this is a good idea
	public boolean SKEW_RIV_BASED_VELOCITY = false; // Maybe this is a good idea
	
	public Map < String, _TimeChunk> series = new HashMap <>(); 
 	public int earliest_day = Integer.MAX_VALUE;
	public void findZeroDay(int days_since_epoch ) {
		if ( earliest_day > days_since_epoch ) {
			earliest_day = days_since_epoch;
		}
	}
	public void addRxEntry(int period, String entry ) {
		String when = "" + period	;
		if ( ! series.containsKey(when)) {		
			series.put(when, new _TimeChunk(period, SKEW_RIV_BASED_ON_TIME));
		}
		series.get(when).addEntry(when,entry);
	}
}