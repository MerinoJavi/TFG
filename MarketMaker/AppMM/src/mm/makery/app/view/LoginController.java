package mm.makery.app.view;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import mm.makery.app.view.PerfilClienteController;
import java.time.LocalDate;

import org.mindrot.jbcrypt.BCrypt;
import mm.makery.app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import mm.makery.app.model.SesionUsuario;
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

	
    public Button getlogoutButton() {
		return logoutButton;
    	
    }
    public static int generateRandomNumber(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative");
        }
        // generar un número aleatorio entre 0 y `n`. Cambio la semilla para evitar repeticiones en numeros random y no sea la misma siempre
        return new Random(System.currentTimeMillis()).nextInt(n + 1);
    }
    @FXML
    private void handleLogin(ActionEvent event) throws IOException, SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (isValidCredentials(username, password)) {
            isLoggedIn = true;
          //  statusLabel.setText("Bienvenido,  " + username);
            usernameField.clear();
            passwordField.clear();
            //Cargo la pantalla del usuario
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaCliente.fxml"));
    		LoginController log = new LoginController();
    		Parent nextScreen = loader.load();
    		Scene nextScreenScene = new Scene(nextScreen);
    		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    		//Para que salga en la misma pantalla y no abra otra ventana
    		currentStage.setScene(nextScreenScene);
    		currentStage.show();
            
        } else {
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
	private boolean isValidCredentials(String username, String password)  {
		// Logica de autenticación
		Connection conexion=null;
		PreparedStatement st = null;
		ResultSet result = null;
	
		
		try {
		 conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
		String sql = "SELECT usuario,password,salt,Nombre,Apellidos,email,provincia,pais,direccion,fechanacimiento FROM cliente WHERE usuario = '" + username + "'";
		 st = conexion.prepareStatement(sql);
		 result = st.executeQuery();
		if (result.next()) {
			String usuarioBBDD = result.getString("usuario");// atributo que se encuentra en la BBDD
			String passwordHashed = result.getString("password");// password hasheada en la bbdd
			String salt = result.getString("salt");
			//Datos del administrador que requiere acceso a la plataforma
			String nombre = result.getString("Nombre");
			String apellidos = result.getString("Apellidos");
			String email = result.getString("email");
			Date fecha = result.getDate("fechanacimiento");
			String provincia = result.getString("provincia");
			String pais = result.getString("pais");
			String direccion = result.getString("direccion");
			
			
			//Sesion del usuario almacenada en el arraylist
			SesionUsuario sesion = new SesionUsuario(nombre, apellidos, fecha, usuarioBBDD, email, provincia, pais, direccion,null,null,null,null);
			SesionUsuario.usuarioABuscar = usuarioBBDD;
			SesionUsuario.usuarios.add(sesion);
			// Comparar usuario con usuario introducido y contraseña hasheada en la base de
			// datos con la contraseña introducida. Para ello, tengo que volver a hashearla
			// con el mismo salt
			String passhashed = BCrypt.hashpw(password, salt);
			//st.close();
			//conexion.close();
			return usuarioBBDD.equals(username) && passwordHashed.equals(passhashed);
		}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally { //Tratamiento de recursos utilizados. Cerrarlos todos
			try {
				if(result!=null) {
					result.close();
				}
				if(st!=null) {
					st.close();
				}
				if(conexion!=null) {
					conexion.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return false;

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

