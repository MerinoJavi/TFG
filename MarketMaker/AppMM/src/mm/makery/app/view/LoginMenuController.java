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
	
	public static boolean esCliente=false;
	public static boolean esComercio=false;
	public static boolean esAdmin=false;
	public static boolean esAnonimo=true;
	//Metodo para cargar el login de cada uno de los usuarios y pasar de una vista a otra.
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
		esCliente=true;
		esComercio=false;
		esAdmin=false;
		esAnonimo=false;
	}
	
	@FXML
	public void handleCommerceButton(ActionEvent event) throws IOException{
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginComercio.fxml"));
		LoginController log = new LoginController();
		
		//loader.setController(log);
		Parent nextScreen = loader.load();
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show();
		esCliente=false;
		esComercio=true;
		esAdmin=false;
		esAnonimo=false;
	}
	
	@FXML
	public void handleAdminButton(ActionEvent event) throws IOException{
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginAdmin.fxml"));
		LoginController log = new LoginController();
		
		//loader.setController(log);
		Parent nextScreen = loader.load();
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show();
		esCliente=false;
		esComercio=false;
		esAdmin=true;
		esAnonimo=false;
	}
	
}
