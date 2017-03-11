package application.music;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer
{
	private MediaPlayer player;
	
	public MusicPlayer() {
		player = null;
	}
	
	public void play(Song song)
	{
		if (player != null) {
			player.dispose();
		}
		player = new MediaPlayer(new Media("file:///" + song.getFileLocation().replace("\\", "/")));
		player.play();
	}

	public void pause()
	{
		// TODO Auto-generated method stub
		System.out.println("Pause pressed");
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
