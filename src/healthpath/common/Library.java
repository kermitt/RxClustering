package healthpath.common;

import java.text.DecimalFormat;

public class Library {
	public final static String db_location	 = "jdbc:sqlite:hack.sqlite";
	public final static String HOME = "C://HEALTHPATH2//";
	public final static String RESULTS_HOME = HOME + "results//";
	public final static int RIV_LENGTH = 50; 

	public static String _join(double[] ary) {
		String out = "";
		for (int i = 0; i < ary.length; i++) {
			if (i < ary.length - 1) {
				out += ary[i] + ",";
			} else {
				out += ary[i] + "";
			}
		}
		return out;
	}

	public static double[] getRivFromRIV_as_String(String riv_as_string) {
		String[] pieces = riv_as_string.split(",");
		double[] riv = new double[pieces.length];

		for (int i = 0; i < pieces.length; i++) {
			riv[i] = Double.parseDouble(pieces[i]);
		}
		return riv;
	}

	public static double[] getBlankRiv() {
		double[] riv = new double[RIV_LENGTH];
		for (int i = 0; i < RIV_LENGTH; i++) {
			riv[i] = 0;// Double.parseDouble(ary[i]);
		}
		riv[0] = 1;
		return riv;
	}

	public static double[] getRandomRiv() {
		double[] riv = new double[RIV_LENGTH];
		for (int i = 0; i < RIV_LENGTH; i++) {
			if (Math.random() > 0.5) {
				riv[i] = -1;
			} else {
				riv[i] = 1;
			}
		}
		riv[0] = 1;
		return riv;
	}

	public static double[] getRiv(String riv_as_string) {
		String[] ary = riv_as_string.split(",");
		double[] riv = new double[ary.length];
		for (int i = 0; i < ary.length; i++) {
			riv[i] = Double.parseDouble(ary[i]);
		}
		return riv;
	}
	public static double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}

	public static double vectorCosineSimilarity(double[] a, double[] b) {
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;

		double[] control = new double[a.length];
		double[] vector = new double[b.length];
		for (int i = 0; i < a.length; i++) {
			control[i] = (double) a[i];
			vector[i] = (double) b[i];
		}

		for (int i = 0; i < control.length; i++) {
			dotProduct += control[i] * vector[i]; // a.b
			magnitude1 += Math.pow(control[i], 2); // (a^2)
			magnitude2 += Math.pow(vector[i], 2); // (b^2)
		}

		magnitude1 = Math.sqrt(magnitude1);// sqrt(a^2)
		magnitude2 = Math.sqrt(magnitude2);// sqrt(b^2)

		if (magnitude1 != 0.0 | magnitude2 != 0.0) {
			cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		} else {
			return 0;
		}
		return cosineSimilarity;
	}

}
