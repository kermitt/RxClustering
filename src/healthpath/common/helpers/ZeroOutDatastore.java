package healthpath.common.helpers;

public class ZeroOutDatastore {
	public static void main(String...strings){
		SQLBridge.nuke_the_tables();
	}
}
