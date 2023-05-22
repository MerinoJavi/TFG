package mm.makery.app.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import  javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import mm.makery.app.model.SesionUsuario;

public class PaginaClienteController {
	@FXML
    private Button logoutButton;
	
	@FXML
    private Button ProfileClient;
	
	@FXML
	private Button DeleteProfile;
	@FXML
	private Label statusLabel;
	
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
	
	// ir al perfil del usuario cliente
	@FXML
	private void handlegoProfile(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PerfilCliente.fxml"));
		
		//loader.setController(log);
		Parent nextScreen = loader.load();
		//log = loader.getController();
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show();
        //logoutButton.setDisable(true);
	}
	
	@FXML
	//Para mostrar el nombre en la label y el numero de productos que tiene en el carrito, pero eso para mas adelante
	public void initialize() {
		for(SesionUsuario sesion: SesionUsuario.usuarios) {
			if(sesion.getUsuario().equals(SesionUsuario.usuarioABuscar)) {
				//Almacenar los datos en la label para mostrar el nombre de usuario
				statusLabel.setText("¡Bienvenido, "+ sesion.getNombre()+"!");
			}
		}
	}
}
