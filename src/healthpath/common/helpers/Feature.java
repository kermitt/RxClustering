package healthpath.common.helpers;

public class Feature {
	public int id;
	public String feature;
	public String type; // string, number, money, boolean or date
	public String treatment; // no_op, curry or ave
	public int global_count;
	
	public Feature(int id, String feature, String type, String treatment, int global_count ) { 
		this.id = id;
		this.feature = feature;
		this.type = type;
		this.treatment = treatment;
		this.global_count = global_count; 
	}
}