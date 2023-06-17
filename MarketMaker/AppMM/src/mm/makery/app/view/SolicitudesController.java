package mm.makery.app.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
	private PasswordField pass = new PasswordField();
	@FXML
	private PasswordField repeatpass = new PasswordField();
	@FXML
	private Button saveEdits = new Button("Guardar");
	@FXML
	private Button cancelEdits = new Button("Cancelar");
	@FXML
	private TextField usuario=new TextField();
	@FXML
	private PasswordField password=new PasswordField();
	@FXML
	public void initialize() {
		List<String> nombresComercios = obtenerNombresComercios();
		for (String nombre : nombresComercios) {
			Button botonNombre = new Button(nombre);
			botonNombre.setOnAction(event -> {
				try {
					mostrarDatosComercio(nombre);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			nombres.getChildren().add(botonNombre);

		}

	}

	// Se muestra los datos de la solicitud del comercio a la que se hace click.
	// TODO: Borrar el botón correspondiente
	@FXML
	private void mostrarDatosComercio(String nombrecomercio) throws NoSuchAlgorithmException {
		//Labels para mostrar datos
	    Label nombreLabel;
	    Label nifLabel;
	    Label municipio;
	    Label provincia;
	    Label codigopostal;
	    Label pais;
	    Label direccion;
	    Label telefono;
	    Label email;
	    
	    //Labels para almacenarlos en la BBDD
	    Label nombreBD;
	    Label nifBD;
	    Label municipioBD;
	    Label provinciaBD;
	    Label paisBD;
	    Label codigopostalBD;
	    Label direccionBD;
	    Label telefonoBD;
	    Label emailBD;
	    
	    //Borro todo por si acaso
	    datos.getChildren().clear();

	    try (Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
	         Statement st = conexion.createStatement()) {

	        String sql = "SELECT * FROM solicitudcomercio where nombre='" + nombrecomercio + "'";
	        ResultSet result = st.executeQuery(sql);

	      

	        if (result.next()) {
	        	String nif = "";
	  	        String municipioStr = "";
	  	        String provinciaStr = "";
	  	        String codigopostalStr = "";
	  	        String paisStr = "";
	  	        String direccionStr = "";
	  	        String telefonoStr = "";
	  	        String emailStr = "";
	  	        
	        	nif = result.getString("nif");
	            municipioStr = result.getString("municipio");
	            provinciaStr = result.getString("provincia");
	            codigopostalStr = result.getString("codigopostal");
	            paisStr = result.getString("pais");
	            direccionStr = result.getString("direccion");
	            telefonoStr = result.getString("telefono");
	            emailStr = result.getString("email");
	            //Labels para mostrar los datos
	            nombreLabel = new Label("Nombre: " + nombrecomercio + "\n\n");
	            nifLabel = new Label("NIF: " + nif);
	            municipio = new Label("Municipio: " + municipioStr);
	            provincia = new Label("Provincia: " + provinciaStr);
	            codigopostal = new Label("Codigo postal: " + codigopostalStr);
	            pais = new Label("Pais: " + paisStr);
	            direccion = new Label("Direccion: " + direccionStr);
	            telefono = new Label("Telefono: " + telefonoStr);
	            email = new Label("Email: " + emailStr);
	            //Labels para almacenar los datos
	            nombreBD=new Label(nombrecomercio);
	            nifBD=new Label(nif);
	            municipioBD=new Label(municipioStr);
	            provinciaBD = new Label(provinciaStr);
	            codigopostalBD=new Label(codigopostalStr);
	            paisBD=new Label(paisStr);
	            direccionBD=new Label(direccionStr);
	            telefonoBD=new Label(telefonoStr);
	            emailBD=new Label(emailStr);
	            
	            datos.getChildren().addAll(nombreLabel, nifLabel, municipio, provincia, codigopostal, pais, direccion,
	                    telefono, email);
	            datos.getChildren().addAll(new Label("Introduce el nombre de usuario: "), usuario);
	            datos.getChildren().addAll(new Label("Introduce la contraseña: "), password);
	            datos.getChildren().add(acceptButton);
	            datos.getChildren().add(rejectButton);

	            acceptButton.setOnAction(event -> {
	                try (Connection conAceptar = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
	                        "9P$H7nI5!*8p");
	                     Statement statement = conAceptar.createStatement()) {
	                	
	                    String s = "DELETE from solicitudcomercio where nombre='" + nombrecomercio + "'";
	                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	                    alert.setHeaderText(null);
	                    alert.setTitle("Confirmación");
	                    alert.setContentText("¿Está seguro de confirmar la acción?");
	                    Optional<ButtonType> action = alert.showAndWait();

	                    if (action.get() == ButtonType.OK) {
	                        int numfilaseliminadas = statement.executeUpdate(s);
	                        if (numfilaseliminadas > 0) {
	                            try {
	                                MessageDigest digest = MessageDigest.getInstance("SHA-256");
	                                String salt = cadenaAleatoria(8);
	                                String passhashed = hashPasswordSHA256(password.getText(), salt);

	                                String str = "INSERT INTO comercio(nombre,nif,municipio,provincia,codigopostal,pais,direccion,telefono,email,contraseña,usuario,salt) VALUES ('"
	                                        + nombrecomercio + "', '" + nifBD.getText()+ "', '"
	                                        + municipioBD.getText() + "', '"
	                                        + provinciaBD.getText() + "', '" + codigopostalBD.getText()
	                                        + "', '" + paisBD.getText()+ "', '" + direccionBD.getText()
	                                        + "', '" + telefonoBD.getText() + "', '" + emailBD.getText()
	                                        + "', '" + passhashed + "', '" + usuario.getText() + "', '" + salt + "')";
	                                try (PreparedStatement savecommerce = conAceptar.prepareStatement(str)) {
	                                    savecommerce.executeUpdate();
	                                }

	                                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
	                                alertInfo.setTitle("¡Solicitud aceptada!");
	                                alertInfo.setHeaderText(null);
	                                alertInfo.setContentText("La solicitud ha sido aceptada con éxito. Proporciona los datos al comercio para que pueda iniciar sesión");
	                                alertInfo.showAndWait();

	                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Solicitudes.fxml"));
	                                Parent nextScreen = loader.load();
	                                Scene nextScreenScene = new Scene(nextScreen);
	                                Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                                current.setScene(nextScreenScene);
	                                current.show();
	                            } catch (IOException e) {
	                                e.printStackTrace();
	                            } catch (NoSuchAlgorithmException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	                        }
	                    }

	                } catch (SQLException e) {
	                    System.err.println("Error en la base de datos");
	                    e.printStackTrace();
	                }
	            });

	            rejectButton.setOnAction(ev -> {
	                try (Connection conRechazar = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
	                        "9P$H7nI5!*8p");
	                     Statement statement = conRechazar.createStatement()) {

	                    String s = "DELETE from solicitudcomercio where nombre='" + nombrecomercio + "'";
	                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	                    alert.setHeaderText(null);
	                    alert.setTitle("Confirmación");
	                    alert.setContentText("¿Está seguro de confirmar la acción?");
	                    Optional<ButtonType> action = alert.showAndWait();

	                    if (action.get() == ButtonType.OK) {
	                        int numfilaseliminadas = statement.executeUpdate(s);
	                        if (numfilaseliminadas > 0) {
	                            try {
	                                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
	                                alertInfo.setTitle("Solicitud eliminada");
	                                alertInfo.setHeaderText(null);
	                                alertInfo.setContentText("La solicitud ha sido eliminada correctamente");
	                                alertInfo.showAndWait();

	                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Solicitudes.fxml"));
	                                Parent nextScreen = loader.load();
	                                Scene nextScreenScene = new Scene(nextScreen);
	                                Stage current = (Stage) ((Node) ev.getSource()).getScene().getWindow();
	                                current.setScene(nextScreenScene);
	                                current.show();
	                            } catch (IOException e) {
	                                e.printStackTrace();
	                            }
	                        }
	                    }

	                } catch (SQLException e) {
	                    System.err.println("Error en la base de datos");
	                    e.printStackTrace();
	                }
	            });
	        }

	    } catch (SQLException e) {
	        System.err.println("Error en la base de datos");
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
	
	//Los tres siguientes metodos es para hashear la contraseña del comercio en SHA-256
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

}
