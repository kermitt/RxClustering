package kmeans;

public class _ReduxPoint extends _ReduxHyperspace {
	public double[] vector;
	public int id; 
	public Integer closest = -1;
	public Integer nextClosest = -1;
	public double closestDistance = Double.MAX_VALUE;
	public _ReduxPoint(int id, double[] vector) {
		this.id = id; 
		this.vector = vector;
	}
	public int getMetadata() { 
		return id; 
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
