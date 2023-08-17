package mm.makery.app.view;

import javafx.fxml.FXMLLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import java.sql.Statement;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import mm.makery.app.model.SesionUsuario;
import javafx.scene.control.MenuItem;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CarritoController { 

	@FXML
	private VBox productos;
	@FXML
	private VBox productosOptions;
	@FXML
	private Label totalPrecio;
	
	private String idcliente = "";
	private TextField newCantidad = new TextField();
	
	private Button goBack=new Button("←");
	@FXML
	private void initialize() {
		try {
			Connection conex = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			// Obtener el id del cliente
			String getClienteIdQuery = "SELECT idCliente FROM cliente WHERE usuario = ?";
			PreparedStatement getClienteIdSta = conex.prepareStatement(getClienteIdQuery);
			getClienteIdSta.setString(1, SesionUsuario.usuarioABuscar);
			ResultSet clienteIdResult = getClienteIdSta.executeQuery();

			if (clienteIdResult.next()) {
				idcliente = clienteIdResult.getString("idCliente");
			}
			// Obtengo todos los productos del carrito del cliente
			String getProductosCarritoCliente = "SELECT id_producto,cantidad,precio_unitario from carrito where id_cliente="
					+ idcliente;
			PreparedStatement getProductosClienteSta = conex.prepareStatement(getProductosCarritoCliente);
			ResultSet productosResultSet = getProductosClienteSta.executeQuery();

			while (productosResultSet.next()) {
				String idproducto = productosResultSet.getString("id_producto");
				int cantidad = productosResultSet.getInt("cantidad");

				String getDatosProducto = "SELECT nombre,pvp,imagen from producto where idproducto=" + idproducto;
				PreparedStatement getDatosProductoSta = conex.prepareStatement(getDatosProducto);
				ResultSet getDatosProductoResultSet = getDatosProductoSta.executeQuery();

				double pvp = 0.0;
				while (getDatosProductoResultSet.next()) {
					pvp = getDatosProductoResultSet.getDouble("pvp");
					String pvpString = Double.toString(pvp);

					// Arreglo la ruta de la imagen
					String pathimg = getDatosProductoResultSet.getString("imagen");
					String fixpath = pathimg.replace(":", ":\\\\").replaceAll("TFG", "TFG\\\\")
							.replace("MarketMaker", "MarketMaker\\\\").replaceAll("AppMM", "AppMM\\\\")
							.replace("src", "src\\\\").replace("images", "images\\\\");

					// Cargo la imagen
					InputStream i = new FileInputStream(fixpath);
					Image image = new Image(i);
					ImageView imageview = new ImageView(image);
					imageview.setFitWidth(100);
					imageview.setFitHeight(100);
					//Cargo el boton del producto para que pueda ser clicable
					Button productoButton = new Button(getDatosProductoResultSet.getString("nombre"));
					//Aplico estilo al boton
					productoButton.setStyle("-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
					productoButton.setOnMouseEntered(e->{
						productoButton.setStyle("-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
					});
					//Cuando se quita el cursor del raton de encima
					productoButton.setOnMouseExited(e->{
						productoButton.setStyle("-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
					});
					//añado el boton y los campos al vbox, y aplico estilos. Creo los Label de los campos para poder modificar la apariencia
					productos.getChildren().add(productoButton);
					Label pvpLabel = new Label(pvpString+"€");
					pvpLabel.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
					Label cantidadLabel = new Label("Cantidad: "+Integer.toString(cantidad));
					cantidadLabel.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
					
					productos.getChildren().addAll(pvpLabel);
					productos.getChildren().add(cantidadLabel);
					productos.getChildren().add(imageview);
					productos.getChildren().add(new Separator(Orientation.HORIZONTAL));

					productoButton.setOnAction(e -> {
						Button deleteItem = new Button("Eliminar del carrito");
						Button cantidadButton = new Button("Cantidad");
						
						deleteItem.setStyle("-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
						deleteItem.setOnMouseEntered(ev->{
							deleteItem.setStyle("-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
						});
						//Cuando se quita el cursor del raton de encima
						deleteItem.setOnMouseExited(e2->{
							deleteItem.setStyle("-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
						});
						
						cantidadButton.setStyle("-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
						cantidadButton.setOnMouseEntered(ev->{
							cantidadButton.setStyle("-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
						});
						//Cuando se quita el cursor del raton de encima
						cantidadButton.setOnMouseExited(e2->{
							cantidadButton.setStyle("-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
						});
						
						// Agrego botones al vbox y aplico estilos
						productosOptions.getChildren().add(deleteItem);
						productosOptions.getChildren().add(cantidadButton);
						// Actualizo la cantidad del producto especifico que ha sido clickado
						// anteriormente
						cantidadButton.setOnAction(k -> {
							VBox formulario = new VBox(10);
							Label newCantidadLabel = new Label("Cantidad");
							newCantidadLabel.setStyle("-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");
							formulario.getChildren().add(newCantidadLabel);
							
							newCantidad.setPromptText("Introduce la cantidad");
							newCantidad.setStyle("-fx-background-color: transparent;-fx-text-fill:black; -fx-font-family: 'Segoe UI Light'; -fx-font-size: 15.0px; -fx-font-style: italic; -fx-font-weight:bold;");	
							formulario.getChildren().add(newCantidad);
							
							goBack.setMaxWidth(Double.MAX_VALUE);
							goBack.setStyle("-fx-font-size: 17.0px; -fx-font-family: 'Segoe UI Light';  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);-fx-background-color: transparent;");
							goBack.setOnMouseEntered(ev->{
								goBack.setStyle("-fx-font-family: 'Segoe UI Light';-fx-background-color: #237F08; -fx-font-size: 17.0px; -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0);");
							});
							//Cuando se quita el cursor del raton de encima
							goBack.setOnMouseExited(e2->{
								goBack.setStyle("-fx-font-family: 'Segoe UI Light'; -fx-scale-x:1; -fx-scale-y:1; -fx-border-color: transparent; -fx-font-size: 17.0px;  -fx-effect: dropshadow(three-pass-box, rgba(0.0,0.0,0.0,0.4), 10.0, 0.0, 0.0, 5.0); -fx-background-color: transparent;");
							});
							formulario.getChildren().add(goBack);
							// Abro la nueva ventana
							Scene formularioScene = new Scene(formulario, 600, 400);
							// Obtengo ventana actual
							Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
							currentStage.setScene(formularioScene);
							
							//Vuelve al carrito
							goBack.setOnAction(ev->{
								// Recargo la pagina
								FXMLLoader loader = new FXMLLoader(getClass().getResource("Carrito.fxml"));
								// LoginController log = new LoginController();
								// loader.setController(log);
								Parent nextScreen;
								try {
									nextScreen = loader.load();
								// log = loader.getController();
								// Cargo el XML siguiente en una nueva escena, que posteriormente casteo la
								// ventana que obtengo y la establezco en la escena actual para que no me
								// cree otra ventana.
								Scene nextScreenScene = new Scene(nextScreen);
								Stage current = (Stage) ((Node) k.getSource()).getScene().getWindow();
								currentStage.setScene(nextScreenScene);
								currentStage.show(); // Muestro la pagina LoginMenu
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
							});
							// Actualizo la cantidad del producto
							newCantidad.setOnKeyPressed(key -> {
								double c = Double.parseDouble(newCantidad.getText());
								if (key.getCode().equals(KeyCode.ENTER)&&c>0&&!newCantidad.getText().isEmpty()){
									String productoIdQuery = "SELECT idproducto from producto where nombre='"
											+ productoButton.getText() + "'";
									PreparedStatement p;
									try {
										p = conex.prepareStatement(productoIdQuery);
										ResultSet idprodSet = p.executeQuery();
										String idprod = "";
										if (idprodSet.next()) {
											idprod = idprodSet.getString("idproducto");
											String quantityUpdateQuery = "UPDATE carrito SET cantidad="
													+ newCantidad.getText() + " WHERE id_producto=" + idprod;
											PreparedStatement quantitySta = conex.prepareStatement(quantityUpdateQuery);
											quantitySta.execute();

											// Muestro alerta de confirmacion
											Alert a = new Alert(AlertType.INFORMATION);
											a.setTitle("¡Cambios guardados");
											a.setHeaderText(null);
											a.setContentText(null);
											a.showAndWait();

											// Recargo la pagina
											FXMLLoader loader = new FXMLLoader(getClass().getResource("Carrito.fxml"));
											// LoginController log = new LoginController();
											// loader.setController(log);
											Parent nextScreen = loader.load();
											// log = loader.getController();
											// Cargo el XML siguiente en una nueva escena, que posteriormente casteo la
											// ventana que obtengo y la establezco en la escena actual para que no me
											// cree otra ventana.
											Scene nextScreenScene = new Scene(nextScreen);
											Stage current = (Stage) ((Node) k.getSource()).getScene().getWindow();
											currentStage.setScene(nextScreenScene);
											currentStage.show(); // Muestro la pagina LoginMenu

										}
									} catch (SQLException | IOException e1) {
										// TODO Auto-generated catch block
										Alert al = new Alert(AlertType.ERROR);
										al.setTitle("Error al intentar actualizar la cantidad del producto");
										al.setHeaderText(null);
										al.setContentText(null);
										al.showAndWait();
									}
								}else { //Cantidad negativa, lanza error
									Alert error = new Alert(AlertType.ERROR);
									error.setTitle("Cantidad no válida");
									error.setHeaderText("La cantidad introducida no es válida");
									error.setContentText(null);
									error.showAndWait();
								}
							});
						});
						
						deleteItem.setOnAction(ev -> {
							String getProducto = "SELECT idproducto from producto where nombre='"
									+ productoButton.getText() + "'";
							PreparedStatement getProductoSta;
							try {
								getProductoSta = conex.prepareStatement(getProducto);
								ResultSet getProductoRes = getProductoSta.executeQuery();

								if (getProductoRes.next()) {
									String getProductoEliminarQuery = "DELETE FROM carrito where id_cliente="
											+ idcliente + " and id_producto=" + getProductoRes.getInt("idproducto");
									Statement staEliminarProducto = conex.prepareStatement(getProductoEliminarQuery);
									int numfilaseliminadas = staEliminarProducto
											.executeUpdate(getProductoEliminarQuery);

									// El producto ha sido eliminado, Muestro alerta y actualizo la pagina
									if (numfilaseliminadas > 0) {
										Alert a = new Alert(AlertType.INFORMATION);
										a.setTitle("Producto eliminado del carrito");
										a.setHeaderText(null);
										a.setContentText(
												"El producto ha sido eliminado del carrito satisfactoriamente.");

										// Recargo la pagina
										FXMLLoader loader = new FXMLLoader(getClass().getResource("Carrito.fxml"));
										// LoginController log = new LoginController();
										// loader.setController(log);
										Parent nextScreen = loader.load();
										// log = loader.getController();
										// Cargo el XML siguiente en una nueva escena, que posteriormente casteo la
										// ventana que obtengo y la establezco en la escena actual para que no me cree
										// otra ventana.
										Scene nextScreenScene = new Scene(nextScreen);
										Stage currentStage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
										currentStage.setScene(nextScreenScene);
										currentStage.show(); // Muestro la pagina LoginMenu
									}
								}

							} catch (SQLException | IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						});

					});
					showTotalPrice();
				}
			}
		} catch (SQLException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//Obtengo el precio total del carrito del cliente y lo muestro en pantalla
	@FXML
	private void showTotalPrice() {
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			String getIdClientQuery = "SELECT idcliente from cliente where usuario='"+SesionUsuario.usuarioABuscar+"'";
			PreparedStatement idClientSta = conexion.prepareStatement(getIdClientQuery);
			
			ResultSet ResultIdClient = idClientSta.executeQuery();
			//Guardo el id del cliente
			int idClientCart;
			//Calculo el total del carrito del cliente
			if(ResultIdClient.next()) {
				idClientCart = ResultIdClient.getInt("idcliente");
				String productsQuery = "SELECT SUM(cantidad*precio_unitario) AS preciototal from carrito where id_cliente="+idClientCart;
				PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
				ResultSet totalSet = totalSta.executeQuery();
				if(totalSet.next()) {
					totalPrecio.setText(totalSet.getString("preciototal")+"€");
				}
				totalSet.close();
				totalSta.close();
			}
			ResultIdClient.close();
			idClientSta.close();
			conexion.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void confirmPurchase(ActionEvent e) {
		try {
			Connection conex = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			String getIdClientQuery = "SELECT idcliente from cliente where usuario='"+SesionUsuario.usuarioABuscar+"'";
			PreparedStatement idClientSta = conex.prepareStatement(getIdClientQuery);
			ResultSet ResultIdClient = idClientSta.executeQuery();
			int idClientCart;
			//Borro el carrito pasandole el id del cliente que ha iniciado sesion
			if(ResultIdClient.next()) {
				idClientCart = ResultIdClient.getInt("idcliente");
				String deleteCartQuery = "DELETE  from carrito where id_cliente="+idClientCart;
				Statement stDelete = conex.prepareStatement(deleteCartQuery);
				
				int numfilaseliminadas = stDelete.executeUpdate(deleteCartQuery);
				//Alerta que confirma la eliminacion
				if(numfilaseliminadas>0) {
					Alert al = new Alert(AlertType.INFORMATION);
					al.setTitle("¡Compra realizada!");
					al.setHeaderText("La compra ha sido realizada");
					al.setContentText("La compra ha sido realizada con éxito. Gracias por utilizar nuestra app :-)");
					al.showAndWait();
					//Recargo la pagina
					FXMLLoader loader = new FXMLLoader(getClass().getResource("Carrito.fxml"));
		    		LoginController log = new LoginController();
		    		Parent nextScreen = loader.load();
		    		Scene nextScreenScene = new Scene(nextScreen);
		    		Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		    		currentStage.setScene(nextScreenScene);
		    		currentStage.show();
				}
				
			}
			//Error al realizar la compra. Lanzo alerta
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Error al realizar la compra");
			a.setHeaderText(null);
			a.setContentText("Ha ocurrido un error al realizar la compra. Vuelve a intentarlo más tarde.");
			a.showAndWait();
			e1.printStackTrace();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Alert alerta = new Alert(AlertType.ERROR);
			alerta.setTitle("Error con el servidor");
			alerta.setHeaderText(null);
			alerta.setContentText("No se ha podido comunicar con el servidor. Intentelo más tarde");
			alerta.showAndWait();
		}
	}
	
	@FXML
	private void goBack(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PaginaCliente.fxml"));
		LoginController log = new LoginController();
		
		//loader.setController(log);
		Parent nextScreen = loader.load();
		Scene nextScreenScene = new Scene(nextScreen);
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(nextScreenScene);
		currentStage.show();
	}
	

}
