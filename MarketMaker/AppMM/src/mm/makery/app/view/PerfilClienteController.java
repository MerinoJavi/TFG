package mm.makery.app.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import  javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import mm.makery.app.Main;
import mm.makery.app.model.SesionUsuario;

public class PerfilClienteController {
	
	
	@FXML
	private Button logoutbutton;
	@FXML
	private Button deleteProfile;
	@FXML
	private Label nombre;
	@FXML
	private Label apellidos;
	@FXML
	private Label correo;
	@FXML
	private Label provincia;
	@FXML
	private Label pais;
	@FXML
	private Label direccion;
	private Main main = new Main();
	@FXML
	   //****************Cerrar sesion*******************************//
	    private void handleLogout(ActionEvent event) throws IOException {
	     //   isLoggedIn = false;
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginMenu.fxml"));
			//LoginController log = new LoginController();
	        
	        
			//loader.setController(log);
			Parent nextScreen = loader.load();
			//log = loader.getController();
			Scene nextScreenScene = new Scene(nextScreen);
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(nextScreenScene);
			currentStage.show();
	        //logoutButton.setDisable(true);
	    }
	
	//Obtengo el nombre de usuario que ha iniciado sesion comparandolo con el hashmap de main y de ahi obtengo sus datos
	@FXML
	private void initialize() { //Se ejecuta automaticamente cuando ejecuto el FXML
		SesionUsuario sesiones = main.getSesiones();
		
		
	}
	@FXML
	private void handleDeleteProfile(ActionEvent event) {
		 
	}

}
