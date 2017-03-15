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

/**
 * A class that contains all song data
 */
public class MusicLibrary
{
	private ObservableList<Song> songBank;
	private StringProperty songCountStringProperty;
	private int songCount;

	private static final int TITLE = 0;
	private static final int ARTIST = 1;
	private static final int BACKGROUND = 2;

	private static final String SPLITTER = "@@@"; //The delimiter to separate fields in the saved .txt file
	private static final long SIZE_CUTOFF = 1000000; // 1 Megabyte
	
	private static final int NO_FILE = 0;
	private static final int NO_OSU = 1;
	private static final int SUCCESS = 2;

	private File libraryFile;
	private FXMLController controller;
	
	/**
	 * Constructs a MusicLibrary with the given controller
	 * @param controller the controller
	 */
	public MusicLibrary(FXMLController controller)
	{
		songCount = 0;
		songBank = FXCollections.observableArrayList();
		songCountStringProperty = new SimpleStringProperty("Song Count: " + songCount);
		initializeLibrary();
		this.controller = controller;
	}

	/**
	 * Adds a single song to the song bank and saves it into the library file. The file must be a .mp3 file.
	 * @param file the file to add
	 */
	public void addSingleFile(File file) {
		int returnVal = addFile(file);
		if (returnVal == NO_OSU) {
			 Alert alert = new Alert(AlertType.ERROR, ".osu file not found. Mp3 will still be added.", ButtonType.OK);
			 alert.showAndWait();
		}
	}
	
	/**
	 * Helper method for adding a file to the library 
	 * @param file the file to add
	 * @return NO_FILE if no file was found. NO_OSU if a file was found, but no .osu was found in the same directory. SUCCESS if both a .mp3 and .osu file were found.
	 */
	private int addFile(File file)
	{
		int returnVal = NO_FILE;
		if (file == null)
		{
			return returnVal;
		}
		if (file.getName().endsWith((".mp3")))
		{
			ArrayList<String> metaData = null;
			try
			{
				File osuFile = findOsuFile(file);
				metaData = extractOsuMetaData(osuFile);
				if (metaData == null)
				{
					returnVal = NO_OSU;
					Song song = new Song(file.getName(), Song.UNKNOWN_FIELD_VALUE, Song.UNKNOWN_FIELD_VALUE, file.getAbsolutePath(), Song.UNKNOWN_FIELD_VALUE);
					songBank.add(song);
					setMP3DurationAndSave(file, song);
				}
				else
				{
					returnVal = SUCCESS;
					Song song = new Song(metaData.get(TITLE).replace("Title:", ""), metaData.get(ARTIST).replace("Artist:", ""), Song.UNKNOWN_FIELD_VALUE, file.getAbsolutePath(), metaData.get(BACKGROUND));
					songBank.add(song);
					setMP3DurationAndSave(file, song);
				}
			}
			catch (FileNotFoundException e)
			{
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

	/**
	 * Finds the duration of a song, sets the song length value to that duration, and saves the song's information to the library file
	 * @param mp3File the mp3 file to find the duration of
	 * @param song the song
	 */
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
	
	/**
	 * Convert a given duration to a string with format "mm:ss"
	 * @param duration the duration to convert
	 * @return a string containing the duration represented in minutes:seconds
	 */
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

	
	/**
	 * Attempts to find a .osu file in the same directory as the given mp3 file
	 * @param mp3File the mp3 file
	 * @return a .osu file if found, null otherwise
	 */
	public File findOsuFile(File mp3File) {
		File parentFolder = mp3File.getParentFile();
		if (!parentFolder.isDirectory())
		{
			return null;
		}

		File[] osuFiles = parentFolder.listFiles((dir, name) -> name.endsWith(".osu"));

		if (osuFiles == null || osuFiles.length == 0)
		{
			return null;
		}
		return osuFiles[0];
	}
	
	/**
	 * Parses a .osu file to extract song metadata
	 * @param file the .osu file to parse
	 * @return an ArrayList containing osu meta data. The song name will be in index TITLE, the artist will be in index ARTIST, and the background image will be in index BACKGROUND. If no image is found, NO_FIELD_VALUE will be stored in index BACKGROUND.
	 * @throws FileNotFoundException
	 */
	private ArrayList<String> extractOsuMetaData(File file) throws FileNotFoundException
	{
		ArrayList<String> metaData = new ArrayList<>();
		Scanner reader = new Scanner(new BufferedReader(new FileReader(file)));
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
					String backgroundImageLocation = file.getParentFile().getAbsolutePath() + "/" + backgroundImageName;
					metaData.add(backgroundImageLocation);	
				}
				//Else no image
				else {
					metaData.add(Song.UNKNOWN_FIELD_VALUE);
				}
			}
			
		}
		reader.close();
		return metaData.size() == 3 ? metaData : null;
	}

	/**
	 * Adds all songs found in the Songs osu directory
	 * @param directory the Songs directory in osu
	 */
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

	/**
	 * Remove the given songs from the song bank
	 * @param songs the songs to remove
	 */
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
				songBank.removeAll(songs);
				updateSongCount();
			}
		}
	}
	
	/**
	 * Updates the song count property
	 */
	private void updateSongCount() {
		songCountStringProperty.setValue("Song Count: " + songBank.size());
	}

	/**
	 * Retrieves the song bank
	 * @return the song bank
	 */
	public ObservableList<Song> getSongData()
	{
		return songBank;
	}

	/**
	 * Retrieves the song count property
	 * @return the song count property
	 */
	public StringProperty getSongCountStringProperty()
	{
		return songCountStringProperty;
	}
	
	/**
	 * Clears the entire song bank and library file
	 */
	public void clearLibrary() {

		Alert alert = new Alert(AlertType.WARNING,
				"This will clear your entire library from this program. Are you sure? (Your beatmaps will be unaffected)", ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get().equals(ButtonType.YES))
		{
			libraryFile.delete();
			songBank.clear();
			songCountStringProperty.setValue("Song Count: " + 0);
		}
	}

	/**
	 * Initializes the library by either creating the appropriate library file or parsing an existing one
	 */
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
					songBank.add(new Song(components[0], components[1],components[2], components[3], components[4]));
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
	
	/**
	 * Writes song information to the library file
	 * @param song the song to save
	 */
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
