package com.drxgb.aurorasheetreader;

import java.io.IOException;
import java.io.InputStream;

import com.drxgb.aurorasheetreader.util.ViewLoader;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * Ponto de entrada da aplicação.
 * 
 * @version 1.0.0
 * @author Dr.XGB
 */
public class App extends Application
{
	public static final String NAME = "AuroraSheet Reader";
	public static final int VERSION = 1000000;
	public static final String VERSION_STRING = "1.0.0";

	private static Scene scene;


	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws IOException
	{
		final Class<App> appClass = App.class;
		
		Parent root;
		InputStream isIcon;

		root = ViewLoader.loadView("Main");
		isIcon = appClass.getResourceAsStream("favicon.png");
		scene = new Scene(root);
		
		if (isIcon != null)
		{
			stage.getIcons().add(new Image(isIcon));
		}
		
		stage.setScene(scene);
		stage.setTitle(NAME);
		stage.setMaximized(true);
		stage.show();
	}


	public static void setRoot(String fxml) throws IOException
	{
		scene.setRoot(ViewLoader.loadView(fxml));
	}


	/**
	 * Ponto de entrada da aplicação.
	 * 
	 * @param args	Conjunto de argumentos da linha de comando.
	 */
	public static void main(String[] args)
	{
		launch();
	}
}