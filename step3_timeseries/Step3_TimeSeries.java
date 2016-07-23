package step3_timeseries;


import common.Caller;
import common.GeneralWriter;
import common.Config;
import common.PSVReader;
import kmeans._ReduxAttractor;
import kmeans.ReduxShiftMeans;

import java.util.*;

public class Step3_TimeSeries extends PSVReader {
	public Map<String, List<String>> time_chunks = new HashMap<>();
	private double[] X_pov, Y_pov, Z_pov;

	public void doClustering() {
		String filepath = Config.RESULTS_HOME + Config.CLUSTER_FILE;
		// String header = "when|seen|X|Y|Z";
		String header = "when|seen|X|Y|Z|velocity|days_supply_count|patient_paid_amount|ingredient_cost_paid_amount|male|female|sex_other|ccs_22|ccs_24|ccs_29|ccs_other";
		GeneralWriter gw = new GeneralWriter(filepath);
		gw.step1_of_2(header);
		GeneralWriter writer = new GeneralWriter(filepath);
		String[] keys = getSortedKeys_forTimeChunks();
		for (String when : keys) {
			ReduxShiftMeans kShiftMeans = new ReduxShiftMeans(shape, maxDepth);
			List<String> entries = time_chunks.get(when);
			int depth_in_this_time_chunk = 0;
			for (String entry : entries) {
				String[] pieces = entry.split(Config.PIPE);
				if (pieces.length == 14) {
					String person_id = pieces[0];
					// int when = Integer.parseInt(pieces[1]);
					int velocity = Integer.parseInt(pieces[2]);
					int days_supply_count = Integer.parseInt(pieces[3]);
					int patient_paid_amount = Integer.parseInt(pieces[4]);
					int ingredient_cost_paid_amount = Integer.parseInt(pieces[5]);
					String riv_as_string = pieces[6];
					// person_id|when|velocity|days_supply_count|patient_paid_amount|ingredient_cost_paid_amount|riv|male|female|sex_other|ccs_category_id_22|ccs_category_id_24|ccs_category_id_29|ccs_cat_other

					int male = Integer.parseInt(pieces[7]);

					// Caller.log("icpa! " + ingredient_cost_paid_amount + "
					// male " + male );

					int female = Integer.parseInt(pieces[8]);
					int sex_other = Integer.parseInt(pieces[9]);
					int ccs_22 = Integer.parseInt(pieces[10]);
					int ccs_24 = Integer.parseInt(pieces[11]);
					int ccs_29 = Integer.parseInt(pieces[12]);
					int ccs_other = Integer.parseInt(pieces[13]);

					double[] riv = Config.getRiv(riv_as_string);

					double XX = Config.vectorCosineSimilarity(X_pov, riv);
					double YY = Config.vectorCosineSimilarity(Y_pov, riv);
					double ZZ = Config.vectorCosineSimilarity(Z_pov, riv);
					kShiftMeans.addPoints_step1(new double[] { XX, YY, ZZ }, velocity, days_supply_count,
							patient_paid_amount, ingredient_cost_paid_amount, male, female, sex_other, ccs_22, ccs_24,
							ccs_29, ccs_other);
					// Caller.log(male +" f " + female + " ccs " + ccs_22 + " "
					// + ccs_24 + " 29 " + ccs_29 + " other "+ ccs_other );
					depth++;
					depth_in_this_time_chunk++;
				}
			}
			kShiftMeans.createAttractors_step2();
			kShiftMeans.recurse_prep_step3();
			kShiftMeans.describe(0);

			kShiftMeans.showBeforeAfterLocation();

			for (Integer key : kShiftMeans.attractors.keySet()) {
				_ReduxAttractor attractor = kShiftMeans.attractors.get(key);
				double count = attractor.count;
				double XX = attractor.vector[0];
				double YY = attractor.vector[1];
				double ZZ = attractor.vector[2];

				double velocity = attractor.velocity /= attractor.count;
				double days_supply_count = attractor.days_supply_count /= attractor.count;
				double patient_paid_amount = attractor.patient_paid_amount /= attractor.count;
				double ingredient_cost_paid_amount = attractor.ingredient_cost_paid_amount;// /=
																							// attractor.count;

				int male = attractor.male;
				int female = attractor.female;
				int sex_other = attractor.sex_other;
				int ccs_22 = attractor.ccs_22;
				int ccs_24 = attractor.ccs_24;
				int ccs_29 = attractor.ccs_29;
				int ccs_other = attractor.ccs_other;

				writer.step2_of_2(when + "|" + (int) count + "|" + XX + "|" + YY + "|" + ZZ + "|" + velocity + "|"
						+ days_supply_count + "|" + patient_paid_amount + "|" + ingredient_cost_paid_amount + "|" + male
						+ "|" + female + "|" + sex_other + "|" + ccs_22 + "|" + ccs_24 + "|" + ccs_29 + "|"
						+ ccs_other);
			}
		}
	}
	
	int shape = 3; // number of dimensions in the observers and the attractors
	int maxDepth = 10; // max number of recurses

	public void setup() {
		X_pov = Config.getRandomRiv();
		Y_pov = Config.getRandomRiv();
		Z_pov = Config.getBlankRiv();
	}

	public static void main(String... strings) {
		long t1 = System.currentTimeMillis();
		Step3_TimeSeries tsc = new Step3_TimeSeries();
		tsc.setup();
		String fullpath = Config.RESULTS_HOME + Config.step2_rollup;
		tsc.read_psv(3000000, fullpath);

		tsc.doClustering();
		Caller.log("The end");
	}

	int depth = 0;

	@Override
	public void populate(String entry) {
		String headers = "person_id|when|velocity|days_supply_count|patient_paid_amount|ingredient_cost_paid_amount|riv";
		try {
			String[] pieces = entry.split(Config.PIPE);
			if (pieces.length == 14) {
				String when = "" + Integer.parseInt(pieces[1]);

				if (!time_chunks.containsKey(when)) {
					List<String> list = new ArrayList<>();
					time_chunks.put(when, list);
				}
				time_chunks.get(when).add(entry);
			} else {
				Caller.note(pieces.length + " [TimeSeriesClustering] Skipping " + entry);
			}
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}

	//////////////////////////////
	public String[] getSortedKeys_forTimeChunks() {
		String[] ary = new String[time_chunks.size()];
		int[] keys = new int[time_chunks.size()];
		int i = 0;
		for (String when : time_chunks.keySet()) {
			keys[i] = Integer.parseInt(when);
			i++;
		}
		Arrays.sort(keys);

		for (int j = 0; j < keys.length; j++) {
			ary[j] = "" + keys[j];
		}
		return ary;
	}

	/*
	 * public void display() {
	 * 
	 * for (String when : time_chunks.keySet()) { List<String> events =
	 * time_chunks.get(when); Caller.note(events.size() + " for " + when +
	 * " from total " + depth ); } }
	 */

}