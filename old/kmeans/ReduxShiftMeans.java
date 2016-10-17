package kmeans;

import common.Caller;
import common.Config;

import java.util.*;

public class ReduxShiftMeans {
	// Note: This collection will grow into the millions
	public Map<Integer, _ReduxPoint> observations = new HashMap<>();
	public Map<Integer, _ReduxAttractor> attractors = new HashMap<>();
	public int maxRecurseDepth;
	public int nextKey = 0;
	public int shape; // how many dimensions the Observations and attractors get
	public int lastEpoch = 0;

	public ReduxShiftMeans(int shape, int maxRecurseDepth) {
		this.shape = shape;
		this.maxRecurseDepth = maxRecurseDepth;
		Caller.log("Shape: " + shape + " Depth: " + maxRecurseDepth);
	}

	// populate w/ observations
	public void addPoints_step1(double[] ary, int velocity, int days_supply_count, int patient_paid_amount,
			int ingredient_cost_paid_amount, int male, int female, int sex_other, int ccs_22, int ccs_24, int ccs_29,
			int ccs_other) {
		
		observations.put(nextKey,
				new _ReduxPoint(ary, velocity, days_supply_count, patient_paid_amount, ingredient_cost_paid_amount,male,female,sex_other,ccs_22, ccs_24,ccs_29, ccs_other));
		nextKey++;
	}

	// 1st time populate the attractors
	public void createAttractors_step2() {
		// make a bunch of Attractors and sprinkle them around in the hypercube
		double count = Math.sqrt(observations.size());
		count *= 2; 
		int result = (int) count;
		if (result < 2) {
			result = 2;
		}
		Caller.log("COUNT: " + count);
		for (int i = 0; i < result; i++) {
			double[] d = new double[shape];
			for (int j = 0; j < shape; j++) {
				d[j] = Math.random() * 4 - 2;
			}
			attractors.put(i, new _ReduxAttractor(i, d));
		}
	}

	// TODO : make the before/after public and write a UnitTest for them
	private Map<Integer, double[]> beforeLoc = new HashMap<>();
	private Map<Integer, double[]> afterLoc = new HashMap<>();

	public void recurse_prep_step3() {

		for (Integer aKey : attractors.keySet()) {
			beforeLoc.put(aKey, attractors.get(aKey).vector);
		}

		_recurse_epoch(0);

		for (Integer aKey : attractors.keySet()) {
			afterLoc.put(aKey, attractors.get(aKey).vector);
		}
	}

	// Not 'in use' per se, but this is nice for TDD purposes
	public void showBeforeAfterLocation() {
		for (Integer key : afterLoc.keySet()) {
			double[] ary = afterLoc.get(key);

			String locationA = "";
			for (int i = 0; i < ary.length; i++) {
				locationA += Config.roundTwoDecimals(ary[i]) + "\t";
			}

			double[] ary2 = beforeLoc.get(key);

			String locationB = "";
			for (int i = 0; i < ary2.length; i++) {
				locationB += Config.roundTwoDecimals(ary2[i]) + "\t";
			}
			Caller.log("    AFTER: " + key + " | " + locationA + "\t|" + attractors.get(key).count);
		}
	}

	private void _recurse_epoch(int depth) {
		if (depth < maxRecurseDepth) {
			depth++;

			for (Integer oKey : observations.keySet()) {
				_ReduxPoint o = observations.get(oKey);
				for (Integer aKey : attractors.keySet()) {
					_ReduxAttractor a = attractors.get(aKey);
					double dist = _getDistance(a.vector, o.vector);
					o.maybeReassign(dist, aKey);
				}
			}

			for (Integer aKey : attractors.keySet()) {
				_ReduxAttractor a = attractors.get(aKey);
			}

			attractors = new HashMap<>();
			for (Integer oKey : observations.keySet()) {
				_ReduxPoint o = observations.get(oKey);
				if (!attractors.containsKey(o.closest)) {
					attractors.put(o.closest, new _ReduxAttractor(o.closest, o.vector));
				} else {
					attractors.get(o.closest).donate(o.vector);
				}
				attractors.get(o.closest).mergeFeatures(o.fetchFeatures());
			}
			for (Integer aKey : attractors.keySet()) {
				attractors.get(aKey).finalizer();
			}
			describe(depth);

			_recurse_epoch(depth);
		} else {
			lastEpoch = depth;
			Caller.note("Bottoming out @ depth " + depth);
		}
	}

	public void describe(int epoch) {

		boolean show = true;

		if (show) {
			Caller.log(" ......................... ");
			int i = 0;
			for (Integer aKey : attractors.keySet()) {
				_ReduxAttractor a = attractors.get(aKey);
				Caller.log(epoch + "\t" + i + "   " + a.id + " children " + a.count + " location  " + a.getLocation());
				i++;
			}
		}
	}

	public double _getDistance(double[] p1, double[] p2) {
		double value = 0.0;
		for (int i = 0; i < p1.length; i++) {
			value += Math.pow(p2[i] - p1[i], 2);
		}
		return Math.sqrt(value);
	}
}
