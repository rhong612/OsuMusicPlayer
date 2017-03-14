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

public class MusicPlayer
{
	private MediaPlayer player;
	private Song currentSong;
	private Duration currentSongDuration;
	private boolean repeat;
	private boolean shuffle;
	
	private FXMLController controller;
	private Slider timeSlider;
	
	public MusicPlayer(FXMLController controller, Slider timeSlider) {
		player = null;
		currentSong = null;
		repeat = false;
		shuffle = false;
		this.controller = controller;
		this.timeSlider = timeSlider;
		currentSongDuration = Duration.UNKNOWN;
	}
	
	public void play(Song song)
	{
		if (song == null) {
			return;
		}
		
		if (song == currentSong) {
			if (player.getStatus() == MediaPlayer.Status.PAUSED) {
				player.play();
			}	
		}
		else {
			if (player != null) {
				player.dispose();
			}
			currentSong = song;
			String location = song.getFileLocation();
			File f = new File(location);
			URI uri = f.toURI();
			player = new MediaPlayer(new Media(uri.toString()));
			player.setOnEndOfMedia(new Runnable() {
				@Override
				public void run()
				{
					if (repeat) {
						play(currentSong);
					}
					else if (shuffle) {
						controller.randomSong();
					}
					else {
						controller.nextSong();
					}
					player.play();
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
		

		player.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
	}

	public void pause()
	{
		if (currentSong == null) {
			return;
		}
		
		if (player.getStatus() == MediaPlayer.Status.PLAYING) {
			player.pause();
		}
	}

	public void setRepeat(boolean repeat)
	{
		this.repeat = repeat;
	}

	public void setShuffle(boolean shuffle)
	{
		this.shuffle = shuffle;
	}
	
	public void seek(double newTime) {
		if (player != null) {
			player.seek(currentSongDuration.multiply(newTime / 100.0));	
		}
	}

}
