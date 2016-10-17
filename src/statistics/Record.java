package statistics;

import healthpath.common.Caller;

public class Record {
	// double bg = 0;
	public double depth;
	public double id;
	public double x;
	public double y;
	public double z;
	public double count;
	public String children;

	public double patient_paid = 0;
	public double ingredient_cost = 0;
	public double days_supply = 0;
	public double complexity = 0;
	public double pain = 0;
	public double velocity = 0;
	public double donate_count = 0;

	public double total_patient_paid = 0;
	public double total_ingredient_cost = 0;
	public double total_days_supply = 0;
	public double total_complexity = 0;
	public double total_pain = 0;
	public double total_velocity = 0;
	private Integer myId = 0;
	public Record(Integer myId) { 
		this.myId = myId;
	}
	
	public void donate(double pd, double cost, double supply, double x, double ouch, double v) {
		patient_paid += (pd / v);
		ingredient_cost += (cost / v);
		days_supply += (supply / v);
		complexity += x;
		pain += ouch;
		velocity += v;
		donate_count++;

		total_patient_paid += pd;
		total_ingredient_cost += cost;
		total_days_supply += supply;
		total_complexity += x;
		total_pain += ouch;
		total_velocity += v;
	}

	public String finish() {
		
		patient_paid /= donate_count;
		ingredient_cost /= donate_count;
		days_supply /= donate_count;
		complexity /= donate_count;
		pain /= donate_count;
		velocity /= donate_count;

		
		String result = "Ave Rx event ( over " + ((int) donate_count) + " claimlines)\n";
		result += ((int) patient_paid) + "\tpatient_paid\n";
		result += ((int) ingredient_cost) + "\tingredient_cost\n";
		result += ((int) days_supply) + "\tdays_supply\n";
		result += ((int) complexity) + "\tcomplexity\n";
		result += ((int) pain) + "\tpain\n";
		result += ((int) velocity) + "\tvelocity\n";

		result += ((int) velocity) + "\tvelocity\n";

		result += "Totals\n";
		result += ((int) total_patient_paid) + "\tpaid\n";
		result += ((int) total_ingredient_cost) + "\tcost\n";
		result += ((int) total_days_supply) + "\tsupply\n";
		result += ((int) total_complexity) + "\tcomplexity\n";
		result += ((int) total_pain) + "\tpain\n";
		result += ((int) total_velocity) + "\tvelocity\n";

		return result;
	}

	
	public String finish_for_JSON() {
		
		patient_paid /= donate_count;
		ingredient_cost /= donate_count;
		days_supply /= donate_count;
		complexity /= donate_count;
		pain /= donate_count;
		velocity /= donate_count;

		String result = "data['" + myId + "'] = {};\n"; 
		result += "data['" + myId + "']['claimlines']=" + ((int) donate_count) + ";\n";
		result += "data['" + myId + "']['patient_paid']=" + ((int) patient_paid) + ";\n";
		result += "data['" + myId + "']['ingredient_cost']=" + ((int) ingredient_cost) + ";\n";
		result += "data['" + myId + "']['days_supply']=" + ((int) days_supply) + ";\n";
		result += "data['" + myId + "']['complexity']=" + ((int) complexity) + ";\n";
		result += "data['" + myId + "']['pain']=" + ((int) pain) + ";\n";
		result += "data['" + myId + "']['tvelocity']=" + ((int) velocity) + ";\n";
		result += "data['" + myId + "']['total_patient_paid']=" + ((int) total_patient_paid) + ";\n";
		result += "data['" + myId + "']['total_ingredient_cost']=" + ((int) total_ingredient_cost) + ";\n";
		result += "data['" + myId + "']['total_days_supply']=" + ((int) total_days_supply) + ";\n";
		result += "data['" + myId + "']['total_complexity']=" + ((int) total_complexity) + ";\n";
		result += "data['" + myId + "']['total_velocity']=" + ((int) total_velocity) + ";\n";

		return result;
	}

	

	private String split(String raw) {
		String result = raw.split(":")[1];
		result = result.trim();
		return result;
	}

	public String show() {
		String show = "";
		show += "depth: " + depth + "\n";
		show += "id: " + id + "\n";
		show += "location: " + x + ", " + y + ", " + z + "\n";
		show += "count: " + count + "\n";
		show += "depth: " + depth + "\n";
		String truncate;
		if (children.length() > 23) {
			truncate = children.substring(0, 20);
			truncate += "...";
		} else {
			truncate = children;
		}
		show += truncate;
		return show;
	}

	public void setDepth(String d) {
		d = split(d);
		depth = Integer.parseInt(d);
	}

	public void setId(String i) {
		try {
			i = split(i);
			id = Integer.parseInt(i);

		} catch (Exception e) {

			Caller.showStack("Fail! " + e.getMessage());

		}
	}

	public void setLocation(String l) {
		// 0.22, 0.08, 0.16
		l = split(l);
		String[] pieces = l.split(",");
		x = Double.parseDouble(pieces[0]);
		y = Double.parseDouble(pieces[1]);
		z = Double.parseDouble(pieces[2]);
	}

	public void setCount(String c) {
		c = split(c);
		count = Integer.parseInt(c);
	}

	public void setChildren(String childIds) {
		childIds = split(childIds);
		children = childIds;
	}
}
