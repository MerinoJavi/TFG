package mm.makery.app.view;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.animation.ScaleTransition;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Statement;
import java.util.ArrayList;
import javafx.scene.layout.HBox;

import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.Menu;

public class PaginaAdmin {
	@FXML
	private Label comercioLabel;
	@FXML
	private TextField searchField;
	@FXML
	private Button logoutButton;
	@FXML
	private Button profile;
	@FXML
	private Button settings;
	@FXML
	private Button solicitudes;
	
	private String nombreBD=""; //Nombre del coemrcio recogido de la base de datos
	private ArrayList<String> nombrescomercios = new ArrayList<>();
	@FXML
	private VBox carruselComercios=new VBox(10);
	@FXML
	private HBox carrusel = new HBox(); //El usado para hacer la animacion
	
	private boolean commerceFound=false;
	
	private ArrayList<String> searchCommercesDB() {
		Connection conexion;
		
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			Statement st = conexion.createStatement();
			String sql = "SELECT nombre from comercio";
			ResultSet result = st.executeQuery(sql);
			while(result.next()) {
				nombrescomercios.add(result.getString("nombre"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nombrescomercios;
	}
	
	@FXML
	private void initialize() {
		
		Platform.runLater(() -> {
	        ArrayList<String> nombres = searchCommercesDB();
	        carrusel.setAlignment(Pos.CENTER);
	        
	        for (String n : nombres) {
	            Button comercio = new Button(n);
	            comercio.setPrefWidth(carrusel.getWidth() / 2);
	            carrusel.getChildren().add(comercio);
	        }
	        
	        startAnimation();
	    });
	}
	
	
	private void startAnimation() {
		// Duracion de la animacion
		int animationDuration=5000;
		// Crea la transición de desplazamiento
		carrusel.setTranslateX(-carrusel.getWidth());
        TranslateTransition transitionRight = new TranslateTransition(Duration.millis(animationDuration), carrusel);
        transitionRight.setToX(carrusel.getWidth());
       
        TranslateTransition transitionLeft = new TranslateTransition(Duration.millis(animationDuration),carrusel);
        transitionLeft.setToX(-carrusel.getWidth());
        
        SequentialTransition sq = new SequentialTransition(transitionRight,transitionLeft);
        sq.setCycleCount(SequentialTransition.INDEFINITE);
        
        sq.play();
        }
        
	@FXML
	private void handleLogout(ActionEvent event) throws IOException {
		//   isLoggedIn = false;
		// Cargo la pagina XML correspondiente al menu de logins
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginMenu.fxml"));
		//LoginController log = new LoginController();
        
        
		//loader.setController(log);
		Parent nextScreen = loader.load();
		//log = loader.getController();
		//Cargo el XML siguiente en una nueva escena, que posteriormente casteo la ventana que obtengo y la establezco en la escena actual para que no me cree otra ventana.
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show(); //Muestro la pagina LoginMenu
        //logoutButton.setDisable(true);
	}
	
	@FXML
	private void handleGoAdminProfile(ActionEvent event) throws IOException {
		//   isLoggedIn = false;
		// Cargo la pagina XML correspondiente al menu de logins
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PerfilAdmin.fxml"));
		//LoginController log = new LoginController();
        
        
		//loader.setController(log);
		Parent nextScreen = loader.load();
		//log = loader.getController();
		//Cargo el XML siguiente en una nueva escena, que posteriormente casteo la ventana que obtengo y la establezco en la escena actual para que no me cree otra ventana.
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show(); //Muestro la pagina LoginMenu
        //logoutButton.setDisable(true);
	}
	
	@FXML
	private void handleGoRequest(ActionEvent event) throws IOException {
	//   isLoggedIn = false;
			// Cargo la pagina XML correspondiente al menu de logins
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("Solicitudes.fxml"));
			//LoginController log = new LoginController();
	        
	        
			//loader.setController(log);
			Parent nextScreen = loader.load();
			//log = loader.getController();
			//Cargo el XML siguiente en una nueva escena, que posteriormente casteo la ventana que obtengo y la establezco en la escena actual para que no me cree otra ventana.
			Scene nextScreenScene = new Scene(nextScreen);
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(nextScreenScene);
			currentStage.show(); //Muestro la pagina LoginMenu
	        //logoutButton.setDisable(true);
	}
	
	//Busca el comercio tecleando el nombre o gran parte del mismo
	@FXML
	private void searchCommerce(ActionEvent event) throws IOException {
		commerceFound=false;
		String commerceName = searchField.getText();
		carruselComercios.getChildren().clear(); //Lo vacio para que no me repita los mismos botones una y otra vez
		
		if(commerceName.isEmpty()) {
		//   isLoggedIn = false;
					// Cargo la pagina XML correspondiente al menu de logins
			        FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaAdmin.fxml"));
					//LoginController log = new LoginController();
					//loader.setController(log);
					Parent nextScreen = loader.load();
					//log = loader.getController();
					//Cargo el XML siguiente en una nueva escena, que posteriormente casteo la ventana que obtengo y la establezco en la escena actual para que no me cree otra ventana.
					Scene nextScreenScene = new Scene(nextScreen);
					Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					currentStage.setScene(nextScreenScene);
					currentStage.show(); //Muestro la pagina LoginMenu
			        //logoutButton.setDisable(true);		
		}
			
		
		searchField.setOnKeyPressed(e->{
			if(e.getCode().equals(KeyCode.ENTER)) { //Confirmacion de la busqueda con la tecla ENTER
				try {
					Connection conexionComercio = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
					Statement sta = conexionComercio.createStatement();
					String sqlcommerce = "SELECT nombre from comercio";
					ResultSet result = sta.executeQuery(sqlcommerce);
					
					while(result.next()) {
						
						nombreBD=result.getString("nombre");
						if(nombreBD.toLowerCase().contains(commerceName.toLowerCase())) { //Los comercios que contengan la frase o vocan introducida por teclado se muestran en el VBox
						//	System.out.println("Los comercios que contienen una "+commerceName+"son:"+nombreBD);
							/*
							Alert a = new Alert(AlertType.INFORMATION);
							a.setTitle("El comercio existe!");
							a.showAndWait();
							 */
							commerceFound=true;
							Button comercioVBox = new Button(nombreBD);
							comercioVBox.setMaxWidth(Double.MAX_VALUE);
							Separator s = new Separator(Orientation.HORIZONTAL);
							System.out.println("El boton es creado");
							carruselComercios.getChildren().addAll(comercioVBox,s);
							System.out.println("El boton es añadido");
							carruselComercios.setSpacing(2);
							//comercioVBox=null;
							nombreBD="";
							
							comercioVBox.setOnAction(ev->{
								ProductosComercioDetalladoController.idcomercio=getidcommerce(comercioVBox.getText());
								 FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductosComercioDetallado.fxml"));
									//LoginController log = new LoginController();
							        
							        
									//loader.setController(log);
									Parent nextScreen = null;
									try {
										nextScreen = loader.load();
									//log = loader.getController();
									//Cargo el XML siguiente en una nueva escena, que posteriormente casteo la ventana que obtengo y la establezco en la escena actual para que no me cree otra ventana.
									Scene nextScreenScene = new Scene(nextScreen);
									Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
									currentStage.setScene(nextScreenScene);
									currentStage.show(); //Muestro la pagina LoginMenu
							        //logoutButton.setDisable(true);
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									
							});
						}
						
					}
					
					if(!commerceFound) {
						Alert a = new Alert(AlertType.INFORMATION);
						a.setTitle("Error al realizar la búsqueda");
						a.setHeaderText("No hay resultados para "+commerceName);
						a.setContentText("El comercio no se encuentra registrado en la aplicación");
						a.showAndWait();
					}
					result.close();
					sta.close();
					conexionComercio.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	private String getidcommerce(String ncomercio) {
		Connection conexionComercio;
		String id="";
		try {
			conexionComercio = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			PreparedStatement sta = conexionComercio.prepareStatement("SELECT nif from comercio where nombre=?");
			sta.setString(1, ncomercio);
			ResultSet result = sta.executeQuery();
			if(result.next()) {
				id=result.getString("nif");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	
	
	
}
