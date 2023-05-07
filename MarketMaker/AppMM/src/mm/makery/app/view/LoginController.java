package mm.makery.app.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.image.*;
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button goBackButton;
    
    @FXML
    private Label statusLabel;
     
    private boolean isLoggedIn = false;

	public LoginController() {
		
	}
    public Button getlogoutButton() {
		return logoutButton;
    	
    }
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (isValidCredentials(username, password)) {
            isLoggedIn = true;
            statusLabel.setText("Bienvenido,  " + username);
            usernameField.clear();
            passwordField.clear();
            
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaCliente.fxml"));
    		LoginController log = new LoginController();
    		Parent nextScreen = loader.load();
    		Scene nextScreenScene = new Scene(nextScreen);
    		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    		currentStage.setScene(nextScreenScene);
    		currentStage.show();
            
        } else {
            //statusLabel.setText("Los datos introducidos no son correctos.");
        	Alert alert = new Alert(Alert.AlertType.ERROR);
        	alert.setHeaderText(null);
        	alert.setTitle("Error");
        	alert.setContentText("Credenciales incorrectas");
        	alert.showAndWait();
        }
    }

    @FXML
   //****************Cerrar sesion*******************************//
    private void handleLogout(ActionEvent event) throws IOException {
        isLoggedIn = false;
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

    //Comprobar si el usuario existe
    private boolean isValidCredentials(String username, String password) {
        // Logica de autenticación
        return username.equals("usuario") && password.equals("contraseña");
    }
    
    //VUELVE A LA VISTA ANTERIOR
    /*
    public void goBack() {
    	Stage stage = (Stage) goBackButton.getScene().getWindow();
    	stage.close();
    	
    }
    */
  //VUELVE A LA PANTALLA DE LOGINMENU
  	public void handleGoBack(ActionEvent event) throws IOException {
  		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginMenu.fxml"));
  		Parent menu = loader.load();
  		//Obtengo la ventana para que me lo abra en la misma ventana.
  		
  		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
  		Scene mainScreen = new Scene(menu);
  		stage.setScene(mainScreen);
  		stage.show();
  		
  	}
  	
  	//Va a la pantalla de cada uno de los usuarios.
  	//TODO: Implementarlo para todos los usuarios. Ahora mismo sólo se implementará para el usuario cliente
  	/*
  	public void handleLogin(ActionEvent event) throws IOException{
  		FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaCliente.fxml"));
  		Parent menu = loader.load();
  		//Obtengo la ventana para que me lo abra en la misma ventana.
  		
  		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
  		Scene mainScreen = new Scene(menu);
  		stage.setScene(mainScreen);
  		stage.show();
  	}
  	*/
  	
    
    
}

