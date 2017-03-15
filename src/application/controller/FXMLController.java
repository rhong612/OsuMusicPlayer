package application.controller;

import java.io.File;
import java.util.Random;

import application.music.MusicLibrary;
import application.music.MusicPlayer;
import application.music.Song;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
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

/**
 * Controller to act as a mediator between the models(Library, Player, Songs) and view
 */
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
	private MenuItem aboutItem;
	
	@FXML
	private ImageView playButton;
	@FXML
	private ImageView nextButton;
	@FXML
	private ImageView backButton;
	
	@FXML
	private Slider timeSlider;
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
	
	public static final Image pauseImage = new Image("file:../../res/pause.png");
	public static final Image playImage = new Image("file:../../res/play.png");
	public static final Image defaultOsuIcon = new Image("file:../../res/Osu_icon.png");
	
	@FXML
	public void initialize()
	{
		player = new MusicPlayer(this, timeSlider);
		library = new MusicLibrary(this);
		
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
				library.addSingleFile(chooseFile(stage));
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
					player.setShuffle(false);
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
					player.setRepeat(false);
				}
				player.setShuffle(shuffleItem.isSelected());
			}
		});
		
		aboutItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				 Alert alert = new Alert(AlertType.NONE,"This application was developed to extract songs from osu beatmaps. You can download and play osu here: https://osu.ppy.sh/\n\nDISCLAIMER:This application and the developer are not affiliated with osu! or any of its trademarks.", ButtonType.OK);
				 alert.setTitle("About");
				 alert.showAndWait();
			}
		});
		
		timeSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0)
			{
				if (timeSlider.isValueChanging()) {
					player.seek(timeSlider.getValue());
				}
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
			playNextSong();
		});
		
		backButton.setOnMouseClicked(event -> {
			playPreviousSong();
		});
		
		songCountField.textProperty().bind(library.getSongCountStringProperty());
	}

	/**
	 * Opens file chooser, prompting the user for a .mp3 file
	 * @param stage
	 * @return the file chosen
	 */
	private File chooseFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a file to add");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("MP3", "*.mp3"));
		return fileChooser.showOpenDialog(stage);
	}
	
	/**
	 * Opens directory chooser, prompting the user for a directory
	 * @param stage
	 * @return the directory chosen
	 */
	private File chooseFolder(Stage stage) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select the Songs folder");
		return directoryChooser.showDialog(stage);
	}
	
	/**
	 * Tells the MusicPlayer to play the selected song
	 */
	private void playSelectedSong() {
		Song song = tableView.getSelectionModel().getSelectedItem();
		if (song == null) {
			return;
		}
		
		
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
	
	/**
	 * Tells the MusicPlayer to pause the song
	 */
	private void pauseSong() {
		player.pause();
		playButton.setImage(playImage);
	}
	
	/**
	 * Sets the stage for the controller to use when opening file choosers
	 * @param stage the stage to set
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	/**
	 * Selects the next song in line and plays it
	 */
	public void playNextSong() {
		int nextIndex = tableView.getSelectionModel().getSelectedIndex() + 1;
		if (nextIndex >= tableView.getItems().size()) {
			nextIndex = 0;
		}
		tableView.getSelectionModel().clearSelection();
		tableView.getSelectionModel().select(nextIndex);
		playSelectedSong();
	}

	/**
	 * Selects the previous song in line and plays it
	 */
	private void playPreviousSong()
	{
		int nextIndex = tableView.getSelectionModel().getSelectedIndex() - 1;
		if (nextIndex < 0) {
			nextIndex = tableView.getItems().size() - 1;
		}
		tableView.getSelectionModel().clearSelection();
		tableView.getSelectionModel().select(nextIndex);
		playSelectedSong();
	}
	
	/**
	 * Selects a random song and plays it
	 */
	public void playRandomSong() {
		Random generator = new Random();
		int nextIndex = generator.nextInt(tableView.getItems().size());
		tableView.getSelectionModel().clearSelection();
		tableView.getSelectionModel().select(nextIndex);
		playSelectedSong();
	}
	
	/**
	 * Refreshes the table view
	 */
	public void updateView() {
		tableView.refresh();
	}
}
