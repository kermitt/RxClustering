package healthpath.merge;

import java.util.Map;

import healthpath.common.Caller;
import healthpath.common.Library;
import healthpath.common.Seen;
import healthpath.common.helpers.Concept;
import healthpath.common.helpers.Person;
import healthpath.common.helpers.SQLBridge;
import healthpath.people.reader.ReadPeople;

public class MergeTest {

	public static void main(String... strings) {
		getPeople();
		getPeopleLookup();
		ReadPeopleMerge_ConceptsTalliedPropery_test();
		getSingleConcept();

	}

	public static void getSingleConcept() {
		String feature = "drug_class_description";
		String payload = "phenothiazines";

		Concept concept = SQLBridge.getConcept(feature, payload);

		boolean isOk = true;

		isOk &= concept.id > 0;
		isOk &= concept.globally_seen > 0;
		isOk &= concept.complexity > 0;
		isOk &= concept.feature != null;
		isOk &= concept.payload != null;
		isOk &= concept.riv != null;

		Caller.log(isOk, "Found the concept? ");
	}
	public static void ReadPeopleMerge_ConceptsTalliedPropery_test() {

		String in_file = Library.RESULTS_HOME + "step0_VETTED_22_24_29.csv";
		ReadPeopleMerge rpm = new ReadPeopleMerge(100, in_file);

		boolean isOk = true;
		// Caller.log(isOk, "'Depth first' people count " +
		// rpm.these_people.size());
		for (String person_id : rpm.these_people.keySet()) {
			Person p = rpm.these_people.get(person_id);
			Caller.log("Complexity " + p.getComplexity() + " for " + person_id);
			isOk &= p.getComplexity() > 0;
			for (String feature : p.claimline.thing_count.keySet()) {
				Map<String, Seen> observations = p.claimline.thing_count.get(feature);
				for (String payload : observations.keySet()) {
					Seen seen = observations.get(payload);
					// Caller.log("\t" + feature + " " + seen.seen + " " +
					// payload );
				}
			}
		}
		Caller.log(isOk, "Depthfirst People/Concept merge test");
	}

	public static void getPeopleLookup() {
		String in_file = Library.RESULTS_HOME + "step0_VETTED_22_24_29.csv";

		ReadPeopleMerge rpm = new ReadPeopleMerge(1, in_file);

		// Map < Integer, String > id_and_personId =
		// SQLBridge.getPeople_ids_for_lookup();
		boolean isOk = rpm.id_and_personId.size() > 0;
		Caller.log(isOk, "Found " + rpm.id_and_personId.size() + " id/person_id dyads ");
	}

	public static void getPeople() {

		String in_file = Library.RESULTS_HOME + "step0_VETTED_22_24_29.csv";
		ReadPeople rp = new ReadPeople(100, in_file);

		int count_people = rp.people.size();
		int count_features = rp.features.size();

		boolean isOk = count_people > 0 && count_features > 0;
		Caller.log(isOk, "count people and features ");
	}
}
