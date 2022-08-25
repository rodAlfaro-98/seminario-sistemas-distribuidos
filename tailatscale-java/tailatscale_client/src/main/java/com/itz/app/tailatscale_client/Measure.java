package com.itz.app.tailatscale_client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Measure {

	public static final String HOST = "127.0.0.1";
	public static final String PORT = "4567";
	public static final String[] ROUTES = {"binary", "hash", "list"};
	public static final String DICT_FILENAME = "src/main/resources/zdic_corto.dic";
	public static final String OUT_FILENAME = "out.csv";
	public List<String> targetStrings;
	
	public Measure() {
		targetStrings = new ArrayList<String>();		
	}
	
	public void read_words() {
		
		try {
			File myFile = new File(DICT_FILENAME);
			Scanner myReader = new Scanner(myFile);
			
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] arrOfStrings = data.split("/");
				this.targetStrings.add(arrOfStrings[0]);
			}
			
			myReader.close();
			Collections.shuffle(targetStrings);
			
		} catch (FileNotFoundException e) {
			System.out.println("Failed to read file");
			e.printStackTrace();	
		}
		
	}
	
	public void save_distribution() {
		
		try {
			File myFile = new File(OUT_FILENAME);
			if (myFile.exists()) {
				myFile.delete();
			}
			
			if (myFile.createNewFile()) {
				FileWriter myWriter = new FileWriter(myFile);
				myWriter.write("binary,hash,list\n");
				
				for (String word : this.targetStrings) {
					String sampleRow = this.measure_distribution(word);
					myWriter.write(sampleRow);
				}
				
				myWriter.close();
			}
			else {
				System.out.println("File already exists...");
			}
		}
		catch (IOException e) {
			System.out.println("An error occurred");
			e.printStackTrace();
		}
	}
	
	public String measure_distribution(String word) {
		try {
			long[] measures = new long[3];
			int i = 0;
			for (String mode : ROUTES) {
				URL url = new URL("http://" + HOST + ":" + PORT + "/" + mode + "/" + word);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				
				long start = System.nanoTime();
				
				con.connect();
				con.getResponseCode();
				con.disconnect();
				
				long elapsed = System.nanoTime() - start;
				measures[i++] = elapsed/1000;
			}
			
			return measures[0] + "," + measures[1] + "," + measures[2] + "\n";
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) {
		Measure myMeasure = new Measure();
		myMeasure.read_words();
		myMeasure.save_distribution();
	}
}
