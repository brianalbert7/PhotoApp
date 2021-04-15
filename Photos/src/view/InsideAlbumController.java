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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import users.Admin;
import users.User;

/**
 * Class that acts as the controller for InsideAlbum.fxml
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class InsideAlbumController {
	@FXML Button deletePicture;
	@FXML Button addPicture;
	@FXML Button quit;
	@FXML Button logout;
	@FXML Button copyToAlbum;
	@FXML Button editTag;
	@FXML Button moveToAlbum;
	@FXML Button submitCaption;
	@FXML Button previous;
	@FXML Button next;
	@FXML ListView<String> imageList;
	@FXML TextField albumToCopyTo;
	@FXML TextField caption;
	@FXML Label captionLabel;
	@FXML Label dateLabel;
	@FXML Label tagLabel;
	@FXML Label albumName;
	@FXML ImageView showImage;
	@FXML ListView<String> tagsList;
	
	/**
	 * Observable list to update image display with
	 */
	private ObservableList<String> obsList;
	
	/**
	 * Observable list to update tags display with
	 */
	private ObservableList<String> tagObsList;
	
	/**
	 * List of album names to keep track of
	 */
	static List<String> albumStrings = new ArrayList<String>();
	
	/**
	 * List of tag names to keep track of
	 */
	static List<String> tagStrings = new ArrayList<String>();
	
	/**
	 * List of photos to keep track of
	 */
	public static ArrayList<String> currentPhotos = new ArrayList<String>();
	
	/**
	 * Stage to display
	 */
	Stage primaryStage;
	
	/**
	 * String holding the name of the album the user has opened
	 */
	String album = AlbumViewerController.currentAlbum;
	
	/**
	 * The photo the user has selected
	 */
	public static Photo currentPhoto;
	
	/**
	 * Method to initialize the list view for the search page
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void initializeListView(Stage mainStage) throws IOException {
		primaryStage = mainStage;
		
		albumName.setText(album);
	}
	
	/**
	 * Method to display the pictures onto the list view
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void updatePictures(Stage mainStage) throws IOException{
		albumName.setText(album);
		InsideAlbumController.currentPhotos.clear();
		ArrayList<String> photoTexts = new ArrayList<String>();
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
//				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
//					if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(AlbumViewerController.currentAlbum)) {
//						for(int r = 0; r < LoginController.users.get(i).albums.get(j).photos.size(); r++) {
//							String toDisp = LoginController.users.get(i).albums.get(j).photos.get(r).getCaption() + " |-| " +
//									LoginController.users.get(i).albums.get(j).photos.get(r).getPath();
//							photoTexts.add(toDisp);
//							System.out.println(toDisp);
//						}
//					}
//				}
				for(int j = 0; j < LoginController.users.get(i).photos.size(); j++) {
					String toDisp = LoginController.users.get(i).photos.get(j).getCaption() + " |-| " +
							LoginController.users.get(i).photos.get(j).getPath();
					for(int r = 0; r < LoginController.users.get(i).photos.get(j).containedIn.size(); r++) {
						if(LoginController.users.get(i).photos.get(j).containedIn.get(r).equals(AlbumViewerController.currentAlbum)) {
							photoTexts.add(toDisp);
							currentPhotos.add(toDisp);
						}
					}
				}
			}
		}
		
		obsList = FXCollections.observableArrayList(photoTexts);
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
//							for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
//								if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(AlbumViewerController.currentAlbum)) {
//									for(int r = 0; r < LoginController.users.get(i).albums.get(j).photos.size(); r++) {
//										String toCompareWith = name.substring(name.lastIndexOf("|-|")+4, name.length());
//										if(LoginController.users.get(i).albums.get(j).photos.get(r).getPath().equals(toCompareWith)) {
//											filePath = (LoginController.users.get(i).albums.get(j).photos.get(r).getPath());
//											System.out.println(filePath);
//										}
//									}
//								}
//							}
							for(int j = 0; j < LoginController.users.get(i).photos.size(); j++) {
								String toCompareWith = name.substring(name.lastIndexOf("|-|")+4, name.length());
								if(LoginController.users.get(i).photos.get(j).getPath().equals(toCompareWith)) {
									filePath = toCompareWith;
									//System.out.println(filePath);
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
	 * Method to refresh the scene
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void load(ActionEvent e) throws IOException{
		albumName.setText(album);
		updatePictures(primaryStage);
	}
	
	/**
	 * Method to delete a picture from the current album
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void delete(ActionEvent e) throws IOException {
		albumName.setText(album);
		int index = imageList.getSelectionModel().getSelectedIndex();
		String imagePath = InsideAlbumController.currentPhotos.get(index).substring(
				InsideAlbumController.currentPhotos.get(index).lastIndexOf("|-|")+4, 
				InsideAlbumController.currentPhotos.get(index).length()
				);
		
		//remove it from the current album, reflect that it is no longer in that album.
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
					if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(AlbumViewerController.currentAlbum)) {
						for(int r = 0; r < LoginController.users.get(i).albums.get(j).photos.size(); r++) {
							if(LoginController.users.get(i).albums.get(j).photos.get(r).getPath().equals(imagePath)) {
								LoginController.users.get(i).albums.get(j).photos.remove(r);
							}
						}
					}
				}
			}
		}
		//also update the photo so it doesnt hold that album in containedIn either
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				for(int j = 0; j < LoginController.users.get(i).photos.size(); j++) {
					if(LoginController.users.get(i).photos.get(j).getPath().equals(imagePath)) {
						for(int r = 0; r < LoginController.users.get(i).photos.get(j).containedIn.size(); r++) {
							if(LoginController.users.get(i).photos.get(j).containedIn.get(r).equals(AlbumViewerController.currentAlbum)) {
								LoginController.users.get(i).photos.get(j).containedIn.remove(r);
							}
						}
					}
				}
			}
		}
		
		for(int i = 0; i < LoginController.users.size(); i++){
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				User.writeState(LoginController.users.get(i));
			}
		}
		
		updatePictures(primaryStage);
		
	}
	
	/**
	 * Method to add a picture to the album
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void add(ActionEvent e) throws IOException {
		albumName.setText(album);
		FileChooser fc = new FileChooser();
		FileChooser.ExtensionFilter fcFilter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif");
		fc.getExtensionFilters().add(fcFilter);
		fc.setTitle("Choose image to upload");
		File selectedFile = fc.showOpenDialog(primaryStage);
		//System.out.println(selectedFile.getAbsolutePath());
		if (selectedFile == null) {
			return;
		}
		String filePath = selectedFile.getAbsolutePath();
		
		//add to their list of photos if not present, otherwise just add the album to the photo
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				User current = LoginController.users.get(i);
				//check if photo exists in current album already, if so then show error and end
				for(int j = 0; j < current.albums.size(); j++) {
					if(current.albums.get(j).getAlbumName().equals(AlbumViewerController.currentAlbum)) {
						for(int r = 0; r < current.albums.get(j).photos.size(); r++) {
							if(current.albums.get(j).photos.get(r).getPath().equals(filePath)) {
								showAlert(primaryStage, "Picture already exists in album", "Picture duplicate");
								return;
							}
						}
					}
				}
				
				//see if photo exists in user's photos arraylist.
				//if it does, add that photo to the album's arraylist of photos directly
				boolean photoExists = false;
				if (current.photos != null) {
					for(int j = 0; j < current.photos.size(); j++) {
						if(current.photos.get(j).getPath().equals(filePath)) {
							photoExists = true;
							//find album and add it in
							for(int r = 0; r < current.albums.size(); r++) {
								if(current.albums.get(r).getAlbumName().equals(AlbumViewerController.currentAlbum)) {
									current.albums.get(r).addPhoto(current.photos.get(j));
								}
							}
							current.photos.get(j).addAlbum(AlbumViewerController.currentAlbum);
							current.photos.get(j).setAlbumDate();
						}
					}
				}
				
				//otherwise, make new photo, add album name to it.  then, add photo to album
				
				if(!photoExists) {
					Photo toAdd = new Photo (filePath);
//					System.out.println(current.photos.size());
//					System.out.println(toAdd.getPath());
					toAdd.addAlbum(AlbumViewerController.currentAlbum);
					toAdd.setAlbumDate();
					
					current.photos.add(toAdd);
//					current.photos.get(0).addAlbum(AlbumViewerController.currentAlbum);
//					current.photos.get(0).setAlbumDate();
					
					for(int j = 0; j < current.albums.size(); j++) {
						if(current.albums.get(j).getAlbumName().equals(AlbumViewerController.currentAlbum)) {
							current.albums.get(j).addPhoto(toAdd);
						}
					}
				}
				
			}
			
			
		}
		
		for(int i = 0; i < LoginController.users.size(); i++){
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				User.writeState(LoginController.users.get(i));
			}
		}
		
		updatePictures(primaryStage);
		
	}
	
	/**
	 * Method to copy the selected picture to another album
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void copy(ActionEvent e) throws IOException {
		String destination = albumToCopyTo.getText();
		
		boolean exists = false;
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
					if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(destination)) {
						exists = true;
					}
				}
			}
		}
		
		if (!exists) {
			showAlert(primaryStage, "Album does not exist", "Copy error");
			return;
		}
		
		int index = imageList.getSelectionModel().getSelectedIndex();
		String imagePath = InsideAlbumController.currentPhotos.get(index).substring(
				InsideAlbumController.currentPhotos.get(index).lastIndexOf("|-|")+4, 
				InsideAlbumController.currentPhotos.get(index).length()
				);
		//System.out.println(imagePath + " " + destination);
		
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
					if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(destination)) {
						for(int r = 0; r < LoginController.users.get(i).albums.get(j).photos.size(); r++) {
							if(LoginController.users.get(i).albums.get(j).photos.get(r).getPath().equals(imagePath)) {
								showAlert(primaryStage, "Photo already exists here", "Copy error");
								return;
							}
						}
						
						//didnt exist, add it there
						Photo p = null;
						for(int r = 0; r < LoginController.users.get(i).photos.size(); r++) {
							if(LoginController.users.get(i).photos.get(r).getPath().equals(imagePath)) {
								p = LoginController.users.get(i).photos.get(r);
							}
						}
						p.addAlbum(destination);
						LoginController.users.get(i).albums.get(j).addPhoto(p);
					}
				}
			}
		}
		
		for(int i = 0; i < LoginController.users.size(); i++){
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				User.writeState(LoginController.users.get(i));
			}
		}
		
		updatePictures(primaryStage);
	}
	
	/**
	 * Method to move the selected picture to another album
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void move(ActionEvent e) throws IOException {
		String destination = albumToCopyTo.getText();
		
		boolean exists = false;
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
					if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(destination)) {
						exists = true;
					}
				}
			}
		}
		
		if (!exists) {
			showAlert(primaryStage, "Album does not exist", "Move error");
			return;
		}
		
		int index = imageList.getSelectionModel().getSelectedIndex();
		String imagePath = InsideAlbumController.currentPhotos.get(index).substring(
				InsideAlbumController.currentPhotos.get(index).lastIndexOf("|-|")+4, 
				InsideAlbumController.currentPhotos.get(index).length()
				);
		//System.out.println(imagePath + " " + destination);
		
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
					if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(destination)) {
						for(int r = 0; r < LoginController.users.get(i).albums.get(j).photos.size(); r++) {
							if(LoginController.users.get(i).albums.get(j).photos.get(r).getPath().equals(imagePath)) {
								showAlert(primaryStage, "Photo already exists here", "Move error");
								return;
							}
						}
						
						//didnt exist, add it there
						Photo p = null;
						for(int r = 0; r < LoginController.users.get(i).photos.size(); r++) {
							if(LoginController.users.get(i).photos.get(r).getPath().equals(imagePath)) {
								p = LoginController.users.get(i).photos.get(r);
							}
						}
						p.addAlbum(destination);
						LoginController.users.get(i).albums.get(j).addPhoto(p);
					}
				}
			}
		}
		
		//remove it from the current album, reflect that it is no longer in that album.
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
					if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(AlbumViewerController.currentAlbum)) {
						for(int r = 0; r < LoginController.users.get(i).albums.get(j).photos.size(); r++) {
							if(LoginController.users.get(i).albums.get(j).photos.get(r).getPath().equals(imagePath)) {
								LoginController.users.get(i).albums.get(j).photos.remove(r);
							}
						}
					}
				}
			}
		}
		//also update the photo so it doesnt hold that album in containedIn either
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				for(int j = 0; j < LoginController.users.get(i).photos.size(); j++) {
					if(LoginController.users.get(i).photos.get(j).getPath().equals(imagePath)) {
						for(int r = 0; r < LoginController.users.get(i).photos.get(j).containedIn.size(); r++) {
							if(LoginController.users.get(i).photos.get(j).containedIn.get(r).equals(AlbumViewerController.currentAlbum)) {
								LoginController.users.get(i).photos.get(j).containedIn.remove(r);
							}
						}
					}
				}
			}
		}
		
		for(int i = 0; i < LoginController.users.size(); i++){
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				User.writeState(LoginController.users.get(i));
			}
		}
		
		updatePictures(primaryStage);
	}
	
	/**
	 * Method to display the selected image in the slideshow
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void displayNewImage() throws IOException{
		int index = imageList.getSelectionModel().getSelectedIndex();
		String currentName = InsideAlbumController.currentPhotos.get(index);
		String filePath = currentName.substring(currentName.lastIndexOf("|-|")+4, currentName.length());
		File pic = new File(filePath);
		Image image = new Image(pic.toURI().toString());
		showImage.setImage(image);
		
		// Get index of selected song
		//int index = imageList.getSelectionModel().getSelectedIndex();
		//if there's nothing in the list to select return to not have exception
		if(index < 0) return;
		
		// Update details
		User current = null;
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				current = LoginController.users.get(i);
			}
		}
		
		String displayCaption = "";
		String displayDate = "";
		
		tagStrings.clear();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				for(int j = 0; j < LoginController.users.get(i).photos.size(); j++) {
					String toCompareWith = InsideAlbumController.currentPhotos.get(index).substring(InsideAlbumController.currentPhotos.
							get(index).lastIndexOf("|-|")+4, InsideAlbumController.currentPhotos.get(index).length());
					if(LoginController.users.get(i).photos.get(j).getPath().equals(toCompareWith)) {
						displayCaption = LoginController.users.get(i).photos.get(j).getCaption();
						displayDate = formatter.format(LoginController.users.get(i).photos.get(j).getAlbumDate().getTime());
						
						for (int k = 0; k < LoginController.users.get(i).photos.get(j).tags.size(); k++) {
							tagStrings.add(LoginController.users.get(i).photos.get(j).tags.get(k));
						}
					}
				}
			}
		}
		
		obsList = FXCollections.observableArrayList(tagStrings);
		tagsList.setItems(obsList);
		
		captionLabel.setText(displayCaption);
		dateLabel.setText(displayDate);
	}
	
	/**
	 * Method to add a caption to a photo
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void addCaption(ActionEvent e) throws IOException {
		String captionContent = caption.getText();
		
		int index = imageList.getSelectionModel().getSelectedIndex();
		
		if (index == -1) {
			showAlert(primaryStage, "No image selected", "Caption error");
			return;
		}
		//System.out.println(InsideAlbumController.currentPhotos.get(index));
		
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				for(int j = 0; j < LoginController.users.get(i).photos.size(); j++) {
					String toCompareWith = InsideAlbumController.currentPhotos.get(index).substring(InsideAlbumController.currentPhotos.
							get(index).lastIndexOf("|-|")+4, InsideAlbumController.currentPhotos.get(index).length());
					if(LoginController.users.get(i).photos.get(j).getPath().equals(toCompareWith)) {
						LoginController.users.get(i).photos.get(j).setCaption(captionContent);
					}
				}
			}
		}
		
		//update in all the albums the picture is in
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
					for(int r = 0; r < LoginController.users.get(i).albums.get(j).photos.size(); r++) {
						String toCompareWith = InsideAlbumController.currentPhotos.get(index).substring(InsideAlbumController.currentPhotos.
								get(index).lastIndexOf("|-|")+4, InsideAlbumController.currentPhotos.get(index).length());
						if(LoginController.users.get(i).albums.get(j).photos.get(r).getPath().equals(toCompareWith)) {
							LoginController.users.get(i).albums.get(j).photos.get(r).setCaption(captionContent);
						}
					}
				}
			}
		}
		
		for(int i = 0; i < LoginController.users.size(); i++){
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				User.writeState(LoginController.users.get(i));
			}
		}
		
		updatePictures(primaryStage);

		String displayCaption = "";
		String displayDate = "";
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				for(int j = 0; j < LoginController.users.get(i).photos.size(); j++) {
					String toCompareWith = InsideAlbumController.currentPhotos.get(index).substring(InsideAlbumController.currentPhotos.
							get(index).lastIndexOf("|-|")+4, InsideAlbumController.currentPhotos.get(index).length());
					if(LoginController.users.get(i).photos.get(j).getPath().equals(toCompareWith)) {
						displayCaption = LoginController.users.get(i).photos.get(j).getCaption();
						displayDate = formatter.format(LoginController.users.get(i).photos.get(j).getAlbumDate().getTime());
					}
				}
			}
		}
		
		captionLabel.setText(displayCaption);
		dateLabel.setText(displayDate);
	}
	
	/**
	 * Method to select the next image in the slideshow
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void selectNext(ActionEvent e) throws IOException {
		int index = imageList.getSelectionModel().getSelectedIndex();
		
		if(index == -1) {
			imageList.getSelectionModel().select(0);
			return;
		}
		
		// Update details
		User current = null;
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				current = LoginController.users.get(i);
			}
		}
		
		int size = current.photos.size();
		
		if (index < size-1) {
			imageList.getSelectionModel().select(index+1);
		}
		
		displayNewImage();
		
	}
	
	/**
	 * Method to select the previous image in the slideshow
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void selectPrevious(ActionEvent e) throws IOException {
		int index = imageList.getSelectionModel().getSelectedIndex();
		
		if(index == -1) {
			imageList.getSelectionModel().select(0);
			return;
		}
		
		if (index > 0) {
			imageList.getSelectionModel().select(index-1);
		}
		
		displayNewImage();
	}
	
	/**
	 * Method to edit the tags of a photo
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void edit(ActionEvent e) throws IOException {
		int index = imageList.getSelectionModel().getSelectedIndex();
		
		if(index == -1) {
			showAlert(primaryStage, "No image selected", "EditTag error");
			return;
		}
		
		User current = null;
		
		for(int i = 0; i < LoginController.users.size(); i++){
			  if(LoginController.users.get(i).username.equals(LoginController.currentUser)){
				  current = LoginController.users.get(i);
			  }
		}
		
		currentPhoto = current.photos.get(index);
		
		FXMLLoader loader;
		Parent parent;
		loader = new FXMLLoader(getClass().getResource("/view/TagsPage.fxml"));
		parent = (Parent) loader.load();
		TagsPageController controller = loader.<TagsPageController>getController();
		Scene scene = new Scene(parent);
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		//controller.start(null);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Method to update the details of the image being displayed
	 * @param mainStage Stage to display on
	 * @throws IOException
	 */
	public void updateDetails(ActionEvent e) throws IOException {
		// Get index of selected song
		int index = imageList.getSelectionModel().getSelectedIndex();
		//if there's nothing in the list to select return to not have exception
		if(index < 0) return;
		
		// Update details
		User current = null;
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(LoginController.currentUser)) {
				current = LoginController.users.get(i);
			}
		}
		
		captionLabel.setText(current.photos.get(index).getCaption());
		dateLabel.setText(current.photos.get(index).getAlbumDate().toString());
		tagLabel.setText(current.photos.get(index).tags.get(0));
	}
	
	/**
	 * Method to quit out of the app
	 * @param mainStage Stage to display on
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
	 * Method to logout of the current users account and go back to the login page
	 * @param mainStage Stage to display on
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
		//controller.start(null);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Method to show any alerts that may need to pop up
	 * @param mainStage Stage to display on
	 * @throws IOException
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
