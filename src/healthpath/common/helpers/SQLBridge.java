package healthpath.common.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import healthpath.common.Caller;
import healthpath.common.Library;

public class SQLBridge {
	static final String db_location = Library.db_location;
	public static int insertConcepts(List<Concept> concepts) {
		return insertConcepts(concepts, false);
	}

	public static void addRIVtoPerson(String person_id, double[] riv ) {
		try {
			Class.forName("org.sqlite.JDBC");			
			String sql = "select id from person where person_id = '" + person_id + "'";
			Connection connection = DriverManager.getConnection(db_location);

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int id = -1;
			while (rs.next()) {
				id = rs.getInt("id");
			}
			rs.close();
			String update = "UPDATE PERSON_RIV set riv = '" + Library._join(riv) + "' where id = " + id;
			stmt.executeUpdate(update);
			stmt.close();
			connection.close();
		} catch (Exception boom) {
			System.out.println("Fail! " + boom.getMessage());
			boom.printStackTrace();
		}
	}
	
	public static Concept getConcept(String feature, String payload) {
		Concept concept = null;
		try {
			Class.forName("org.sqlite.JDBC");
			//String sql = "select * from CONCEPTS_META where feature = '" + feature + "' and payload = '" + payload + "'";
			
			String sql = "select a.id, a.globally_seen, a.complexity, b.riv from CONCEPTS_META a, CONCEPTS_RIV b where a.id = b.id and a.feature = '" + feature + "' and a.payload = '" + payload + "'";
			
			//Caller.log(sql);
			Connection connection = DriverManager.getConnection(db_location);

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				int globally_seen = rs.getInt("globally_seen");
				int complexity = rs.getInt("complexity");
				String riv = rs.getString("riv"); 
				//String feature_str = rs.getString("feature");
				//String payload_str = rs.getString("payload");
				concept = new Concept(id, globally_seen, complexity, feature, payload, riv);
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception boom) {
			System.out.println("Fail! " + boom.getMessage());
			boom.printStackTrace();
		}
		return concept;
	}

	public static int insertConcepts(List<Concept> concepts, boolean show_the_sql) {
		Caller.log("Begin inserting " + concepts.size() + " concepts");
		int total = concepts.size();
		int inserted = 0;
		try {
			Class.forName("org.sqlite.JDBC");
			String sql_meta = "insert into CONCEPTS_META (id, globally_seen,complexity,feature, payload) values ";
			String sql_riv = "insert into CONCEPTS_RIV (id, riv ) values ";
			Connection connection = DriverManager.getConnection(db_location);

			Statement stmt = connection.createStatement();
			for (Concept c : concepts) {
				String meta = sql_meta + c.getSqlMeta();
				try {
					String sql = sql_meta + c.getSqlMeta();
					if (show_the_sql) {
						Caller.log(sql);
					}
					stmt.executeUpdate(sql);
				} catch (Exception e) {
					e.printStackTrace();
					Caller.log("FAILURE! " + meta + "\nExiting now...");
					System.exit(0);
				}

				String riv = sql_riv + c.getSqlRIV();
				try {
					stmt.executeUpdate(sql_riv + c.getSqlRIV());
				} catch (Exception e) {
					Caller.log("FAILURE: " + riv);
					System.exit(0);
				}
				inserted++;

				if (inserted % 100 == 0) {
					Caller.log("Passing " + inserted + " of " + total);
				}
			}
			stmt.close();
			connection.close();
		} catch (Exception boom) {
			System.out.println("[Concepts] Fail! " + boom.getMessage());
			boom.printStackTrace();
		}
		return inserted;
	}

	public static int insertPersonObjects(LinkedHashMap<String, Person> people) {
		return insertPersonObjects(people, false);
	}

	public static int insertPersonObjects(LinkedHashMap<String, Person> people, boolean show_the_sql) {
		Caller.log("\t\tBegin inserting " + people.size() + " people");
		int total = people.size();
		int inserted = 0;
		try {
			Class.forName("org.sqlite.JDBC");
			String sql = "insert into PERSON (id, velocity, sex, ccs, is_alive, dod, dob, person_id) values ";

			String sql_calc = "insert into PERSON_CALC (id, patient_paid, "
					+ "ingredient_cost, days_supply, complexity, pain, velocity) values ";

			String sql_riv = "insert into PERSON_RIV (id, riv ) values ";
			Connection connection = DriverManager.getConnection(db_location);

			Statement stmt = connection.createStatement();
			// for (Person p : people) {
			for (String person_id : people.keySet()) {
				Person p = people.get(person_id);
				String the_sql = sql + p.getSQL_Person();
				try {
					if (show_the_sql) {
						Caller.log(the_sql);
					}
					stmt.executeUpdate(the_sql);
				} catch (Exception e) {
					e.printStackTrace();
					Caller.log("FAILURE! " + the_sql + "\nExiting now...");
					System.exit(0);
				}

				the_sql = sql_calc + p.getSQL_Person_calc();
				try {
					stmt.executeUpdate(the_sql);
				} catch (Exception e) {
					Caller.log("FAILURE: " + the_sql);
					Caller.log(e.getMessage());
					System.exit(0);
				}

				the_sql = sql_riv + p.getSQL_RIV();
				try {
					stmt.executeUpdate(the_sql);
				} catch (Exception e) {
					Caller.log("FAILURE: " + the_sql);
					Caller.log(e.getMessage());
					System.exit(0);
				}
				inserted++;

				if (inserted % 100 == 0) {
					Caller.log("[People] Passing " + inserted + " of " + total);
				}
			}
			stmt.close();
			connection.close();
		} catch (Exception boom) {
			System.out.println("Fail! " + boom.getMessage());
			boom.printStackTrace();
		}
		return inserted;
	}

	/**
	 * Used for TDD purposes only
	 * 
	 * @return count for the selected Person rows
	 */
	public static int countPersonObjects() {
		int selected = 0;
		try {
			Class.forName("org.sqlite.JDBC");
			String sql = "select * from PERSON";
			Connection connection = DriverManager.getConnection(db_location);

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				selected++;
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception boom) {
			System.out.println("Fail! " + boom.getMessage());
			boom.printStackTrace();
		}
		return selected;
	}

	public static Map<Integer, String> getPeople_ids_for_lookup() {
		Map<Integer, String> people = new HashMap<>();
		try {
			Class.forName("org.sqlite.JDBC");
			String sql = "select * from PERSON where id > 0";
			Connection connection = DriverManager.getConnection(db_location);

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String person_id = rs.getString("person_id");
				people.put(id, person_id);
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception boom) {
			System.out.println("Fail! " + boom.getMessage());
			boom.printStackTrace();
		}
		return people;
	}

	public static void updatePerson_as_dead(String id) {
		try {
			Class.forName("org.sqlite.JDBC");
			String sql = "UPDATE person set is_alive = 0 where person_id = " + id;
			Caller.log(sql);
			Connection connection = DriverManager.getConnection(db_location);

			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			connection.close();
		} catch (Exception boom) {
			System.out.println("Fail! " + boom.getMessage());
			boom.printStackTrace();
		}
	}

	public static LinkedHashMap<String, Feature> getFeatures() {
		LinkedHashMap<String, Feature> features = new LinkedHashMap<>();
		String sql = "select id, feature, type, treatment, global_count from FEATURES";
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String feature = rs.getString("feature");
				String type = rs.getString("type");
				String treatment = rs.getString("treatment");
				int global_count = rs.getInt("global_count");

				features.put(feature, new Feature(id, feature, type, treatment, global_count));
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception boom) {
			System.out.println("Failbot! " + boom.getMessage());
			boom.printStackTrace();
		}
		return features;
	}

	public static void cleanup() {
		String sql = "delete from concepts_meta where id < ?";
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			PreparedStatement pstmt = connection.prepareStatement(sql);// {
			pstmt.setInt(1, 0);
			pstmt.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		sql = "delete from concepts_riv where id < ?";
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			PreparedStatement pstmt = connection.prepareStatement(sql);// {
			pstmt.setInt(1, 0);
			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		sql = "delete from person where id < ?";
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			PreparedStatement pstmt = connection.prepareStatement(sql);// {
			pstmt.setInt(1, 0);
			pstmt.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		sql = "delete from person_calc where id < ?";
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			PreparedStatement pstmt = connection.prepareStatement(sql);// {
			pstmt.setInt(1, 0);
			pstmt.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		sql = "delete from person_riv where id < ?";
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			PreparedStatement pstmt = connection.prepareStatement(sql);// {
			pstmt.setInt(1, 0);
			pstmt.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	public static void nuke_the_tables() {
		String sql = "delete from concepts_meta";
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			PreparedStatement pstmt = connection.prepareStatement(sql);// {
			pstmt.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		sql = "delete from concepts_riv";
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(db_location);
			PreparedStatement pstmt = connection.prepareStatement(sql);// {
			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		Caller.log("Tables concepts_main & concepts_riv have been truncated");
	}
}
