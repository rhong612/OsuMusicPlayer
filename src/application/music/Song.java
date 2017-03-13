package application.music;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class Song
{
	private SimpleStringProperty name;
	private SimpleStringProperty artist;
	private SimpleStringProperty length;
	
	private String fileLocation;
	private String backgroundLocation;
	
	private Image backgroundImg;
	private boolean hasImage;
	
	public Song(String name, String artist, String length, String location, String background) {
		this.name = new SimpleStringProperty(name);
		this.artist = new SimpleStringProperty(artist);
		this.length = new SimpleStringProperty(length);
		
		fileLocation = "file:///" + location.replace("\\", "/").replaceAll(" ", "%20");
		backgroundLocation = "file:///" + background.replace("\\", "/").replaceAll(" ", "%20");
		if (background != " ") {
			backgroundImg = new Image(backgroundLocation);
			hasImage = true;
		}
		else {
			hasImage = false;
		}
	}

    public String getName() {
        return name.get();
    }
    
    public void setFirstName(String name) {
        this.name.set(name);
    }
        
    public String getArtist() {
        return artist.get();
    }
    public void setArtist(String artist) {
        this.artist.set(artist);
    }
    
    public String getLength() {
        return length.get();
    }
    public void setLength(String length) {
        this.length.set(length);
    }
    
    public String getFileLocation() {
    	return fileLocation;
    }
    
    public String getBackgroundLocation() {
    	return backgroundLocation;
    }
    
    public Image getBackgroundImage() {
    	return backgroundImg;
    }
    
    public boolean hasImage() {
    	return hasImage;
    }
}
