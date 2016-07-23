package step2_people;

import common.Caller;
import common.Config;
import common.GeneralWriter;
import common.PSVReader;
import common.Seen;

import java.util.*;

public class Step2_people extends PSVReader {
	static String IN_VETTED_RAW_FILE = Config.RESULTS_HOME + Config.step0_prepped;
	static String OUT_FILE = Config.RESULTS_HOME + Config.step2_rollup;
	static String IN_VETTED_CONCEPT_FILE = Config.RESULTS_HOME + Config.step1_concepts;

	public _ReadConcept conceptSpace;
	public Map<String, _Person> people = new HashMap<>();
	public List<String> entries = new ArrayList<>();
	private LinkedHashMap<String, String> features = Config.getFeatures();
	private int date_index; // CURRENTLY '8' but might change in future

	public Step2_people() {
		int index = 0;
		for (String key : features.keySet()) {
			if (key.equals("filled_date")) {
				date_index = index;
			}
			index++;
		}
	}

	// untested
	private void marshal(String OUT_FILE) {
		// String header =
		// "person_id|when|velocity|days_supply_count|patient_paid_amount|ingredient_cost_paid_amount|riv";
		String header = "person_id|when|velocity|days_supply_count|patient_paid_amount|ingredient_cost_paid_amount|riv|male|female|sex_other|ccs_category_id_22|ccs_category_id_24|ccs_category_id_29|ccs_cat_other";

		// String filepath = Config.fullPath + Config.step2_rollup;
		GeneralWriter gw = new GeneralWriter(OUT_FILE);
		gw.step1_of_2(header);
		int depth = 0;
		for (String person_id : people.keySet()) {
			_Person person = people.get(person_id);
			for (String when : person.series.keySet()) {
				String row = person_id + "|" + when + "|" + person.series.get(when).toPSV();
				gw.step2_of_2(row);
				depth++;
			}
		}
		Caller.context_note("Wrote " + depth + " lines");
	}

	public void merge() {
		for (String person_id : people.keySet()) {
			_Person person = people.get(person_id);

			for (String when : person.series.keySet()) {
				_TimeChunk tc = person.series.get(when);
				for (String feature : tc.router.keySet()) {
					if (!feature.equals("person_id") && !feature.equals("ndc_code")) {
						Map<String, Seen> payloads = tc.router.get(feature);
						for (String payload : payloads.keySet()) {
							try {
								Seen concept = conceptSpace.router.get(feature).get(payload);
								double[] riv = Config.getRiv(concept.riv);
								tc.addRIV(riv);
							} catch (java.lang.NullPointerException npe) {
								Caller.log(npe.getMessage() + "   " + feature + "|" + payload + "|" + npe.getMessage());
							}
						}
					}
				}
			}
		}
	}

	public void readConceptSpace(String file_that_has_the_concept_spaces) {
		conceptSpace = new _ReadConcept();
		conceptSpace.read_psv(Config.READ_ALL_THE_FILE, file_that_has_the_concept_spaces);
	}

	public void readPeopleData(String IN_FILE) {
		read_psv(Config.READ_ALL_THE_FILE, IN_FILE);
	}

	@Override
	public void populate(String entry) {
		/*
		 * Prep the global entry so we can next call populateTimeSeries()
		 */
		try {
			String[] pieces = entry.split(",");

			if (pieces.length == features.size()) {
				entries.add(entry);
				String person_id = pieces[0];
				int days_since_epoch = getDaysSinceEpoch_yyyyddmmhhmmss(pieces[8]);

				if (!people.containsKey(person_id)) {
					_Person person = new _Person();
					people.put(person_id, person);
				}
				people.get(person_id).findZeroDay(days_since_epoch);
			} else {
				Caller.note("[step2_people.Step2_people.java] Skipping " + entry);
			}

		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}

	public void populateTimeSeries() {
		for (String entry : entries) {
			String[] pieces = entry.split(",");
			try {
				if (pieces.length == 14) {
					String person_id = pieces[0];
					int days_since_epoch = getDaysSinceEpoch_yyyyddmmhhmmss(pieces[date_index]);
					_Person person = people.get(person_id);
					days_since_epoch -= person.earliest_day;
					int period = days_since_epoch / Config.TIME_PERIOD;

					person.addRxEntry(period, entry);
				} else {
					Caller.note("[PersonRollup_Step2 populateTimeSeries] Skipping " + entry);
				}
			} catch (Exception e) {
				Caller.log("! FAILURE ON " + entry);
				e.printStackTrace();
			}
		}
	}

	public static void main(String... strings) {
		long t1 = System.currentTimeMillis();

		Step2_people pc = new Step2_people();
		// read in the hyper cube
		pc.readConceptSpace(IN_VETTED_CONCEPT_FILE);
		// read in the vetted raw file
		pc.readPeopleData(IN_VETTED_RAW_FILE);
		// A & B will take a little while...
		// A: Time series! <- Important/complicated
		pc.populateTimeSeries();
		// B: Time series! <- Important/complicated
		pc.merge();
		// write the results
		pc.marshal(OUT_FILE);

		long t2 = System.currentTimeMillis();
		Caller.context_note("The end in " + (t2 - t1) + " milsec ");

	}

}