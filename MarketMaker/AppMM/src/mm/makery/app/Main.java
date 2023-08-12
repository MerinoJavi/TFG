package mm.makery.app;
	
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mm.makery.app.model.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import mm.makery.app.view.*;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;

import javafx.concurrent.Task;
import javafx.scene.layout.VBox;


public class Main extends Application {
	private Stage primaryStage;
    private Scene loginScene;
	/*
	 * Constructor
	 */
	public Main() {
		this.primaryStage = new Stage();
	
	}


	//PRIMERA VISTA QUE SALE AL INICIAR LA APLICACION
	public void initRootLayout() {
	    // Mostrar la ventana de carga
		Stage loadingStage = new Stage();
		ProgressBar progressBar = new ProgressBar();
		Label progressLabel = new Label("Cargando...");
		VBox vbox = new VBox(progressBar, progressLabel);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		//vbox.prefWidth(400);
	//	loadingStage.setWidth(600);
		loadingStage.initStyle(StageStyle.DECORATED);
		loadingStage.setScene(new Scene(vbox));
		loadingStage.show();

		// Cargar la escena principal en segundo plano con la clase Task para no bloquear la interfaz
		Task<Void> loadTask = new Task<Void>() {
		    @Override
		    protected Void call() throws Exception {
		        // Simular una carga en segundo plano
		        for (int i = 0; i <= 100; i++) {
		            Thread.sleep(50);
		             double progress = i/100.00;
		            Platform.runLater(() -> {
		                progressBar.setProgress(progress);
		                progressLabel.setText("Cargando: " + progress*100 + "%"); //Para que salga con el formato x.x%
		            });
		        }

		        // Cargar la escena principal y los archivos CSS
		        Platform.runLater(() -> {
		            try {
		                Parent root = FXMLLoader.load(Main.class.getResource("view/Home.fxml"));
		                loginScene = new Scene(root);
		                loginScene.getStylesheets().add(getClass().getResource("style/button.css").toExternalForm());

		                primaryStage.setScene(loginScene);
		                primaryStage.setTitle("MarketMakerAPP");
		                //TODO: Poner icono de la app
		              
		                primaryStage.show();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        });

		        return null;
		    }
		};

		// Ejecutar la tarea de carga en segundo plano
		loadTask.setOnSucceeded(event -> {
		    // Cerrar la ventana de carga
		    loadingStage.close();
		});
		new Thread(loadTask).start();
	}

	/*******************SE INICIA AUTOMATICAMENTE CUANDO EJECUTO LA APLICACION*****************************/
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
	
		//sesiones = new SesionUsuario();
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
