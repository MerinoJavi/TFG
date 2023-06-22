package mm.makery.app.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.transform.Scale;

public class ProductosComercioDetalladoController {
	//al hacer click sobre el boton, que cargue en el initialize todos los productos pertenecientes a esa empresa
	protected static String idcomercio;
	@FXML
	private VBox productos;
	
	@FXML
	public void initialize() {
		loadProducts(idcomercio);
	}
	
	//Cargar los productso del comercio que se ha pulsado anteriormente
	@FXML
	private void loadProducts(String nombrecomercio) {
		Connection conexionComercio;
		//Necesito la id de comercio
		 
		try {
			conexionComercio = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
			PreparedStatement sta = conexionComercio.prepareStatement("SELECT nombre,descripcion,pvp,imagen from producto where idcomercio=?");
			sta.setString(1, idcomercio);
			ResultSet result = sta.executeQuery();
			
			if(result.next()) {
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
	

}
