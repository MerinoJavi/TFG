package mm.makery.app.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import mm.makery.app.model.SesionUsuario;

public class ProductosComercioDetalladoController {
	
	protected static String idcomercio;
	@FXML
	private VBox productos;
	@FXML
	private TextField productField=new TextField();
	@FXML
	private Button addcart=new Button("Añadir al carrito");
	private boolean encontrado=false;
	
	//al hacer click sobre el boton, que cargue en el initialize todos los productos pertenecientes a esa empresa
	@FXML
	public void initialize() {
		loadProducts(idcomercio);
	}
	
	//Cargar los productos del comercio que se ha pulsado anteriormente
	@FXML
	private void loadProducts(String nombrecomercio) {
		Connection conexionComercio;
		//Necesito la id de comercio
		 
		try {
			conexionComercio = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			PreparedStatement sta = conexionComercio.prepareStatement("SELECT nombre,descripcion,pvp,imagen from producto where idcomercio=?");
			sta.setString(1, idcomercio);
			ResultSet result = sta.executeQuery();
			
			while(result.next()) {
				Label nombreproducto =new Label( result.getString("nombre"));
				Label descripcion =new Label( result.getString("descripcion"));
				Label pvp =new Label( result.getString("pvp"));
				String pathimg = result.getString("imagen");
				
				String fixpath=pathimg.replace(":", ":\\\\").replaceAll("TFG", "TFG\\\\")
						.replace("MarketMaker", "MarketMaker\\\\").replaceAll("AppMM", "AppMM\\\\")
						.replace("src", "src\\\\").replace("images", "images\\\\");
				
				//Cargo la imagen
				InputStream i = new FileInputStream(fixpath);
				Image image = new Image(i);
				ImageView imageview = new ImageView(image);
				imageview.setFitWidth(100);
				imageview.setFitHeight(100);
				
				productos.getChildren().add(nombreproducto);
				productos.getChildren().add(descripcion);
				productos.getChildren().add(pvp);
				
				productos.getChildren().add(imageview);
				
				productos.getChildren().add(new Separator(Orientation.HORIZONTAL));
				
				 Scale scale = new Scale(1, 1, 0, 0);
			     imageview.getTransforms().add(scale);
			     imageview.setOnMouseEntered(event->{
			    	 scale.setX(1.2);
			           scale.setY(1.2);
			     });
			     imageview.setOnMouseExited(e->{
			    	 scale.setX(1.0);
			          scale.setY(1.0);
			     });
			     //Que al hacer click se abran los detalles de la imagen
			}
		} catch (SQLException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@FXML
	private void searchProducts() throws IOException {
		productos.getChildren().clear();
		String productName=productField.getText();
		
		/*
		if(productName.isEmpty()) {
		//   isLoggedIn = false;
							// Cargo la pagina XML correspondiente al menu de logins
					        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductosComercioDetallado.fxml"));
							//LoginController log = new LoginController();
							//loader.setController(log);
							Parent nextScreen = loader.load();
							//log = loader.getController();
							//Cargo el XML siguiente en una nueva escena, que posteriormente casteo la ventana que obtengo y la establezco en la escena actual para que no me cree otra ventana.
							Scene nextScreenScene = new Scene(nextScreen);
							Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
							currentStage.setScene(nextScreenScene);
							currentStage.show(); //Muestro la pagina LoginMenu
					        //logoutButton.setDisable(true);		
		}
		*/
		productField.setOnKeyPressed(e->{
			if(e.getCode().equals(KeyCode.ENTER)) {
				System.out.println("Enter pulsado");
				try {
					Connection conexionComercio = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
					PreparedStatement sta = conexionComercio.prepareStatement("SELECT nombre,descripcion,pvp,imagen from producto where idcomercio=?");
					sta.setString(1, idcomercio);
					ResultSet result = sta.executeQuery();
					while(result.next()) {
						if(result.getString("nombre").toLowerCase().contains(productName.toLowerCase())) { //Buscar el producto que contenga la palabra que hayamos introducido
							encontrado=true;
							System.out.println("Definitivamente coinciden");
							Label nombreproducto =new Label( result.getString("nombre"));
							Label descripcion =new Label( result.getString("descripcion"));
							Label pvp =new Label( result.getString("pvp"));
							String pathimg = result.getString("imagen");
							
							String fixpath=pathimg.replace(":", ":\\\\").replaceAll("TFG", "TFG\\\\")
									.replace("MarketMaker", "MarketMaker\\\\").replaceAll("AppMM", "AppMM\\\\")
									.replace("src", "src\\\\").replace("images", "images\\\\");
							
							//Cargo la imagen
							InputStream i = new FileInputStream(fixpath);
							Image image = new Image(i);
							ImageView imageview = new ImageView(image);
							imageview.setFitWidth(100);
							imageview.setFitHeight(100);
							
							productos.getChildren().add(nombreproducto);
							productos.getChildren().add(descripcion);
							productos.getChildren().add(pvp);
							productos.getChildren().add(imageview);
							productos.getChildren().add(addcart);
							productos.getChildren().add(new Separator(Orientation.HORIZONTAL));
							
							 Scale scale = new Scale(1, 1, 0, 0);
						     imageview.getTransforms().add(scale);
						     //Al pasar el cursor hace un mini-zoom
						     imageview.setOnMouseEntered(evento->{
						    	 scale.setX(1.2);
						           scale.setY(1.2);
						     });
						     imageview.setOnMouseExited(e2->{
						    	 scale.setX(1.0);
						          scale.setY(1.0);
						     });
						     addcart.setOnAction(event2 -> {
						    	    try {
						    	        Connection conex = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");

						    	        // Obtener el id del cliente
						    	        String getClienteIdQuery = "SELECT idCliente FROM cliente WHERE usuario = ?";
						    	        PreparedStatement getClienteIdSta = conex.prepareStatement(getClienteIdQuery);
						    	        getClienteIdSta.setString(1, SesionUsuario.usuarioABuscar);
						    	        ResultSet clienteIdResult = getClienteIdSta.executeQuery();
						    	        int idcliente = -1;
						    	        if (clienteIdResult.next()) {
						    	            idcliente = clienteIdResult.getInt("idCliente");
						    	        }
						    	        
						    	        // Obtener el id del producto
						    	        String getProductoIdQuery = "SELECT idproducto FROM producto WHERE nombre = ?";
						    	        PreparedStatement getProductoIdSta = conex.prepareStatement(getProductoIdQuery);
						    	        getProductoIdSta.setString(1, nombreproducto.getText());
						    	        ResultSet productoIdResult = getProductoIdSta.executeQuery();
						    	        int idproducto = -1;
						    	        if (productoIdResult.next()) {
						    	            idproducto = productoIdResult.getInt("idproducto");
						    	        }

						    	        // Verificar si el cliente tiene un carrito existente
						    	        String checkCarritoQuery = "SELECT idcarrito FROM carrito WHERE id_cliente = ?";
						    	        PreparedStatement checkCarritoSta = conex.prepareStatement(checkCarritoQuery);
						    	        checkCarritoSta.setInt(1, idcliente);
						    	        ResultSet carritoResult = checkCarritoSta.executeQuery();

						    	        // Si el cliente tiene un carrito, agregar el producto al carrito
						    	        if (carritoResult.next()) {
						    	            int idcarrito = carritoResult.getInt("idcarrito");
						    	            String addProductoQuery = "INSERT INTO carrito (idcarrito,id_producto,id_cliente,cantidad,precio_unitario) VALUES (?,?,?,1,?)";
						    	            PreparedStatement addProductoSta = conex.prepareStatement(addProductoQuery);
						    	            addProductoSta.setInt(1, idcarrito);

						    	            // Obtener el precio del producto desde la tabla Producto
						    	            String getPrecioQuery = "SELECT pvp FROM Producto WHERE idproducto = ?";
						    	            PreparedStatement getPrecioSta = conex.prepareStatement(getPrecioQuery);
						    	            getPrecioSta.setInt(1, idproducto);
						    	            ResultSet precioResult = getPrecioSta.executeQuery();

						    	            if (precioResult.next()) {
						    	                double precioProducto = precioResult.getDouble("pvp");
						    	                addProductoSta.setDouble(4, precioProducto);
						    	            }
						    	            addProductoSta.setInt(2, idproducto);
						    	            addProductoSta.setInt(3, idcliente);
						    	            // Ejecutar la inserción del producto en el carrito
						    	            addProductoSta.executeUpdate();
						    	            
						    	            Alert a = new Alert(AlertType.INFORMATION);
						    	            a.setTitle("¡Producto añadido!");
						    	            a.setContentText("Dirigete al carrito si deseas incrementar la cantidad de este producto");
						    	            a.showAndWait();
						    	        } else {
						    	        	// Si el cliente no tiene un carrito, crear uno nuevo y agregar el producto al carrito
						    	        	Random r = new Random();
						    	        	int idCarrito=r.nextInt(100); // Obtener el id del carrito generado
						    	        	  String createCarritoQuery = "INSERT INTO carrito (idcarrito,id_cliente) VALUES (?,?)";
							    	            PreparedStatement createCarritoSta = conex.prepareStatement(createCarritoQuery, Statement.RETURN_GENERATED_KEYS);

							    	            createCarritoSta.setInt(1, idCarrito);
							    	            createCarritoSta.setInt(2, idcliente);
							    	            createCarritoSta.executeUpdate();
						    	            // Agregar el producto al carrito. Aqui mejor hacer un UPDATE en vez de un INSERT
						    	            String addProductoQuery = "UPDATE carrito SET id_producto=?,cantidad=1,precio_unitario=? WHERE idcarrito="+idCarrito;
						    	            PreparedStatement addProductoSta = conex.prepareStatement(addProductoQuery);
						    	            addProductoSta.setInt(1, idproducto);
						    	          

						    	            // Obtener el precio del producto desde la tabla Producto
						    	            String getPrecioQuery = "SELECT pvp FROM producto WHERE idproducto = ?";
						    	            PreparedStatement getPrecioSta = conex.prepareStatement(getPrecioQuery);
						    	            getPrecioSta.setInt(1, idproducto);
						    	            ResultSet precioResult = getPrecioSta.executeQuery();

						    	            if (precioResult.next()) {
						    	                double precioProducto = precioResult.getDouble("pvp");
						    	                addProductoSta.setDouble(2, precioProducto);
						    	            }

						    	            // Ejecutar la inserción del producto en el carrito
						    	            addProductoSta.executeUpdate();
						    	            /*
						    	            // Incrementar la cantidad de objetos del cliente (será 1, ya que es el primer producto en el carrito)
						    	            String updateClienteQuery = "UPDATE carrito SET cantidad = 1 WHERE idcliente = ?";
						    	            PreparedStatement updateClienteSta = conex.prepareStatement(updateClienteQuery);
						    	            updateClienteSta.setInt(1, idcliente);
						    	            updateClienteSta.executeUpdate();
						    	            */
						    	        }
						    	    } catch (SQLException e1) {
						    	       Alert a = new Alert(AlertType.ERROR);
						    	       a.setTitle("Producto añadido");
						    	       a.setHeaderText(null);
						    	       a.setContentText("Ya has añadido este producto. Dirigete al carrito para incrementar la cantidad de este producto.");
						    	       a.showAndWait();
						    	    }
						    	});

						}
						
					     
					}
					if(!encontrado) {						//Si no encuentro el objeto que quiero, salta la alerta
							Alert a = new Alert(AlertType.INFORMATION);
							a.setTitle("Error al realizar la búsqueda");
							a.setHeaderText("No hay resultados para "+productName);
							a.setContentText("El producto no existe o no se encuentra en este comercio");
							a.showAndWait();
						
					}
					sta.close();
					conexionComercio.close();
				} catch (SQLException | FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

}
