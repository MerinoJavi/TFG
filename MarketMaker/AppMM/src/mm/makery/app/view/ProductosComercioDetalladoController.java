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

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class ProductosComercioDetalladoController {
	
	protected static String idcomercio;
	@FXML
	private VBox productos;
	@FXML
	private TextField productField=new TextField();
	
	
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
						}
						
					     
					}

				} catch (SQLException | FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

}
