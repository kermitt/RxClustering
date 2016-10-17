package kmeans;

import common.Config;

public class _ReduxAttractor extends _ReduxHyperspace {
	public int id;
	public double[] vector;
	public double count = 0;

	public _ReduxAttractor( int id, double[] vector) {
		this.id = id;
		this.vector = vector;
		count = 1.0;
	}
	public String getLocation() { 
		/* Where in the hyper-space is this? */

		String location = ""; 
		for ( int i = 0; i < vector.length; i++ ) { 
			if ( i < vector.length - 1 ) {
				location += Config.roundTwoDecimals(vector[i]) + ",\t";
			} else {
				location += Config.roundTwoDecimals(vector[i]);
			}
		}
		return location;
	}
	public void donate( double[] v ) {
		for ( int i = 0; i < v.length; i++ ) {
			vector[i] += v[i];
		}
		count++;
	}
	public void finalizer() {
		for ( int i = 0; i < vector.length; i++ ) {
			vector[i] /= count;
		}		
	}
	/*
	public String describe() {
		String out  = count + "   ATTRACTOR: " + id + ":\t"; 
		for ( int i = 0; i < vector.length; i++ ) { 
			out += vector[i] + "|";
		}
		out += "   " + vector.length;
		return out;
	}
	*/
}
