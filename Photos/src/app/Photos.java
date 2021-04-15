package app;

/**
*
* @authors Brian Albert and Elijah Ongoco
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.LoginController;

/**
 * Main application class to run the program
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class Photos extends Application{
	
	/**
	 * Method to start the scenes
	 */
	@Override
	public void start(Stage primaryStage) throws Exception{
		//get fxml from photos.view.login.fxml
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Login.fxml"));
		Pane root = loader.load();
		
		//initialize app and controller
		LoginController listController = loader.getController();
		listController.initializeListView(primaryStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photos");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * Main method to launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}