package healthpath.concept.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import healthpath.common.Caller;
import healthpath.common.Library;
import healthpath.common.Seen;
import healthpath.common.helpers.Concept;
import healthpath.common.helpers.Feature;
import healthpath.common.helpers.SQLBridge;

public class ReadConcepts {
	public LinkedHashMap<String, Feature> features = SQLBridge.getFeatures();
	public LinkedHashMap<String, Map<String, Seen>> router = new LinkedHashMap<>();

	public ReadConcepts(int limit_number_of_lines_to_read, String path_to_psv) {
		for (String key : features.keySet()) {
			router.put(key, new HashMap<String, Seen>());
		}
		read_psv(limit_number_of_lines_to_read, path_to_psv);
	}

	public int populateConcepts() {
		List<Concept> concepts = new ArrayList<>();
		int uid = 0;
		for (String feature : router.keySet()) {
			Map<String, Seen> map = router.get(feature);

			if (features.get(feature).treatment.equals("CURRY")) {

				for (String payload : map.keySet()) {

					// Concept (int id, int globally_seen, String feature,
					// String payload, String riv ) {
					uid++;

					Seen seen = router.get(feature).get(payload);
					int complexity = seen.complexity.size();
					double[] riv = Library.getRandomRiv();
					String riv_as_string = Library._join(riv);
					Concept concept = new Concept(uid, seen.seen, complexity, feature, payload, riv_as_string);
					concepts.add(concept);
				}
			} else {
				// else skip this: either it will be something to average ( in
				// the next step ) or it is a no_op
			}
		}
		int inserted_count = SQLBridge.insertConcepts(concepts);
		return inserted_count;
	}

	private void read_psv(Integer limit, String file) {
		Caller.context_note("Reading " + file + " with a limit of " + limit);
		int depth = 0;
		try {
			@SuppressWarnings("resource")
			Scanner inputFile = new Scanner(new File(file));
			String header = inputFile.nextLine(); // The files have a header
			// the humans... ...herefore pop it off and show it.
			Caller.context_note(header);

			while (inputFile.hasNext()) {
				depth++;
				String x = inputFile.nextLine();
				populate(x);

				if (depth % 100000 == 0 && depth > 0) {
					Caller.note("passing " + depth);
				}

				// punt
				if (depth > limit) {
					// QA issue
					if (depth > 90) {
						Caller.log("\t[Test!] Exiting@depth:" + depth);
					}
					break;
				}
			}
		} catch (FileNotFoundException e) {
			Caller.log("FAILBOT! " + e.getMessage());
			e.printStackTrace();
		}
		Caller.note("Total depth " + depth);
	}

	private void route(String feature, String value, String personId) {
		value = value.trim();
		value = value.toLowerCase();
		value = value.replace("'s", "");
		if (router.get(feature).containsKey(value)) {
			router.get(feature).get(value).seen++;
			router.get(feature).get(value).maybeAddComplexity(personId);
		} else {
			Seen seen = new Seen();
			seen.maybeAddComplexity(personId);
			router.get(feature).put(value, seen);
		}
	}

	private void populate(String entry) {
		try {
			String[] pieces = entry.split(",");
			if (pieces.length == features.size()) {

				String personId = pieces[0];

				int index = 0;
				for (String feature : features.keySet()) {
					Feature f = features.get(feature);
					if (f.type.equals("STRING")) {
						route(feature, pieces[index], personId);
					} else if (f.type.equals("DATE")) {
						int days_since_epoch = getDaysSinceEpoch_yyyyddmmhhmmss(pieces[8]);
						route(feature, "" + days_since_epoch, personId);
					} else if (f.type.equals("NUMBER")) {
						int num = roundToNearest10th(pieces[index]);
						route(feature, "" + num, personId);
					} else if (f.type.equals("BOOLEAN")) {
						route(feature, pieces[index], personId);
					}
					index++;
				}
			} else {
				Caller.note("[healthpath.concept.reader.ReadRawData.java] Skipping " + entry);
			}
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}

	private int roundToNearest10th(String amount) throws Exception {
		double d = new Double(amount);
		return round(d, 10);
	}

	private int round(double actual_money_amount, int divisor) {
		return (int) Math.round(actual_money_amount / divisor) * divisor;
	}

	//// Date to days since epoch... part of the 'know the quarter' mechanism
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private Date epoch = new Date(0);

	@SuppressWarnings("deprecation")
	public int getDaysSinceEpoch(String mmddyyyy) {
		// 06/03/2016
		Date this_date = getDate_mmddyyyy(mmddyyyy);
		this_date.setDate(1);// reduce pointlessly unique information
		int delta = (int) ((this_date.getTime() - epoch.getTime()) / 86400000);
		return delta;
	}

	@SuppressWarnings("deprecation")
	public int getDaysSinceEpoch_yyyyddmmhhmmss(String mmddyyyyhhmmss) {
		// "2016-03-06 00:00:00"

		mmddyyyyhhmmss = mmddyyyyhhmmss.replaceAll("\"", "");
		String tmp = mmddyyyyhhmmss.split(" ")[0];
		String[] ary = tmp.split("-");
		String mmddyyyy = ary[2] + "/" + ary[1] + "/" + ary[0];
		Date this_date = getDate_mmddyyyy(mmddyyyy);
		this_date.setDate(1);// reduce pointlessly unique information
		int delta = (int) ((this_date.getTime() - epoch.getTime()) / 86400000);
		return delta;
	}

	private Date getDate_mmddyyyy(String date_mmddyyyy) {
		try {
			return sdf.parse(date_mmddyyyy);
		} catch (ParseException e) {
			Caller.log("Failbot! " + date_mmddyyyy + " recieved " + e.getMessage());
		}
		return null;
	}
}