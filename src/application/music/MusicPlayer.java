package application.music;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer
{
	private MediaPlayer player;
	private Song currentSong;
	private boolean repeat;
	private boolean shuffle;
	
	public MusicPlayer() {
		player = null;
		currentSong = null;
		repeat = false;
		shuffle = false;
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
			player = new MediaPlayer(new Media(song.getFileLocation()));
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
