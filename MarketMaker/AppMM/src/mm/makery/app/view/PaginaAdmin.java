package mm.makery.app.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PaginaAdmin {

	@FXML
	private Button logoutButton;
	@FXML
	private Button profile;
	@FXML
	private Button settings;
	@FXML
	private Button solicitudes;
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
}
