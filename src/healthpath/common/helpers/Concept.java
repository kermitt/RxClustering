package healthpath.common.helpers;

import healthpath.common.Library;

public class Concept {
	// CREATE TABLE CONCEPTS_META (id INT, globally_seen INT, feature STRING,
	// payload STRING)
	// CREATE TABLE CONCEPTS_RIV (id INT, riv STRING)
	public int id;
	public int globally_seen;
	public int complexity;
	public String feature;
	public String payload;
	//public String riv = null; // only CURRIED features will get a RIV
	public double[] riv; // only CURRIED features will get a RIV
	
	public Concept(int id, int globally_seen, int complexity, String feature, String payload,String riv_as_string) {
		this.id = id;
		this.globally_seen = globally_seen;
		this.complexity = complexity;
		this.feature = feature;
		this.payload = payload;
		this.riv = Library.getRivFromRIV_as_String(riv_as_string);
	}
	public void setRiv(String riv_as_string ) {
//		this.riv = riv;
		this.riv = Library.getRivFromRIV_as_String(riv_as_string);
	}
	public Concept(int id, int globally_seen, int complexity, String feature, String payload) {
		this.id = id;
		this.globally_seen = globally_seen;
		this.complexity = complexity;
		this.feature = feature;
		this.payload = payload;
	}

	public String getSqlRIV() {
		String sql_riv = "(" + id + ",'" + riv + "')";
		return sql_riv;
	}

	public String getSqlMeta() {
		String values_meta = "(" + id + "," + globally_seen + "," + complexity + ",'" + feature + "','" + payload
				+ "')";
		return values_meta;
	}
}
