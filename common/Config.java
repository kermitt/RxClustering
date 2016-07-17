package common;
import java.util.*;
public class Config {
	public static String STRING = "string";
	public static String NUMBER = "number";
	public static String DATE = "date";
	public static String BOOLEAN = "boolean";
	
	public static LinkedHashMap<String, String> getFeatures() {
		/* return the features and the type of each feature, to be 
		 * read in from the originating .csv file 
		 */
		LinkedHashMap < String, String > features = new LinkedHashMap<>();
		features.put("person_id",STRING);
		features.put("gender_code",STRING);
		features.put("ccs_category_id",STRING);
		features.put("ndc_code",STRING);
		features.put("drug_label_name",STRING);
		features.put("drug_group_description",STRING);
		features.put("drug_class_description",STRING);
		features.put("days_supply_count", NUMBER);
		features.put("filled_date",DATE);
		features.put("patient_paid_amount",NUMBER);
		features.put("ingredient_cost_paid_amount",NUMBER);
		features.put("age_filled",NUMBER);
		features.put("after_cure",BOOLEAN);
		features.put("during_treatment",BOOLEAN);

		return features;
	}
}
