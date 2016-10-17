package common;

import java.text.DecimalFormat;
import java.util.*;

public class Config {
	////////////////// PATH RELATED ///////////
	public static String HOME = "C:/sites/healthpath/data/";
	public static String RESULTS_HOME = "C:/sites/healthpath/data/results/";
	public static int READ_ALL_THE_FILE = 10000000; // lines of a PSV/CSV to read
	public static int READ_SOME_OF_FILE = 1000; // lines of a PSV/CSV to read
	
	public static String testfile = "test_22_24_29.csv";
	public static String RAW_INPUT_FILE = "pd_raw_pharma_22_24_29.csv"; // raw
	public static String step0_prepped = "step0_VETTED_22_24_29.csv"; // removed dupes
	public static String step1_concepts = "step1_CONCEPTSPACE_22_24_29.psv"; // semantic
																// spaces
	public static String step2_rollup = "step2_ROLLUP_22_24_29.psv"; // people
																		// across
																		// time
	public static String CLUSTER_FILE = "step3_CLUSTER_22_24_29.psv";
	///////////////// TIMESERIES
	public static int TIME_PERIOD = 90;// 3000; // NORMALLY this 90

	public static double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}

	///////////////// CSV vs PSV
	public static String PIPE = "\\|";

	///////////////// DEALING w/ the RIV
	public static String _join(double[] ary) {
		String out = "";
		for (int i = 0; i < ary.length; i++) {
			if (i < ary.length - 1) {
				out += ary[i] + ",";
			} else {
				out += ary[i] + "";
			}
		}
		return out;
	}

	public static double[] getBlankRiv() {
		double[] riv = new double[50];
		for (int i = 0; i < 50; i++) {
			riv[i] = 0;// Double.parseDouble(ary[i]);
		}
		riv[0] = 1;
		return riv;
	}

	public static double[] getRandomRiv() {
		double[] riv = new double[50];
		for (int i = 0; i < 50; i++) {
			if (Math.random() > 0.5) {
				riv[i] = -1;
			} else {
				riv[i] = 1;
			}
		}
		riv[0] = 1;
		return riv;
	}

	public static double[] getRiv(String riv_as_string) {
		String[] ary = riv_as_string.split(",");
		double[] riv = new double[ary.length];
		for (int i = 0; i < ary.length; i++) {
			riv[i] = Double.parseDouble(ary[i]);
		}
		return riv;
	}

	public static double vectorCosineSimilarity(double[] a, double[] b) {
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;

		double[] control = new double[a.length];
		double[] vector = new double[b.length];
		for (int i = 0; i < a.length; i++) {
			control[i] = (double) a[i];
			vector[i] = (double) b[i];
		}

		for (int i = 0; i < control.length; i++) {
			dotProduct += control[i] * vector[i]; // a.b
			magnitude1 += Math.pow(control[i], 2); // (a^2)
			magnitude2 += Math.pow(vector[i], 2); // (b^2)
		}

		magnitude1 = Math.sqrt(magnitude1);// sqrt(a^2)
		magnitude2 = Math.sqrt(magnitude2);// sqrt(b^2)

		if (magnitude1 != 0.0 | magnitude2 != 0.0) {
			cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		} else {
			return 0;
		}
		return cosineSimilarity;
	}

	////////////////// FEATURE RELATED ////////
	public static String STRING = "string";
	public static String MONEY = "money";
	public static String NUMBER = "number";
	public static String DATE = "date";
	public static String BOOLEAN = "boolean";

	public static LinkedHashMap<String, String> getFeatures() {
		/*
		 * return the features and the type of each feature, to be read in from
		 * the originating .csv file
		 */
		LinkedHashMap<String, String> features = new LinkedHashMap<>();
		features.put("person_id", STRING);
		features.put("gender_code", STRING);
		features.put("ccs_category_id", STRING);
		features.put("ndc_code", STRING);
		features.put("drug_label_name", STRING);
		features.put("drug_group_description", STRING);
		features.put("drug_class_description", STRING);
		features.put("days_supply_count", NUMBER);
		features.put("filled_date", DATE);
		features.put("patient_paid_amount", MONEY);
		features.put("ingredient_cost_paid_amount", MONEY);
		features.put("age_filled", NUMBER);
		features.put("after_cure", BOOLEAN);
		features.put("during_treatment", BOOLEAN);

		return features;
	}

	public static String getHeader() {
		LinkedHashMap<String, String> lhm = getFeatures();
		String header = "";
		int index = 0;
		for (String key : lhm.keySet()) {
			header += key;
			if (index < (lhm.size() - 1)) {
				header += "|";
			}
			index++;
		}
		return header;
	}
}
