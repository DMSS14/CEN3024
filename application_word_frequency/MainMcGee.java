//Author Name: Daniel McGee
//Date: 6/26/2021
//Program Name: MainMcGee
//Purpose: Primary application class of the GUI
package application_word_frequency;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The main class of the application. Doesn't handle much of the UI, just sets the stage up and hands control off to ControllerMcGee.
 */
public class MainMcGee extends Application {
	/**
	 * A singleton instance of the application class.
	 */
	static MainMcGee application;
	/**
	 * A pointer to the main stage of the application.
	 */
	Stage mainStage; 
	
	/**
	 * Starts the application by loading the FXML file. This also creates a Controller for the FXML, which is where all the logic is done.
	 */
	@Override
	public void start(Stage primaryStage) {
		application = this;
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("MainMcGee.fxml"));		
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("applicationMcGee.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
