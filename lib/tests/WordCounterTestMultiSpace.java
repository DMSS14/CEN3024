//Author Name: Daniel McGee
//Date: 7/4/2021
//Program Name: WordCounterTestMultiSpace
//Purpose: A test of the WordCounter. Ensures that words are still counted correctly when separated by more than one whitespace character and by various types of whitespace characters.
package lib.tests;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import javafx.util.Pair;
/**
 * A test of the WordCounter. Ensures that words are still counted correctly when separated by more than one whitespace character and by various types of whitespace characters.
 */
class WordCounterTestMultiSpace extends WordCounterTestBasicMcGee {

	protected String getData() {
		return "word1            word1\nword2 \n\n\n     \t\t  word1\tword1";
	}
	protected void testList(List<Pair<String, Integer>> r) {
		Assertions.assertEquals(r.size(),2);
		Assertions.assertTrue(r.contains(new Pair<String, Integer>("word1",4)));
		Assertions.assertTrue(r.contains(new Pair<String, Integer>("word2",1)));
	}
}
