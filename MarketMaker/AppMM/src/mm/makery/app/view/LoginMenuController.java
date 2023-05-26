package mm.makery.app.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import  javafx.scene.control.Button;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.stage.*;

public class LoginMenuController {

	@FXML
	private Button ClientButton;
	@FXML
	private Button AdminButton;
	@FXML 
	private Button CommerceButton;
	
	//Metodo para cargar el login de cada uno de los usuarios y pasar de una vista a otra. Por ahora solo voy a hacer el del cliente. Cargo su pantalla fxml.
	@FXML
	public void handleNextScreen(ActionEvent event) throws IOException{
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginClient.fxml"));
		LoginController log = new LoginController();
		
		//loader.setController(log);
		Parent nextScreen = loader.load();
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show();
		
	}
	
	
}
