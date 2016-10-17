package healthpath.merge;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import healthpath.common.Caller;
import healthpath.common.Library;
import healthpath.common.helpers.SQLBridge;
/**
 * This is a one-off: Not super needed.
 * THis will set the is_alive flag to 0 for dead folk in the data
 *
 */
public class MasterDeathList {
	private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	public static void main(String...strings){
		MasterDeathList mdl = new MasterDeathList();
		mdl.readFile();
	}

	public void readFile() { 
		String in_file = Library.HOME + "MasterDeathListVetted.psv";
		read_psv(10000000, in_file);

	}
	
	private void read_psv(Integer limit, String file) {
		Caller.context_note("Reading " + file + " with a limit of " + limit);
		int depth = 0;
		try {
			@SuppressWarnings("resource")
			//person_id|gender_code|date_of_birth|date_of_death

			Scanner inputFile = new Scanner(new File(file));
			String header = inputFile.nextLine(); // The files have a header
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
	
	int depth = 0; 
	private void populate(String entry) {
		try {
			String[] pieces = entry.split("\\|");
			if (pieces.length == 4) {

				String personId = pieces[0];
				String sex = pieces[1]; 
				String dob = pieces[2];
				String dod = pieces[3]; 
				
				long age = getAge(dob,dod);
				
				SQLBridge.updatePerson_as_dead(personId);
				
			} else {
				Caller.note( depth + " [healthpath.merge.MasterDeathList] Skipping " + entry);
			}
			depth++;
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}

	private long getAge(String dob, String dod) {

		dob = dob.replaceAll("\"", "");
		dod = dod.replaceAll("\"", "");

		Date start = getDate(dob);
		Date end = getDate(dod);

		long delta = Math.abs(end.getTime() - start.getTime());
		long days = delta / (24 * 60 * 60 * 1000);
		long years = days /= 365;
		// Caller.log( years + " dob: " + dob + " dod: " + dod );
		return years;
	}
	
	public Date getDate(String s) {
		try {
			return sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			Caller.log("? " + s);
			System.exit(0);
			return null;
		}
	}
}