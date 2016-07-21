package step0_removeNoise;

public class _Entry {
	public boolean isPositive = true;
	public String entry;
	public String composite;
	public _Entry(String entry) {
		this.entry = entry;
	}
	public void setCompositeKey(String key ) {
		this.composite = key; 
	}
	public void setIsPositive(int n) {
		if (n < 0) {
			isPositive = false;
		}
	}

	public void setIsPositive(double n) {
		if (n < 0) {
			isPositive = false;
		}
	}
}