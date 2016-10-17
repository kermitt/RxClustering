package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import common.Caller;

public class PSVReader extends FeatureRouter {
	
	public PSVReader() { 
	}
	
	public void populate(String x) {
	} // this exists to be overridden

	public void read_psv(Integer limit, String file) {
		if (limit == null) {
			limit = 2000000;
		}
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
						Caller.context_note("\nExiting now - Depth based stop. Depth: " + depth + " and Limit: " + limit
								+ " ( This is not an exception ) ");
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

	///////////////////////// HELPER METHODS FOLLOW

	///////// Little tool to clean up the data somewhat
	///////// I.e, squish some pointless uniqueness out of numbers
	public int roundToNearest10th(String amount) throws Exception {
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
		//06/03/2016
		Date this_date = getDate_mmddyyyy(mmddyyyy);
		this_date.setDate(1);// reduce pointlessly unique information
		int delta = (int) ((this_date.getTime() - epoch.getTime()) / 86400000);
		return delta;
	}

	@SuppressWarnings("deprecation")
	public int getDaysSinceEpoch_yyyyddmmhhmmss(String mmddyyyyhhmmss) {
		//"2016-03-06 00:00:00"
		
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