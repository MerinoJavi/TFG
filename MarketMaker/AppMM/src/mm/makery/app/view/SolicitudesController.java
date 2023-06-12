package mm.makery.app.view;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class SolicitudesController {

	@FXML
	private VBox nombres = new VBox();
	@FXML
	private VBox datos = new VBox();
	@FXML
	private Button acceptButton = new Button("Aceptar");
	@FXML
	private Button rejectButton = new Button("Rechazar");

	@FXML
	public void initialize() {
		List<String> nombresComercios = obtenerNombresComercios();
		for (String nombre : nombresComercios) {
			Button botonNombre = new Button(nombre);
			botonNombre.setOnAction(event -> mostrarDatosComercio(nombre));
			nombres.getChildren().add(botonNombre);

		}

	}

	// Se muestra los datos de la solicitud del comercio a la que se hace click.
	// TODO: Borrar el botón correspondiente
	@FXML
	private void mostrarDatosComercio(String nombrecomercio) {
		Connection conexion;

		Label nombreLabel;
		Label nifLabel;
		Label municipio;
		Label provincia;
		Label codigopostal;
		Label pais;
		Label direccion;
		Label telefono;
		Label email;
		// Limpio datos que hubiesen anteriormente si he clickado antes en otro comercio
		datos.getChildren().clear();
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			String sql = "SELECT * FROM solicitudcomercio where nombre='" + nombrecomercio + "'";
			Statement st = conexion.createStatement();
			ResultSet result = st.executeQuery(sql);
			while (result.next()) {
				nombreLabel = new Label("Nombre: " + nombrecomercio + "\n\n");
				nifLabel = new Label("NIF: " + result.getString("nif"));
				municipio = new Label("Municipio: " + result.getString("municipio"));
				provincia = new Label("Provincia: " + result.getString("provincia"));
				codigopostal = new Label("Codigo postal: " + result.getString("codigopostal"));
				pais = new Label("Pais: " + result.getString("pais"));
				direccion = new Label("Direccion: " + result.getString("direccion"));
				telefono = new Label("Telefono: " + result.getString("telefono"));
				email = new Label("Email: " + result.getString("email"));
				// añado las label de la informacion de la solicitud enviada por el comercio,
				// con los datos añadidos anteriormente sacados de la consulta a la tabla
				// solicitudcomercio
				//Hago una consulta que se ejecutara mas adelante si la solicitud es aceptada 
			//	String str = "INSERT INTO comercio(nombre,nif,municipio,provincia,codigopostal,pais,direccion,telefono,email) VALUES ('" + nombrecomercio + "', '" + nifLabel.getText() + "','" + municipio.getText() + "','" + provincia.getText() + "','" + codigopostal.getText() + "','" + pais.getText() + "','" + direccion.getText() + "','" + telefono.getText() + "','" + email.getText() + "')";

				datos.getChildren().addAll(nombreLabel, nifLabel, municipio, provincia, codigopostal, pais, direccion,
						telefono, email);

				datos.getChildren().add(acceptButton);
				datos.getChildren().add(rejectButton);

				acceptButton.setOnAction(event -> {
					Connection conAceptar;
					ResultSet r;
					Statement statement = null;

					try {
						conAceptar = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
								"9P$H7nI5!*8p");
						String s = "DELETE from solicitudcomercio where nombre='" + nombrecomercio + "'"; // Creo la consulta pero no la disparo todavia, hasta que no sea confirmada
						statement = conAceptar.prepareStatement(s);

						// Mensaje de alerta para confirmar la accion
						Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
						alert.setHeaderText(null);
						alert.setTitle("Confirmación");
						alert.setContentText("¿Está seguro de confirmar la acción?");
						Optional<ButtonType> action = alert.showAndWait();
						if (action.get() == (ButtonType.OK)) { // Acepto la solicitud
							int numfilaseliminadas = st.executeUpdate(s);
							if (numfilaseliminadas > 0) {
								Alert alertInfo = new Alert(AlertType.INFORMATION);
								alertInfo.setTitle("¡Solicitud aceptada!");
								alertInfo.setHeaderText(null);
								alertInfo.setContentText("La solicitud ha sido aceptada con éxito. ");
								alertInfo.showAndWait();
								//Añado la solicitud al comercio, aunque falta por ponerle contraseña y salt. Que me salte un formulario para añadir una contraseña creada por el administrasdor, generar salt aleatoria, hashear con sha256 y añadirlo a la bbdd, y ya directamente se crea el usuario, para automatizarlo todo :)
							//	statement = conAceptar.prepareStatement(str);
							//	statement.executeUpdate(str);
								//Creo de nuevo la escena para actualizar la pagina de solicitudes
								FXMLLoader loader = new FXMLLoader(getClass().getResource("Solicitudes.fxml"));
								Parent nextScreen = loader.load();
								Scene nextScreenScene = new Scene(nextScreen);
								Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
								currentStage.setScene(nextScreenScene);
								currentStage.show();
							}
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.err.println("Error en la base de datos");
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						result.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						st.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						conexion.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				});

				rejectButton.setOnAction(ev->{
					Connection conRechazar;
					ResultSet r;
					Statement statement = null;

					try {
						conRechazar = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
								"9P$H7nI5!*8p");
						String s = "DELETE from solicitudcomercio where nombre='" + nombrecomercio + "'"; // Creo la consulta pero no la disparo todavia, hasta que no sea confirmada
						statement = conRechazar.prepareStatement(s);

						// Mensaje de alerta para confirmar la accion
						Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
						alert.setHeaderText(null);
						alert.setTitle("Confirmación");
						alert.setContentText("¿Está seguro de confirmar la acción?");
						Optional<ButtonType> action = alert.showAndWait();
						if (action.get() == (ButtonType.OK)) { // Acepto la solicitud
							int numfilaseliminadas = st.executeUpdate(s);
							if (numfilaseliminadas > 0) {
								Alert alertInfo = new Alert(AlertType.INFORMATION);
								alertInfo.setTitle("Solicitud eliminada");
								alertInfo.setHeaderText(null);
								alertInfo.setContentText("La solicitud ha sido rechazada. Notifica al comercio para que vuelva a realizar la solicitud.");
								alertInfo.showAndWait();
								//Creo de nuevo la escena para actualizar la pagina de solicitudes
								FXMLLoader loader = new FXMLLoader(getClass().getResource("Solicitudes.fxml"));
								Parent nextScreen = loader.load();
								Scene nextScreenScene = new Scene(nextScreen);
								Stage currentStage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
								currentStage.setScene(nextScreenScene);
								currentStage.show();
								
							}
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.err.println("Error en la base de datos");
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						result.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						st.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						conexion.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<String> obtenerNombresComercios() {
		List<String> nombrescomercios = new ArrayList<String>();
		Connection conexion;
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			String sql = "SELECT nombre FROM solicitudcomercio";
			Statement st = conexion.createStatement();
			ResultSet result = st.executeQuery(sql);

			while (result.next()) {
				String nombre = result.getString("nombre");
				nombrescomercios.add(nombre);
			}

			result.close();
			st.close();
			conexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nombrescomercios;

	}

}
