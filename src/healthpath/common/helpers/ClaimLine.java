package healthpath.common.helpers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import healthpath.common.Seen;
/**
 * This is a HoHoH
 * 1st H = feature
 * 2nd H = value w/in that feature
 * 3rd H = seen count of that value
 * 
 * Basically, this will keep track of the Concepts inside of a given Person object
 */
public class ClaimLine {
	public LinkedHashMap <String, Map<String, Seen>> thing_count = new LinkedHashMap<>();  
	public int RxCount = 0 ; 	
	public void addCurriedInformation(String feature, String value) {
		RxCount++;
		if ( ! thing_count.containsKey(feature )) {
			Map < String, Seen > map = new HashMap<>(); 
			thing_count.put(feature, map);
		}
		if ( ! thing_count.get(feature).containsKey(value)) {
			Seen seen = new Seen();
			thing_count.get(feature).put(value, seen);
		} else {
			thing_count.get(feature).get(value).seen++;
		}
	}
}