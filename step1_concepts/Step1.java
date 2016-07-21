package step1_concepts;

import java.util.LinkedHashMap;
import java.util.Map;

import common.Caller;
import common.Config;
import common.GeneralWriter;
import common.PSVReader;
import common.Seen;

public class Step1 extends PSVReader {
	private LinkedHashMap<String, String> features = Config.getFeatures();

	@Override
	public void populate(String entry) {
		try {
			String[] pieces = entry.split(",");
			if (pieces.length == features.size()) {

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
					} else if (type.equals(Config.MONEY)) {
						int num = roundToNearest10th(pieces[index]);
						route(feature, "" + num);
					} else if (type.equals(Config.BOOLEAN)) {
						route(feature, pieces[index]);
					}
					index++;
				}

			} else {
				Caller.note("[step1_concepts.Step1.java] Skipping " + entry);
			}
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}

	public void readFile(String path) {
		// 'read_psv' is in the extended PSVReader and it calls the
		// overridden populate() here
		read_psv(10000000, path);
	}

	public void marshal(String filepath ) {
		String header = "seen|feature|payload|riv";

		GeneralWriter gw = new GeneralWriter(filepath);

		gw.step1_of_2(header);
		for (String feature : router.keySet()) {

			String type = features.get(feature);
			// DO NOT Store dates and money and age into the concept space
			if (type.equals(Config.STRING) || type.equals(Config.BOOLEAN)) {
				// DO NOT Store noisey ndc_code or person_id in concept space
				// 1: RE: person_id we have to CURRY those
				// 2: RE: node_code 'cause those are redundent ( label/class/desc ) 
				if (!feature.equals("person_id") && !feature.equals("ndc_code")) {
					Map<String, Seen> map = router.get(feature);
					for (String payload : map.keySet()) {
						Seen seen = map.get(payload);
						String riv = createRIV();
						String row = seen.seen + "|" + feature + "|" + payload + "|" + riv;
						gw.step2_of_2(row);
					}
				}
			}
		}
	}

	private String createRIV() {

		double[] riv = new double[50];
		for (int i = 0; i < 50; i++) {
			double v = 1;
			if (Math.random() > 0.5) {
				v *= -1;
			}
			riv[i] = v;
		}
		riv[0] = 1;
		String result = "";
		for (int i = 0; i < riv.length; i++) {
			if (i < riv.length - 1) {
				result += riv[i] + ",";
			} else {
				result += riv[i];
			}
		}
		return result;
	}

	public static void main(String... strings) {
//		String fullPath = Config.fullPath + "step0_22_24_29_TEST.psv";
		String fullPath = Config.fullPath + Config.step0_prepped; 
		Step1 s1 = new Step1();
		s1.readFile(fullPath);
		s1.marshal(Config.fullPath + Config.step1_concepts);
		Caller.log("THE END");
	}
}