package application.music;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

/**
 * A class that represents a Song (An entire beatmap from osu)
 */
public class Song
{
	private SimpleStringProperty name;
	private SimpleStringProperty artist;
	private SimpleStringProperty length;
	
	private String mp3FileLocation;
	private String backgroundLocation;
	
	private boolean hasImage; //Set to true if an img associated with the beatmap was found - false otherwise
	
	public static final String UNKNOWN_FIELD_VALUE = " "; //Used to fill fields if the value was not found
	
	/**
	 * Constructs a Song instance with the given arguments
	 * @param name the name of the song
	 * @param artist the artist of the song
	 * @param length the duration of the song
	 * @param location the location of the mp3 file of the song
	 * @param background the location of the background image file of the song
	 */
	public Song(String name, String artist, String length, String location, String background) {
		this.name = new SimpleStringProperty(name);
		this.artist = new SimpleStringProperty(artist);
		this.length = new SimpleStringProperty(length);
		mp3FileLocation = location.replace("\\", "/");
		
		if (background != UNKNOWN_FIELD_VALUE) {
			backgroundLocation = background.replace("\\", "/");	
		}
		else {
			backgroundLocation = background;
		}
		
		hasImage = backgroundLocation.contains(".png") || backgroundLocation.contains(".jpg");
	}

	/**
	 * Gets the name of the song
	 * @return the name of the song
	 */
    public String getName() {
        return name.get();
    }
    
    /**
     * Sets the name of the song
     * @param name the new name
     */
    public void setName(String name) {
        this.name.set(name);
    }
        
    /**
     * Gets the artist of the song
     * @return the artist
     */
    public String getArtist() {
        return artist.get();
    }
    
    /**
     * Sets the artist of the song
     * @param artist the new artist
     */
    public void setArtist(String artist) {
        this.artist.set(artist);
    }
    
    /**
     * Gets the duration of the song in mm:ss
     * @return a string representing the duration of the song
     */
    public String getLength() {
        return length.get();
    }
    
    /**
     * Sets the duration of the song
     * @param length the new length of the song in string format
     */
    public void setLength(String length) {
        this.length.set(length);
    }
    
    /**
     * Gets the absolute location of the .mp3 file of the song
     * @return the absolute location of the .mp3 file represented as a string
     */
    public String getFileLocation() {
    	return mp3FileLocation;
    }
    
    /**
     * Gets the absolute location of the background image of the song if one exists. Otherwise, returns UNKNOWN_FIELD_VALUE.
     * @return the absolute location of the background image of the song
     */
    public String getBackgroundLocation() {
    	return backgroundLocation;
    }
    
    /**
     * Returns an Image instance containing the background image. This should only be called after ensuring hasImage() returns true.
     * @return an Image instance containing the background image
     */
    public Image getBackgroundImage() {
    	return new Image("file:///" + backgroundLocation.replaceAll(" ", "%20"));
    }
    
    /**
     * Checks if this Song object has an image associated with it
     * @return true if there is an image, false otherwise
     */
    public boolean hasImage() {
    	return hasImage;
    }
}
