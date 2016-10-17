package kmeans;

public class Test_Redux_ExampleUsage {

	public static void main(String...strings) {
		Test_Redux_ExampleUsage test = new Test_Redux_ExampleUsage(); 
		test.bigTest();
	}
	
	public void bigTest() { 
		int shape = 3; // number of dimensions in the observers and the attractors
		int maxDepth = 10; // max number of recurses
		ReduxShiftMeans rsm = new ReduxShiftMeans( shape, maxDepth ); 
		
		for ( int i = 0; i < 100; i++ ) {
			double[] ary = new double[shape];
			ary[0] = Math.random() * 2 - 1;
			ary[1] = Math.random() * 2 - 1;
			ary[2] = Math.random() * 2 - 1;
			rsm.addPoints_step1(ary);
		}
		rsm.createAttractors_step2();
		rsm.recurse_prep_step3();
	}
}
