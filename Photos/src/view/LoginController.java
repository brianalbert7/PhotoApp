package view;

/**
*
* @authors Brian Albert and Elijah Ongoco
*/

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Collections;
import java.util.List;

import javafx.fxml.FXMLLoader;

import users.User;

/**
 * Class that acts as the controller for the Login.fxml
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class LoginController {
	@FXML Button submitbtn;
	@FXML TextField usernameField;
	
	/**
	 * List to keep track of users
	 */
	public static ArrayList<User> users = new ArrayList<User>();
	
	/**
	 * Stage to display on
	 */
	Stage primaryStage;
	
	/**
	 * Static variable to keep track of which user is logged in
	 */
	public static String currentUser = "";
	/**
	 * Variable to keep track of the directory data is stored in
	 */
	public static final String storeDir = "src\\users\\data";
	
	/**
	 * Method to initialize the list view for the login page
	 * @param mainStage The stage to display on
	 * @throws IOException
	 */
	public void initializeListView(Stage mainStage) throws IOException {
		primaryStage = mainStage;
		User.initializeUsers();
	}
	
	/**
	 * Method to submit a username into the application
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void submit(ActionEvent e) throws IOException {
		String username = usernameField.getText();
		currentUser = username;
		
		if (username.equals("admin")) {
			// Go to admin page
			FXMLLoader loader;
			Parent parent;
			loader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
			parent = (Parent) loader.load();
			AdminController controller = loader.<AdminController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			controller.start(users);
			stage.setScene(scene);
			stage.show();
		} else {
			if (User.checkUserExists(username)) {
				// If the username is valid, go to albumsviewer page
				FXMLLoader loader;
				Parent parent;
				loader = new FXMLLoader(getClass().getResource("/view/AlbumsViewer.fxml"));
				parent = (Parent) loader.load();
				AlbumViewerController controller = loader.<AlbumViewerController>getController();
				Scene scene = new Scene(parent);
				Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
				controller.start(users);
				stage.setScene(scene);
				stage.show();
			} else {
				// Show error if user doesn't exist
				showAlert(primaryStage, "User does not exist", "Could not log in");
			}
		}
		
	}
	
	/**
	 * Method to show a given alert 
	 * @param mainStage Stage to display on
	 * @param message Message to display
	 * @param header Header to display
	 */
	public static void showAlert(Stage mainStage, String message, String header) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(mainStage);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}	
	
	public void start(Stage stage) {
	}
	
}
