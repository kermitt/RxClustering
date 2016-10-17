package healthpath.merge;

import java.util.HashMap;
import java.util.Map;

import healthpath.common.Caller;
import healthpath.common.helpers.Person;
import healthpath.common.helpers.SQLBridge;
import healthpath.people.reader.ReadPeople;

public class ReadPeopleMerge extends ReadPeople {
	public Map<Integer, String> id_and_personId;
	public Map<String, Person> these_people = new HashMap<>();

	public ReadPeopleMerge(int limit_number_of_lines_to_read, String path_to_psv) {
		// super(limit_number_of_lines_to_read, path_to_psv);
		id_and_personId = SQLBridge.getPeople_ids_for_lookup();
		read_psv(limit_number_of_lines_to_read, path_to_psv);
	}

	private int depth = 0;

	@Override
	public void populate(String entry) {
		entry = entry.toLowerCase();
		entry = entry.replaceAll("'s", "");
		try {
			String[] pieces = entry.split(",");

			if (pieces.length == features.size()) {

				String person_id = pieces[0];
				if (person_id != null && person_id.length() > 0) {
					String gender_code = pieces[1];
					String ndc_code = pieces[3];
					String drug_label_name = pieces[4];
					String drug_group_description = pieces[5];
					String drug_class_description = pieces[6];

					if (these_people.containsKey(person_id)) {
					} else {
						these_people.put(person_id, new Person());
						// Caller.log("adding " + person_id );
					}

					these_people.get(person_id).claimline.addCurriedInformation("gender_code", gender_code);
					these_people.get(person_id).claimline.addCurriedInformation("drug_label_name", drug_label_name);
					these_people.get(person_id).claimline.addCurriedInformation("drug_group_description",
							drug_group_description);
					these_people.get(person_id).claimline.addCurriedInformation("drug_class_description",
							drug_class_description);

					these_people.get(person_id).maybeAddComplexity(ndc_code);

					if (depth % 100000 == 0) {
						Caller.log("Passing " + depth + "...");
					}
					depth++;
				}
			} else {
				Caller.note("[common.people.reader.ReadPeople.java] Skipping " + entry);
			}

		} catch (Exception e) {
			Caller.log(depth + "   ACK! \n" + entry + " \t" + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}
}