package kmeans;
import java.util.ArrayList;
import java.util.List;

import healthpath.common.Library; 

public class _ReduxAttractor extends _ReduxHyperspace {
	public int id;
	public double[] vector;
	public double count = 0;
	public List<Integer>children = new ArrayList<>();
	public _ReduxAttractor( int id, double[] vector) {
		this.id = id;
		this.vector = vector;
		count = 1.0;
	}
	public void addMetadata(Integer id) { 
		children.add(id);
	}
	public String getLocation() { 
		String location = ""; 
		for ( int i = 0; i < vector.length; i++ ) { 
			if ( i < vector.length - 1 ) {
				location += Library.roundTwoDecimals(vector[i]) + ",\t";
			} else {
				location += Library.roundTwoDecimals(vector[i]);
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
}