package view;

/**
*
* @authors Brian Albert and Elijah Ongoco
*/

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import photos.Photo;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import users.Admin;
import users.User;

/**
 * Class that acts as the controller for SearchPage.fxml
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class SearchPageController {
	@FXML Button searchByDate;
	@FXML Button makeAlbum;
	@FXML Button quit;
	@FXML Button logout;
	@FXML Button searchByTag;
	@FXML ListView<String> imageList;
	@FXML DatePicker startDate;
	@FXML DatePicker endDate;
	@FXML TextField tagInput;
	@FXML TextField albumName;
	
	/**
	 * Observable list to update display with
	 */
	private ObservableList<String> obsList;
	
	/**
	 * List of album names to keep track of
	 */
	static List<String> albumStrings = new ArrayList<String>();
	
	/**
	 * Stage to display on
	 */
	Stage primaryStage;
	
	/**
	 * Method to initialize the list view for the search page
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void initializeListView(Stage mainStage) throws IOException {
		primaryStage = mainStage;
	}
	
	/**
	 * Method to search given date range
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void searchDate(ActionEvent e) throws IOException {
		LocalDate date1 = startDate.getValue();
		LocalDate date2 = endDate.getValue();
		
		if (date1 == null || date2 == null) {
			showAlert(primaryStage, "Both dates need to be selected", "Search error");
			return;
		}
		
		if (date1.isAfter(date2)) {
			showAlert(primaryStage, "Start Date needs to be before End Date", "Search error");
			return;
		}
		
		List<LocalDate> dateRange = getDatesBetween(date1, date2);
		
		User current = null;
		
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				current = LoginController.users.get(i);
			}
		}
		
		Date end = toDate(date2);
		Calendar calEnd = toCalendar(end);
		
		List<String> photoPaths = new ArrayList<String>();
		
		for(int i = 0; i < dateRange.size(); i++) {
            dateRange.get(i);
            LocalDate thisDate = dateRange.get(i);
            Date dateDate = toDate(thisDate);
            Calendar calDate = toCalendar(dateDate);
            calDate.set(Calendar.MILLISECOND,0);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			String date = formatter.format(calDate.getTime());
			String endDate = formatter.format(calEnd.getTime());
			//System.out.println(date);
            // Iterate through all albums and all pictures and if calDate = pictures date add it to the list
            for(int j = 0; j < current.photos.size(); j++) {
            	String photoDate = formatter.format(current.photos.get(j).getAlbumDate().getTime());
				if(photoDate.equals(date) || photoDate.equals(endDate)) {
					photoPaths.add(current.photos.get(j).getPath());
				}
            }
        }
		
		obsList = FXCollections.observableArrayList(photoPaths);
		imageList.setItems(obsList);
		
		imageList.setCellFactory(param -> new ListCell<String>() {
			private ImageView iv = new ImageView();
			@Override
			public void updateItem(String name, boolean empty) {
				super.updateItem(name, empty);
				//System.out.println(name);
				if(empty) {
					setText(null);
					setGraphic(null);
				}
				else {
					String filePath = "";
					for(int i = 0; i < LoginController.users.size(); i++) {
						if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
							for(int j = 0; j < LoginController.users.get(i).photos.size(); j++) {
								if(LoginController.users.get(i).photos.get(j).getPath().equals(name)) {
									filePath = name;
								}
							}
						}
					}
					
					File pic = new File(filePath);
					Image image = new Image(pic.toURI().toString());
					iv.setFitHeight(50);
					iv.setFitWidth(50);
					iv.setImage(image);
					setText(name);
					setGraphic(iv);
				}
			}
		});
	}
	
	/**
	 * Method to convert a LocalDate to a Date object
	 * @param dateToConvert LocalDate to convert
	 * @return
	 */
	// LocalDate to Date
	public static Date toDate(LocalDate dateToConvert) {
		   return Date.valueOf(dateToConvert);
	}
	
	/**
	 * Method to convert a Date to a Calendar object
	 * @param date Date object to convert
	 * @return
	 */
	// Date to Calendar
	public static Calendar toCalendar(Date date) {
		   Calendar calendar = Calendar.getInstance();
		   calendar.setTime(date);
		   return calendar;
	}
	
	/**
	 * Method to get a list of LocalDates between two LocalDates
	 * @param startDate Start of the range
	 * @param endDate End of the range
	 * @return List of LocalDates between startDate and endDate
	 */
	public static List<LocalDate> getDatesBetween(
	  LocalDate startDate, LocalDate endDate) {
	 
	    return startDate.datesUntil(endDate)
	      .collect(Collectors.toList());
	}
	
	public void makeAlbum(ActionEvent e) throws IOException {
        String newAlbum = albumName.getText();
        User current = null;
        for(int i = 0; i < LoginController.users.size(); i++) {
            if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
                current = LoginController.users.get(i);
                for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
                    if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(newAlbum)) {
                        showAlert(primaryStage, "Album already exists", "Make album error");
                    }
                }
            }
        }
        
        ArrayList<Photo> photos = new ArrayList<Photo>();
        Album toAdd = new Album(newAlbum);
        for(int i = 0; i < listOfPaths.size(); i++) {
            photos.add(new Photo(listOfPaths.get(i)));
        }
        
        for(int i = 0; i < photos.size(); i++) {
            toAdd.addPhoto(photos.get(i));
        }
        
        User.addAlbum(newAlbum, LoginController.currentUser);
        
        for(int i = 0; i < LoginController.users.size(); i++){
            if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
                User.writeState(LoginController.users.get(i));
            }
        }
    }
	
	/**
	 * Method to search given a tag
	 * @param e ActionEvent to recognize button press
	 * @throws IOException
	 */
	public void search(ActionEvent e) throws IOException {
		String tagValue = tagInput.getText();
		
		List<String> photoPaths = new ArrayList<String>();
		
		User current = null;
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				current = LoginController.users.get(i);
			}
		}
		
		if (tagValue.contains(" ")) {			
			String[] tags = tagValue.split(" ");
			
			if (tags.length > 3) {
				showAlert(primaryStage, "Invalid Input", "Search error");
				return;
			}
			
			String tag1 = tags[0];
			String junctive = tags[1];
			String tag2 = tags[2];
			
			ArrayList<Photo> photos = new ArrayList<Photo>();
			
			if (junctive.equalsIgnoreCase("and")) {
				for (int j = 0; j < current.photos.size(); j++) {
	            	if (current.photos.get(j).tags.contains(tag1) && current.photos.get(j).tags.contains(tag2)) {
	            		photoPaths.add(current.photos.get(j).getPath());
	            	}
				}
			} else if (junctive.equalsIgnoreCase("or")) {
				for (int j = 0; j < current.photos.size(); j++) {
	            	if (current.photos.get(j).tags.contains(tag1) || current.photos.get(j).tags.contains(tag2)) {
	            		photoPaths.add(current.photos.get(j).getPath());
	            	}
				}
			} else {
				showAlert(primaryStage, "Invalid Input", "Search error");
				return;
			}
			
		} else {
			for (int j = 0; j < current.photos.size(); j++) {
            	if (current.photos.get(j).tags.contains(tagValue)) {
            		photoPaths.add(current.photos.get(j).getPath());
            	}
			}
		}
		
		obsList = FXCollections.observableArrayList(photoPaths);
		imageList.setItems(obsList);
		
		imageList.setCellFactory(param -> new ListCell<String>() {
			private ImageView iv = new ImageView();
			@Override
			public void updateItem(String name, boolean empty) {
				super.updateItem(name, empty);
				//System.out.println(name);
				if(empty) {
					setText(null);
					setGraphic(null);
				}
				else {
					String filePath = "";
					for(int i = 0; i < LoginController.users.size(); i++) {
						if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
							for(int j = 0; j < LoginController.users.get(i).photos.size(); j++) {
								if(LoginController.users.get(i).photos.get(j).getPath().equals(name)) {
									filePath = name;
								}
							}
						}
					}
					
					File pic = new File(filePath);
					Image image = new Image(pic.toURI().toString());
					iv.setFitHeight(50);
					iv.setFitWidth(50);
					iv.setImage(image);
					setText(name);
					setGraphic(iv);
				}
			}
		});
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
