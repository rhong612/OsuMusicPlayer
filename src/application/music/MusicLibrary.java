package application.music;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MusicLibrary
{
	private ObservableList<Song> songData;
	
	public MusicLibrary() {
		songData = FXCollections.observableArrayList(
			    new Song("Jacob", "Smith", "jacob.smith@example.com"),
			    new Song("Isabella", "Johnson", "isabella.johnson@example.com"),
			    new Song("Ethan", "Williams", "ethan.williams@example.com"),
			    new Song("Emma", "Jones", "emma.jones@example.com"),
			    new Song("Michael", "Brown", "michael.brown@example.com")
			);
	}
	
	public void addFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a file to add");
		File file = fileChooser.showOpenDialog(stage);
	}
	
	public void addFolder(Stage stage) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select the Songs folder");
		File directory = directoryChooser.showDialog(stage);
	}

	public void removeFile(Stage primaryStage)
	{
		// TODO Auto-generated method stub
		System.out.println("Remove file pressed");
	}

	public void setImageVisible()
	{
		// TODO Auto-generated method stub
		System.out.println("Set Image Visible Pressed");
	}

	public ObservableList<Song> getSongData()
	{
		return songData;
	}
	
	
}
