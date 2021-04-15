package photos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class that represents a Photo
 * @author Elijah Ongoco and Brian Albert
 *
 */
public class Photo implements Serializable{
	/**
	 * Serial version id for serialization
	 */
	static final long serialVersionUID = 1L;
	//arraylist of arraylists of tags
	/**
	 * ArrayList of strings to keep track of the tags for this photo
	 */
	public ArrayList<String> tags;
	/**
	 * The path to the photo
	 */
	String path;
	/**
	 * The photo's caption
	 */
	String caption;
	/**
	 * ArrayList of names for all the albums this photo is contained in
	 */
	public ArrayList<String> containedIn;
	/**
	 * The date that this photo was added
	 */
	Calendar date;
	
	/**
	 * Constructor for a new photo
	 * @param path The path to the photo
	 */
	public Photo(String path) {
		this.tags = new ArrayList<String>();
		this.containedIn = new ArrayList<String>();
		this.path = path;
		this.caption = "";
		this.date = Calendar.getInstance();
		this.date.set(Calendar.MILLISECOND,0);
	}
	
	/**
	 * Method to set the caption of a photo
	 * @param caption The caption to set the photo's caption to
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Method to get the caption of a photo
	 * @return String that is the photo's caption
	 */
	public String getCaption() {
		return this.caption;
	}
	
	/**
	 * Method to get the path of a photo
	 * @return String that is the photo's path
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * Method to add a new album to the photo's list of contained albums
	 * @param albumName
	 */
	public void addAlbum(String albumName) {
		this.containedIn.add(albumName);
	}
	
	/**
	 * Method to add a tag to the photo
	 * @param tagValue The tag value pair for the photo
	 */
	public void addTag(String tagValue) {
        //check if that tag type exists already. if so, add it in there
        int tagExists = 0;
        for(int i = 0; i < tags.size(); i++) {
            if(tags.get(i).equals(tagValue)) {
                tagExists = 1;
                tags.add(tagValue);
            }
        }
        
        if(tagExists == 0) {
            tags.add(tagValue);
        }
    }
    
	/**
	 * Method to remove a tag from a photo
	 * @param tagValue The tag value pair to remove
	 */
    public void removeTag(String tagValue) {
        for(int i = 0; i < tags.size(); i++) {
           if (tagValue.equals(tags.get(i))) {
        	   tags.remove(i);
           }
        }
    }
    
    /**
     * Method to set the date of the photo to the current time
     */
    public void setAlbumDate() {
		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.MILLISECOND,0);
		this.date = curr;
	}
	
    /**
     * Method to get the date of the photo
     * @return Calendar object that is the date of the photo
     */
	public Calendar getAlbumDate() {
		return this.date;
	}
}
