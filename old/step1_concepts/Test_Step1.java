package step1_concepts;

import common.Caller;
import common.Seen;
import java.util.*;
public class Test_Step1 {

	public static  void main(String...strings) {
		
		Test_Step1 tests = new Test_Step1(); 
		tests.readTestInputFile_getProperFeatures_and_getProperFeatureCounts();

		
	}
	
	
	public void readTestInputFile_getProperFeatures_and_getProperFeatureCounts() {

		String fullPath = "C:/sites/healthpath/data/";
		String file = "step0_22_24_29_TEST.psv";
		String path = fullPath + file; 
		Step1 s1 = new Step1(); 
		s1.readFile(path); 
		
		// isOk = enough features? AND enough of the  features have at least 
		// one child member with more than 1 'seen'? 
		boolean isOk = true; 
		for ( String feature : s1.router.keySet()) { 
			Map < String, Seen > map = s1.router.get(feature); 
			//Caller.note( " F=" + feature + " count " + map.size());
			boolean anythingSeenMoreThan1Time = false;
			for ( String value : map.keySet()) {
				
				if ( feature.equals("ndc_code")) {
				//	Caller.note( " F=" + feature + " count " + map.size() + " value " + value );
					
				}
				
				
//				Caller.note( ( map.get(value).seen > 1 ) + "\t" + value + "   " + map.get(value).seen);
				if ( map.get(value).seen > 1 ) { 
					anythingSeenMoreThan1Time = true;
				} 
			}
Caller.log(anythingSeenMoreThan1Time + "     feature " + feature + " anythingSeenMoreThan1Time " );
			
			
			
			isOk &= anythingSeenMoreThan1Time;
		}
		Caller.log(isOk);
	}
}