package application.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import application.controller.FXMLController;

public class MusicLibrary
{
	private ObservableList<Song> songData;
	private StringProperty songCountStringProperty;
	private int songCount;

	private static final int TITLE = 0;
	private static final int ARTIST = 1;
	private static final int BACKGROUND = 2;

	private static final String SPLITTER = "@@@";
	private static final long SIZE_CUTOFF = 1000000; // 1 Megabyte
	
	public static final String UNKNOWN_FIELD_VALUE = " ";
	
	private static final int NO_FILE = 0;
	private static final int NO_OSU = 1;
	private static final int SUCCESS = 2;

	private File libraryFile;
	private FXMLController controller;
	
	public MusicLibrary(FXMLController controller)
	{
		songCount = 0;
		songData = FXCollections.observableArrayList();
		songCountStringProperty = new SimpleStringProperty("Song Count: " + songCount);
		initializeLibrary();
		this.controller = controller;
	}

	
	public void addSingleFile(File file) {
		int returnVal = addFile(file);
		if (returnVal == NO_OSU) {
			 Alert alert = new Alert(AlertType.ERROR, ".osu file not found. Mp3 will still be added.", ButtonType.OK);
			 alert.showAndWait();
		}
	}
	
	private int addFile(File file)
	{
		if (file == null)
		{
			return NO_FILE;
		}
		int returnVal = 0;
		if (file.getName().endsWith((".mp3")))
		{
			ArrayList<String> metaData = null;
			try
			{
				metaData = extractOsuMetaData(file);
				if (metaData == null)
				{
					returnVal = NO_OSU;
					Song song = new Song(file.getName(), UNKNOWN_FIELD_VALUE, UNKNOWN_FIELD_VALUE, file.getAbsolutePath(), UNKNOWN_FIELD_VALUE);
					songData.add(song);
					setMP3DurationAndSave(file, song);
				}
				else
				{
					returnVal = SUCCESS;
					Song song = new Song(metaData.get(TITLE).replace("Title:", ""), metaData.get(ARTIST).replace("Artist:", ""), UNKNOWN_FIELD_VALUE, file.getAbsolutePath(), metaData.get(BACKGROUND));
					songData.add(song);
					setMP3DurationAndSave(file, song);
				}
			}
			catch (FileNotFoundException e)
			{
				// Something is seriously wrong if this occurs
				e.printStackTrace();
			}
			updateSongCount();
		}
		else
		{
			Alert alert = new Alert(AlertType.ERROR, "Please select an mp3 file!", ButtonType.OK);
			alert.showAndWait();
		}
		return returnVal;
	}

	private void setMP3DurationAndSave(File mp3File, Song song)
	{
		URI uri = mp3File.toURI();
		Media media = new Media(uri.toString());
		MediaPlayer mp = new MediaPlayer(media);
		mp.setOnReady(() -> {
			song.setLength(convertDurationToMS(mp.getMedia().getDuration()));
			mp.dispose();
			controller.updateView();
			addSongInformationToLibrary(song);
		});
	}
	
	private String convertDurationToMS(Duration duration) {
		int seconds = ((int) duration.toSeconds()) % 60;
		int minutes = ((int)duration.toSeconds()) / 60;
		if (seconds < 10) {
			return minutes + ":0" + seconds;
		}
		else {
			return minutes + ":" + seconds;	
		}
	}

	private ArrayList<String> extractOsuMetaData(File file) throws FileNotFoundException
	{
		File parentFolder = file.getParentFile();
		if (!parentFolder.isDirectory())
		{
			return null;
		}

		File[] osuFiles = parentFolder.listFiles((dir, name) -> name.endsWith(".osu"));

		if (osuFiles == null || osuFiles.length == 0)
		{
			return null;
		}

		ArrayList<String> metaData = new ArrayList<>();
		Scanner reader = new Scanner(new BufferedReader(new FileReader(osuFiles[0])));
		while (reader.hasNextLine())
		{
			String line = reader.nextLine();
			if (line.startsWith("Title:") || line.startsWith("Artist:"))
			{
				metaData.add(line);
			}
			else if (line.startsWith("//Background and Video events")) {
				line = reader.nextLine();
				if (line.contains(".avi")) {
					line = reader.nextLine();
				}
				if (line.contains(".jpg") || line.contains(".png")) {
					String backgroundImageName = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
					String backgroundImageLocation = osuFiles[0].getParentFile().getAbsolutePath() + "/" + backgroundImageName;
					metaData.add(backgroundImageLocation);	
				}
				//Else no image
				else {
					metaData.add(UNKNOWN_FIELD_VALUE);
				}
			}
			
		}
		reader.close();
		return metaData.size() == 3 ? metaData : null;
	}

	public void addFolder(File directory)
	{
		if (directory == null)
		{
			return;
		}

		File[] subDirectories = directory.listFiles();
		final int SIZE = 10;
		File[][] mp3Files = new File[subDirectories.length][SIZE];

		for (int i = 0; i < subDirectories.length; i++)
		{
			mp3Files[i] = subDirectories[i].listFiles((dir, name) -> name.endsWith(".mp3"));
		}

		boolean atLeastOneMatch = false;
		boolean missingOsu = false;
		for (int i = 0; i < mp3Files.length; i++)
		{
			if (mp3Files[i] != null)
			{
				for (int j = 0; j < mp3Files[i].length; j++)
				{
					if (mp3Files[i][j].length() > SIZE_CUTOFF)
					{
						if (!atLeastOneMatch) {
							atLeastOneMatch = true;
						}
						int returnVal = addFile(mp3Files[i][j]);
						if (returnVal == NO_OSU) {
							missingOsu = true;
						}
					}
				}
			}
		}
		
		if (!atLeastOneMatch) {
			 Alert alert = new Alert(AlertType.ERROR, "No files found!", ButtonType.OK);
			 alert.showAndWait();
		}
		else if (missingOsu) {
			 Alert alert = new Alert(AlertType.ERROR, "One of more .mp3 files is missing a .osu file", ButtonType.OK);
			 alert.showAndWait();
		}
	}

	public void removeFile(ObservableList<Song> songs)
	{
		int songsRemoved = songs.size();
		if (songsRemoved > 0)
		{
			Alert alert = new Alert(AlertType.WARNING,
					"This will remove the mp3 file(s) from this program. Are you sure? (Your beatmaps will be unaffected)", ButtonType.YES, ButtonType.NO);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get().equals(ButtonType.YES))
			{
				songData.removeAll(songs);
				updateSongCount();
			}
		}
	}
	
	private void updateSongCount() {
		songCountStringProperty.setValue("Song Count: " + songData.size());
	}

	public ObservableList<Song> getSongData()
	{
		return songData;
	}

	public StringProperty getSongCountStringProperty()
	{
		return songCountStringProperty;
	}
	
	public void clearLibrary() {

		Alert alert = new Alert(AlertType.WARNING,
				"This will clear your entire library from this program. Are you sure? (Your beatmaps will be unaffected)", ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get().equals(ButtonType.YES))
		{
			libraryFile.delete();
			songData.clear();
			songCountStringProperty.setValue("Song Count: " + 0);
		}
	}

	private void initializeLibrary()
	{
		libraryFile = new File(System.getProperty("user.home") + "/osuMusicPlayer/songDirectories.txt");
		File directoryFolder = new File(System.getProperty("user.home") + "/osuMusicPlayer");
		if (!directoryFolder.exists())
		{
			directoryFolder.mkdir();
		}
		
		if (!libraryFile.exists()) {
			try
			{
				libraryFile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else {
			//Extract songs from existing library
			try(Scanner in = new Scanner(new BufferedReader(new FileReader(libraryFile)));)
			{
				while (in.hasNextLine()) {
					String line = in.nextLine();
					String[] components = line.split(SPLITTER);
					songData.add(new Song(components[0], components[1],components[2], components[3], components[4]));
					songCount++;
					songCountStringProperty.setValue("Song Count: " + songCount);
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private synchronized void addSongInformationToLibrary(Song song) {
		try (FileWriter writer = new FileWriter(libraryFile, true)) {
			writer.write(song.getName() + SPLITTER + song.getArtist() + SPLITTER + song.getLength() + SPLITTER + song.getFileLocation() + SPLITTER + song.getBackgroundLocation() + "\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
