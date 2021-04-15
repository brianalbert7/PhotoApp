package photos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class that represents an album
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class Album implements Serializable{
	/**
	 * ArrayList of current photos in order to keep track
	 */
	public ArrayList<Photo> photos;
	/**
	 * String to keep track of the album name
	 */
	String albumName;
	/**
	 * Calendar to keep times for the album and the pictures therein
	 */
	Calendar date;
	
	/**
	 * Serial version id in order to serialize
	 */
	static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for an album
	 * @param albumName The name of the album
	 */
	public Album(String albumName) {
		photos = new ArrayList<Photo>();
		this.albumName = albumName;
		this.date = Calendar.getInstance();
		this.date.set(Calendar.MILLISECOND,0);
	}
	
	/**
	 * Method to add a photo to an album
	 * @param p The photo to add
	 */
	public void addPhoto(Photo p) {
		this.photos.add(p);
	}
	
	/**
	 * Method to get the name of an album
	 * @return the name of the album
	 */
	public String getAlbumName() {
		return this.albumName;
	}
	
	/**
	 * Method to set the name of the album to something else
	 * @param albumName The new name for the album
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	
	/**
	 * Method to set the date of an album to the current time
	 */
	public void setAlbumDate() {
		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.MILLISECOND,0);
		this.date = curr;
	}
	
	/**
	 * Method to get the date of the album
	 * @return Calendar object that represents the date created
	 */
	public Calendar getAlbumDate() {
		return this.date;
	}
	
	/**
	 * Method to get the size of an album
	 * @return int value of how many items are in the album's photo arraylist
	 */
	public int getSize() {
		return photos.size();
	}
	
	
}
