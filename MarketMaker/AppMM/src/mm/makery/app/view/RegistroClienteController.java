package mm.makery.app.view;

import javafx.event.ActionEvent;
import  javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

public class RegistroClienteController {

	private TextField usuario;
	
	//TODO: hacer registro de usuario, comprobar TODOS los datos y si sale todo bien, que muestre una alerta de todo OK
	public RegistroClienteController() {
		
	}
	
	/*
	@FXML
	private void handleSaveButtonAction(ActionEvent event) {
	    try {
	        String url = "jdbc:mysql://localhost:3306/mydatabase";
	        String user = "root";
	        String password = "mypassword";
	        Connection conn = DriverManager.getConnection(url, user, password);

	        String sql = "INSERT INTO mytable (field1, field2, field3) VALUES (?, ?, ?)";
	        PreparedStatement statement = conn.prepareStatement(sql);
	        statement.setString(1, field1.getText());
	        statement.setString(2, field2.getText());
	        statement.setString(3, field3.getText());

	        int rowsInserted = statement.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("A new row has been inserted.");
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	*/
}
