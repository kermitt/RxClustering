package kmeans;

import kmeans._ReduxAttractor;
import kmeans.ReduxShiftMeans;
import healthpath.common.Caller;
import healthpath.common.Library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Clusterer {
	
	// Places X,Y and Z in hyperspace
	private double[] X_pov = Library.getRandomRiv();
	private double[] Y_pov = Library.getRandomRiv();
	private double[] Z_pov = Library.getRandomRiv();
	private int shape = 3; // Number of dimensions to castdown from
	private final String db_location = "jdbc:sqlite:hack.sqlite";// db name

	public LinkedHashMap<Integer, _ReduxPoint> selectPersonRIVs(int the_limit) {
		LinkedHashMap<Integer, _ReduxPoint> points = new LinkedHashMap<>();
		try {
			Class.forName("org.sqlite.JDBC");
			String sql = "select id, riv from person_riv limit " + the_limit;
			Connection connection = DriverManager.getConnection(db_location);

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int id = -1;
			while (rs.next()) {
				id = rs.getInt("id");
				String riv_as_string = rs.getString("riv");
				double[] riv = Library.getRivFromRIV_as_String(riv_as_string);
				points.put(id, new _ReduxPoint(id, riv));
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception boom) {
			System.out.println("Fail! " + boom.getMessage());
			boom.printStackTrace();
		}
		/*
		for ( Integer id : points.keySet()) {
			Caller.log(id + "|" + points.get(id).id + " riv " + 
					Library._join(points.get(id).vector));
		}
		*/
		return points;
	}
	/**
	 * 
	 * @param limit
	 * LIMIT the number of PERSON_RIV which will be fetched
	 */
	public void doClustering(int limit, int recurse_to_depth) {
		ReduxShiftMeans kShiftMeans = new ReduxShiftMeans(shape,  recurse_to_depth);
		LinkedHashMap<Integer, _ReduxPoint> points = selectPersonRIVs(limit);
		Caller.log(" doClustering!" + points.size() );
		for (Integer id : points.keySet()) {
			double[] riv = points.get(id).vector;
			double XX = Library.vectorCosineSimilarity(X_pov, riv);
			double YY = Library.vectorCosineSimilarity(Y_pov, riv);
			double ZZ = Library.vectorCosineSimilarity(Z_pov, riv);
			kShiftMeans.addPoints_step1(new double[] { XX, YY, ZZ });
		}
		kShiftMeans.createAttractors_step2();
		kShiftMeans.recurse_prep_step3();

		kShiftMeans.recordResults("RESULTS.psv");
	}	
}