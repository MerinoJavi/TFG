package mm.makery.app.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import  javafx.scene.control.Button;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

public class PerfilClienteController {
	
	
	@FXML
	private Button logoutbutton;
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

}
