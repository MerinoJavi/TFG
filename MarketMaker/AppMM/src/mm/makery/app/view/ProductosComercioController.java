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
import javafx.scene.input.KeyCode;


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
	@FXML
	private TextField productField;
	
	private String imagen="";
	private String newImagen=""; //String en caso de que se quiera cambiar la ruta de la imagen si se quiere coger otra
	private String path;
	private String nifcomercio;
	private boolean found=false;
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
				final Button boton = new Button(p); //Variable final para crear los productos que tiene el comercio
				//Establezco el ancho del boton y los estilos que correspondan.
				boton.setMaxWidth(Double.MAX_VALUE);
				boton.setStyle("-fx-text-fill: black; -fx-font-size: 14.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent; -fx-font-style: italic;");
				boton.setOnMouseEntered(e->{
					boton.setStyle("-fx-font-style: italic; -fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-text-fill: black; -fx-font-size: 14.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
				});
				//Cuando se quita el cursor del raton de encima
				boton.setOnMouseExited(e->{
					boton.setStyle("-fx-font-family: 'Segoe UI Light'; -fx-font-style: italic; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-text-fill: black; -fx-font-size: 14.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
				});
				//	botones.setVgrow(boton, Priority.ALWAYS);
				botones.getChildren().addAll(boton);
				botones.setSpacing(5); //Espacio de separacion de 10px
				boton.setOnAction(event->{
					mostrarDatos(p);
				});
			//	boton=null;
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
			Label title = new Label("Título");
			Label pvpLabel = new Label("PVP(En €)");
			Label descripcionLabel = new Label("Descripción");
			
			title.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 30.0px; -fx-font-style: italic;");
			pvpLabel.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 30.0px; -fx-font-style: italic;");
			descripcionLabel.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 30.0px; -fx-font-style: italic;");			
			nombre.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 20.0px; ");
			pvp.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 20.0px; ");
			descripcion.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 20.0px; ");
			//añadimos la informacion al vbox
			infoproducto.getChildren().addAll(title,new Separator(Orientation.HORIZONTAL),nombre);
			infoproducto.getChildren().addAll(pvpLabel,new Separator(Orientation.HORIZONTAL),pvp);
			infoproducto.getChildren().addAll(descripcionLabel,new Separator(Orientation.HORIZONTAL),descripcion);
			
			
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
			fixpath=""; //Variable que uso para arreglar el path donde están guardadas las imágenes
			
			//Aplico el estilo correspondiente a los botones editar y cancelar, y los añado al VBox correspondiente
			edit.setMaxWidth(Double.MAX_VALUE);
			delete.setMaxWidth(Double.MAX_VALUE);
			
			edit.setStyle(
					"-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
			edit.setOnMouseEntered(e -> {
				edit.setStyle(
						"-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
			});
			edit.setOnMouseExited(e -> {
				edit.setStyle(
						"-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
			});
			delete.setStyle(
					"-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
			delete.setOnMouseEntered(e -> {
				delete.setStyle(
						"-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
			});
			delete.setOnMouseExited(e -> {
				delete.setStyle(
						"-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
			});
			infoproducto.getChildren().add(edit);
			infoproducto.getChildren().add(delete);
			
			//Boton para editar los datos del producto
			edit.setOnAction(e->{ 
				//Pongo los datos que hay guardados en la bbdd. Creo formulario y añado los campos
				VBox formulario = new VBox(10);
				//Creo boton para editar imagen
				Button editImage = new Button("*Añadir imagen");
				// Formulario con los campos y los botones
				saveEdits.setStyle(
						"-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
				saveEdits.setOnMouseEntered(eve -> {
					saveEdits.setStyle(
							"-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
				});
				// Cuando se quita el cursor del raton de encima
				saveEdits.setOnMouseExited(e3 -> {
					saveEdits.setStyle(
							"-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
				});
				
				Label titleEdit = new Label("*"+title.getText());
				titleEdit.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 30.0px; -fx-font-style: italic;");
				formulario.getChildren().addAll(titleEdit,nombreField);
				
				Label pvpEditLabel = new Label("*Precio (En €, expresar decimales con .)");
				pvpEditLabel.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 30.0px; -fx-font-style: italic;");
				formulario.getChildren().addAll(pvpEditLabel,pvpField);
				
				Label editDescripcionLabel = new Label("*"+descripcionLabel.getText());
				editDescripcionLabel.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 30.0px; -fx-font-style: italic;");
				formulario.getChildren().addAll(editDescripcionLabel,descripcionField);
				
				editImage.setStyle(
						"-fx-font-size: 30.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
				editImage.setOnMouseEntered(eve -> {
					editImage.setStyle(
							"-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 30.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
				});
				// Cuando se quita el cursor del raton de encima
				editImage.setOnMouseExited(e3 -> {
					editImage.setStyle(
							"-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 30.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
				});
				editImage.setMaxWidth(Double.MAX_VALUE);
				formulario.getChildren().add(editImage);
				formulario.getChildren().add(new Separator(Orientation.HORIZONTAL));
				saveEdits.setMaxWidth(Double.MAX_VALUE);
				formulario.getChildren().add(saveEdits);
				//estilo para el boton de cancelar la edicion
				cancelEdits.setStyle(
						"-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
				cancelEdits.setOnMouseEntered(eve -> {
					cancelEdits.setStyle(
							"-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
				});
				// Cuando se quita el cursor del raton de encima
				cancelEdits.setOnMouseExited(e3 -> {
					cancelEdits.setStyle(
							"-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
				});
				cancelEdits.setMaxWidth(Double.MAX_VALUE);
				formulario.getChildren().add(cancelEdits);
				Label obligatorio=new Label("(*) Campos obligatorios");
				formulario.getChildren().add(new Separator(Orientation.HORIZONTAL));
				formulario.getChildren().add(obligatorio);
				//Pongo los datos actuales de la base de datos
				nombreField.setText(nombre.getText());
				pvpField.setText(pvp.getText());
				descripcionField.setText(descripcion.getText());
				// Crear escena con el formulario
				Scene formularioScene = new Scene(formulario, 1024, 768);
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
					if(newImagen==null) {
						Alert a = new Alert(AlertType.ERROR);
						a.setTitle("Error al cargar la imagen");
						a.setContentText("Debe cargar la imagen del producto que desea añadir.");
						a.setHeaderText(null);
						a.showAndWait();
					}
					else if(!(Double.parseDouble(pvp.getText())>0)) { //El precio introducido es 0 o negativo
						Alert a = new Alert(AlertType.ERROR);
						a.setTitle("Precio no válido");
						a.setContentText("Introduzca un precio válido.");
						a.setHeaderText(null);
						a.showAndWait();
					}else if(descripcionField.getText().isEmpty()) {
						Alert a = new Alert(AlertType.ERROR);
						a.setTitle("Descripción no válida");
						a.setContentText("Debes introducir una descripción para detallar el producto.");
						a.setHeaderText(null);
						a.showAndWait();
					} else if(nombreField.getText().isEmpty()) {
						Alert a = new Alert(AlertType.ERROR);
						a.setTitle("Título no válido");
						a.setContentText("Debes introducir el nombre del producto.");
						a.setHeaderText(null);
						a.showAndWait();
					}
					else {
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
				
				
			});
			
			delete.setOnAction(evento->{
				Connection conEliminar=null;
				try {
					conEliminar = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root",
							"9P$H7nI5!*8p");
				Statement statement = conEliminar.createStatement();
				String deleteQuery = "DELETE FROM producto where idcomercio='"+nifcomercio+"' and nombre='"+nombre.getText()+"'";
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("Confirmación");
                alert.setContentText("¿Está seguro de confirmar la acción? Una vez lo haga no podrá restablecer los datos.");
                Optional<ButtonType> action = alert.showAndWait();
                if(action.get()==ButtonType.OK) {
                	int filaeliminada = st.executeUpdate(deleteQuery);
                	if(filaeliminada>0) {
                		Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setHeaderText(null);
                        a.setTitle("¡Producto eliminado!");
                        a.setContentText("El producto ha sido eliminado satisfactoriamente");
                        a.showAndWait();
                     // Recargo la pagina para comprobar que el objeto ha sido eliminado
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
    					Stage current = (Stage) ((Node) evento.getSource()).getScene().getWindow();
    					current.setScene(nextScreenScene);
    					current.show(); 
                        
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
		Label obligatorio = new Label("(*) Campos obligatorios");
		
		nombreproducto.setText("");
		pvp.setText("");
		descripcion.setText("");
		// Configurar el título y los filtros de archivos
		fc.setTitle("Seleccionar imagen");
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png"),
				new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
		//Creo las etiquetas y aplico estilos a los botones y etiquetas correspondientes a esta pantalla
		Button image = new Button("*Añadir imagen");
		Label nombreLabel = new Label("*Nombre");
		Label PVPLabel = new Label("*PVP(En €, expresar decimales con .)");
		Label descripcionLabel = new Label("*Descripcion");
		nombreLabel.setStyle(
				"-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
		PVPLabel.setStyle(
				"-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
		descripcionLabel.setStyle(
				"-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
		nombreproducto.setStyle(
				"-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
		pvp.setStyle(
				"-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
		descripcion.setStyle(
				"-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
		
		accept.setStyle(
				"-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
		accept.setOnMouseEntered(eve -> {
			accept.setStyle(
					"-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
		});
		// Cuando se quita el cursor del raton de encima
		accept.setOnMouseExited(e3 -> {
			accept.setStyle(
					"-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
		});
		
		reject.setStyle(
				"-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
		reject.setOnMouseEntered(eve -> {
			reject.setStyle(
					"-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
		});
		// Cuando se quita el cursor del raton de encima
		reject.setOnMouseExited(e3 -> {
			reject.setStyle(
					"-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
		});
		
		image.setStyle(
				"-fx-font-size: 30.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
		image.setOnMouseEntered(eve -> {
			image.setStyle(
					"-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 30.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
		});
		// Cuando se quita el cursor del raton de encima
		image.setOnMouseExited(e3 -> {
			image.setStyle(
					"-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 30.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
		});
		accept.setMaxWidth(Double.MAX_VALUE); //Para que ocupen el ancho de la ventana 
		reject.setMaxWidth(Double.MAX_VALUE);
		image.setMaxWidth(Double.MAX_VALUE);
		
		// añado los datos al formulario y los botones
		formulario.getChildren().addAll(nombreLabel, nombreproducto);
		formulario.getChildren().addAll(PVPLabel, pvp);
		formulario.getChildren().addAll(descripcionLabel, descripcion);
		formulario.getChildren().add(image);
		image.setMaxWidth(Double.MAX_VALUE);
		formulario.getChildren().add(accept);
		formulario.getChildren().add(reject);
		formulario.getChildren().add(new Separator(Orientation.HORIZONTAL));
		
		formulario.getChildren().add(obligatorio);

		// Crear escena con el formulario
		Scene formularioScene = new Scene(formulario, 1024, 768);
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
			//Que lance un error si no ha subido una imagen
			if(path==null) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error al cargar la imagen");
				a.setContentText("Debe cargar la imagen del producto que desea añadir.");
				a.setHeaderText(null);
				a.showAndWait();
			}
			else if(!(Double.parseDouble(pvp.getText())>0)) { //El precio introducido es 0 o negativo
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Precio no válido");
				a.setContentText("Introduzca un precio válido.");
				a.setHeaderText(null);
				a.showAndWait();
			} else if(descripcion.getText().isEmpty()) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Descripción vacía");
				a.setContentText("Introduce una descripción para detallar el producto.");
				a.setHeaderText(null);
				a.showAndWait();
			}else if(nombreproducto.getText().isEmpty()) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Nombre del producto vacío");
				a.setContentText("Introduce el nombre del producto.");
				a.setHeaderText(null);
				a.showAndWait();
			}
			else {
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
					a.setTitle("¡Producto añadido!");
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
	@FXML
	//Busqueda del producto en el campo textfield, devuelve el que he escrito o el que coincida con lo que escribo
	private void searchProduct(ActionEvent event) {
		String nombreproducto = productField.getText();
	
		if(nombreproducto.isEmpty()) {
							// Cargo la pagina XML correspondiente al menu de logins
					        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductosComercio.fxml"));
				
							Parent nextScreen;
							try {
								nextScreen = loader.load();
								//Cargo el XML siguiente en una nueva escena, que posteriormente casteo la ventana que obtengo y la establezco en la escena actual para que no me cree otra ventana.
								Scene nextScreenScene = new Scene(nextScreen);
								Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
								currentStage.setScene(nextScreenScene);
								currentStage.show(); //Muestro la pagina 
						        //logoutButton.setDisable(true);	
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}				
		}
		productField.setOnKeyPressed(e->{
			botones.getChildren().clear();
			if(e.getCode().equals(KeyCode.ENTER)) {
				for(String p:getProductos()) {
					if(p.toLowerCase().contains(nombreproducto.toLowerCase())) {
						found=true;
						Separator separator = new Separator(Orientation.HORIZONTAL);
						final Button boton = new Button(p);
						boton.setStyle(
								"-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
						boton.setOnMouseEntered(eve -> {
							boton.setStyle(
									"-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
						});
						// Cuando se quita el cursor del raton de encima
						boton.setOnMouseExited(e3 -> {
							boton.setStyle(
									"-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
						});
						//Establezco el ancho del boton al mismo que el del VBox para que ocupe toda su celda
						boton.setMaxWidth(Double.MAX_VALUE);
						botones.getChildren().addAll(boton,separator);
						botones.setSpacing(5); //Espacio de separacion de 10px
						boton.setOnAction(ev->{
							mostrarDatos(p);
						});
						//boton=null; // pongo a null para el siguiente nombre
					}
					
				}
				if(!found) {
					Alert a = new Alert(AlertType.INFORMATION);
					a.setTitle("Error al realizar la búsqueda");
					a.setHeaderText("No hay resultados para "+nombreproducto);
					a.setContentText("Este producto no está registrado en la aplicación");
					a.showAndWait();
				}
			}
		});
		
	}
	
	@FXML
	private void goBack(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaComercio.fxml"));
		LoginController log = new LoginController();
		
		//loader.setController(log);
		Parent nextScreen = loader.load();
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show();
	}
}
