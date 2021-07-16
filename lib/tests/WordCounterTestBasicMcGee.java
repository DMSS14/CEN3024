//Author Name: Daniel McGee
//Date: 7/4/2021
//Program Name: WordCounterTestBasicMcGee
//Purpose: A basic test of the WordCounter using the simplest case: an empty file. Has 2 methods that can be overridden to change the test data and test case.
//This allows the boilerplate logic of the test to be reused by multiple files while changing the actual data and logic.

package lib.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.util.Pair;
import lib.WordCounterMcGee;

/**
 * A basic test of the WordCounter using the simplest case: an empty file. Has 2 methods that can be overridden to change the test data and test case.
 * This allows the boilerplate logic of the test to be reused by multiple files while changing the actual data and logic.
 */
class WordCounterTestBasicMcGee {
	
	private File testData; 
	@BeforeEach
	final void createFile() throws IOException
    {
		testData = File.createTempFile("testData", ".tmp");
		testData.deleteOnExit();
		PrintWriter p = new PrintWriter(testData);
		p.print(getData());
		p.close();
    }
	@Test
	final void test() {
		try {
			List<Pair<String, Integer>> result = WordCounterMcGee.analyzeFile(testData);
			testList(result);
		} catch (FileNotFoundException e) {
			Assertions.fail(e);
		}
	}
	@AfterEach
    final void deleteFile() throws IOException
    {
		testData.delete();
		System.out.println(testData);
    }
	
	
	protected void testList(List<Pair<String, Integer>> r) {
		Assertions.assertTrue(r.isEmpty());
	}
	protected String getData() {
		return "";
	}
}
