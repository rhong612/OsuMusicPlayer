package application.music;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MusicLibrary
{
	private ObservableList<Song> songData;
	
	public MusicLibrary() {
		songData = FXCollections.observableArrayList();
	}
	
	public void addFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a file to add");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("MP3", "*.mp3"));
		File file = fileChooser.showOpenDialog(stage);
		
		if (file == null) {
			return;
		}
		extractOsuMetaData(file);
		if (file.getName().endsWith((".mp3"))) {
			songData.add(new Song(file.getName(), "test", "test", file.getAbsolutePath()));
		}
		else {
			Alert alert = new Alert(AlertType.ERROR, "Please select an mp3 file!", ButtonType.OK);
			alert.showAndWait();
		}
	}
	
	private ArrayList<String> extractOsuMetaData(File file) {
		File parentFolder = file.getParentFile();
		if (!parentFolder.isDirectory()) {
			return null;
		}
		
		File[] osuFiles = parentFolder.listFiles((dir, name) -> name.endsWith(".osu"));
		
		if (osuFiles == null) {
			return null;
		}
		
		System.out.println(osuFiles[0]);
		return null;
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
