//Author Name: Daniel McGee
//Date: 7/4/2021
//Program Name: WordCounterTestOneWord
//Purpose: A basic test of the WordCounter. Ensures that the word counter can handle single word files.
package lib.tests;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import javafx.util.Pair;

/**
 * A basic test of the WordCounter. Ensures that the word counter can handle single word files.
 */
class WordCounterTestOneWord extends WordCounterTestBasicMcGee {

	protected String getData() {
		return "oneword";
	}
	protected void testList(List<Pair<String, Integer>> r) {
		Assertions.assertEquals(r.size(),1);
		Assertions.assertEquals(r.get(0),new Pair<String, Integer>("oneword",1));
	}
}
