//Author Name: Daniel McGee
//Date: 7/4/2021
//Program Name: WordCounterTestMultiWord
//Purpose: A basic test of the WordCounter. Ensures the word counter can handle a simple string of words separated by a single space each.
package lib.tests;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import javafx.util.Pair;
/**
 * A basic test of the WordCounter. Ensures the word counter can handle a simple string of words separated by a single space each.
 */
class WordCounterTestMultiWord extends WordCounterTestBasicMcGee {

	protected String getData() {
		return "word1 word1 word2 word1 word2 word3";
	}
	protected void testList(List<Pair<String, Integer>> r) {
		Assertions.assertEquals(r.size(),3);
		Assertions.assertTrue(r.contains(new Pair<String, Integer>("word1",3)));
		Assertions.assertTrue(r.contains(new Pair<String, Integer>("word2",2)));
		Assertions.assertTrue(r.contains(new Pair<String, Integer>("word3",1)));
	}
}
