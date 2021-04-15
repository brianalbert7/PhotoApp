package users;
import java.io.*;
import java.util.*;

import photos.Album;
import photos.Photo;
import view.LoginController;

/**
 * Class that represents a user
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class User implements Serializable{
	/**
	 * Serial version id for serialization
	 */
	static final long serialVersionUID = 1L;
	/**
	 * The user's username
	 */
	public String username;
	/**
	 * An ArrayList of all of the user's albums
	 */
	public ArrayList<Album> albums;
	/**
	 * An arraylist of all of the user's photos
	 */
	public ArrayList<Photo> photos;
	
	/**
	 * Where the data is serialized to
	 */
	public static final String storeDir = "src\\users\\data";
//	public static final String storeFile = username + ".dat";
	
	/**
	 * Constructor for a new user
	 * @param name The username of the user
	 */
	public User(String name) {
		username = name;
		photos = new ArrayList<Photo>();
		albums = new ArrayList<Album>();
	}
	
//	public void addPhoto(Photo p) {
//		photos.add(p);
//	}
	
	/**
	 * Method to check if a user exists
	 * @param username The username to check
	 * @return True if they do exist, false otherwise
	 */
	public static boolean checkUserExists(String username) {
		File path = new File("src\\users\\data");
		
		//get an array of all the path names
		String[] fileNames = path.list();
		
		if (fileNames != null) {
			for(int i = 0; i < fileNames.length; i++) {
				if(fileNames[i].substring(0, fileNames[i].length()-4).equals(username)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Method to serialize a user's data
	 * @param user The user to serialized
	 * @throws IOException
	 */
	public static void writeState(User user) throws IOException{
		try {
			String storeFile = user.username + ".ser";
			File makeFile = new File(storeDir + File.separator + storeFile);
			makeFile.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
			oos.writeObject(user);
			oos.close();
			System.out.println("Serialized data saved in src/users/data/" + user.username + ".ser");
		} catch(IOException ex) {
			System.out.println(ex.toString());
		}
		
	}
	
	/**
	 * Method to deserialize a user's data
	 * @param username The username of the desired user
	 * @return A User object for this user
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static User readState(String username) throws IOException, ClassNotFoundException{
		String storeFile = username + ".ser";
		
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		User user = (User)ois.readObject();
		return user;
	}
	
	/**
	 * Method to add an album under a user
	 * @param albumName The name of the album
	 * @param username The username of the user to add the album for
	 */
	public static void addAlbum(String albumName, String username) {
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(username)) {
				LoginController.users.get(i).albums.add(new Album(albumName));
				break;
			}
		}
	}
	
	/**
	 * Method to remove an album from a user's profile
	 * @param albumName The name of the album to remove
	 * @param username The username to remove the album from
	 */
	public static void removeAlbum(String albumName, String username) {
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(username)) {
				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
					if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(albumName)) {
						LoginController.users.get(i).albums.remove(j);
					}
				}
			}
		}
	}
	
	/**
	 * Method to change the name of an album for a user
	 * @param albumName The old name of the labum
	 * @param newAlbumName The new name of the album
	 * @param username The username of the user whose album will be changed
	 */
	public static void changeAlbumName(String albumName, String newAlbumName, String username) {
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(username)) {
				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
					if(LoginController.users.get(i).albums.get(j).getAlbumName().equals(albumName)) {
						LoginController.users.get(i).albums.get(j).setAlbumName(newAlbumName);
					}
				}
			}
		}
	}
	
	/**
	 * Method to list all the albums under a user
	 * @param username The user to list the albums for
	 * @return An ArrayList of the names of the albums
	 */
	public static ArrayList<String> listAlbums(String username){
		ArrayList<String> toReturn = new ArrayList<String>();
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(username)) {
				for(int j = 0; j < LoginController.users.get(i).albums.size(); j++) {
					toReturn.add(LoginController.users.get(i).albums.get(j).getAlbumName());
				}
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Method to initialize all of the users on app start, deserializes all of the users and also initializes the stock user
	 * @throws IOException
	 */
	public static void initializeUsers() throws IOException {
		//fill users list
		File path = new File("src\\users\\data");
		//array of all path names
		String[] fileNames = path.list();
		
		if (fileNames != null) {
			for(int i = 0; i < fileNames.length; i++) {
				String storeFile = fileNames[i];
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
					User user = (User)ois.readObject();
					LoginController.users.add(user);
					if (user.photos != null) {
						for(int j = 0; j < user.photos.size(); j++) {
							for(int r = 0; r < user.photos.get(j).containedIn.size(); r++) {
								//System.out.println(user.photos.get(j).getPath() + " " + user.photos.get(j).containedIn.get(r));
							}
						}
					}
				} catch(IOException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException ex1) {
					// TODO Auto-generated catch block
					ex1.printStackTrace();
				}
			}
		}
		
		//initialize stock
		User stockUser = new User("stock");
		//if stockuser not already in logincontroller's list of userse add it
		boolean stockExists = false;
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals("stock")) {
				stockExists = true;
			}
		}
		
		if(!stockExists) {
			LoginController.users.add(stockUser);
			User.addAlbum("stock", "stock");
			Photo first = new Photo("src\\users\\stockPhotos\\1.jpg");
			Photo second = new Photo("src\\users\\stockPhotos\\2.jpeg");
			Photo third = new Photo("src\\users\\stockPhotos\\3.jpg");
			Photo fourth = new Photo("src\\users\\stockPhotos\\4.png");
			Photo fifth = new Photo("src\\users\\stockPhotos\\5.jpg");
			
			first.addAlbum("stock");
			second.addAlbum("stock");
			third.addAlbum("stock");
			fourth.addAlbum("stock");
			fifth.addAlbum("stock");
			
			stockUser.photos.add(first);
			stockUser.photos.add(second);
			stockUser.photos.add(third);
			stockUser.photos.add(fourth);
			stockUser.photos.add(fifth);
			
			stockUser.albums.get(0).photos.add(first);
			stockUser.albums.get(0).photos.add(second);
			stockUser.albums.get(0).photos.add(third);
			stockUser.albums.get(0).photos.add(fourth);
			stockUser.albums.get(0).photos.add(fifth);
		}

		User.writeState(stockUser);
	}
	
	/**
	 * Method to get the album name of a user at a specific index
	 * @param username The username to get the album from
	 * @param index The index of the album
	 * @return String that is the name of the album
	 */
	public static String getAlbumName(String username, int index){
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(username)) {
				return LoginController.users.get(i).albums.get(index).getAlbumName();
			}
		}
		
		return null;
	}
	
	/**
	 * Method to get the date of an album at a specific index
	 * @param username The username to get the album from 
	 * @param index The index of the album
	 * @return Calendar that is the date for the album
	 */
	public static Calendar getAlbumDate(String username, int index){
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(username)) {
				return LoginController.users.get(i).albums.get(index).getAlbumDate();
			}
		}
		
		return null;
	}
	
	/**
	 * Method to get the size of a user's album's list
	 * @param username The user to get the size from
	 * @param index The index for the album to get the size of
	 * @return
	 */
	public static int getAlbumSize(String username, int index){
		for(int i = 0; i < LoginController.users.size(); i++) {
			if(LoginController.users.get(i).username.equals(username)) {
				return LoginController.users.get(i).albums.get(index).getSize();
			}
		}
		
		return 0;
	}
}
