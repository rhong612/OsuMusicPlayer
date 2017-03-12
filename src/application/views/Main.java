package application.views;

import java.io.File;

import application.music.MusicLibrary;
import application.music.MusicPlayer;
import application.music.Song;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class Main extends Application
{
	public static final int WINDOW_WIDTH = 500; //Temporary
	public static final int WINDOW_HEIGHT = 300; //Temporary
	
	private MusicLibrary library;
	private MusicPlayer player;
	private TableView<Song> songTable;
	
	@Override
	public void start(Stage primaryStage)
	{
		library = new MusicLibrary();
		player = new MusicPlayer();
		
		primaryStage.setTitle("osu! Music Player");
		primaryStage.getIcons().add(new Image("file:res/Osu_icon.png"));
		
		MenuBar menuBar = initializeMenuBar(primaryStage);
		songTable = initializeTableView();
		TextField songCount = new TextField();
		songCount.setEditable(false);
		songCount.setStyle("-fx-background-color: pink; -fx-cursor: default;");
		songCount.textProperty().bind(library.getNumSongs().asString());
		
		Scene scene = new Scene(new VBox(), WINDOW_WIDTH, WINDOW_HEIGHT);
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, songTable, songCount);
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
	private TableView<Song> initializeTableView()
	{
		TableView<Song> table = new TableView<>();
		table.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Song song = table.getSelectionModel().getSelectedItem();
				player.play(song);
			}
		});
		
		TableColumn<Song, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("name"));
		TableColumn<Song, String> artistColumn = new TableColumn<>("Artist");
		artistColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
		TableColumn<Song, String> lengthColumn = new TableColumn<>("Length");
		lengthColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("length"));
		
		table.setItems(library.getSongData());
		table.getColumns().addAll(nameColumn, artistColumn, lengthColumn);
		
		return table;
	}


	private MenuBar initializeMenuBar(Stage primaryStage) {
		MenuBar menuBar = new MenuBar();
		
		
		Menu fileMenu = new Menu("File");
		MenuItem addFileItem = new MenuItem("Add File");
		addFileItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.addFile(chooseFile(primaryStage));
			}
		});
		MenuItem addFolderItem = new MenuItem("Add Folder");
		addFolderItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.addFolder(chooseFolder(primaryStage));
			}
		});
		MenuItem removeFileItem = new MenuItem("Remove File");
		removeFileItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.removeFile(primaryStage);
			}
		});
		fileMenu.getItems().addAll(addFileItem, addFolderItem, removeFileItem);
		
		
		
		Menu editMenu = new Menu("Edit");
		MenuItem playItem = new MenuItem("Play");
		playItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				Song song = songTable.getSelectionModel().getSelectedItem();
				player.play(song);
			}
		});
		MenuItem pauseItem = new MenuItem("Pause");
		pauseItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				player.pause();
			}
		});
		CheckMenuItem repeatItem = new CheckMenuItem("Repeat");
		repeatItem.setSelected(false);
		CheckMenuItem shuffleItem = new CheckMenuItem("Shuffle");
		shuffleItem.setSelected(false);
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
		
		
		CheckMenuItem toggleImageItem = new CheckMenuItem("Show Image");
		toggleImageItem.setSelected(false);
		toggleImageItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.setImageVisible();
			}
		});
		Menu sortSubMenu = new Menu("Sort");
		MenuItem alphabeticalSort = new MenuItem("Alphabetical");
		sortSubMenu.getItems().add(alphabeticalSort);
		editMenu.getItems().addAll(playItem, pauseItem, repeatItem, shuffleItem, toggleImageItem, sortSubMenu);
		
		
		Menu helpMenu = new Menu("Help");
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				//TODO: Show dialog showing "About" page
			}
		});
		MenuItem bugItem = new MenuItem("Bug Report");
		bugItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				//TODO: Show dialog showing how to send bug reports
			}
		});
		helpMenu.getItems().addAll(aboutItem, bugItem);
		
		
		
		
		
		
		menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
		return menuBar;
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
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
