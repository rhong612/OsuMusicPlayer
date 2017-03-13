package application.music;


import java.io.File;
import java.net.URI;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import application.controller.FXMLController;

public class MusicPlayer
{
	private MediaPlayer player;
	private Song currentSong;
	private boolean repeat;
	private boolean shuffle;
	
	private FXMLController controller;
	
	public MusicPlayer(FXMLController controller) {
		player = null;
		currentSong = null;
		repeat = false;
		shuffle = false;
		this.controller = controller;
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
					else {
						controller.nextSong();
					}
					player.play();
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

}
