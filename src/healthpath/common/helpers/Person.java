package healthpath.common.helpers;

import java.util.HashSet;
import java.util.Set;

import healthpath.common.Caller;
import healthpath.common.Library;

public class Person {

	public int id;
	public String sex;
	public String ccs;
	public String person_id;
	public int is_alive;
	public int dod;// date of death
	public int dob;// date of birth

	public int patient_paid = 0;
	public int ingredient_cost = 0;
	public int days_suppy = 0;
	public ClaimLine claimline = new ClaimLine();
	
	public double complexity = 0;
	private double pain = 0;
	public double[] riv = Library.getBlankRiv();
	private int velocity = 0;

	
	public Set < String> witnessed = new HashSet<>();
	public void maybeAddComplexity(String ndc_code) {
		if ( ! witnessed.contains(ndc_code)) {
			witnessed.add(ndc_code); 
		}
	}
	public int getComplexity() { 
		return witnessed.size();
	}
	
	/* This constructor is not in use yet. Why? Because I am not yet using
	 * the Master Death List ( i.e., the is_alive, dod and dob information) 
	 */
	public Person(int id, String sex, String ccs, String person_id, int is_alive, int dod, int dob) {
		this.id = id;
		this.sex = sex;
		this.ccs = ccs;
		this.person_id = person_id;
		this.is_alive = is_alive;
		this.dod = dod;
		this.dob = dob;

	}
	
	public void donateRiv(int scalar, double[] incoming ) { 
		for ( int i = 0; i < incoming.length; i++ ) { 
			riv[i] += incoming[i];
		}
	}
		
	public Person() { 
		// Used by healthpath.merge.ReadPeopleMerge.java
	}
	public Person(int id, String sex, String ccs, String person_id) {
		this.id = id;
		this.sex = sex;
		this.ccs = ccs;
		this.person_id = person_id;
		this.is_alive = 1;

		riv = Library.getBlankRiv();
	}
	
	public void curryThese(String gender_code, String drug_label_name,String drug_group_description, String drug_class_description) {
		addCurriedInformation("gender_code",gender_code);
		addCurriedInformation("drug_label_name",drug_label_name);
		addCurriedInformation("drug_group_description",drug_group_description);
		addCurriedInformation("drug_class_description",drug_class_description);
	}
	
	private void addCurriedInformation(String feature, String value ) { 
		
		/* TODO: Make sure the 'feature' being donated here is supposed to be CURRIED */
		
		value = value.trim();
		value = value.toLowerCase();
		value = value.replace("'s","");
		
		claimline.addCurriedInformation(feature, value);
	}
	
	public void addInformation(int patient_paid, int ingredient_cost, int days_suppy) {
		this.patient_paid += patient_paid;
		this.ingredient_cost += ingredient_cost;
		this.days_suppy += days_suppy;

		velocity++;
	}

	public String getSQL_Person() {
		String values_meta = "(" + id + "," + velocity + ",'" + sex + "','" + ccs + "'," + is_alive + "," + dod + "," + dob + "," + person_id + ")";
		return values_meta;
	}
	
	public String getSQL_Person_calc() {
		String values_meta = "(" + id + "," + patient_paid + "," + ingredient_cost + 
				"," + days_suppy + "," + complexity + "," + pain + "," + velocity + ")";
		return values_meta;
	}

	public String getSQL_RIV() {
		String sql_riv = "(" + id + ",'" + Library._join(riv) + "')";
		return sql_riv;
	}

}