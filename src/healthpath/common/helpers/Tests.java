package healthpath.common.helpers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import healthpath.common.Caller;
import healthpath.common.Library;

public class Tests {
	public static void main(String... strings) {
	//	getFeatures();
	//	insert_concepts();
		update_riv_from_personId();
		SQLBridge.cleanup();
	}

	public static void getFeatures() {
		LinkedHashMap<String, Feature> features = SQLBridge.getFeatures();
		boolean isOk = features.size() > 0;
		Caller.log(isOk, "Found " + features.size());
	}

	public static void insert_concepts() {
		List<Concept> concepts = new ArrayList<>();
		concepts.add(new Concept(-9, 0, 0, "fake1_feature", "fake1_payload", "fake1_riv"));
		concepts.add(new Concept(-8, 0, 1111, "fake2_feature", "fake2_payload", "fake2_riv"));
		concepts.add(new Concept(-7, 0, 26, "fake3_feature", "fake3_payload", "fake3_riv"));
		boolean show_the_sql = false;
		int inserted_count = SQLBridge.insertConcepts(concepts, show_the_sql);
		boolean isOk = inserted_count == concepts.size();
		Caller.log(isOk, "Inserted  " + inserted_count + " concepts and then clean them up");
	}

	public static void update_riv_from_personId() {
		boolean isOk = false;
		String person_id = "981160";
		try {
			double[] riv = Library.getRandomRiv();
			SQLBridge.addRIVtoPerson(person_id, riv);
			isOk = true; 
		} catch (Exception e) {
			Caller.log("Fail! " + e.getMessage());
		}
		Caller.log(isOk ,"updated properly the person_id " + person_id );
	}
}
