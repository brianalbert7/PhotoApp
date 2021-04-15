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
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import users.Admin;
import users.User;
import view.LoginController;

/**
 * Class that represents the controller for the AlbumsViewer.fxml
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class AlbumViewerController {
	@FXML Button deleteAlbum;
	@FXML Button createAlbum;
	@FXML Button quit;
	@FXML Button logout;
	@FXML Button renameAlbum;
	@FXML Button searchPhotos;
	@FXML Button openAlbum;
	@FXML ListView<String> albumList;
	@FXML TextField newName;
	@FXML Label nameLabel;
	@FXML Label numberLabel;
	@FXML Label dateLabel;
	
	/**
	 * Observable list for updating the display
	 */
	private ObservableList<String> obsList;
	
	/**
	 * List of names of the albums
	 */
	static List<String> albumStrings = new ArrayList<String>();
	
	/**
	 * Stage to dispaly on
	 */
	Stage primaryStage;
	
	/**
	 * Variable to keep track of current album
	 */
	public static String currentAlbum = "";
	
	/**
	 * Method to initalize the list view for the albums
	 * @param mainStage
	 * @throws IOException
	 */
	public void initializeListView(Stage mainStage) throws IOException {
		primaryStage = mainStage;
		
		albumStrings = User.listAlbums(LoginController.currentUser);
		obsList = FXCollections.observableArrayList(albumStrings);
		albumList.setItems(obsList);
		
		//select first album if it exists
		if(albumStrings.size() > 0) {
			albumList.getSelectionModel().select(0);
			
			nameLabel.setText(User.getAlbumName(LoginController.currentUser, 0));
			numberLabel.setText(String.valueOf(User.getAlbumSize(LoginController.currentUser, 0)));
			Calendar date = User.getAlbumDate(LoginController.currentUser, 0);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			dateLabel.setText(formatter.format(date.getTime()));
		}
	}
	
	/**
	 * Method to create a new album for this user
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void create(ActionEvent e) throws IOException {
		albumStrings = User.listAlbums(LoginController.currentUser);
		String newAlbumName = newName.getText();
		
		// Check if an album with that name already exists
		if (albumStrings.contains(newAlbumName)) {
			showAlert(primaryStage, "Album already exists", "Create error");
			return;
		}
		
		// Check if entered album name is blank
		if (newAlbumName.isEmpty()) {
			showAlert(primaryStage, "Can't create blank album", "Create error");
			return;
		}
		
		User.addAlbum(newAlbumName, LoginController.currentUser);
		
		//update the listview
		albumStrings = User.listAlbums(LoginController.currentUser);
		obsList = FXCollections.observableArrayList(albumStrings);
		albumList.setItems(obsList);
		
		for(int i = 0; i < LoginController.users.size(); i++){
			  if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
			    User.writeState(LoginController.users.get(i));
			  }
		}
		
		int index = albumStrings.indexOf(newAlbumName);
		albumList.getSelectionModel().select(index);
		updateDetails();
	}
	
	/**
	 * Method to delete an album
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void delete(ActionEvent e) throws IOException {
		// Get index of selected song
		int index = albumList.getSelectionModel().getSelectedIndex();
		//System.out.println(index);
		
		if(index == -1) {
			showAlert(primaryStage, "No album selected", "Delete error");
			return;
		}
		
		String selectedAlbum = albumStrings.get(index);
		System.out.println(selectedAlbum);
		
		if(index == -1) {
			//showAlert(primaryStage, "No song selected", "Delete error");
			return;
		}
		
		User.removeAlbum(selectedAlbum, LoginController.currentUser);
	
		//update the listview
		albumStrings = User.listAlbums(LoginController.currentUser);
		obsList = FXCollections.observableArrayList(albumStrings);
		albumList.setItems(obsList);
		
		for(int i = 0; i < LoginController.users.size(); i++){
			  if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
			    User.writeState(LoginController.users.get(i));
			  }
		}
		
		if  (albumStrings.size() == 0) {
			nameLabel.setText("");
			numberLabel.setText("");
			dateLabel.setText("");
		}
		
		if (index > 0) {
			albumList.getSelectionModel().select(index-1);
			updateDetails();
		} else if (index == 0) {
			albumList.getSelectionModel().select(index);
			updateDetails();
		}
		
	}
	
	/**
	 * Method to rename an album
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void rename(ActionEvent e) throws IOException {
		// Get index of selected song
		int index = albumList.getSelectionModel().getSelectedIndex();
		//System.out.println(index);
		
		if(index == -1) {
			showAlert(primaryStage, "No album selected", "Rename error");
			return;
		}
		
		String selectedAlbum = albumStrings.get(index);
		//System.out.println(selectedAlbum);
		
		String newAlbumName = newName.getText();
		
		// Check if an album with that name already exists
		if (albumStrings.contains(newAlbumName)) {
			showAlert(primaryStage, "Album already exists", "Rename error");
			return;
		}
		
		// Check if entered album is blank
		if (newAlbumName.isEmpty()) {
			showAlert(primaryStage, "Can't create blank album", "Rename error");
			return;
		}
		
		User.changeAlbumName(selectedAlbum, newAlbumName, LoginController.currentUser);
	
		//update the listview
		albumStrings = User.listAlbums(LoginController.currentUser);
		obsList = FXCollections.observableArrayList(albumStrings);
		albumList.setItems(obsList);
		
		for(int i = 0; i < LoginController.users.size(); i++){
			  if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
			    User.writeState(LoginController.users.get(i));
			  }
		}
		
		int index1 = albumStrings.indexOf(newAlbumName);
		albumList.getSelectionModel().select(index1);
		updateDetails();
	}
	
	/**
	 * Method to enter the search stage
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void search(ActionEvent e) throws IOException {
		FXMLLoader loader;
		Parent parent;
		loader = new FXMLLoader(getClass().getResource("/view/SearchPage.fxml"));
		parent = (Parent) loader.load();
		SearchPageController controller = loader.<SearchPageController>getController();
		Scene scene = new Scene(parent);
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		//controller.start(null);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Method to open an album and enter the InsideAlbum stage
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void open(ActionEvent e) throws IOException {
		int index = albumList.getSelectionModel().getSelectedIndex();
		
		if(index == -1) {
			showAlert(primaryStage, "No album selected", "Open error");
			return;
		}
		
		String selectedAlbum = albumStrings.get(index);
		
		currentAlbum = selectedAlbum;
		
		FXMLLoader loader;
		Parent parent;
		loader = new FXMLLoader(getClass().getResource("/view/InsideAlbum.fxml"));
		parent = (Parent) loader.load();
		InsideAlbumController controller = loader.<InsideAlbumController>getController();
		Scene scene = new Scene(parent);
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		//controller.start(null);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Method to load in the albums
	 * @param e ActionEvent to recognize button press
	 */
	public void load(ActionEvent e) {
		//update the listview
		albumStrings = User.listAlbums(LoginController.currentUser);
		obsList = FXCollections.observableArrayList(albumStrings);
		albumList.setItems(obsList);
	}
	
	/**
	 * Method to quit out of the application
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
	 * Method to log out of the current user
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
	 * Method to show an alert based on given message
	 * @param mainStage Stage to show alert on
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
	
	/**
	 * Method to update the details of the albums list
	 */
	// Update the details of the song when the list is changed
	public void updateDetails(){
		// Get index of selected song
		int index = albumList.getSelectionModel().getSelectedIndex();
		//if there's nothing in the list to select return to not have exception
		if(index < 0) return;
		
		// Update details
		User current = null;
		nameLabel.setText(User.getAlbumName(LoginController.currentUser, index));
		numberLabel.setText(String.valueOf(User.getAlbumSize(LoginController.currentUser, index)));
		if (User.getAlbumSize(LoginController.currentUser, index) > 0) {
			for(int i = 0; i < LoginController.users.size(); i++) {
				if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
					current = LoginController.users.get(i);
				}
			}
			int last = current.photos.size()-1;
			Calendar date = current.photos.get(0).getAlbumDate();
			Calendar date1 = current.photos.get(0).getAlbumDate();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			String startDate = formatter.format(date.getTime());
			String endDate = formatter.format(date1.getTime());
			String dateRange = startDate + " - " + endDate;
			dateLabel.setText(dateRange);
		} else {
			dateLabel.setText("No Date Range");
		}
	}

	public void start(ArrayList<User> users) {
	}
}
