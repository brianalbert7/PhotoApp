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
import photos.Photo;

/**
 * Class that acts as the controller for TagsPage.fxml
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class TagsPageController {
	@FXML Button addTag;
	@FXML Button deleteTag;
	@FXML Button quit;
	@FXML Button logout;
	@FXML ListView<String> tagList;
	@FXML TextField newTag;
	@FXML TextField newValue;
	
	/**
	 * Observable list to update display with
	 */
	private ObservableList<String> obsList;
	
	/**
	 * ArrayList to keep track of the strings of the tags
	 */
	static List<String> tagStrings = new ArrayList<String>();
	
	/**
	 * Field to keep track of which photo is currently selected
	 */
	Photo selectedPhoto = InsideAlbumController.currentPhoto;
	
	/**
	 * Stage to display on
	 */
	Stage primaryStage;
	
	/**
	 * Method to initialize the lsit view for the tags page
	 * @param mainStage
	 * @throws IOException
	 */
	public void initializeListView(Stage mainStage) throws IOException {
		primaryStage = mainStage;
		
		tagStrings = selectedPhoto.tags;
		obsList = FXCollections.observableArrayList(tagStrings);
		tagList.setItems(obsList);
	}
	
	/**
	 * Method to add a new tag to a photo
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void add(ActionEvent e) throws IOException {
		tagStrings = selectedPhoto.tags;
		
		String tag = "";
		String value = "";
		tag = newTag.getText();
		value = newValue.getText();
		
		tag = tag.replaceAll("\\s", "");
		value = value.replaceAll("\\s", "");
		
		String tagValue = tag + "=" + value;
		if (tagStrings != null) {
			if (tagStrings.contains(tagValue)) {
				showAlert(primaryStage, "Tag and Value already exist", "Add error");
				return;
			}
		}
		
		if (tag == "" || value == "") {
			showAlert(primaryStage, "Tag and Value both need values", "Add error");
			return;
		}
		
		selectedPhoto.tags.add(tagValue);
		
		//update the listview
		tagStrings = selectedPhoto.tags;
		obsList = FXCollections.observableArrayList(tagStrings);
		tagList.setItems(obsList);
		
		for(int i = 0; i < LoginController.users.size(); i++){
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				User.writeState(LoginController.users.get(i));
			}
		}
	}
	
	/**
	 * Method to delete a tag
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void delete(ActionEvent e) throws IOException {
		tagStrings = selectedPhoto.tags;
		
		// Get index of selected song
		int index = tagList.getSelectionModel().getSelectedIndex();
		//System.out.println(index);
		
		if(index == -1) {
			showAlert(primaryStage, "No album selected", "Delete error");
			return;
		}
		
		String selectedTag = tagStrings.get(index);
	
		selectedPhoto.tags.remove(index);
		tagStrings = selectedPhoto.tags;
		
		//update the listview
		obsList = FXCollections.observableArrayList(tagStrings);
		tagList.setItems(obsList);
		
		for(int i = 0; i < LoginController.users.size(); i++){
			  if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
			    User.writeState(LoginController.users.get(i));
			  }
		}
		
		if (index > 0) {
			tagList.getSelectionModel().select(index-1);
		} else if (index == 0) {
			tagList.getSelectionModel().select(index);
		}
		
		for(int i = 0; i < LoginController.users.size(); i++){
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				User.writeState(LoginController.users.get(i));
			}
		}
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
	 * Method to update the tags 
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void load(ActionEvent e) throws IOException{
		updateTags(primaryStage);
	}
	
	/**
	 * Method to update the displayed tags
	 * @param primaryStage2 Another stage to display on
	 */
	private void updateTags(Stage primaryStage2) {
		primaryStage = primaryStage2;
		
		tagStrings = selectedPhoto.tags;
		obsList = FXCollections.observableArrayList(tagStrings);
		tagList.setItems(obsList);
	}

	/**
	 * Method to show a given alert message
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

	public void start(ArrayList<User> users) {
	}
}
