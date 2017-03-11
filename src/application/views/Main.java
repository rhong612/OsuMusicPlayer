package application.views;

import application.music.MusicLibrary;
import application.music.MusicPlayer;
import application.music.Song;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class Main extends Application
{
	public static final int WINDOW_WIDTH = 500; //Temporary
	public static final int WINDOW_HEIGHT = 300; //Temporary
	
	private MusicLibrary library;
	private MusicPlayer player;
	
	@Override
	public void start(Stage primaryStage)
	{
		library = new MusicLibrary();
		player = new MusicPlayer();
		
		primaryStage.setTitle("osu! Music Player");
		primaryStage.getIcons().add(new Image("file:res/Osu_icon.png"));
		
		MenuBar menuBar = initializeMenuBar(primaryStage);
		TableView<Song> songTable = initializeTableView();
		
		Scene scene = new Scene(new VBox(), WINDOW_WIDTH, WINDOW_HEIGHT);
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, songTable);
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
	private TableView<Song> initializeTableView()
	{
		TableView<Song> table = new TableView<>();
		
		TableColumn<Song, String> nameColumn = new TableColumn<>("Name");
		TableColumn<Song, String> artistColumn = new TableColumn<>("Artist");
		TableColumn<Song, String> lengthColumn = new TableColumn<>("Length");
		
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
				library.addFile(primaryStage);
			}
		});
		MenuItem addFolderItem = new MenuItem("Add Folder");
		addFolderItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.addFolder(primaryStage);
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
				player.play();
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
		MenuItem repeatItem = new MenuItem("Repeat");
		repeatItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				player.setRepeat();
			}
		});
		MenuItem shuffleItem = new MenuItem("Shuffle");
		shuffleItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				player.setShuffle();
			}
		});
		MenuItem toggleImageItem = new MenuItem("Show Image");
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
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
