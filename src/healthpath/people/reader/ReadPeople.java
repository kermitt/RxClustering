package healthpath.people.reader;

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
import healthpath.common.helpers.Person;
import healthpath.common.helpers.SQLBridge;

public class ReadPeople {
	public LinkedHashMap<String, Feature> features = SQLBridge.getFeatures();
	public LinkedHashMap<String, Person> people = new LinkedHashMap<>();

	public ReadPeople() {
	}

	
	public ReadPeople(int limit_number_of_lines_to_read, String path_to_psv) {
		read_psv(limit_number_of_lines_to_read, path_to_psv);
	}

	public int populateConcepts() {
		int inserted_count = SQLBridge.insertPersonObjects(people);
		return inserted_count;
	}
	
	public void read_psv(Integer limit, String file) {
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

	int UUID = 0; 
	public void populate(String entry) {
		try {
			String[] pieces = entry.split(",");

			if (pieces.length == features.size()) {

				String person_id = pieces[0];
				String gender_code = pieces[1];
				String ccs_category_id = pieces[2];
				String ndc_code = pieces[3];
				String drug_label_name = pieces[4];
				String drug_group_description = pieces[5];
				String drug_class_description = pieces[6];
				int days_suppy = roundToNearest10th(pieces[7]);
				int filled_date = getDaysSinceEpoch_yyyyddmmhhmmss(pieces[8]);
				int patient_paid = roundToNearest10th(pieces[9]);
				int ingredient_cost = roundToNearest10th(pieces[10]);
				int age_filled = roundToNearest10th(pieces[11]);
				String after_cure = pieces[12];
				String during_treatment = pieces[13];
			
				if ( ! people.containsKey(person_id)) {
					Person p = new Person(UUID,gender_code,ccs_category_id, person_id);
					people.put(person_id, p);
					UUID++;
				} else {
				}
				
				people.get(person_id).addInformation(patient_paid, ingredient_cost, days_suppy);

				/* ADD CURRIED INFORMATION - SUPER IMPORTANT STEP */
				//people.get(person_id).addCurriedInformation(feature, value);
				
				people.get(person_id).curryThese(gender_code,drug_label_name,drug_group_description,drug_class_description);
				
				
			} else {
				Caller.note("[common.people.reader.ReadPeople.java] Skipping " + entry);
			}

		} catch (Exception e) {
			Caller.log( UUID + "   ACK! \n" + entry + " \t" + e.getMessage());
			e.printStackTrace();
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