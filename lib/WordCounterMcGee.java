//Author Name: Daniel McGee
//Date: 6/26/2021
//Program Name: WordCounterMcGee
//Purpose: Count the occurrences of words in a file 
package lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javafx.util.Pair;

/**
 * A libaray class containing a static method to count the number of times a word appears in a file.
 */
public class WordCounterMcGee {
	
	/**
	 * Counts the occurrences of each word in the file provided. A word is defined as a sequence of 1 or more word characters (\w and ') surrounded by non word characters (\W excluding ').
	 * @param f The file to read.
	 * @return a list of each word and its number of occurrences, wrapped in a pair.
	 * @throws FileNotFoundException if the file could not be opened
	 */
	public static List<Pair<String,Integer>> analyzeFile(File f) throws FileNotFoundException{
		HashMap<String, Integer> counters = new HashMap<>();
		
		try(Scanner s = new Scanner(new FileInputStream(f))){
			s.useDelimiter("[^\\w']+");
			while(s.hasNext()) {
				String value = s.next();
				if(value.startsWith("'")){
					value = value.substring(1);
				}
				if(value.endsWith("'")) {
					value = value.substring(0, value.length()-1);
				}
						
				incrementCounter(counters,value.toLowerCase());
			}
		}
		
		ArrayList<Pair<String,Integer>> returnList = new ArrayList<>(counters.size());
		for(Entry<String, Integer> entry: counters.entrySet()) {
			returnList.add(new Pair<>(entry.getKey(),entry.getValue()));
		}
		return returnList;
	}
	/**
	 * A helper method for maps used to keep track of the number of times their keys occurred. The value in the map at key will be incremented by 1, unless there is no
	 * value at key. In that case, the value of 1 will be inserted into the map using they key provided. 
	 * @param <A> The type of the key
	 * @param map A map holding the number of times each key has occurred
	 * @param key the key that has occurred another time
	 */
	static <A> void incrementCounter(Map<A,Integer> map, A key) {
		if(map.containsKey(key)) {
			map.put(key, map.get(key)+1);
		}
		else {
			map.put(key, 1);
		}
	}

}
