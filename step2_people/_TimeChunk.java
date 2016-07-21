package step2_people;

import common.Caller;
import common.Config;
import common.PSVReader;

public class _TimeChunk extends PSVReader {
	
	public int days_supply_count = 0;
	public int patient_paid_amount = 0;
	public int ingredient_cost_paid_amount = 0;
	public int velocity = 0;
	
	/// For TDD instrumentation
	int ccs_category_id_22 = 0;
	int ccs_category_id_24 = 0;
	int ccs_category_id_29 = 0;
	int ccs_category_id_other = 0;
	int male = 0;
	int female = 0;
	int sex_other = 0;

	// END for TDD instrumentation
	
	private boolean maybeSkewBasedOnTime = false;
	private int when = 0; 
	
	public double[] riv = new double[50];
	public _TimeChunk(int when, boolean maybeSkewBasedOnTime) {
		this.when = when; 
		this.maybeSkewBasedOnTime = maybeSkewBasedOnTime;
	//	router.remove("person_id");
	//	//router.remove("ndc_code");
	//	router.remove("days_supply_count");
	//	router.remove("filled_date");
	//	router.remove("patient_paid_amount");
	//	router.remove("ingredient_cost_paid_amount");
	}
	public String toPSV() {

		int dsc = days_supply_count / velocity;
		int ppa = patient_paid_amount / velocity;
		int icpa = ingredient_cost_paid_amount / velocity;
		
		if ( maybeSkewBasedOnTime ) {
			// THIS MIGHT BE A GOOD IDEA. MAYBE.
			// The more 'deep' into 'when' then the more this
			// timechunk's RIV will be skewed. 
//			for ( int i = 0; i < when; i++ ) {
//				riv[i] += 1;
//			}
			int limit = 50;
			if ( velocity < limit ) {
				limit = velocity;
			}
			for ( int i = 0 ; i < limit; i++ ) {
				riv[i] += 1;				
			}
		}
		
		String riv_as_string = Config._join(riv);
		String psv = velocity + "|" + dsc + "|" + ppa + "|" + icpa + "|" + riv_as_string + "|" + male + "|" + female + "|" + sex_other + "|" + ccs_category_id_22 + "|" + ccs_category_id_24 + "|" + ccs_category_id_29 + "|" + ccs_category_id_other;		
		return psv;
	}
	
	public void addEntry(String when, String entry) {
		try {
			// route via parent's parent ( i.e., CommonPSVFormat.java )
			String[] pieces = entry.split(",");
			if (pieces.length == 14) {
				
					velocity++;

					
					
					// Add 1 to each of the Seen.seen for these 
					route("gender_code", pieces[1]);
					route("ccs_category_id", pieces[2]);
					route("ndc_code", pieces[3]);
					route("drug_label_name", pieces[4]);
					route("drug_group_description", pieces[5]);
route("drug_class_description", pieces[6]);

					////////// Start instrumentation
					if ( pieces[1].equals("M")) {
						male += 1;
					} else if ( pieces[1].equals("F")) {
						female += 1;
					} else {
						sex_other++;
					}

					if ( pieces[2].equals("24")) {
						ccs_category_id_24++; 
					} else if ( pieces[2].equals("29")) { 
						ccs_category_id_29++;
					} else if ( pieces[2].equals("22")) {
						ccs_category_id_22++;
					} else {
						ccs_category_id_other++;// = Integer.parseInt(pieces[2]);
					}
					////////// end instrumentation
					
					
					
					double dsc = Double.parseDouble(pieces[7]);
					String filled_date = pieces[8];
					double ppa = Double.parseDouble(pieces[9]);
					double icpa = Double.parseDouble(pieces[10]);
					String age_filled = pieces[11];
					String after_cure = pieces[12];
					String during_treatment = pieces[13];
	
					
					days_supply_count += roundToNearest10th(pieces[7]);
					patient_paid_amount += roundToNearest10th(pieces[9]);
					ingredient_cost_paid_amount += roundToNearest10th(pieces[10]);
//route("age_filled", pieces[12]);

					route("after_cure", pieces[12]);
					route("during_treatment", pieces[13]);
					
					
					
					
			} else {
				Caller.log( pieces.length + "[_TimeChunk] Skipping " + entry);
			}
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}
	
	public void addRIV(double[] riv_from_concept ) {
		for ( int i =0; i < riv.length; i++ ) { 
			riv[i] += riv_from_concept[i];
		}
	}
}