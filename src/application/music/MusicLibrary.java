package application.music;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
	
	private static final int TITLE = 0;
	private static final int ARTIST = 1;
	
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
	
		if (file.getName().endsWith((".mp3"))) {
			ArrayList<String> metaData = null;
			try
			{
				metaData = extractOsuMetaData(file);
				if (metaData == null) {
					Alert alert = new Alert(AlertType.ERROR, ".osu file not found!", ButtonType.OK);
					alert.showAndWait();
				}
			}
			catch (FileNotFoundException e)
			{
				//Something is seriously wrong if this occurs
				e.printStackTrace();
			}
			songData.add(new Song(metaData.get(TITLE).replace("Title:", ""), metaData.get(ARTIST).replace("Artist:", ""), "test", file.getAbsolutePath()));
		}
		else {
			Alert alert = new Alert(AlertType.ERROR, "Please select an mp3 file!", ButtonType.OK);
			alert.showAndWait();
		}
	}
	
	private ArrayList<String> extractOsuMetaData(File file) throws FileNotFoundException {
		File parentFolder = file.getParentFile();
		if (!parentFolder.isDirectory()) {
			return null;
		}
		
		File[] osuFiles = parentFolder.listFiles((dir, name) -> name.endsWith(".osu"));
		
		if (osuFiles == null) {
			return null;
		}
		
		ArrayList<String> metaData = new ArrayList<>();
		Scanner reader = new Scanner(osuFiles[0]);
		while (reader.hasNextLine()) {
			String line = reader.nextLine();
			if (line.equals("[Metadata]")) {
				metaData.add(reader.nextLine()); //Title
				metaData.add(reader.nextLine()); //Artist
			}
		}
		return metaData;
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
