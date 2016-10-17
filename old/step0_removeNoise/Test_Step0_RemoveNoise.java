package step0_removeNoise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import common.Seen;
import common.Caller;

public class Test_Step0_RemoveNoise {
	public static void main(String... strings) {
		Test_Step0_RemoveNoise test = new Test_Step0_RemoveNoise();
		test.test_noise_removal();
	}

	private Step0_RemoveNoise sieve = new Step0_RemoveNoise();
	
	private void test_noise_removal() {
		String header = "person_id,gender_code,ccs_category_id,ndc_code,drug_label_name,drug_group_description,drug_class_description,days_supply_count,filled_date,patient_paid_amount,ingredient_cost_paid_amount,age_filled,after_cure,during_treatment";

		String[] given = new String[] {
				// should have ONE left
				"1,M,29,999,label_z,group_a,Z, 30,\"2012-03-08 00:00:00\",  5, 3,65,True,False",
				"1,M,29,999,label_z,group_a,Z,-30,\"2012-03-08 00:00:00\", -5,-3,65,True,False",
				"1,M,29,999,label_z,group_a,Z, 30,\"2012-03-08 00:00:00\",  5, 3,65,True,False",

				// should have TWO left
				"1,M,29,999,label_z,group_a,Z, 30,\"2012-03-08 00:00:00\",  5, 4,65,True,False",
				"1,M,29,999,label_z,group_a,Z,-30,\"2012-03-08 00:00:00\", -5,-4,65,True,False",
				"1,M,29,999,label_z,group_a,Z, 30,\"2012-03-08 00:00:00\",  5, 4,65,True,False",
				"1,M,29,999,label_z,group_a,Z, 30,\"2012-03-08 00:00:00\",  5, 4,65,True,False",

				// should have THREE left
				"2,M,29,555,label_v,group_h,D,1,\"2012-03-13 00:00:00\",0,10.63,73,True,False",
				"2,M,29,555,label_v,group_h,D,1,\"2012-03-13 00:00:00\",0,10.63,73,True,False",
				"2,M,29,555,label_v,group_h,D,1,\"2012-03-13 00:00:00\",0,10.63,73,True,False",

				// should be removed because this PID is a single lonely one
				"3,M,29,444,label_w,group_a,F,-67,\"2012-02-03 00:00:00\",-13.74,-13.73,76,True,False",
				// should be removed because this PID is a single lonely one
				"4,M,29,333,label_r,group_b,G,30,\"2012-12-03 00:00:00\",2.87,1.42,58,True,False",
				// should be removed because this PID is a single lonely one
				"5,M,29,222,label_s,group_c,H,90,\"2012-03-01 00:00:00\",3.55,3.54,81,True,False",
		};
		for ( String entry : given ) {
			sieve.populate(entry);
		}
		
		///////////////// THIS IS IMPORTANT PART OF THIS UNITTEST
		sieve.winnow(); 
		///////////////// 
		
		
		Map < String, Seen > actual = new HashMap <> () ; 		
		Caller.log("BEFORE=" + given.length + "\tAFTER=" + sieve.people.size());
		for ( String key : sieve.people.keySet()) {
			List<_Entry> _entries = sieve.people.get(key);
			for ( _Entry _entry : _entries ) {
				//Caller.note( key + "\t" + _entry.isPositive + "\t" + _entry.composite);
				if ( ! actual.containsKey(_entry.composite)) {
					actual.put(_entry.composite, new Seen());
				} else {
					actual.get(_entry.composite).seen++;
				}
			}
		}
		
		boolean isOk = true; 
		// different composite strings seen the correct number of times?
		isOk &= actual.get("1|M|29|999|label_z|group_a|Z|30.0|15553|5.0|3.0|65.0|True|False|").seen == 1;
		isOk &= actual.get("1|M|29|999|label_z|group_a|Z|30.0|15553|5.0|4.0|65.0|True|False|").seen == 2;
		isOk &= actual.get("2|M|29|555|label_v|group_h|D|1.0|15706|0.0|10.63|73.0|True|False|").seen == 3;
		
		// different number of unique PID in the outer map ( 'people' ) is correct?
		isOk &= sieve.people.size() == 2;
		
		
		
		Caller.log(isOk);
	}
}