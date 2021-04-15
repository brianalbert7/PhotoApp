package users;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that represents the admin account
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class Admin implements Serializable{
	
	/**
	 * Method to get al ist of the currently existing users
	 * @return An ArrayList of strings of usernames 
	 */
	public static ArrayList<String> listUsers() {
		//get path
		File path = new File("src\\users\\data");
		
		//get an array of all the path names
		String[] fileNames = path.list();
		
		ArrayList<String> toReturn = new ArrayList<String>();
		
		//iterate through list of path names, remove the .ser at the end
		if (fileNames != null) {
			for(int i = 0; i < fileNames.length; i++) {
				//System.out.println(fileNames[i].substring(0, fileNames[i].length()-4));
				toReturn.add(fileNames[i].substring(0, fileNames[i].length()-4));
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Method that creates a new user
	 * @param username The new user's username
	 * @throws IOException
	 */
	public static void createUser(String username) throws IOException {
		//simply write a new serialization page for them in 
		User.writeState(new User(username));
	}
	
	/**
	 * Method that deletes a user
	 * @param username The username of the user to delete
	 */
	public static void deleteUser(String username) {
		//get path, delete their serialization from data folder
		File toDelete = new File("src\\users\\data\\" + username + ".ser");
		toDelete.delete();
	}
}