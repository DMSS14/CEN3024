//Author Name: Daniel McGee
//Date: 7/4/2021
//Program Name: WordCounterTestPunctuation
//Purpose: A test of the WordCounter. Ensures that the word counter can handle various types of punctuation correctly. 
package lib.tests;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import javafx.util.Pair;

/**
 * A test of the WordCounter. Ensures that the word counter can handle various types of punctuation correctly. 
 */
class WordCounterTestPunctuation extends WordCounterTestBasicMcGee {

	protected String getData() {
		return "word1!!!word1? \"word2 word3, word1, word4, word4!\" word1't?";
	}
	protected void testList(List<Pair<String, Integer>> r) {
		Assertions.assertEquals(r.size(),5);
		Assertions.assertTrue(r.contains(new Pair<String, Integer>("word1",3)));
		Assertions.assertTrue(r.contains(new Pair<String, Integer>("word2",1)));
		Assertions.assertTrue(r.contains(new Pair<String, Integer>("word3",1)));
		Assertions.assertTrue(r.contains(new Pair<String, Integer>("word4",2)));
		Assertions.assertTrue(r.contains(new Pair<String, Integer>("word1't",1)));
	}
}
