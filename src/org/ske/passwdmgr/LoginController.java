package org.ske.passwdmgr;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the Login UI form.
 * Handles events and input from the Login UI.
 * 
 * @author jim
 */
public class LoginController {
	private final String default_message = "Encrypt My Password!";
	// The @FXML annotations are used to associate controls in FXML form
	// with objects in this controller.
	@FXML
	TextField usernameField;
	@FXML
	PasswordField passwordField;
	@FXML
	Button loginButton;
	@FXML
	Button addUserButton;
	// label at top used to display messages
	@FXML
	Label messageLabel;

	
	/**
	 * Event handler for login button press.
	 */
	public void handleLogin(ActionEvent evt) {
		String username = usernameField.getText().trim();
		if (username.isEmpty()) {
			showErrorMessage("Please enter username");
			return;
		}
		String password = passwordField.getText().trim();
		if (password.isEmpty()) {
			showErrorMessage("Please enter a password");
			return;
		}
		// validate it!
		UserInfo info = UserManager.getInstance().getUser(username);
		if (info == null) {
			showErrorMessage("Unknown user "+username);
			return;
		}
		clearErrorMessage();
		
		// check his password. UserInfo.password is the encrypted password
		boolean ok = BCrypt.checkpw(password, info.password);
		
		// If he logs in successfully, show gratuitous welcome message.
		// In a real app, we'd move on to the main app window.
		if (ok) {
			welcome(info.realname); // just for amusement
			// clear the input fields so you can login again
			clearFormFields();
		}
		else {
			showErrorMessage("Sorry, wrong password");
		}	
	}
	
	private void welcome(String who) {
		JOptionPane.showMessageDialog(null, "Welcome back, "+who+"!");
	}
	
	private void clearFormFields() {
		usernameField.setText("");
		passwordField.setText("");
	}

	/** Display a message in the information field. */
	private void showErrorMessage(String message) {
		messageLabel.setText(message);	
	}

	/** Clear the information field. */
	private void clearErrorMessage() {
		messageLabel.setText(default_message);	
	}
	
	/**
	 * Display the adduser form.
	 */
	public void handleAddUser(ActionEvent evt) {
		
		try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(LoginApp.ADD_USER_FORM));
            Stage stage = new Stage();
            stage.setTitle("Add User");
            stage.setScene(new Scene(root));
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node)(evt.getSource())).getScene().getWindow().hide();
            
            // after the AddUser form finishes, clear this form.
            clearFormFields();
        }
        catch (IOException e) {
            Logger.getLogger("LoginController").warning("Couldn't show form.\n"+e.getMessage());
        }
	}

}
