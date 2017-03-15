package application.music;


import java.io.File;
import java.net.URI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import application.controller.FXMLController;

/**
 * A music player that plays mp3 files from the MusicLibrary
 */
public class MusicPlayer
{
	private MediaPlayer player;
	private Song currentSong;
	private Duration currentSongDuration;
	private boolean repeat;
	private boolean shuffle;
	
	private FXMLController controller;
	private Slider timeSlider;
	
	/**
	 * Constructs a MusicPlayer instance associated with a given FXMLController
	 * @param controller
	 * @param timeSlider
	 */
	public MusicPlayer(FXMLController controller, Slider timeSlider) {
		player = null;
		currentSong = null;
		repeat = false;
		shuffle = false;
		this.controller = controller;
		this.timeSlider = timeSlider;
		currentSongDuration = Duration.UNKNOWN;
	}
	
	/**
	 * Plays the given song
	 * @param song the song to play
	 */
	public void play(Song song)
	{
		if (song == null) {
			return;
		}
		
		if (song == currentSong && player.getStatus() == MediaPlayer.Status.PAUSED) {
			player.play();
		}
		else {
			if (player != null) {
				player.dispose();
			}
			currentSong = song;
			String uriStr = convertPathToValidURIString(song.getFileLocation());
			player = new MediaPlayer(new Media(uriStr));
			player.setOnEndOfMedia(new Runnable() {
				@Override
				public void run()
				{
					if (repeat) {
						play(currentSong);
					}
					else if (shuffle) {
						controller.playRandomSong();
					}
					else {
						controller.playNextSong();
					}
				}
			});
			player.setOnReady(new Runnable() {
				@Override
				public void run()
				{
					currentSongDuration = player.getMedia().getDuration();
				}
			});
			player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				@Override
				public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue)
				{
					if (!timeSlider.isValueChanging()) {
						timeSlider.setValue(newValue.toMillis() / currentSongDuration.toMillis() * 100.0);	
					}
				}
			});
			player.play();
		}
	}
	
	/**
	 * Converts a given file path to a valid URI string
	 * @param path the path to convert
	 * @return the path with all illegal URI characters replaced
	 */
	private String convertPathToValidURIString(String path) {
		File f = new File(path);
		URI uri = f.toURI();
		return uri.toString();
	}

	/**
	 * Pauses the current song
	 */
	public void pause()
	{
		if (currentSong == null) {
			return;
		}
		else if (player.getStatus() == MediaPlayer.Status.PLAYING) {
			player.pause();
		}
	}

	/**
	 * Set whether or not the music player should repeat songs
	 * @param repeat the repeat flag
	 */
	public void setRepeat(boolean repeat)
	{
		this.repeat = repeat;
	}

	/**
	 * Sets whether or not the music player should shuffle through songs
	 * @param shuffle the shuffle flag
	 */
	public void setShuffle(boolean shuffle)
	{
		this.shuffle = shuffle;
	}
	
	/**
	 * Sets the music player at a particular position of the song. The value should be between 0 and 100 (inclusive).
	 * @param newTime the new track position of the song
	 */
	public void seek(double newTime) {
		if (player != null) {
			player.seek(currentSongDuration.multiply(newTime / 100.0));	
		}
	}

}
