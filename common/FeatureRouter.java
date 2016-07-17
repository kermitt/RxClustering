package common;
import java.util.HashMap;
import java.util.LinkedHashMap; 
import java.util.Map;
/*
 * Question: Not sure what to call this yet but...What does it do? 
 * Answer: It will hold Map-of-Maps-of-Features-and-their-values. The 'values' will be 'seen' objects
 * Further answer: It will be extended by a whatever needs to deal with the Map_Features_Count ( i.e., Step1 and Step2 and Step3 - maybe more ) 
 * 
 * Maybe 'Feature' or 'FeatureRouter' or 'FeatureSeenCount' or... erm.
 */
public class FeatureRouter {
	
	//public Map<String, Map<String, Seen>> router = new HashMap<>();
	public LinkedHashMap <String, Map<String, Seen>> router = new LinkedHashMap();  
	public FeatureRouter() {		
		LinkedHashMap <String, String>lhm = Config.getFeatures();
		for ( String key : lhm.keySet()) {
			router.put(key, new HashMap<String, Seen>());
		}
	}
	
	public void route(String feature, String value) {
		value = value.trim();
		value = value.toLowerCase();
		if ( router.get(feature).containsKey(value)) {
			router.get(feature).get(value).seen++;
		} else {
			router.get(feature).put(value, new Seen());
		}
	}
}