package application.music;

import javafx.beans.property.SimpleStringProperty;

public class Song
{
	private SimpleStringProperty name;
	private SimpleStringProperty artist;
	private SimpleStringProperty length;
	
	private String fileLocation;
	
	public Song(String name, String artist, String length, String location) {
		this.name = new SimpleStringProperty(name);
		this.artist = new SimpleStringProperty(artist);
		this.length = new SimpleStringProperty(length);
		
		fileLocation = location;
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
}
