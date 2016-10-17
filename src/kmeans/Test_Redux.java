package kmeans;

import java.util.HashMap;

import healthpath.common.Caller;

public class Test_Redux {
	private ReduxShiftMeans rsm;
	private void setup() { 
		int shape = 3; // number of dimensions in the observers and the attractors
		int maxDepth = 10; // max number of recurses
		rsm = new ReduxShiftMeans( shape, maxDepth ); 
	}
	
	public static void main(String... strings) {
		Test_Redux tr = new Test_Redux();
		tr.setup();
		tr.addPoints_step1();
		tr.createAttractors_step2();
		//	tr.overRide_createAttractors_step2();
		tr.recurse_prep_step3();
		tr.showFinalState();
		Caller.log("Test end");
		
		
	}
	public void showFinalState() {
		
		rsm.describe(1000);
	}
	public void recurse_prep_step3() { 
		rsm.recurse_prep_step3();
		boolean isOk = rsm.lastEpoch >= rsm.maxRecurseDepth;
		Caller.log( isOk );
	}
	public void overRide_createAttractors_step2() {
		rsm.attractors = new HashMap<>();
		rsm.attractors.put(0, new _ReduxAttractor(0,new double[]{1,1,1}));
		rsm.attractors.put(1, new _ReduxAttractor(1,new double[]{.9,.9,.9}));
		rsm.attractors.put(2, new _ReduxAttractor(2,new double[]{.0,.0,.0}));
		
	}
	public void createAttractors_step2() { 
		rsm.createAttractors_step2();

		boolean show_action = false;
		
		if ( show_action) {
			for ( Integer key : rsm.attractors.keySet()) {
				_ReduxAttractor a = rsm.attractors.get(key);
			}
		}

		boolean isOk = rsm.attractors.size() > 0;
		int shape = rsm.observations.get(0).vector.length;
		isOk &= rsm.attractors.get(0).vector.length == shape;
		Caller.log( isOk );
	}

	public void addPoints_step1() {

		double[][] raw = new double[][] {
			{1,1,1},
			{.9,0.0,0.2},
			{-.9,-0.0,-0.2},
			{-1,-1,-1},
			{-1,1,1},
			{1,-1,1},
			{1,1,-1},
			{1,-1,-1},
			{-1,-1,1},
			{1,-1,-1},
			
		};
		for ( int i = 0; i < raw.length; i++ ) { 
			double[] ary = raw[i];
			rsm.addPoints_step1(ary);
		}
		
		boolean show_activity = false;
		if ( show_activity ) { 
			for ( Integer key : rsm.observations.keySet()) {
				_ReduxPoint o = rsm.observations.get(key);
				Caller.log( o.describe());
			}
		}
		
		boolean isOk = rsm.observations.size() == raw.length;
		isOk &= rsm.observations.get(0).vector.length == raw[0].length;		
		Caller.log( isOk );
	}
}