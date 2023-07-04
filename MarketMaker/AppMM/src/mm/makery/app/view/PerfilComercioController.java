package mm.makery.app.view;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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

public class PerfilComercioController {

	@FXML
	private Button logoutbutton;
	@FXML
	private Button deleteProfile;
	@FXML
	private Label user;
	@FXML
	private Label nombre;
	@FXML
	private Label NIF;
	@FXML
	private Label municipio;
	@FXML
	private Label provincia;
	@FXML
	private Label codigopostal;
	@FXML
	private Label direccion;
	@FXML
	private Label tlf;
	@FXML
	private Label email;
	@FXML
	private Label pais;
	
	@FXML
	private Button saveEdits = new Button("Guardar");
	@FXML
	private Button cancelEdits = new Button("Cancelar");
	@FXML
	private Button editDataComercio;
	@FXML
	private TextField nombreField = new TextField();
	@FXML
	private TextField nifField = new TextField();
	@FXML
	private TextField municipioField = new TextField();
	@FXML
	private TextField provinciaField = new TextField();
	@FXML
	private TextField codigopostalField = new TextField();
	@FXML
	private TextField paisField = new TextField();
	@FXML
	private TextField direccionField = new TextField();
	@FXML
	private TextField telefonoField = new TextField();
	@FXML
	private TextField emailField = new TextField();
	@FXML
	private PasswordField password = new PasswordField();
	@FXML
	private TextField usuarioField = new TextField();
	
	@FXML
	   //****************Cerrar sesion*******************************//
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
			currentStage.show();
	        //logoutButton.setDisable(true);
	    }
	
	//Obtengo los datos de la sesion del usuario
	@FXML
	private void initialize() { // Se ejecuta automaticamente cuando ejecuto el FXML

		for (SesionUsuario sesion : SesionUsuario.usuarios) {
			if (sesion.getUsuario().equals(SesionUsuario.usuarioABuscar)) {
				// Almacenar los datos en las label
				// System.out.println(sesion.getUsuario());

				user.setText(sesion.getUsuario());
				nombre.setText(sesion.getNombre());
				NIF.setText(sesion.getNif());
				municipio.setText(sesion.getMunicipio());
				provincia.setText(sesion.getProvincia());
				pais.setText(sesion.getPais());
				codigopostal.setText(sesion.getCodigopostal());
				direccion.setText(sesion.getDireccion());
				tlf.setText(sesion.getTelefono());
				email.setText(sesion.getEmail());
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
			 String sql = "DELETE from comercio where usuario='"+user.getText()+"'";
			 st = conex.prepareStatement(sql);
			 //Mensaje de alerta para confirmar la accion
			 Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			 alert.setHeaderText(null);
			 alert.setTitle("Confirmación");
			 alert.setContentText("¿Está seguro que desea realizar esta acción?");
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
		            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginComercio.fxml"));
		    	//	LoginController log = new LoginController();
		    		
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
		usuarioField.setText("");
		nifField.setText("");
		municipioField.setText("");
		provinciaField.setText("");
		codigopostalField.setText("");
		paisField.setText("");
		direccionField.setText("");
		telefonoField.setText("");
		emailField.setText("");
		
		
		// Agrego los campos al formulario, y los botones guardar y cancelar
		formulario.getChildren().addAll(new Label("Nombre: "), nombreField);
		formulario.getChildren().addAll(new Label("Usuario: "), usuarioField);
		formulario.getChildren().addAll(new Label("NIF: "), nifField);
		formulario.getChildren().addAll(new Label("Municipio: "), municipioField);
		formulario.getChildren().addAll(new Label("Provincia: "), provinciaField);
		formulario.getChildren().addAll(new Label("Codigo posta: "), codigopostalField);
		formulario.getChildren().addAll(new Label("Pais: "), paisField);
		formulario.getChildren().addAll(new Label("Direccion: "), direccionField);
		formulario.getChildren().addAll(new Label("Telefono: "), telefonoField);
		formulario.getChildren().addAll(new Label("Email: "), emailField);
		
		//Pongo los datos actuales del perfil
		nombreField.setText(nombre.getText());
		usuarioField.setText(user.getText());
		nifField.setText(NIF.getText());
		municipioField.setText(municipio.getText());
		provinciaField.setText(provincia.getText());
		codigopostalField.setText(codigopostal.getText());
		paisField.setText(pais.getText());
		direccionField.setText(direccion.getText());
		telefonoField.setText(tlf.getText());
		emailField.setText(email.getText());
		
		
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
				String sql = "UPDATE comercio SET nombre='" + nombreField.getText() + "',nif='" + nifField.getText()
						+ "',municipio='" + municipioField.getText() + "',provincia='" + provinciaField.getText()
						+ "',codigopostal='" + codigopostalField.getText() + "',pais='" + paisField.getText()
						+ "',direccion='" + direccionField.getText() + "',telefono='" + telefonoField.getText()
						+ "',email='" + emailField.getText() + "',usuario='" + usuarioField.getText()
						+ "' WHERE usuario='" + usuarioField.getText() + "'";
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PerfilComercio.fxml"));
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
		
		
	
	
	
}