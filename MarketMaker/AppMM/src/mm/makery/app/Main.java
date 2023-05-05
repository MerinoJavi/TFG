package mm.makery.app;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class Main extends Application {
	private Stage primaryStage;
	private Scene loginScene;
	
//	private BorderPane r;
	public Main() {
		this.primaryStage = new Stage();
	}



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
	
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		initRootLayout();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
