package kmeans;

import java.util.LinkedHashMap;

import healthpath.common.Caller;
import healthpath.common.Library;

public class TestOther {
	public static void main(String... strings) {
		selectIsOk_test();
		playground();

	}

	public static void playground() {
		int result = (int) (Math.random() * 1000);
		Caller.log("result: " + result);
	}

	public static void selectIsOk_test() {

		Clusterer cm = new Clusterer();
		int expected = 20;
		LinkedHashMap<Integer, _ReduxPoint> map = cm.selectPersonRIVs(expected);
		boolean isOk = true;
		int seen = 0;
		for (Integer id : map.keySet()) {
			isOk &= map.get(id).vector.length > Library.RIV_LENGTH;
			seen++;
		}
		isOk = seen == expected;

		Caller.log(isOk, "selected PERSON_RIV properly ");
	}
}
