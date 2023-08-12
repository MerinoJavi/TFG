package mm.makery.app.view;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Statement;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import javafx.event.ActionEvent;
import  javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import mm.makery.app.Main;
import mm.makery.app.model.SesionUsuario;

public class PerfilClienteController {
	
	
	@FXML
	private Button logoutbutton;
	@FXML
	private Button deleteProfile;
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
	private Label user;
	
	@FXML
	private Button saveEdits = new Button("Guardar");
	@FXML
	private Button cancelEdits = new Button("Cancelar");
	@FXML
	private Button editButton;
	@FXML
	private TextField usuarioField = new TextField();
	@FXML
	private TextField nombreField = new TextField();
	@FXML
	private TextField apellidosField = new TextField();
	@FXML
	private TextField correoField = new TextField();
	@FXML
	private TextField provinciaField = new TextField();
	@FXML
	private TextField paisField = new TextField();
	@FXML
	private TextField direccionField = new TextField();
	@FXML
	private PasswordField passField=new PasswordField();
	@FXML
	private PasswordField repeatpassField = new PasswordField();

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
	
	//Obtengo los datos de la sesion del usuario
	@FXML
	private void initialize() { //Se ejecuta automaticamente cuando ejecuto el FXML
		
		for(SesionUsuario sesion: SesionUsuario.usuarios) {
			if(sesion.getUsuario().equals(SesionUsuario.usuarioABuscar)) {
				//Almacenar los datos en las label
				//System.out.println(sesion.getUsuario());
				Connection conexion;
				try {
					conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
					String sql = "SELECT * from cliente where usuario='"+SesionUsuario.usuarioABuscar+"'";
					Statement st = conexion.createStatement(); 
					ResultSet result = st.executeQuery(sql);
					if(result.next()) {
						user.setText(result.getString("usuario"));
						nombre.setText(result.getString("Nombre"));
						apellidos.setText(result.getString("Apellidos"));
						correo.setText(result.getString("email"));
						provincia.setText(result.getString("provincia"));
						pais.setText(result.getString("pais"));
						direccion.setText(result.getString("direccion"));
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Alert a = new Alert(AlertType.ERROR);
					a.setTitle("Error al buscar el usuario");
					a.setContentText("No se ha encontrado el usuario");
					a.showAndWait();
				}
				
			}
		}
		
	}
	@FXML
	private void handleDeleteProfile(ActionEvent event) {
		Connection conex=null;
		PreparedStatement st = null;
		ResultSet result = null;
		 try {
			 conex = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			 String sql = "DELETE from cliente where usuario='"+user.getText()+"'";
			 st = conex.prepareStatement(sql);
			 //Mensaje de alerta para confirmar la accion
			 Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			 alert.setHeaderText(null);
			 alert.setTitle("Confirmación");
			 alert.setContentText("¿Está seguro de confirmar la acción?");
			 Optional<ButtonType> action = alert.showAndWait();
			 
			 if(action.get()==ButtonType.OK) {
				int numfilaseliminadas= st.executeUpdate();
				if(numfilaseliminadas >0){//El delete se ha hecho correctamente
					Alert alertInfo = new Alert(AlertType.INFORMATION);
		            alertInfo.setTitle("Perfil eliminado");
		            alertInfo.setHeaderText(null);
		            alertInfo.setContentText("El perfil ha sido eliminado exitosamente.");
		            alertInfo.showAndWait();
		            //me voy a la pagina de login
		            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginClient.fxml"));
		    		LoginController log = new LoginController();
		    		
		    		//loader.setController(log);
		    		Parent nextScreen = loader.load();
		    		Scene nextScreenScene = new Scene(nextScreen);
		    		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    		currentStage.setScene(nextScreenScene);
		    		currentStage.show();
		            
				}else {
					Alert error = new Alert(AlertType.INFORMATION);
		            error.setTitle("Error al eliminar el perfil.");
		            error.setHeaderText(null);
		            error.setContentText("El perfil no ha podido ser eliminado");
		            error.showAndWait();
				}
				
				
			 }else {
				 // Nada
			 }
			
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al eliminar el perfil");
			alert.setHeaderText(null);
	        alert.setContentText("No se pudo eliminar el perfil. Inténtelo de nuevo.");
	        alert.showAndWait();
			e.printStackTrace();
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
				provinciaField.setText("");
				paisField.setText("");
				direccionField.setText("");
				// Agrego los campos al formulario, y los botones guardar y cancelar
				formulario.getChildren().addAll(new Label("Nombre: "), nombreField);
				formulario.getChildren().addAll(new Label("Apellidos: "), apellidosField);
				formulario.getChildren().addAll(new Label("Usuario: "), usuarioField);
				formulario.getChildren().addAll(new Label("Correo: "), correoField);
				formulario.getChildren().addAll(new Label("Provincia: "), provinciaField);
				formulario.getChildren().addAll(new Label("País: "), paisField);
				formulario.getChildren().addAll(new Label("Dirección: "), direccionField);
				
				//Pongo los datos actuales del perfil
				nombreField.setText(nombre.getText());
				apellidosField.setText(apellidos.getText());
				usuarioField.setText(user.getText());
				correoField.setText(correo.getText());
				provinciaField.setText(provincia.getText());
				paisField.setText(pais.getText());
				direccionField.setText(direccion.getText());
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
						String sql = "UPDATE cliente SET Nombre='" + nombreField.getText() + "',Apellidos='"
								+ apellidosField.getText() + "',usuario='" + usuarioField.getText() + "',email='"
								+ correoField.getText() + "',provincia='" + provinciaField.getText() + "',pais='"
								+ paisField.getText() + "',direccion='" + direccionField.getText() + "' WHERE usuario='"
								+ user.getText() + "'";
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
						FXMLLoader loader = new FXMLLoader(getClass().getResource("PerfilCliente.fxml"));
						LoginController log = new LoginController();
						
						//loader.setController(log);
						Parent nextScreen = loader.load();
						Scene nextScreenScene = new Scene(nextScreen);
						Stage c = (Stage) ((Node) event.getSource()).getScene().getWindow();
						currentStage.setScene(nextScreenScene);
						currentStage.show();
						
						initialize();

					} catch (SQLException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				});

				cancelEdits.setOnAction(event -> {
					// Cargo la pagina XML correspondiente al menu de logins
					FXMLLoader loader = new FXMLLoader(getClass().getResource("PerfilCliente.fxml"));
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
	
	@FXML
	private void goBack(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaCliente.fxml"));
		LoginController log = new LoginController();
		
		//loader.setController(log);
		Parent nextScreen = loader.load();
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show();
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
						String salt = BCrypt.gensalt();
						
						String passhashed = null;
						passhashed = BCrypt.hashpw(passField.getText(), salt);
						
						try {
							Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
									"9P$H7nI5!*8p");
							String sql = "UPDATE cliente SET password='" + passhashed + "', salt='" + salt + "' WHERE usuario='" + user.getText() + "'";
							PreparedStatement st = conexion.prepareStatement(sql);
							st.executeUpdate();

							Alert a = new Alert(AlertType.INFORMATION);
							a.setTitle("¡Cambios guardados!");
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
				cancelEdits.setOnAction(e->{
					FXMLLoader loader = new FXMLLoader(getClass().getResource("PerfilComercio.fxml"));
		    		LoginController log = new LoginController();
		    		//Cargo la siguiebnte pantalla en la escena actual, para ello hago un cast a Stage 
		    		Parent nextScreen;
					try {
						nextScreen = loader.load();
						Scene nextScreenScene = new Scene(nextScreen);
			    		Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
			    		currentStage.setScene(nextScreenScene);
			    		currentStage.show();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		
				});
	}

}
