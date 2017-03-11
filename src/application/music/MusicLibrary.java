package application.music;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.tag.TagException;

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
	
	public void addFile(File file) {
		if (file == null) {
			return;
		}
	
		if (file.getName().endsWith((".mp3"))) {
			ArrayList<String> metaData = null;
			try
			{
				metaData = extractOsuMetaData(file);
				String length = findMP3Duration(file);
				if (metaData == null) {
					Alert alert = new Alert(AlertType.ERROR, ".osu file not found!", ButtonType.OK);
					alert.showAndWait();
					songData.add(new Song(file.getName(), "", length, file.getAbsolutePath()));
				}
				else {
					songData.add(new Song(metaData.get(TITLE).replace("Title:", ""), metaData.get(ARTIST).replace("Artist:", ""), length, file.getAbsolutePath()));
				}
			}
			catch (FileNotFoundException e)
			{
				//Something is seriously wrong if this occurs
				e.printStackTrace();
			}
		}
		else {
			Alert alert = new Alert(AlertType.ERROR, "Please select an mp3 file!", ButtonType.OK);
			alert.showAndWait();
		}
	}
	
	
	private String findMP3Duration(File mp3File) {
		String length = "";
		try
		{
			AudioFile audioFile = AudioFileIO.read(mp3File);
			MP3AudioHeader mp3Header = (MP3AudioHeader) audioFile.getAudioHeader();
			length = mp3Header.getTrackLengthAsString();
		}
		catch (CannotReadException | IOException | TagException | ReadOnlyFileException
				| InvalidAudioFrameException e)
		{
			e.printStackTrace();
		}
		return length;
	}
	
	private ArrayList<String> extractOsuMetaData(File file) throws FileNotFoundException {
		File parentFolder = file.getParentFile();
		if (!parentFolder.isDirectory()) {
			return null;
		}
		
		File[] osuFiles = parentFolder.listFiles((dir, name) -> name.endsWith(".osu"));
		
		if (osuFiles == null || osuFiles.length == 0) {
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
		reader.close();
		return metaData;
	}
	
	public void addFolder(File directory) {
		if (directory == null) {
			return;
		}
		
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
