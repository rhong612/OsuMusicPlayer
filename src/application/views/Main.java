package application.views;

import application.music.MusicLibrary;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class Main extends Application
{
	public static final int WINDOW_WIDTH = 500; //Temporary
	public static final int WINDOW_HEIGHT = 300; //Temporary
	
	
	private MusicLibrary library;
	
	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("osu! Music Player");
		primaryStage.getIcons().add(new Image("file:res/Osu_icon.png"));
		
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem addFolderItem = new MenuItem("Add Folder");
		
		addFolderItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0)
			{
				library.addFolder(primaryStage);
			}
		});
		
		fileMenu.getItems().add(addFolderItem);
		Menu editMenu = new Menu("Edit");
		Menu helpMenu = new Menu("Help");
		menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
		
		Scene scene = new Scene(new VBox(), WINDOW_WIDTH, WINDOW_HEIGHT);
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar);
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
