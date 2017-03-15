package application.views;

import java.io.IOException;

import application.controller.FXMLController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * A class that loads the UI from an FXML file
 */
public class Main extends Application
{
	//Starting sizes - can be adjusted
	public static final int WINDOW_WIDTH = 500;
	public static final int WINDOW_HEIGHT = 400;

	@Override
	public void start(Stage primaryStage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		FXMLController controller = loader.getController();
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
