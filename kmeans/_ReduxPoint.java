package kmeans;

public class _ReduxPoint extends _ReduxHyperspace {
	public double[] vector;
	
	public Integer closest = -1;
	public Integer nextClosest = -1;
	public double closestDistance = Double.MAX_VALUE;
	
	public _ReduxPoint(double[] vector, int velocity, 
			int days_supply_count, int patient_paid_amount, int ingredient_cost_paid_amount,
			int male,int female,int sex_other,int ccs_22, int ccs_24,int ccs_29, int ccs_other) {
		this.vector = vector;
		
		this.velocity = velocity;
		this.days_supply_count = days_supply_count;
		this.patient_paid_amount = patient_paid_amount;
		this.ingredient_cost_paid_amount = ingredient_cost_paid_amount; 
		
		this.male = male;
		this.female = female;
		this.sex_other = sex_other;
		this.ccs_22 = ccs_22;
		this.ccs_24 = ccs_24;
		this.ccs_29 = ccs_29;
		this.ccs_other = ccs_other;
		
		
//		Caller.log("icpa " + ingredient_cost_paid_amount + " m " + male + " f " + female + " so " + sex_other + " tt " + ccs_22 + " tf " + ccs_24 ); 
		
	}
	
	public void maybeReassign(double distance, Integer attractorId ){
		if ( distance < closestDistance ) {
			closestDistance = distance;
			closest = attractorId;
		}
	}
	public String describe() {
		String out  = closest + ":\t"; 
		for ( int i = 0; i < vector.length; i++ ) { 
			out += vector[i] + "|";
		}
		out += " :: " + closestDistance;
		return out;
	}
}
