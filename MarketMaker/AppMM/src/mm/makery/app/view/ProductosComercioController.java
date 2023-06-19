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
	private Button reject=new Button("Rechazar");
	@FXML
	private FileChooser fc= new FileChooser();
	@FXML
	private VBox infoproducto;
	@FXML
	private VBox botones;
	@FXML
	private Button edit = new Button("Editar...");
	
	private String path;
	private String nifcomercio;

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
	
	@FXML
	private void mostrarDatos(String nombreproducto) {
		//Para sacar los datos de la bbdd
		Label nombre=new Label("");
		Label pvp=new Label("");
		Label descripcion=new Label("");
		String imagen="";
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
			//Para evitar atauqes de inyeccion SQL, le paso los parametros más tarde
			String sql = "SELECT nombre,pvp,descripcion,imagen from producto where idcomercio= ? and nombre=?";
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
			//Ahora meto los datos en el VBox creado en Scenebuilder para mostrar los datos
			infoproducto.getChildren().addAll(new Label("Título: "),nombre);
			infoproducto.getChildren().addAll(new Label("PVP: "),pvp);
			infoproducto.getChildren().addAll(new Label("Descripción: "),descripcion);
			//Transformacion de la ruta de la imagen para que se muestre la imagen correctamente. Obtengo la ruta y lo transformo en un objeto ImageView de JavaFX
			String fixpath = imagen.replace(":", ":\\\\").replaceAll("TFG","TFG\\\\").replace("MarketMaker", "MarketMaker\\\\").replaceAll("AppMM", "AppMM\\\\").replace("src", "src\\\\").replace("images", "images\\\\");

			
			InputStream i = new FileInputStream(fixpath);
			Image image = new Image(i);
			ImageView imageview = new ImageView(image);
			imageview.setFitWidth(100);
			imageview.setFitHeight(100);
			infoproducto.getChildren().add(imageview);
			fixpath="";
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
		// Creoy añado los datos al formulario
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
				Parent nextScreen;
				nextScreen = loader.load();
				Scene nextScreenScene = new Scene(nextScreen);
				Stage current = (Stage) ((Node) e.getSource()).getScene().getWindow();
				current.setScene(nextScreenScene);
				current.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
	}
}
