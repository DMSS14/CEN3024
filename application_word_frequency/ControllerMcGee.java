//Author Name: Daniel McGee
//Date: 6/26/2021
//Program Name: ControllerMcGee
//Purpose: Act as a JavaFX controller for the fxml and handle GUI interactions.
package application_word_frequency;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;
import lib.WordCounterMcGee;
/**
 * The backbone of the UI of the application. Is an fxml controller that uses various fxml methods and fields to implement the UI.
 */
public class ControllerMcGee implements javafx.fxml.Initializable {

	/**
	 * A reference to the primary ScrollPane. Defined in the fxml.
	 */
	@FXML
	private ScrollPane scroll;
	
	/**
	 * A reference to the GridPane that is used to display the data. Defined in the fxml.
	 */
	@FXML
	private GridPane dataGrid;
	
	/**
	 * Called when the user selects the save menu option. Pops up a save file dialog and then saves the word count information to that file. 
	 */
	@FXML
	private void saveToFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Word Count");
		
		ExtensionFilter text = new ExtensionFilter("Text File", "*.txt");
		ExtensionFilter all = new ExtensionFilter("Any File", "*");
		fileChooser.getExtensionFilters().add(text);
		fileChooser.getExtensionFilters().add(all);
		fileChooser.setSelectedExtensionFilter(text);
		
		File selected = fileChooser.showSaveDialog(MainMcGee.application.mainStage);
		
		try (PrintStream out = new PrintStream(selected)){
			for(Pair<String,Integer> pair: wordCountCache) {
				out.printf("%s\t%d\n",pair.getKey(),pair.getValue());
			}
			
		} 
		catch (IOException e) {
			e.printStackTrace();
			
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("An error occoured while writing the file: " + e.getLocalizedMessage());
			error.setTitle("Write Error");
			error.showAndWait();
		}
	}
	
	/**
	 * Called when the user selects the open menu option. Pops up an open file dialog and then passes the selected file to the word counter function.
	 */
	@FXML
	private void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		
		ExtensionFilter text = new ExtensionFilter("Text Files", "*.txt");
		ExtensionFilter all = new ExtensionFilter("All Files", "*");
		fileChooser.getExtensionFilters().add(text);
		fileChooser.getExtensionFilters().add(all);
		fileChooser.setSelectedExtensionFilter(text);
		
		File selected = fileChooser.showOpenDialog(MainMcGee.application.mainStage);
		if(selected == null) {
			return;
		}
		
		try {
			wordCountCache = WordCounterMcGee.analyzeFile(selected);
			
			resetRows();
			
			wordCountCache.sort((e1,e2)->{
				return e2.getValue().compareTo(e1.getValue());
			});
			
			for(Pair<String,Integer> pair: wordCountCache) {
				addRow(pair.getKey(),pair.getValue()+"",Pos.BASELINE_RIGHT, Pos.BASELINE_LEFT);
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("An error occoured while reading the file: " + e.getLocalizedMessage());
			error.setTitle("Read Error");
			error.showAndWait();
		}
	}
	

	/**
	 * Ends the program. Called by the fxml.
	 */
	@FXML
	private void shutdown() {
		 Platform.exit();
	     System.exit(0);
	}

	/**
	 * Initializes the controller. Called by the fxml.
	 */
	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	getScroll().setFitToWidth(true);
    	resetRows();
    }
	
	
	private int row = 0;
	
	private List<Pair<String, Integer>> wordCountCache = null;
	
	/**
	 * Resets the GridPane to just be the headers.
	 */
	public void resetRows() {
		getDataGrid().getChildren().clear();
		addRow("Word", "Count", Pos.BASELINE_CENTER, Pos.BASELINE_CENTER);
	}
	/**
	 * Adds a row to the grid. The strings will be added as labels that take up the entire cell and have their text aligned with the provided alignments. This allows
	 * us to create grid lines by giving the labels boarders. 
	 * @param word1
	 * @param word2
	 * @param alignment1
	 * @param alighnment2
	 */
	public void addRow(String word1, String word2, Pos alignment1, Pos alighnment2) {
		Label name = new Label(word1);
		Label amount = new Label(word2);
		
		getDataGrid().addRow(row++, name, amount);
		
		GridPane.setFillHeight(name, true);
		GridPane.setFillWidth(name, true);
		GridPane.setFillHeight(amount, true);
		GridPane.setFillWidth(amount, true);
		
		name.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		amount.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		name.getStyleClass().add("gridCell");
		amount.getStyleClass().add("gridCell");
		name.setAlignment(alignment1);
		amount.setAlignment(alighnment2);
	}
	
	ScrollPane getScroll() {
		return scroll;
	}
	GridPane getDataGrid() {
		return dataGrid;
	}

}
