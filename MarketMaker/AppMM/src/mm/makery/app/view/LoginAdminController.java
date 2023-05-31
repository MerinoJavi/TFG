package mm.makery.app.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mm.makery.app.model.SesionUsuario;

public class LoginAdminController {

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

	    public LoginAdminController() {
	    	
	    }
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
	            //statusLabel.setText("Bienvenido,  " + username);
	            usernameField.clear();
	            passwordField.clear();
	            //Cargo la pantalla del usuario
	        	FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaAdmin.fxml"));
	    		LoginController log = new LoginController();
	    		//Cargo la siguiebnte pantalla en la escena actual, para ello hago un cast a Stage 
	    		Parent nextScreen = loader.load();
	    		Scene nextScreenScene = new Scene(nextScreen);
	    		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    		currentStage.setScene(nextScreenScene);
	    		currentStage.show();
	            
	        } else {
	        	Alert alert = new Alert(Alert.AlertType.ERROR);
	        	alert.setHeaderText(null);
	        	alert.setTitle("Error al iniciar sesión");
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
			String sql = "SELECT nombre,apellidos,usuario,contraseña,email,salt FROM administrador WHERE usuario = '" + username + "'";
			 st = conexion.prepareStatement(sql);
			 result = st.executeQuery();
			if (result.next()) {
				String usuarioBBDD = result.getString("usuario");// atributo que se encuentra en la BBDD
				String passwordHashed = result.getString("contraseña");// password hasheada en la bbdd
				String salt = result.getString("salt");
				
				String nombre = result.getString("nombre");
				String apellidos = result.getString("apellidos");
				String email = result.getString("email");
				//Date fecha = result.getDate("fechanacimiento");
				//String provincia = result.getString("provincia");
				//String pais = result.getString("pais");
				//String direccion = result.getString("direccion");
				
				
				//Sesion del usuario almacenada en el arraylist
				SesionUsuario sesion = new SesionUsuario(nombre, apellidos, null, usuarioBBDD, email, null, null, null,null,null,null);
				SesionUsuario.usuarioABuscar = usuarioBBDD;
				SesionUsuario.usuarios.add(sesion);
				// Comparar usuario con usuario introducido y contraseña hasheada en la base de
				// datos con la contraseña introducida. Para ello, tengo que volver a hashearla
				// con el mismo salt
				//El administrador hashea con SHA-256
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				String passhashed =hashPasswordSHA256(password, salt);
				//st.close();
				//conexion.close();
				return usuarioBBDD.equals(username) && passwordHashed.equals(passhashed);
			}
			}catch(SQLException | NoSuchAlgorithmException e) {
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
		
		private String hashPasswordSHA256(String password,String salt) throws NoSuchAlgorithmException {
			String saltedPassword = salt+password;
			//Clase para utilzar sha-256
			MessageDigest dig = MessageDigest.getInstance("SHA-256"); //Establezco algoritmo
			byte[]hashBytes = dig.digest(saltedPassword.getBytes(StandardCharsets.UTF_8)); //Completes the hash computation by performing final operationssuch as padding. The digest is reset after this call is made
			
			StringBuilder string = new StringBuilder();
			for(byte b:hashBytes) {
				String hex = String.format("%02x", b); //Convierto el byte en su representacion hexadecimal con un ancho de 2 digitos como minomo para formar el hash
				string.append(hex);
			}
			return string.toString();
		}
		
		public void handlegoBack(ActionEvent event) throws IOException{
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginMenu.fxml"));
			LoginController log = new LoginController();
			
			//loader.setController(log);
			Parent nextScreen = loader.load();
			Scene nextScreenScene = new Scene(nextScreen);
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(nextScreenScene);
			currentStage.show();
			
		}
}
