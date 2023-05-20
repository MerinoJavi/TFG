package mm.makery.app.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import  javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import mm.makery.app.model.*;



public class RegistroClienteController {

	@FXML
	private Button registerButton;
	@FXML
	private TextField nombreField;
	@FXML
	private TextField apellidosField;
	@FXML
	private TextField usuarioField;
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passField;
	@FXML
	private DatePicker fechanacimientoField;
	@FXML
	private TextField provinciaField;
	@FXML
	private TextField paisField;
	@FXML
	private TextField direccionField;
	
	
	//TODO: hacer registro de usuario, comprobar TODOS los datos y si sale todo bien, que muestre una alerta de todo OK
	public RegistroClienteController() {
	}
	public static int generateRandomNumber(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative");
        }
        // generar un número aleatorio entre 0 y `n`. Cambio la semilla para evitar repeticiones en numeros random y no sea la misma siempre
        return new Random(System.currentTimeMillis()).nextInt(n + 1);
    }
	
	@FXML
	private void handleSaveButtonAction(ActionEvent event) { //TODO: Comprobar errores en los campos
	    String nombre = nombreField.getText();
	    String apellidos = apellidosField.getText();
	    LocalDate fechaNacimiento = fechanacimientoField.getValue();
	    String password= passField.getText();
	    String usuario = usuarioField.getText();
	    String email = emailField.getText();
	    String provincia = provinciaField.getText();
	    String pais = paisField.getText();
	    String direccion = direccionField.getText();
	    
	    //Compruebo si algun campo está vacio. Faltan errores varios concretos de cada uno de los campos (nombre incorrecto, email con formato no valido...)
		if (nombre.isEmpty() || apellidos.isEmpty() || fechaNacimiento == null || password.isEmpty()
				|| usuario.isEmpty() || email.isEmpty() || provincia.isEmpty() || pais.isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Error");
			a.setHeaderText("Error en algun campo introducido. Revise los campos");
			a.setContentText("Revise los campos de nuevo");
			a.showAndWait();
		}
		
		
		//Encripto contraseña
		String salt = BCrypt.gensalt();
		String hashedpassword = BCrypt.hashpw(password, salt);
		
		//Guardo datos en la BBDD haciendo inyeccion en SQL
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG","root","9P$H7nI5!*8p");
			int id=generateRandomNumber(1000);
			String sql = "INSERT INTO cliente(idCliente,Nombre,Apellidos,fechanacimiento,password,usuario,email,provincia,pais,direccion,salt) VALUES ("
					+ id + ",'" + nombre + "','" + apellidos + "','" + fechaNacimiento + "','"
					+ hashedpassword + "','" + usuario + "','" + email + "','" + provincia + "','" + pais+"','"+direccion+"','"+salt+"')";
			PreparedStatement st = conexion.prepareStatement(sql);
		    st.executeUpdate();
			//Creo el objeto cliente con los datos del formulario
		    Cliente c = new Cliente(nombre,apellidos,email,provincia,direccion,salt,fechaNacimiento);
		    //Limpio campos
		    salt = "";
		    hashedpassword = "";
		    
			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("¡Has sido registrado con éxito!");
			a.setHeaderText(null);
			a.setContentText("Ya puedes iniciar sesion en la plataforma");
			a.showAndWait();
			
			st.close();
			conexion.close();
			
		}catch(SQLException e) {
			//Mostrar mensaje de error al intentar registrar el usuario
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setHeaderText(null);
	        alert.setContentText("No se pudo guardar los datos. Por favor, inténtalo de nuevo.");
	        alert.showAndWait();
	        e.printStackTrace();
		}
		//Limpiamos los campos
		nombreField.clear();
		apellidosField.clear();
		usuarioField.clear();
		emailField.clear();
		passField.clear();
		fechanacimientoField.setValue(null);
		provinciaField.clear();
		paisField.clear();
		direccionField.clear();
		
		
	}	
}
