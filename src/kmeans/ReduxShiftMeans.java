package kmeans;

import healthpath.common.Caller;
import healthpath.common.GeneralWriter;

import java.util.*;
import healthpath.common.Library;

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

	public void addPoints_step1(double[] ary) {
		observations.put(nextKey, new _ReduxPoint(nextKey, ary));
		nextKey++;
	}

	public void createAttractors_step2() {
		// make a bunch of Attractors and sprinkle them around in the hypercube
		double count = Math.sqrt(observations.size());
		count *= 6;
		
		Caller.log("CREATING " + count + " ATTRACTORS from " + observations.size() + " OBSERVATIONS");
		
		int result = (int) count;
		if (result < 2) {
			result = 2;
		}
		//Caller.log("COUNT: " + count + " from " + observations.size());
		for (int i = 0; i < result; i++) {
			double[] d = new double[shape];
			for (int j = 0; j < shape; j++) {
				d[j] = Math.random() * 4 - 2;
			}
			
			//Caller.log("ADDING _ReduxAttractor " + i + " d: " + d );
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
	public void recordResults(String filename) {
		
		GeneralWriter gw = new GeneralWriter(filename); 
		
		gw.step1_of_2("RIV run");
		int depth = 0; 
		for (Integer key : afterLoc.keySet()) {
			++depth; 
			double[] ary = afterLoc.get(key);

			String locationA = "";
			for (int i = 0; i < ary.length; i++) {
				locationA += Library.roundTwoDecimals(ary[i]) + "\t";
			}

			double[] ary2 = beforeLoc.get(key);

			String locationB = "";
			for (int i = 0; i < ary2.length; i++) {
				locationB += Library.roundTwoDecimals(ary2[i]) + "\t";
			}
			//			Caller.log("LOCATIONS| BEFORE: " + locationB + "    AFTER: " + key + " | " + locationA + "\t|"
			//					+ attractors.get(key).count);
			
		//	Caller.log("|-|" + key + " | " + locationA + "\t|"
		//			+ attractors.get(key).count);
			Caller.note("..................... ");
			gw.step2_of_2("..................... ");
			Caller.note("depth: " + depth ); 
			Caller.note("cluster: " + key );
			gw.step2_of_2("depth: " + depth );
			gw.step2_of_2("cluster: " + key);
			String location = attractors.get(key).getLocation(); 
			Caller.note("location: " + location );
			gw.step2_of_2("location: " + location);
			Caller.note("count: " + attractors.get(key).children.size());
			gw.step2_of_2("count: " + attractors.get(key).children.size());
			String out = "";
			int stop = 10;
			for ( int x = 0; x < stop; x++ ) {
				// limit to the 'stop' for readiblity's sake
				out += attractors.get(key).children.get(x) + ",";
			}
			Caller.note("children: " + out);
			out = "";
			for ( int x = 0; x < attractors.get(key).children.size(); x++ ) {
				// get everything for accuracy's sake
				out += attractors.get(key).children.get(x);
				if ( x < attractors.get(key).children.size() - 1 ) {
					out += ",";
				}
			}

			gw.step2_of_2("children: " + out);
		}
		Caller.log("\n***WROTE TO " + filename + " ***");
		
		
		//for ( Integer id : attractors.keySet()) {
		//	Caller.log(id + "!" + attractors.get(id).id + " riv " + 
		//			Library._join(attractors.get(id).vector));
		//}

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
			attractors = new HashMap<>();
			int jj = 0;
			for (Integer oKey : observations.keySet()) {
				_ReduxPoint o = observations.get(oKey);
				
			//	Caller.log( jj + " _ReduxPoint: " + o.id + " closest: " + o.closest + " next: " + o.nextClosest );
				jj++;
				if (!attractors.containsKey(o.closest)) {
					attractors.put(o.closest, new _ReduxAttractor(o.closest, o.vector));
				} else {
					attractors.get(o.closest).donate(o.vector);
				}
				attractors.get(o.closest).addMetadata(o.getMetadata());
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

	public void describe( int epoch) {

		Caller.log(" ......................... ");
		int i = 0;
		
		for (Integer aKey : attractors.keySet()) {
			_ReduxAttractor a = attractors.get(aKey);
			Caller.log( aKey + " of "  + attractors.size() + "   epoch " + epoch + "\ti" + i + "   " + a.id + " children " + a.count + "\tloc:  "
					+ a.getLocation());
			i++;
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
