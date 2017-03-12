package application.controller;

import java.io.File;
import java.io.IOException;

import application.music.MusicLibrary;
import application.music.MusicPlayer;
import application.music.Song;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private MenuItem playItem;
	
	@FXML
	private MenuItem pauseItem;
	
	@FXML
	private CheckMenuItem repeatItem;
	
	@FXML
	private CheckMenuItem shuffleItem;
	
	@FXML
	private CheckMenuItem showImageItem;
	
	@FXML
	private MenuItem sortAlphabetical;
	
	@FXML
	private MenuItem sortFavorites;
	
	private MusicPlayer player;
	private MusicLibrary library;

	private Stage stage;
	
	@FXML
	public void initialize()
	{
		player = new MusicPlayer();
		library = new MusicLibrary();
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("name"));
		artistColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
		lengthColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("length"));
		

		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Song song = tableView.getSelectionModel().getSelectedItem();
				player.play(song);
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
		
		
		
		playItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				Song song = tableView.getSelectionModel().getSelectedItem();
				player.play(song);
			}
		});
		
		pauseItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				player.pause();
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
		
		
		showImageItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.setImageVisible();
			}
		});
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
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
