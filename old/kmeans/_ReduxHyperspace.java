package kmeans;

public class _ReduxHyperspace {
	public int velocity = 0;
	public int days_supply_count = 0;
	public int patient_paid_amount = 0;
	public int ingredient_cost_paid_amount = 0;
	public int count = 0;
	public int male = 0;
	public int female = 0;
	public int sex_other = 0;
	public int ccs_22 = 0;
	public int ccs_24 = 0;
	public int ccs_29 = 0;
	public int ccs_other = 0;

	public int[] fetchFeatures() {
		int[] features = new int[] { velocity, days_supply_count, patient_paid_amount, ingredient_cost_paid_amount,
				male, female, sex_other, ccs_22, ccs_24, ccs_29, ccs_other };
		return features;
	}

	public void mergeFeatures(int[] features) {
		velocity += features[0];
		days_supply_count += features[1];
		patient_paid_amount += features[2];
		ingredient_cost_paid_amount += features[3];
		male += features[4];
		female += features[5];
		sex_other += features[6];
		ccs_22 += features[7];
		ccs_24 += features[8];
		ccs_29 += features[9];
		ccs_other += features[10];

		count++;
	}
}
