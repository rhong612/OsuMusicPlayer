package application.music;

import java.io.File;
import java.util.ArrayList;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MusicLibrary
{
	private ArrayList<String> songLocations;
	
	public MusicLibrary() {
		songLocations = new ArrayList<>();
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
	
	
}
