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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import mm.makery.app.model.SesionUsuario;
import javafx.scene.control.MenuItem;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CarritoController {

	@FXML
	private VBox productos;
	@FXML
	private VBox productosOptions;
	@FXML
	private Button cantidadButton=new Button("Añadir cantidad");
	@FXML
	private Button deleteItemCart = new Button("Eliminar producto");
	
	private String idcliente="";
	@FXML
	private void initialize() {
		try {
			Connection conex = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			// Obtener el id del cliente
	        String getClienteIdQuery = "SELECT idCliente FROM cliente WHERE usuario = ?";
	        PreparedStatement getClienteIdSta = conex.prepareStatement(getClienteIdQuery);
	        getClienteIdSta.setString(1, SesionUsuario.usuarioABuscar);
	        ResultSet clienteIdResult = getClienteIdSta.executeQuery();
	    //    String idcliente="";
	        if(clienteIdResult.next()) {
	        	 idcliente=clienteIdResult.getString("idCliente");
	        }
	        //Obtengo todos los productos del carrito del cliente
	        String getProductosCarritoCliente = "SELECT id_producto,cantidad,precio_unitario from carrito where id_cliente="+idcliente;
	        PreparedStatement getProductosClienteSta=conex.prepareStatement(getProductosCarritoCliente);
	        ResultSet productosResultSet = getProductosClienteSta.executeQuery();
	        
	        while(productosResultSet.next()) {
	        	String idproducto = productosResultSet.getString("id_producto");
	        	int cantidad=productosResultSet.getInt("cantidad");
	        	
	        	String getDatosProducto = "SELECT nombre,pvp,imagen from producto where idproducto="+idproducto;
	        	PreparedStatement getDatosProductoSta = conex.prepareStatement(getDatosProducto);
	        	ResultSet getDatosProductoResultSet = getDatosProductoSta.executeQuery();
	        	
	        	double pvp=0.0;
	        	while(getDatosProductoResultSet.next()) {
	        		pvp = getDatosProductoResultSet.getDouble("pvp");
	        		String pvpString = Double.toString(pvp);
	        		
	        		//Arreglo la ruta de la imagen
	        		String pathimg = getDatosProductoResultSet.getString("imagen");
	        		String fixpath=pathimg.replace(":", ":\\\\").replaceAll("TFG", "TFG\\\\")
							.replace("MarketMaker", "MarketMaker\\\\").replaceAll("AppMM", "AppMM\\\\")
							.replace("src", "src\\\\").replace("images", "images\\\\");
	        		
	        		//Cargo la imagen
					InputStream i = new FileInputStream(fixpath);
					Image image = new Image(i);
					ImageView imageview = new ImageView(image);
					imageview.setFitWidth(100);
					imageview.setFitHeight(100);
					
					Button productoButton = new Button(getDatosProductoResultSet.getString("nombre"));
					productos.getChildren().add(productoButton);
	        		productos.getChildren().addAll(new Label(pvpString+"€"));
	        		productos.getChildren().add(new Label("Cantidad: "+cantidad));
	        		productos.getChildren().add(imageview);
	        		productos.getChildren().add(new Separator(Orientation.HORIZONTAL));
				
	        		productoButton.setOnAction(e->{
	        			Button deleteItem = new Button("Eliminar del carrito");
	        			Button cantidadButton = new Button("Cantidad");
	        			
	        			//Agrego botones al vbox 
	        			productosOptions.getChildren().add(deleteItem);
	        			productosOptions.getChildren().add(cantidadButton);
	        			
	        			deleteItem.setOnAction(ev->{
	        				String getProducto = "SELECT idproducto from producto where nombre='"+productoButton.getText()+"'";
							PreparedStatement getProductoSta;
								try {
									getProductoSta = conex.prepareStatement(getProducto);
									ResultSet getProductoRes = getProductoSta.executeQuery();
									if(getProductoRes.next()) {
										String getProductoEliminarQuery = "DELETE FROM carrito where id_cliente="+idcliente+" and id_producto="+getProductoRes.getInt("idproducto");
										Statement staEliminarProducto = conex.prepareStatement(getProductoEliminarQuery);
										int numfilaseliminadas = staEliminarProducto.executeUpdate(getProductoEliminarQuery);
										
										//El producto ha sido eliminado, Muestro alerta y actualizo la pagina
										if(numfilaseliminadas>0) {
											Alert a = new Alert(AlertType.INFORMATION);
											a.setTitle("Producto eliminado del carrito");
											a.setContentText("El producto ha sido eliminado del carrito satisfactoriamente.");
											
											
											//Recargo la pagina
									        FXMLLoader loader = new FXMLLoader(getClass().getResource("Carrito.fxml"));
											//LoginController log = new LoginController();
											//loader.setController(log);
											Parent nextScreen = loader.load();
											//log = loader.getController();
											//Cargo el XML siguiente en una nueva escena, que posteriormente casteo la ventana que obtengo y la establezco en la escena actual para que no me cree otra ventana.
											Scene nextScreenScene = new Scene(nextScreen);
											Stage currentStage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
											currentStage.setScene(nextScreenScene);
											currentStage.show(); //Muestro la pagina LoginMenu
									        //logoutButton.setDisable(true);	
									         
									         
										}
									}
								
									
								} catch (SQLException | IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} 
								

	        				
	        				
	        			});
	        			
	        		});
	        		
	        	}
	        	}
		} catch (SQLException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
