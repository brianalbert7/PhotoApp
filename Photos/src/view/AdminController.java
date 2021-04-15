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
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import users.Admin;
import users.User;

/**
 * Class that acts as the controller for the Admin.fxml
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class AdminController {
	@FXML Button addUser;
	@FXML Button deleteUser;
	@FXML Button quit;
	@FXML Button logout;
	@FXML TextField newUser;
	@FXML ListView<String> userList;
	
	/**
	 * Observable list to use for display
	 */
	private ObservableList<String> obsList;
	/**
	 * ArrayList of the users
	 */
	static List<String> stringUsers = new ArrayList<String>();
	
	/**
	 * Stage to display on
	 */
	Stage primaryStage;
	
	/**
	 * Method to initialize the listView for the users
	 * @param mainStage The stage to display
	 * @throws IOException
	 */
	public void initializeListView(Stage mainStage) throws IOException {
		primaryStage = mainStage;
		
		stringUsers = Admin.listUsers();
		obsList = FXCollections.observableArrayList(Admin.listUsers());
		userList.setItems(obsList);
		
		userList.getSelectionModel().select(0);
	}
	
	/**
	 * Method to add a new user
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void add(ActionEvent e) throws IOException {
		String user = newUser.getText();
		
		// Check if a user with that name already exists
		if (!stringUsers.isEmpty()) {
			if (User.checkUserExists(user)) {
				showAlert(primaryStage, "User already exists", "Add error");
				return;
			}
		}
		
		// Check if entered username is blank
		if (user.isEmpty()) {
			showAlert(primaryStage, "Can't create blank user", "Add error");
			return;
		}
		
		Admin.createUser(user);
		
		//update the listview
		stringUsers = Admin.listUsers();
		obsList = FXCollections.observableArrayList(stringUsers);
		userList.setItems(obsList);
	}
	
	/**
	 * Method to delete a user
	 * @param e ActionEvent to recognize a button press
	 * @throws IOException
	 */
	public void delete(ActionEvent e) throws IOException {
		// Get index of selected song
		int index = userList.getSelectionModel().getSelectedIndex();
		//System.out.println(index);
		String selectedUser = stringUsers.get(index);
		//System.out.println(selectedUser);
		
		if(index == -1) {
			//showAlert(primaryStage, "No song selected", "Delete error");
			return;
		}
		
		Admin.deleteUser(selectedUser);
	
		//update the listview
		stringUsers = Admin.listUsers();
		obsList = FXCollections.observableArrayList(stringUsers);
		userList.setItems(obsList);
	}
	
	/**
	 * Method to load in the user data on press
	 * @param e ActionEvent to recognize button press
	 */
	public void load(ActionEvent e) {
		//update the listview
		stringUsers = Admin.listUsers();
		obsList = FXCollections.observableArrayList(stringUsers);
		userList.setItems(obsList);
	}
	
	/**
	 * Method to quit the application
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void quit(ActionEvent e) throws IOException {
		for(int i = 0; i < LoginController.users.size(); i++){
			  if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
			    User.writeState(LoginController.users.get(i));
			  }
		}
		
		Platform.exit();
	}
	
	/**
	 * Method to log out of current user
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void logout(ActionEvent e) throws IOException {
		for(int i = 0; i < LoginController.users.size(); i++){
			  if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
			    User.writeState(LoginController.users.get(i));
			  }
		}
		
		LoginController.currentUser = "";
		
		FXMLLoader loader;
		Parent parent;
		loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
		parent = (Parent) loader.load();
		LoginController controller = loader.<LoginController>getController();
		Scene scene = new Scene(parent);
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		controller.start(null);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Method to show alert given message
	 * @param mainStage Stage to show on
	 * @param message Message to display
	 * @param header Header to displayt
	 */
	public static void showAlert(Stage mainStage, String message, String header) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(mainStage);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void start(ArrayList<User> users) {
	}
}
