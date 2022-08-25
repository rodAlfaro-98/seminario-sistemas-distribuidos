package com.itz.app.tailatscale;

import static spark.Spark.*;

public class Server {
	
    public static void main(String[] args) {
        
        Search mySearch = new Search();
        
        get("/hello", (request, response) -> "Hello World!");
        
        get("/binary/:word", (request, response) -> {
        	String word = request.params(":word");
        	boolean found = mySearch.binary_search(word, 0, mySearch.list_dict.size() - 1);
        	if (found) {
        		return "Word \"" + word + "\" was Found! :) ";	
        	}
        	else {
        		return "Word \"" + word + "\" was not Found! :( ";
        	}        	
        });
        
        get("/list/:word", (request, response) -> {
        	String word = request.params(":word");
        	boolean found = mySearch.list_search(word);
        	if (found) {
        		return "Word \"" + word + "\" was Found! :) ";	
        	}
        	else {
        		return "Word \"" + word + "\" was not Found! :( ";
        	}         	
        });
        
        get("/hash/:word", (request, response) -> {
        	String word = request.params(":word");
        	boolean found = mySearch.hash_search(word);
        	if (found) {
        		return "Word \"" + word + "\" was Found! :) ";	
        	}
        	else {
        		return "Word \"" + word + "\" was not Found! :( ";
        	}
        });
    }
    
}
