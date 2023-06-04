package mm.makery.app.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonAreaLayout;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mm.makery.app.model.SesionUsuario;

public class PerfilAdminController {

	@FXML
	private Label usuario;
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
	@FXML
	private Button editDataAdmin;
	@FXML
	private Button saveEdits = new Button("Guardar");
	@FXML
	private Button cancelEdits = new Button("Cancelar");
	@FXML
	private TextField usuarioField = new TextField();
	@FXML
	private TextField apellidosField = new TextField();
	@FXML
	private TextField nombreField = new TextField();
	@FXML
	private TextField correoField = new TextField();
	@FXML
	private PasswordField passField=new PasswordField();
	@FXML
	private PasswordField repeatpassField = new PasswordField();
	@FXML
	private Label newName = new Label("Nombre: ");

	@FXML
	// ****************Cerrar sesion*******************************//
	private void handleLogout(ActionEvent event) throws IOException {
		// isLoggedIn = false;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginMenu.fxml"));
		// LoginController log = new LoginController();

		// loader.setController(log);
		Parent nextScreen = loader.load();
		// log = loader.getController();
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show();
		// logoutButton.setDisable(true);
	}

	@FXML
	private void initialize() { // Se ejecuta automaticamente cuando ejecuto el FXML

		for (SesionUsuario sesion : SesionUsuario.usuarios) {
			if (sesion.getUsuario().equals(SesionUsuario.usuarioABuscar)) {
				// Almacenar los datos en las label
				// System.out.println(sesion.getUsuario());

				usuario.setText(sesion.getUsuario());
				nombre.setText(sesion.getNombre());
				apellidos.setText(sesion.getApellidos());
				correo.setText(sesion.getEmail());
				// provincia.setText(sesion.getProvincia());
				// pais.setText(sesion.getPais());
				// direccion.setText(sesion.getDireccion());
			}
		}

	}

	@FXML
	private void handleEditButton(ActionEvent e) {
		// Creo formulario
		VBox formulario = new VBox(10);
		nombreField.setText("");
		apellidosField.setText("");
		usuarioField.setText("");
		correoField.setText("");
		// Agrego los campos al formulario, y los botones guardar y cancelar
		formulario.getChildren().addAll(new Label("Nombre: "), nombreField);
		formulario.getChildren().addAll(new Label("Apellidos: "), apellidosField);
		formulario.getChildren().addAll(new Label("Usuario: "), usuarioField);
		formulario.getChildren().addAll(new Label("Correo: "), correoField);
		
		//Pongo los datos actuales del perfil
		nombreField.setText(nombre.getText());
		apellidosField.setText(apellidos.getText());
		usuarioField.setText(usuario.getText());
		correoField.setText(correo.getText());
		//Añado los botones
		formulario.getChildren().add(saveEdits);
		formulario.getChildren().add(cancelEdits);
		// Crear escena con el formulario

		Scene formularioScene = new Scene(formulario, 600, 400);
		// Obtengo ventana actual
		Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		currentStage.setScene(formularioScene);

		// Guardar los cambios en la base de datos
		saveEdits.setOnAction(event -> {
			try {
				Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
						"9P$H7nI5!*8p");
				String sql = "UPDATE administrador SET usuario='" + usuarioField.getText() + "',nombre='"
						+ nombreField.getText() + "',apellidos='" + apellidosField.getText() + "',email='"
						+ correoField.getText() + "' WHERE usuario='" + usuario.getText() + "'";
				PreparedStatement st = conexion.prepareStatement(sql);
				st.executeUpdate();

				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("¡Cambios guardados");
				a.setHeaderText(null);
				a.setContentText(null);
				a.showAndWait();
				// Cierro recursos, esta vez lo hago asi porque estoy en una expresion lambda
				st.close();
				conexion.close();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		cancelEdits.setOnAction(event -> {
			// Cargo la pagina XML correspondiente al menu de logins
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PerfilAdmin.fxml"));
			// LoginController log = new LoginController();

			// loader.setController(log);
			Parent nextScreen = null;
			try {
				nextScreen = loader.load();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// log = loader.getController();
			// Cargo el XML siguiente en una nueva escena, que posteriormente casteo la
			// ventana que obtengo y la establezco en la escena actual para que no me cree
			// otra ventana.
			Scene nextScreenScene = new Scene(nextScreen);
			Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(nextScreenScene);
			currentStage.show(); // Muestro la pagina LoginMenu
			// logoutButton.setDisable(true);
		});

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
	
	public static int numeroAleatorioEnRango(int minimo, int maximo) {
	    // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
	    return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
	}
	
	public static String cadenaAleatoria(int longitud) {
	    // El banco de caracteres
	    String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	    // La cadena en donde iremos agregando un carácter aleatorio
	    String cadena = "";
	    for (int x = 0; x < longitud; x++) {
	        int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
	        char caracterAleatorio = banco.charAt(indiceAleatorio);
	        cadena += caracterAleatorio;
	    }
	    return cadena;
	}

	@FXML
	private void changePassword(ActionEvent event) throws NoSuchAlgorithmException {
		// Creo formulario
				VBox formulario = new VBox(10);
				passField.setText("");
				repeatpassField.setText("");
				
				formulario.getChildren().addAll(new Label("Nueva contraseña: "),passField);
				formulario.getChildren().addAll(new Label("Repite la contraseña: "),repeatpassField);
				formulario.getChildren().add(saveEdits);
				formulario.getChildren().add(cancelEdits);
				
				// Crear escena con el formulario y mostrarlo en la misma ventana

				Scene formularioScene = new Scene(formulario, 600, 400);
				// Obtengo ventana actual
				Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				currentStage.setScene(formularioScene);
				saveEdits.setOnAction(ev->{
					if(passField.getText().equals(repeatpassField.getText())) {
						String salt = cadenaAleatoria(8);
						try {
							MessageDigest digest = MessageDigest.getInstance("SHA-256");
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String passhashed = null;
						try {
							passhashed = hashPasswordSHA256(passField.getText(), salt);
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						try {
							Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
									"9P$H7nI5!*8p");
							String sql = "UPDATE administrador SET contraseña='" + passhashed + "', salt='" + salt + "' WHERE usuario='" + usuario.getText() + "'";
							PreparedStatement st = conexion.prepareStatement(sql);
							st.executeUpdate();

							Alert a = new Alert(AlertType.INFORMATION);
							a.setTitle("¡Cambios guardados");
							a.setHeaderText(null);
							a.setContentText(null);
							// Cierro recursos, esta vez lo hago asi porque estoy en una expresion lambda
							st.close();
							conexion.close();
							Optional<ButtonType> action = a.showAndWait();
							if(action.get()==ButtonType.OK) {
								FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaAdmin.fxml"));
					    		LoginController log = new LoginController();
					    		//Cargo la siguiebnte pantalla en la escena actual, para ello hago un cast a Stage 
					    		Parent nextScreen = loader.load();
					    		Scene nextScreenScene = new Scene(nextScreen);
					    		Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
					    		currentStage.setScene(nextScreenScene);
					    		currentStage.show();
							}
							
							

						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}else {// Alerta de contraseña no coincidentes
						Alert a = new Alert(AlertType.ERROR);
						a.setTitle("Las contraseñas no coinciden.");
						a.setHeaderText(null);
						a.setContentText(null);
						a.showAndWait();
					}
				});

	}
}
