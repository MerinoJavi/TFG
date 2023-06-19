package mm.makery.app.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mm.makery.app.model.SesionUsuario;
import javafx.stage.FileChooser;
import javafx.scene.control.TableCell;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ProductosComercioController {
	@FXML
	private Button accept=new Button("Aceptar");
	@FXML
	private Button reject=new Button("Volver");
	@FXML
	private FileChooser fc= new FileChooser();
	@FXML
	private VBox infoproducto;
	@FXML
	private VBox botones;
	@FXML
	private Button edit = new Button("Editar...");
	@FXML
	private Button saveEdits = new Button("Guardar");
	@FXML
	private Button cancelEdits = new Button("Cancelar");
	//Campos para editar el producto
	@FXML
	private TextField nombreField = new TextField();
	@FXML
	private TextField pvpField = new TextField();
	@FXML
	private TextField descripcionField = new TextField();
	@FXML
	private Button delete = new Button("Eliminar producto");
	private String imagen="";
	private String newImagen=""; //String en caso de que se quiera cambiar la ruta de la imagen si se quiere coger otra
	private String path;
	private String nifcomercio;

	private int idproducto;
	@FXML
	private void initialize() { // Se ejecuta automaticamente cuando ejecuto el FXML
		
		for (SesionUsuario sesion : SesionUsuario.usuarios) {
			if (sesion.getUsuario().equals(SesionUsuario.usuarioABuscar)) {
				// Almaceno la ID del comercio
				nifcomercio = sesion.getNif();
			}
			List<String>productos = getProductos();
			for(String p:productos) {
				Separator separator = new Separator(Orientation.HORIZONTAL);
				Button boton = new Button(p);
				//Establezco el ancho del boton al mismo que el del VBox para que ocupe toda su celda
				boton.setMaxWidth(Double.MAX_VALUE);
				botones.setVgrow(boton, Priority.ALWAYS);
				botones.getChildren().addAll(boton,separator);
				botones.setSpacing(5); //Espacio de separacion de 10px
				boton.setOnAction(event->{
					mostrarDatos(p);
				});
				boton=null;
			}
		}
		//Tengo que crear un boton por cada producto que tenga el comercio, y que al clikcar me muestre la informacion d ese producto
	}
	
	//Ademas de mostrar los datos del producto, tengo las opciones de editar los datos y de eliminarlos, que los realizo en este metodo
	@FXML
	private void mostrarDatos(String nombreproducto) {
		//Para sacar los datos de la bbdd
		Label nombre=new Label("");
		Label pvp=new Label("");
		Label descripcion=new Label("");
		
		
		//Para guardar en la base de datos
		Label nombreBD;
		Label pvpBD;
		Label descrBD;
		Label imagenBD;

		infoproducto.getChildren().clear(); //Borramos todos los datos anteriores que hubieran
		
		//Proceso para leer y visualizar los datos del producto
		try {
			Connection conex = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
					"9P$H7nI5!*8p");
			//Recopilo la informacion, añado botones y hago la funcionalidad de cada uno
			//Para evitar ataques de inyeccion SQL, le paso los parametros más tarde
			String sql = "SELECT idproducto,nombre,pvp,descripcion,imagen from producto where idcomercio= ? and nombre=?";
			PreparedStatement st = conex.prepareStatement(sql);
			st.setString(1, nifcomercio);
			st.setString(2, nombreproducto);
			ResultSet result = st.executeQuery();
			
			//Almaceno los valores de la base de datos para mostrarlos. El tratamiento de la imagen queda pendiente para mas adelante
			if(result.next()) {
				nombre.setText(result.getString("nombre"));
				pvp.setText(result.getString("pvp"));
				descripcion.setText(result.getString("descripcion"));
				imagen = result.getString("imagen");
			}
			//Ahora meto los datos en el VBox creado en Scenebuilder para mostrar los datos. El boton de salvar o cancelar los datos también los incluyo
			infoproducto.getChildren().addAll(new Label("Título: "),nombre);
			infoproducto.getChildren().addAll(new Label("PVP: "),pvp);
			infoproducto.getChildren().addAll(new Label("Descripción: "),descripcion);
			
			
			//Transformacion de la ruta de la imagen para que se muestre la imagen correctamente. Obtengo la ruta y lo transformo en un objeto ImageView de JavaFX
			String fixpath = imagen.replace(":", ":\\\\").replaceAll("TFG", "TFG\\\\")
					.replace("MarketMaker", "MarketMaker\\\\").replaceAll("AppMM", "AppMM\\\\")
					.replace("src", "src\\\\").replace("images", "images\\\\");

			//Cargo la imagen
			InputStream i = new FileInputStream(fixpath);
			Image image = new Image(i);
			ImageView imageview = new ImageView(image);
			imageview.setFitWidth(100);
			imageview.setFitHeight(100);
			infoproducto.getChildren().add(imageview);
			fixpath="";
			//Añado boton editar y eliminar
			infoproducto.getChildren().add(edit);
			infoproducto.getChildren().add(delete);
			edit.setOnAction(e->{ 
				//Pongo los datos que hay guardados en la bbdd. Creo formulario y añado los campos
				VBox formulario = new VBox(10);
				//Creo boton para editar imagen
				Button editImage = new Button("Añadir imagen");
				// Formulario con los campos y los botones
				formulario.getChildren().addAll(new Label("Nombre: "),nombreField);
				formulario.getChildren().addAll(new Label("PVP: "),pvpField);
				formulario.getChildren().addAll(new Label("Descripción"),descripcionField);
				formulario.getChildren().add(editImage);
				formulario.getChildren().add(saveEdits);
				formulario.getChildren().add(cancelEdits);
				//Pongo los datos actuales de la base de datos
				nombreField.setText(nombre.getText());
				pvpField.setText(pvp.getText());
				descripcionField.setText(descripcion.getText());
				// Crear escena con el formulario
				Scene formularioScene = new Scene(formulario, 600, 400);
				// Obtengo ventana actual
				Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
				currentStage.setScene(formularioScene);
				currentStage.show();
				
				editImage.setOnAction(evento->{
					File selected = fc.showOpenDialog(currentStage);

					if (selected != null) {// Obtengo la nueva ruta y la guardo mas tarde en la BBDD
						newImagen = selected.getAbsolutePath();

					}
				});
				//Si el boton no ha sido presionado, que siga con la misma imagen
				if(!editImage.isPressed()) {
					newImagen=imagen;
				}
				saveEdits.setOnAction(ev->{
					try {
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
								"9P$H7nI5!*8p");
						String consulta = "UPDATE producto SET nombre='"+nombreField.getText()+"', pvp='"+pvpField.getText()+"', descripcion='"+descripcionField.getText()+"', imagen='"+newImagen+"'"+"WHERE idcomercio='"+nifcomercio+"' and nombre='"+nombre.getText()+"'";
						PreparedStatement statement = con.prepareStatement(consulta);
						statement.executeUpdate();
						
						Alert a = new Alert(AlertType.INFORMATION);
						a.setTitle("¡Cambios guardados");
						a.setHeaderText("");
						a.setContentText("Los cambios ha sido guardados con éxito");
						a.showAndWait();
						
						//Cierro recursos de BBDD
						st.close();
						con.close();
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				
				cancelEdits.setOnAction(event->{
					// Cargo la pagina XML correspondiente al menu de logins
					FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductosComercio.fxml"));
					Parent nextScreen = null;
					try {
						nextScreen = loader.load();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// Cargo el XML siguiente en una nueva escena, que posteriormente casteo la
					// ventana que obtengo y la establezco en la escena actual para que no me cree
					// otra ventana.
					Scene nextScreenScene = new Scene(nextScreen);
					Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
					currentStage.setScene(nextScreenScene);
					currentStage.show(); 
				});
				
				delete.setOnAction(evento->{
					Connection conEliminar;
					try {
						conEliminar = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
								"9P$H7nI5!*8p");
					Statement statement = conEliminar.createStatement();
					String deleteQuery = "DELETE FROM producto where idcomercio='"+nifcomercio+"' and nombre='"+nombre.getText()+"'";
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Está seguro de confirmar la acción?");
                    Optional<ButtonType> action = alert.showAndWait();
                    if(action.get()==ButtonType.OK) {
                    	int filaeliminada = st.executeUpdate(deleteQuery);
                    	if(filaeliminada>0) {
                    		Alert a = new Alert(Alert.AlertType.INFORMATION);
                            a.setHeaderText(null);
                            a.setTitle("¡Producto eliminado!");
                            a.setContentText("El producto ha sido eliminado satisfactoriamente");
                    	}else {
                    		Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setHeaderText(null);
                            error.setTitle("Error al eliminar!");
                            error.setContentText("El producto no existe");
                    	}
                    }
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
				});
			});
		} catch (SQLException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//Obtengo los productos del comercio para que los botones lleven su nombre
	private List<String>getProductos(){
		List<String>productos = new ArrayList<>();
		Connection conexion;
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			String sql = "SELECT nombre from producto where idcomercio="+nifcomercio;
			Statement st = conexion.createStatement();
			ResultSet result = st.executeQuery(sql);

			while (result.next()) {
				String nombre = result.getString("nombre");
				productos.add(nombre);
			}
			//Cierro recursos de la base de datos
			result.close();
			st.close();
			conexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productos;
	}
	
	@FXML
	private void handleAddProduct(ActionEvent e) {
		VBox formulario = new VBox(10);

		TextField nombreproducto = new TextField();
		TextField pvp = new TextField();
		TextField descripcion = new TextField();

		nombreproducto.setText("");
		pvp.setText("");
		descripcion.setText("");
		// Configurar el título y los filtros de archivos
		fc.setTitle("Seleccionar imagen");
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png"),
				new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));

		Button image = new Button("Añadir imagen");
		// Creo y añado los datos al formulario, y añado los botones
		formulario.getChildren().addAll(new Label("Nombre: "), nombreproducto);
		formulario.getChildren().addAll(new Label("PVP: "), pvp);
		formulario.getChildren().addAll(new Label("Descripción: "), descripcion);
		formulario.getChildren().add(image);
		formulario.getChildren().add(accept);
		formulario.getChildren().add(reject);

		// Crear escena con el formulario
		Scene formularioScene = new Scene(formulario, 600, 400);
		// Obtengo ventana actual
		Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		currentStage.setScene(formularioScene);
		currentStage.show();
		image.setOnAction(event -> {
			File selected = fc.showOpenDialog(currentStage);

			if (selected != null) {// Obtengo la ruta y la guardo mas tarde en la BBDD
				path = selected.getAbsolutePath();

			}
		});
		accept.setOnAction(ev -> {
			try {
				Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
						"9P$H7nI5!*8p");
				// Genero el numero aleatorio del id del producto.
				Random r = new Random();
				int randomnumber = r.nextInt(1000);
				String sql = "INSERT INTO producto(idproducto,nombre,pvp,descripcion,imagen,idcomercio)VALUES('"
						+ randomnumber + "', '" + nombreproducto.getText() + "', '" + pvp.getText() + "', '"
						+ descripcion.getText() + "', '" + path + "', '" + nifcomercio + "')";
				PreparedStatement st = conexion.prepareStatement(sql);
				st.executeUpdate();

				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("Producto añadido!");
				a.showAndWait();
				//Cargo de nuevo la pagina
				 FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaComercio.fxml"));
                 Parent nextScreen = loader.load();
                 Scene nextScreenScene = new Scene(nextScreen);
                 Stage actual = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                 actual.setScene(nextScreenScene);
                 actual.show();

				st.close();
				conexion.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		reject.setOnAction(eve -> {

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductosComercio.fxml"));
		        Parent nextScreen = loader.load();
		        Scene nextScreenScene = new Scene(nextScreen);

		        // Obtener la ventana actual
		        Stage ventanaactual = (Stage) ((Node) eve.getSource()).getScene().getWindow();

		        // Establecer la escena en la ventana actual
		        ventanaactual.setScene(nextScreenScene);
		        ventanaactual.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
	}
}
