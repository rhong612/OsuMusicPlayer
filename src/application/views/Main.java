package application.views;

import java.io.IOException;

import application.controller.FXMLController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
