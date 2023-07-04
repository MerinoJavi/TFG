package mm.makery.app.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Button;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import mm.makery.app.model.SesionUsuario;

public class CarritoController {

	@FXML
	private VBox productos;
	@FXML
	private VBox preciototal;
	@FXML
	private Button cantidadButton=new Button("Añadir cantidad");
	@FXML
	private Button deleteItemCart = new Button("Eliminar producto");
	@FXML
	private void initialize() {
		try {
			Connection conex = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			// Obtener el id del cliente
	        String getClienteIdQuery = "SELECT idCliente FROM cliente WHERE usuario = ?";
	        PreparedStatement getClienteIdSta = conex.prepareStatement(getClienteIdQuery);
	        getClienteIdSta.setString(1, SesionUsuario.usuarioABuscar);
	        ResultSet clienteIdResult = getClienteIdSta.executeQuery();
	        
	        String idcliente="";
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
	        	if(getDatosProductoResultSet.next()) {
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
					
					productos.getChildren().addAll(new Label(getDatosProductoResultSet.getString("nombre")));
	        		productos.getChildren().addAll(new Label(pvpString+"€"));
	        		productos.getChildren().add(new Label("Cantidad: "+cantidad));
	        		productos.getChildren().add(cantidadButton);
	        		productos.getChildren().add(imageview);
	        		productos.getChildren().add(deleteItemCart);
	        		productos.getChildren().add(new Separator(Orientation.HORIZONTAL));
					
	        		cantidadButton.setOnAction(e->{
	        			
	        		});
	        		
	        		deleteItemCart.setOnAction(ev->{
	        			
	        		});
	        	}
	        	}
		} catch (SQLException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
