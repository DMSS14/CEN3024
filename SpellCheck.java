import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SpellCheck {

	public static List<String> spellCheck(List<String> words, List<String> dictionary){
		ArrayList<String> wrongWords = new ArrayList<>();
		for(String word:words) {
			
			if(!dictionary.contains(word)) {
				wrongWords.add(word);
			}
		}
		return wrongWords;
	}
	public static void main(String[] args) throws FileNotFoundException {
		//Open both files before we start processing so that if the dictionary is omitted or points to an invalid file, we catch it before we begin reading.
		File wordsFile = new File(args[0]);
		File dictionaryFile = new File(args[1]);
		
		Scanner wordsScanner = new Scanner(new FileInputStream(wordsFile));
		Scanner dictionaryScanner = new Scanner(new FileInputStream(dictionaryFile));
		
		//read both files into a data structure
		ArrayList<String> wordsList = new ArrayList<>();
		while(wordsScanner.hasNextLine()) {
			wordsList.add(wordsScanner.nextLine().toLowerCase().trim());
		}
		wordsScanner.close();
		
		//we should really be using a hash set because its far more efficient
		ArrayList<String> dictionarySet = new ArrayList<>();
		while(dictionaryScanner.hasNextLine()) {
			dictionarySet.add(dictionaryScanner.nextLine().toLowerCase().trim());
		}
		dictionaryScanner.close();
		
		List<String> wrongWords = spellCheck(wordsList,dictionarySet);
		
		for(String word:wrongWords) {
			System.out.printf("%s was not in the dictionary!\n", word);
		}
	}

}
