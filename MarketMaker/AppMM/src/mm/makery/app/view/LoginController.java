package mm.makery.app.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label statusLabel;

    private boolean isLoggedIn = false;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (isValidCredentials(username, password)) {
            isLoggedIn = true;
            statusLabel.setText("Bienvenido,  " + username);
            usernameField.clear();
            passwordField.clear();
            loginButton.setDisable(true);
            logoutButton.setDisable(false);
        } else {
            statusLabel.setText("Los datos introducidos no son correctos.");
        }
    }

    @FXML
    //Cerrar sesión
    private void handleLogout() {
        isLoggedIn = false;
        statusLabel.setText("Sesión cerrada.");
        //Activo boton de login y desactivo el de logout
        loginButton.setDisable(false);
        logoutButton.setDisable(true);
    }

    private boolean isValidCredentials(String username, String password) {
        // Logica de autenticación
        return username.equals("usuario") && password.equals("contraseña");
    }
}

