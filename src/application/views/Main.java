package application.views;

import java.io.File;
import java.io.IOException;

import application.controller.FXMLController;
import application.music.MusicLibrary;
import application.music.MusicPlayer;
import application.music.Song;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class Main extends Application
{
	public static final int WINDOW_WIDTH = 500; //Temporary
	public static final int WINDOW_HEIGHT = 400; //Temporary
	
	private FXMLController controller;
	
	@Override
	public void start(Stage primaryStage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		controller = loader.getController();
		controller.setStage(primaryStage);
		
		primaryStage.setTitle("osu! Music Player");
		primaryStage.getIcons().add(FXMLController.defaultOsuIcon);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
