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
import javax.mail.Authenticator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
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
import javafx.scene.control.Separator;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class SolicitudesController extends Authenticator{ 

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
	//Inicializo el FXML para que aparezcan las solicitudes al ser ejecutado
	public void initialize() {
		List<String> nombresComercios = obtenerNombresComercios();
		for (String nombre : nombresComercios) {
			Button botonNombre = new Button(nombre);
			botonNombre.setMaxWidth(Double.MAX_VALUE); //Establezco anchura de boton para que ocupe lo máximo posible 
			//Aplico estilo al boton
			botonNombre.setStyle("-fx-text-fill: black; -fx-font-size: 14.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent; -fx-font-style: italic;");
			//Cuando pase el ratón por encima
			botonNombre.setOnMouseEntered(e->{
				botonNombre.setStyle("-fx-font-style: italic; -fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-text-fill: black; -fx-font-size: 14.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
			});
			//Cuando se quita el cursor del raton de encima
			botonNombre.setOnMouseExited(e->{
				botonNombre.setStyle("-fx-font-family: 'Segoe UI Light'; -fx-font-style: italic; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-text-fill: black; -fx-font-size: 14.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
			});
			//Muestro los datos de la solicitud cuando hago clic
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
	            nifLabel = new Label("NIF: " + nif+ "\n\n");
	            municipio = new Label("Municipio: " + municipioStr+ "\n\n");
	            provincia = new Label("Provincia: " + provinciaStr+ "\n\n");
	            codigopostal = new Label("Codigo postal: " + codigopostalStr+ "\n\n");
	            pais = new Label("País: " + paisStr+ "\n\n");
	            direccion = new Label("Dirección: " + direccionStr+ "\n\n");
	            telefono = new Label("Teléfono: " + telefonoStr+ "\n\n");
	            email = new Label("Email: " + emailStr+ "\n\n");
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
	            //Agrego los datos al VBox correspondiente, y aplico la hoja de estilos para modificar el estilo predeterminado que poseen los Labels de JavaFX
	            nombreLabel.setStyle("-fx-text-fill:white; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic;");
	            nifLabel.setStyle("-fx-text-fill:white; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px;-fx-font-style: italic;");
	            municipio.setStyle("-fx-text-fill:white;-fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px;-fx-font-style: italic;");
	            provincia.setStyle("-fx-text-fill:white;-fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px;-fx-font-style: italic;");
	            codigopostal.setStyle("-fx-text-fill:white;-fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px;-fx-font-style: italic;");
	            pais.setStyle("-fx-text-fill:white;-fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px;-fx-font-style: italic;");
	            direccion.setStyle("-fx-text-fill:white;-fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px;-fx-font-style: italic;");
	            telefono.setStyle("-fx-text-fill:white;-fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px;-fx-font-style: italic;");
	            email.setStyle("-fx-text-fill:white;-fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px;-fx-font-style: italic;");
	            
				datos.getChildren().addAll(nombreLabel, new Separator(Orientation.HORIZONTAL), nifLabel,
						new Separator(Orientation.HORIZONTAL), municipio, new Separator(Orientation.HORIZONTAL),
						provincia, new Separator(Orientation.HORIZONTAL), codigopostal,
						new Separator(Orientation.HORIZONTAL), pais, new Separator(Orientation.HORIZONTAL), direccion,
						new Separator(Orientation.HORIZONTAL), telefono, new Separator(Orientation.HORIZONTAL), email,new Separator(Orientation.HORIZONTAL));
				datos.getChildren().add(new Label("\n\n"));
				
				//Establezco estilo para los botones
				usuario.setPromptText("Usuario"); //Texto que sale por defecto en el campo del usuario, idem con contraseña
				password.setPromptText("********");
				
				usuario.setStyle("-fx-background-radius: 30.0; -fx-prompt-text-fill: white; -fx-background-color: transparent; -fx-font-style: italic;");
				password.setStyle("-fx-background-radius: 30.0; -fx-prompt-text-fill: white; -fx-background-color: transparent; -fx-font-style: italic;");
				
				Label title = new Label("En caso de que la solicitud sea aprobada...");
				title.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 25.0px; -fx-font-style: italic; -fx-font-weight:bold;");

				//Añado campos de usuario y contraseña, y le aplico los estilos correspondientes
				datos.getChildren().add(title);
				Label userTitle = new Label("Introduce el usuario: ");
				Label passwordTitle=new Label("Introduce la contraseña:");
				
				userTitle.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
				passwordTitle.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
				
				datos.getChildren().add(userTitle);
	            datos.getChildren().addAll(usuario, new Separator(Orientation.HORIZONTAL));
	            datos.getChildren().add(passwordTitle);
	            datos.getChildren().addAll(password);
	            //Aplico estilo CSS a los botones aceptar y rechazar
	            acceptButton.setMaxWidth(Double.MAX_VALUE); //Establezco anchura de boton para que ocupe lo máximo posible 
	            rejectButton.setMaxWidth(Double.MAX_VALUE);
	            
	            acceptButton.setStyle("-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
	            acceptButton.setOnMouseEntered(e->{
					acceptButton.setStyle("-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
				});
				//Cuando se quita el cursor del raton de encima
				acceptButton.setOnMouseExited(e->{
					acceptButton.setStyle("-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
				});
				
	            rejectButton.setStyle("-fx-font-family: 'Segoe UI Light'; -fx-font-size: 17.0px;-fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
				rejectButton.setOnMouseEntered(e->{
					rejectButton.setStyle("-fx-font-family: 'Segoe UI Light'; -fx-background-color: #237F08; -fx-text-fill: black; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
				});
				//Cuando se quita el cursor del raton de encima
				rejectButton.setOnMouseExited(e->{
					rejectButton.setStyle("-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
				});
				
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
	                                //Lanzo alerta de solicitud
	                                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
	                                alertInfo.setTitle("¡Solicitud aceptada!");
	                                alertInfo.setHeaderText(null);
	                                alertInfo.setContentText("La solicitud ha sido aceptada con éxito.");
	                                alertInfo.showAndWait();
	                                
	                                //Correo de confirmacion
	                                enviarCorreo(nombrecomercio,usuario.getText(),password.getText(), "marketmakertfg@gmail.com");
	                                
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
	                                
	                                enviarCorreoDenegacion(nombrecomercio,usuario.getText(),password.getText(), "marketmakertfg@gmail.com");
	                                
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
	
	@FXML
	private void goBack(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaAdmin.fxml"));
		LoginController log = new LoginController();
		
		//loader.setController(log);
		Parent nextScreen = loader.load();
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show();
	}
	//Envia un correo automaticamente para confirmar que ya está registrado, junto a su usuario y contraseña. En este caso solo uso de gmail uno creado e inventado
	private static void enviarCorreo(String nombrecomercio,String usuario, String password, String destinatario) {
		//Variables de las partes que forman un email.
		String remitente="marketmakertfg@gmail.com";
		String asunto="Registro en plataforma. Resolución";
		String msg = "Estimado " + nombrecomercio + ",\n\n"
                + "Su solicitud ha sido revisada por un administrador del sistema, que ha APROBADO satisfactoriamente su registro en nuestra aplicación"+"\n\n"
				+"Tu nombre de usuario es: " + usuario + "\n\n"
                + "Tu contraseña es: " + password + "\n\n"
                + "Recuerda cambiar las credenciales al iniciar sesión por primera vez y no compartir estos datos con nadie"+"\n\n"
                +"¡Gracias por registrarte!";
	    //La clave de aplicación obtenida:
	    String claveemail = "zsmznuxfauftexzk";

	    Properties props = System.getProperties(); //Objeto para establecer las propiedades para el envio del correo (servidor, contraseña de aplicación...)
	    props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
	    props.put("mail.smtp.user", remitente); //Remitente del correo
	    props.put("mail.smtp.clave", claveemail);    //La clave de aplicación para aplicaciones de terceros
	    props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave, estableciendo una conexión segura
	    props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
	    props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

	    Session session = Session.getDefaultInstance(props); //Creo la sesión con las propiedades establecidas
	    MimeMessage message = new MimeMessage(session); //Se crea el objeto para enviar el email

	    try {
	        message.setFrom(new InternetAddress(remitente));
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));   //Se añade el destinatario del correo
	        message.setSubject(asunto); //Asunto del email
	        message.setText(msg); //Cuerpo del correo
	        Transport transport = session.getTransport("smtp"); //Establezco protocolo SMTP para el envío del correo
	        transport.connect("smtp.gmail.com", remitente, claveemail); //Establecimiento de la conexión con el servidor SMTP para el envío del correo
	        transport.sendMessage(message, message.getAllRecipients()); //Envío del correo
	      //  System.out.println("MENSAJE ENVIADO");
	        transport.close(); //Cierre de conexión
	    }
	    catch (MessagingException me) {
	        System.out.println(me.getMessage());   //Si se produce un error
	    }
	    
	    
	}
	//Usando JavaMail, se envia un correo para que el comercio sea informado de que su solicitud ha sido rechazada
	private static void enviarCorreoDenegacion(String nombrecomercio,String usuario, String password, String destinatario) {
		String remitente="marketmakertfg@gmail.com";
		String asunto="Registro en plataforma. Resolución";
		String msg = "Estimado " + nombrecomercio + ", \n\n"
				+ "Lamento comunicarle que su solicitud ha sido rechazada por un administrador de nuestro sistema. Puede rellenarla de nuevo si así lo desea.";
	    //La clave de aplicación obtenida:
	    String claveemail = "zsmznuxfauftexzk";

	    Properties props = System.getProperties();
	    props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
	    props.put("mail.smtp.user", remitente);
	    props.put("mail.smtp.clave", claveemail);    //La clave de la cuenta
	    props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
	    props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
	    props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

	    Session session = Session.getDefaultInstance(props);
	    MimeMessage message = new MimeMessage(session);

	    try {
	        message.setFrom(new InternetAddress(remitente));
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));   //Se podrían añadir varios de la misma manera
	        message.setSubject(asunto);
	        message.setText(msg);
	        Transport transport = session.getTransport("smtp");
	        transport.connect("smtp.gmail.com", remitente, claveemail);
	        transport.sendMessage(message, message.getAllRecipients());
	//        System.out.println("MENSAJE ENVIADO");
	        transport.close();
	    }
	    catch (MessagingException me) {
	        System.out.println(me.getMessage());   //Si se produce un error
	    }
	    
	    
	}

}
