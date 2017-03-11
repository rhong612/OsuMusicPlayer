package application.music;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer
{
	private MediaPlayer player;
	private Song currentSong;
	
	public MusicPlayer() {
		player = null;
		currentSong = null;
	}
	
	public void play(Song song)
	{
		if (song == currentSong) {
			if (player.getStatus() == MediaPlayer.Status.PAUSED) {
				System.out.println("Resuming");
				player.play();
			}	
		}
		else {
			if (player != null) {
				player.dispose();
			}
			currentSong = song;
			player = new MediaPlayer(new Media("file:///" + song.getFileLocation().replace("\\", "/").replaceAll(" ", "%20")));
			player.play();	
		}
	}

	public void pause()
	{
		if (player.getStatus() == MediaPlayer.Status.PLAYING) {
			player.pause();
		}
	}

	public void setRepeat()
	{
		// TODO Auto-generated method stub
		System.out.println("Set Repeat pressed");
	}

	public void setShuffle()
	{
		// TODO Auto-generated method stub

		System.out.println("Set Shuffle pressed");
	}

}
