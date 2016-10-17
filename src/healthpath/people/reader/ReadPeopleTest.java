package healthpath.people.reader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import healthpath.common.Caller;
import healthpath.common.Library;
import healthpath.common.Seen;
import healthpath.common.helpers.Person;
import healthpath.common.helpers.SQLBridge;

public class ReadPeopleTest {
	public static void main(String[] args) {
		db_and_object_are_meshedup_test(); 
		populateClaimlines();
		read_and_populate_step1(); 
		SQLBridge.cleanup();
		Caller.log("The end");
	}
	private static void read_and_populate_step1() { 
		//select * from PERSON
		int before = SQLBridge.countPersonObjects();
		
		String in_file = Library.RESULTS_HOME + "step0_VETTED_22_24_29.csv";
		ReadPeople readPeople = new ReadPeople(100, in_file);
		
		readPeople.populateConcepts(); 

		int after = SQLBridge.countPersonObjects();
		
		boolean isOk = after > before;
		Caller.log(isOk, "Completely read and inserted? Before: " + before + " After: " + after );
	}

	private static void db_and_object_are_meshedup_test() { 
		LinkedHashMap<String, Person> people = new LinkedHashMap<>();
		people.put("personId1", new Person(-1,"F","24","1001"));
		people.put("personId2", new Person(-2,"F","24","1002"));
		people.put("personId3", new Person(-3,"F","29","1003"));

		int inserts_count = SQLBridge.insertPersonObjects(people);
		
		boolean isOk = inserts_count == people.size();
		Caller.log(isOk, "Inserted " + inserts_count);
	}

	private static void populateClaimlines() {
		//String in_file = Library.RESULTS_HOME + "step0_VETTED_22_24_29.csv";
		//ReadPeople readPeople = new ReadPeople(10, in_file);
		
		LinkedHashMap<String, Person> people = new LinkedHashMap<>();
		String test_id = "deleteme";
		people.put(test_id, new Person(-1,"F","24","1001"));
		people.get(test_id).addInformation(1, 2, 3);

		int inserts_count = SQLBridge.insertPersonObjects(people);
		
		boolean isOk = inserts_count == people.size();
		Caller.log(isOk, "Inserted " + inserts_count);
	}
	
	
	private static void concepts_creation_test() {
		String in_file = Library.RESULTS_HOME + "step0_VETTED_22_24_29.csv";
		ReadPeople rr = new ReadPeople(100, in_file);
		int inserted_count = rr.populateConcepts(); 
		boolean isOk = inserted_count > 0; 
		Caller.log(isOk, " Inserted " + inserted_count );
	}
	
}
