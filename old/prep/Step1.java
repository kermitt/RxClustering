package prep;

import java.util.LinkedHashMap;

import common.Caller;
import common.Config;
import common.PSVReader;

public class Step1 extends PSVReader {
	private LinkedHashMap<String, String> features = Config.getFeatures();

	@Override
	public void populate(String entry) {
		try {
			String[] pieces = entry.split(",");
			if (pieces.length == 14) {

				int index = 0;
				for (String feature : features.keySet()) {
					String type = features.get(feature);

					if (type.equals(Config.STRING)) {
						route(feature, pieces[index]);
					} else if (type.equals(Config.DATE)) {
						int days_since_epoch = getDaysSinceEpoch_yyyyddmmhhmmss(pieces[8]);
						route(feature, "" + days_since_epoch);
					} else if (type.equals(Config.NUMBER)) {
						int num = roundToNearest10th(pieces[index]);
						route(feature, "" + num);
					} else if (type.equals(Config.BOOLEAN)) {
						route(feature, pieces[index]);
					}
					index++;
				}

			} else {
				Caller.note("[PopulateConcepts_NEW_Step1] Skipping " + entry);
			}
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}

	}

	public void readFile(String path) {
		// 'read_psv' is in the extended PSVReader and it calls the
		// overridden populate() here
		read_psv(1000000, path);
	}

	public Step1() {
	}

	public static void main(String... strings) {
		String fullPath = "C:/sites/healthpath/data/";
		String file = "test_22_24_29.csv";
		String path = fullPath + file;
		// Step1 s1 = new Step1();
		// s1.readFile(path);
	}
}