package common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import common.Caller;

public class GeneralWriter {
	private String targetPSV = null;

	public GeneralWriter(String targetPSV ) {
		this.targetPSV = targetPSV;
	}

	public void step1_of_2(String header) {
		Caller.log("Writing to " + targetPSV);
		try (FileWriter fw = new FileWriter(targetPSV, false);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(header);
		} catch (IOException e) {
			Caller.log("FAILBOT! Because " + e.getMessage());
			e.printStackTrace();
		}
	}

	private int depth = 0;

	public void step2_of_2(String row) {
		try (FileWriter fw = new FileWriter(targetPSV, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(row);
			if (depth % 10000 == 0 && depth > 0) {
				Caller.note("Write is passing " + depth);
			}
			depth++;
		} catch (IOException e) {
			Caller.log("FAILBOT! Because " + e.getMessage());
			e.printStackTrace();
		}
	}
}