package step2_people;
import common.Config;
import common.Caller;
public class Test_step2 {
	public static void main(String...strings) { 
		Test_step2 tests = new Test_step2();
		tests.readConcept(); 
	} 
	public void readConcept() { 
		_ReadConcept rc = new _ReadConcept();
		String fullpath = Config.fullPath + Config.step1_concepts;
		rc.read_psv(50000, fullpath); // it is currently about 5k 

		boolean isOk = true; 
		
		/* There are other features - this is more a spot check */
		isOk &= rc.router.get("during_treatment").size() == 2; // boolean
		isOk &= rc.router.get("after_cure").size() == 2; // boolean
		isOk &= rc.router.get("age_filled").size() == 0; // number is be EMPTY
		isOk &= rc.router.get("person_id").size() == 0; // PID! Curry this! Not place! Silly. Be EMPTY
		isOk &= rc.router.get("drug_group_description").size() > 50; // More than a bunch.
		
		Caller.log( isOk );
	}
}