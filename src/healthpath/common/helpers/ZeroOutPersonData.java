package healthpath.common.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ZeroOutPersonData {
	static final String db_location = "jdbc:sqlite:hack.sqlite";

	public static void main(String... strings) {
		String sql = "delete from PERSON";
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			PreparedStatement pstmt = connection.prepareStatement(sql);// {
			pstmt.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		sql = "delete from PERSON_CALC";
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			PreparedStatement pstmt = connection.prepareStatement(sql);// {
			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		sql = "delete from PERSON_RIV";

		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			PreparedStatement pstmt = connection.prepareStatement(sql);// {
			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
