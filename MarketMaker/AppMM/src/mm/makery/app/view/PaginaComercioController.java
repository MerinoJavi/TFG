package mm.makery.app.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mm.makery.app.model.SesionUsuario;

public class PaginaComercioController {
	@FXML
    private Button logoutButton;
	
	@FXML
    private Button ProfileClient;
	@FXML
	private Label statusLabel;
	@FXML
	private void initialize() {
		//Para mostrar el nombre del usuario
				for(SesionUsuario sesion: SesionUsuario.usuarios) {
					if(sesion.getUsuario().equals(SesionUsuario.usuarioABuscar)) {
						//Almacenar los datos en la label para mostrar el nombre de usuario
						statusLabel.setText("¡Hola, "+ sesion.getNombre()+"!");
					}
				}
	}
	
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
	
	// ir al perfil del usuario comercio
		@FXML
		private void handlegoProfile(ActionEvent event) throws IOException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PerfilComercio.fxml"));
			
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
		private void handleGoProducts(ActionEvent event) throws IOException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductosComercio.fxml"));
			
			//loader.setController(log);
			Parent nextScreen = loader.load();
			//log = loader.getController();
			Scene nextScreenScene = new Scene(nextScreen);
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(nextScreenScene);
			currentStage.show();
		}

}
