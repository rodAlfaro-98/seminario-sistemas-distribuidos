package com.itz.app.tailatscale;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Search {

	static final String DICT_FILENAME = "src/main/resources/zdict_corto.dic";
	List<String> list_dict;
	Set<String> hash_dict;
	
	public Search() {
		list_dict = new ArrayList<String>();
		hash_dict = new HashSet<String>();
		
		load_dictionary();
		//print_dictionary();
	}
	
	public boolean binary_search(String word, int start, int end) {

		int spot;
		int res;
		
		if (start > end)
			return false;

		spot = start + (end - start) / 2;
		res = this.list_dict.get(spot).compareTo(word); 
		
		if (res == 0) {
			return true;
		}
		else if (res < 0) {
			return binary_search(word, spot+1, end);
		}
		else {
			return binary_search(word, start, spot);
		}
	}
	
	public boolean list_search(String word) {
		return this.list_dict.contains(word);
	}
	
	public boolean hash_search(String word) {
		return this.hash_dict.contains(word);
	}
	
	private void load_dictionary() {
		try {
			File myFile = new File(DICT_FILENAME);
			Scanner myReader = new Scanner(myFile);
			
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] arrOfStrings = data.split("/");
				this.list_dict.add(arrOfStrings[0]);
				this.hash_dict.add(arrOfStrings[0]);
			}
			
			myReader.close();
			Collections.sort(this.list_dict);
			
		} catch (FileNotFoundException e) {
			System.out.println("Failed to read file");
			e.printStackTrace();	
		}
	}
	
	@SuppressWarnings("unused")
	private void print_dictionary() {
		for (String entry : this.list_dict) {
			System.out.println(entry);
		}
	}
	
	
}
