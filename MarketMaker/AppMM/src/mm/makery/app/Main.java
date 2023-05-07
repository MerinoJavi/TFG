package mm.makery.app;
	
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mm.makery.app.model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import mm.makery.app.view.*;

public class Main extends Application {
	private Stage primaryStage;
	private Scene loginScene;
	
	
	/*
	 * Constructor
	 */
	public Main() {
		this.primaryStage = new Stage();
	
	}


//MENÚ PRINCIPAL PARA ELEGIR USUARIO
	public void initRootLayout() { 
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("view/LoginMenu.fxml"));
			
			//Creo la escena
			loginScene = new Scene(root);
			//Configuro y muestro la ventana con el menu de login
			primaryStage.setTitle("Menu de login");
			primaryStage.setScene(loginScene);
			//TODO: Poner icono de la app
			
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/*******************SE INICIA AUTOMATICAMENTE CUANDO EJECUTO LA APLICACION*****************************/
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		

		initRootLayout();
	}
	/**
	 * @throws SQLException ************************************************************************************************************/
	public static void main(String[] args) throws SQLException {
		//Creo la conexion a la base de datos 
		String url = "jdbc:mysql://localhost:3306/TFG";
		String user = "root";
		String pass = "9P$H7nI5!*8p";
		Connection conexion = DriverManager.getConnection(url,user,pass);
		launch(args);
	}
}
