package application.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.tag.TagException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
	private IntegerProperty numSongs;

	private static final int TITLE = 0;
	private static final int ARTIST = 1;

	private static final String SPLITTER = "@@@";
	private static final long SIZE_CUTOFF = 1000000; // 1 Megabyte

	private File libraryFile;
	
	public MusicLibrary()
	{
		songData = FXCollections.observableArrayList();
		numSongs = new SimpleIntegerProperty(0);
		initializeLibrary();
	}

	public void addFile(File file)
	{
		if (file == null)
		{
			return;
		}

		if (file.getName().endsWith((".mp3")))
		{
			ArrayList<String> metaData = null;
			try
			{
				metaData = extractOsuMetaData(file);
				String length = findMP3Duration(file);
				if (metaData == null)
				{
					/*
					 * Alert alert = new Alert(AlertType.ERROR,
					 * ".osu file not found!", ButtonType.OK);
					 * alert.showAndWait();
					 */
					Song song = new Song(file.getName(), " ", length, file.getAbsolutePath());
					songData.add(song);
					addSongInformationToLibrary(song);
				}
				else
				{
					Song song = new Song(metaData.get(TITLE).replace("Title:", ""), metaData.get(ARTIST).replace("Artist:", ""), length, file.getAbsolutePath());
					songData.add(song);
					addSongInformationToLibrary(song);
				}
			}
			catch (FileNotFoundException e)
			{
				// Something is seriously wrong if this occurs
				e.printStackTrace();
			}
			numSongs.setValue(numSongs.getValue() + 1);
		}
		else
		{
			Alert alert = new Alert(AlertType.ERROR, "Please select an mp3 file!", ButtonType.OK);
			alert.showAndWait();
		}
	}

	private String findMP3Duration(File mp3File)
	{
		String length = " ";
		try
		{
			AudioFile audioFile = AudioFileIO.read(mp3File);
			MP3AudioHeader mp3Header = (MP3AudioHeader) audioFile.getAudioHeader();
			length = mp3Header.getTrackLengthAsString();
		}
		catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e)
		{
			e.printStackTrace();
		}
		return length;
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
		}
		reader.close();
		return metaData.size() == 2 ? metaData : null;
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
						addFile(mp3Files[i][j]);
					}
				}
			}
		}
		
		if (!atLeastOneMatch) {
			 Alert alert = new Alert(AlertType.ERROR, "No files found!", ButtonType.OK);
			 alert.showAndWait();
		}
	}

	public void removeFile(ObservableList<Song> songs)
	{
		int songCount = songs.size();
		if (songCount > 0)
		{
			Alert alert = new Alert(AlertType.WARNING,
					"This will remove the mp3 file(s) from your library. Are you sure?", ButtonType.YES, ButtonType.NO);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get().equals(ButtonType.YES))
			{
				songData.removeAll(songs);
				numSongs.setValue(numSongs.getValue() - songCount);
			}
		}
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

	public IntegerProperty getNumSongs()
	{
		return numSongs;
	}
	
	public void clearLibrary() {
		libraryFile.delete();
		songData.clear();
		numSongs.setValue(0);
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
					songData.add(new Song(components[0], components[1],components[2], components[3]));
					numSongs.setValue(numSongs.getValue() + 1);
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void addSongInformationToLibrary(Song song) {
		try (FileWriter writer = new FileWriter(libraryFile, true)) {
			writer.write(song.getName() + SPLITTER + song.getArtist() + SPLITTER + song.getLength() + SPLITTER + song.getFileLocation() + "\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
