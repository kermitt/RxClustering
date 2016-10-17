package common;

public class Caller {

	public static void context_note(String msg) {
		StackTraceElement[] ste = new Throwable().getStackTrace();

		String line = " ln: " + ste[1].getLineNumber();
		String clazz = " c: " + ste[1].getClassName();
		String method = " m: " + ste[1].getMethodName();

		String out = msg + " |\t" + line + clazz + method;

		System.out.println("\t\t*** " + out);
	}

	
	public static void showStack(String msg) {
		StackTraceElement[] ste = new Throwable().getStackTrace();
		String out = msg + "\n";
		for (int i = 1; i < ste.length; i++) {
			/* skip the 0th, i.e., the Caller object itself */
			int j = i - 1;
			String clazz = ste[i].getClassName();
			if (doNotNotPayAttention(clazz)) {
				String line = " ln: " + ste[i].getLineNumber();
				String name = " c: " + ste[i].getClassName();
				String method = " m: " + ste[i].getMethodName();
				out += j + line + name + method + "\n";
			}
		}
		System.out.println(out);
	}

	public static void log(String msg) {
		StackTraceElement[] ste = new Throwable().getStackTrace();

		String line = " ln: " + ste[1].getLineNumber();
		String clazz = " c: " + ste[1].getClassName();
		String method = " m: " + ste[1].getMethodName();

		String out = msg + " |\t" + line + clazz + method;

		System.out.println(out);
	}

	public static void log(String msg, boolean showOrNot) {
		if (showOrNot) {
			StackTraceElement[] ste = new Throwable().getStackTrace();

			String line = " ln: " + ste[1].getLineNumber();
			String clazz = " c: " + ste[1].getClassName();
			String method = " m: " + ste[1].getMethodName();

			String out = msg + " |\t" + line + clazz + method;

			System.out.println(out);
		}
	}

	public static void note(String msg) {
		System.out.println("\t" + msg);
	}

	public static void log(boolean b, String msg) {
		StackTraceElement[] ste = new Throwable().getStackTrace();

		String line = " ln: " + ste[1].getLineNumber();
		String clazz = " c: " + ste[1].getClassName();
		String method = " m: " + ste[1].getMethodName();
		String m = "FAIL";
		if (b) {
			m = "PASS";
		}

		String out = m + " " + msg + " |\t" + line + clazz + method;

		System.out.println(out);
	}

	public static void log(boolean b) {
		StackTraceElement[] ste = new Throwable().getStackTrace();

		String line = " ln: " + ste[1].getLineNumber();
		String clazz = " c: " + ste[1].getClassName();
		String method = " m: " + ste[1].getMethodName();
		String m = "FAIL";
		if (b) {
			m = "PASS";
		}

		String out = m + "|\t" + line + clazz + method;

		System.out.println(out);
	}

	public static boolean doNotNotPayAttention(String candidate) {
		if (candidate.startsWith("sun") || candidate.startsWith("com.sun") || candidate.startsWith("com.ibm")
				|| candidate.startsWith("java.") || candidate.startsWith("org.junit")
				|| candidate.startsWith("org.eclipse")) {
			return false;
		}
		return true;
	}
}
