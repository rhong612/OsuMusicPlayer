package application.views;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class MainWindow extends Application
{
	public static final int WINDOW_WIDTH = 500; //Temporary
	public static final int WINDOW_HEIGHT = 300; //Temporary
	
	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("osu! Music Player");
		primaryStage.getIcons().add(new Image("file:res/Osu_icon.png"));
		
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
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
