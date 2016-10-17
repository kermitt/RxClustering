package common;

import java.util.LinkedHashMap;
import java.util.Set;

public class Test_Common {
	public static void main(String...strings) {
		Test_Common tests = new Test_Common();
		tests.getFeatures();
		
	}
	private void getFeatures() { 
		LinkedHashMap <String,String>lhm = Config.getFeatures();
		boolean isOk = 14 == lhm.size();
		Caller.log(isOk);
	}

	
	
}
