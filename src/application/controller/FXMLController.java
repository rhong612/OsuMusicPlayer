package application.controller;

import java.io.File;

import application.music.MusicLibrary;
import application.music.MusicPlayer;
import application.music.Song;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class FXMLController
{
	@FXML
	private TableView<Song> tableView;
	@FXML
	private TableColumn<Song, String> nameColumn;
	@FXML
	private TableColumn<Song, String> artistColumn;
	@FXML
	private TableColumn<Song, String> lengthColumn;
	
	@FXML
	private MenuBar menuBar;
	
	@FXML
	private MenuItem addFileItem;
	
	@FXML
	private MenuItem addFolderItem;

	@FXML
	private MenuItem deleteFileItem;
	
	@FXML
	private MenuItem removeAllItem;
	
	@FXML
	private MenuItem playItem;
	
	@FXML
	private MenuItem pauseItem;
	
	@FXML
	private CheckMenuItem repeatItem;
	
	@FXML
	private CheckMenuItem shuffleItem;
	
	@FXML
	private ImageView playButton;
	@FXML
	private ImageView nextButton;
	@FXML
	private ImageView backButton;
	
	@FXML
	private ImageView backgroundImg;
	
	@FXML
	private TextField songCountField;
	
	@FXML
	private Label songNameLabel;
	@FXML
	private Label artistLabel;
	
	private MusicPlayer player;
	private MusicLibrary library;

	private Stage stage;
	
	

	
	public static final Image pauseImage = new Image("file:res/pause.png");
	public static final Image playImage = new Image("file:res/play.png");
	public static final Image defaultOsuIcon = new Image("file:res/Osu_icon.png");
	
	
	@FXML
	public void initialize()
	{
		player = new MusicPlayer(this);
		library = new MusicLibrary();
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("name"));
		artistColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
		lengthColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("length"));
		

		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				playSelectedSong();
			}
		});
		tableView.setItems(library.getSongData());
		
		
		
		
		addFileItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.addFile(chooseFile(stage));
			}
		});
		
		addFolderItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.addFolder(chooseFolder(stage));
			}
		});
		
		deleteFileItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.removeFile(tableView.getSelectionModel().getSelectedItems());
			}
		});
		
		removeAllItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.clearLibrary();
			}
		});
		
		
		
		playItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				playSelectedSong();
			}
		});
		
		pauseItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				pauseSong();
			}
		});
		
		repeatItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				if (repeatItem.isSelected()) {
					shuffleItem.setSelected(false);
				}
				player.setRepeat(repeatItem.isSelected());
			}
		});
		
		shuffleItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				if (shuffleItem.isSelected()) {
					repeatItem.setSelected(false);	
				}
				player.setShuffle(shuffleItem.isSelected());
			}
		});
		
		
		playButton.setOnMouseClicked(event -> {
			if (playButton.getImage() == playImage) {
				playSelectedSong();
			}
			else {
				pauseSong();
			}
		});
		
		nextButton.setOnMouseClicked(event -> {
			nextSong();
		});
		
		backButton.setOnMouseClicked(event -> {
			previousSong();
		});
		
		songCountField.textProperty().bind(library.getSongCountStringProperty());
	}

	private File chooseFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a file to add");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("MP3", "*.mp3"));
		return fileChooser.showOpenDialog(stage);
	}
	
	private File chooseFolder(Stage stage) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select the Songs folder");
		return directoryChooser.showDialog(stage);
	}
	
	private void playSelectedSong() {
		Song song = tableView.getSelectionModel().getSelectedItem();
		player.play(song);
		playButton.setImage(pauseImage);
		
		if (song.hasImage()) {
			backgroundImg.setImage(song.getBackgroundImage());	
		}
		else {
			backgroundImg.setImage(defaultOsuIcon);
		}
		
		songNameLabel.setText(song.getName());
		artistLabel.setText(song.getArtist());
	}
	
	private void pauseSong() {
		player.pause();
		playButton.setImage(playImage);
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void nextSong() {
		int nextIndex = tableView.getSelectionModel().getSelectedIndex() + 1;
		if (nextIndex >= tableView.getItems().size()) {
			nextIndex = 0;
		}
		tableView.getSelectionModel().clearSelection();
		tableView.getSelectionModel().select(nextIndex);
		playSelectedSong();
	}

	private void previousSong()
	{
		int nextIndex = tableView.getSelectionModel().getSelectedIndex() - 1;
		if (nextIndex < 0) {
			nextIndex = tableView.getItems().size() - 1;
		}
		tableView.getSelectionModel().clearSelection();
		tableView.getSelectionModel().select(nextIndex);
		playSelectedSong();
	}
}
