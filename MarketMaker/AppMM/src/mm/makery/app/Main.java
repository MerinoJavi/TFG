package mm.makery.app;
	
import java.io.IOException;

import javafx.application.Application;
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
	/**************************************************************************************************************/
	public static void main(String[] args) {
		
		launch(args);
	}
}
