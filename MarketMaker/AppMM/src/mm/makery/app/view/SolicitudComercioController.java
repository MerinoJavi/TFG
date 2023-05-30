package mm.makery.app.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class SolicitudComercioController {

	@FXML
	private Button enviar;
	@FXML
	private Button cancelar;
	@FXML
	private TextField nombre;
	@FXML
	private TextField NIF;
	@FXML
	private TextField municipio;
	@FXML
	private TextField provincia;
	@FXML
	private TextField codigopostal;
	@FXML
	private TextField pais;
	@FXML
	private TextField direccion;
	@FXML
	private TextField telefono;
	@FXML
	private TextField email;
	
	
	@FXML
	private void handleSaveButtonAction(ActionEvent event) {
		String name = nombre.getText();
		String nif = NIF.getText();
		String mun = municipio.getText();
		String prov = provincia.getText();
		String cod = codigopostal.getText();
		String p = pais.getText();
		String dir = direccion.getText();
		String tlf = telefono.getText();
		String correo = email.getText();
		
		if(name.isEmpty() || nif.isEmpty() || mun.isEmpty()||prov.isEmpty()||cod.isEmpty()||p.isEmpty()||dir.isEmpty()||tlf.isEmpty()||correo.isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Error");
			a.setHeaderText("Error en algun campo introducido. Revise los campos");
			a.setContentText("Revise los campos de nuevo");
			a.showAndWait();
		}
		
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG","root","9P$H7nI5!*8p");
			String sql = "INSERT INTO solicitudcomercio(nombre,nif,municipio,provincia,codigopostal,pais,direccion,telefono,email) VALUES ('"
		             + name + "','" + nif + "','" + mun + "','" + prov + "','" + cod + "','" + p + "','" + dir + "','" + tlf + "','" + correo + "')";
			PreparedStatement prep = conexion.prepareStatement(sql);
			prep.executeUpdate();
			prep.close();
			
			conexion.close();
		}catch(SQLException e) {
			//Mostrar mensaje de error al intentar guardar la solicitud en la BBDD
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setHeaderText(null);
	        alert.setContentText("No se pudo guardar los datos. Por favor, inténtalo de nuevo.");
	        alert.showAndWait();
	        e.printStackTrace();
		}
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("¡Solicitud enviada! :-)");
		alert.setHeaderText(null);
        alert.setContentText("La solicitud está a la espera de ser aceptada por un administrador. Será comunicado por el correo introducido en la solicitud una vez se sepa la resolución de la misma.");
        alert.showAndWait();
		
		//Borro los campos
		nombre.clear();
		NIF.clear();
		municipio.clear();
		provincia.clear();
		codigopostal.clear();
		pais.clear();
		direccion.clear();
		telefono.clear();
		email.clear();
		
	}
	
	
}
