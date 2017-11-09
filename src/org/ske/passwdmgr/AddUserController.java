package org.ske.passwdmgr;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for FXML AddUser form.
 * @author jim
 */
public class AddUserController {

	@FXML
	TextField usernameField;
	@FXML
	TextField realnameField;
	@FXML
	PasswordField passwordField;
	@FXML
	Label messageField;
	// we don't really need a references to the buttons
	// but it makes for clean form code
	@FXML
	Button submitButton;
	@FXML
	Button cancelButton;
	
	/**
	 * Handle submit event from the form.
	 */
	public void handleSubmit(ActionEvent evt) {
		String username = usernameField.getText().trim();
		String password = passwordField.getText().trim();
		String realname = realnameField.getText().trim();
		if (username.isEmpty() || password.isEmpty() || realname.isEmpty()) {
			showErrorMessage("Please fill-in all fields.");
		}
		boolean ok = UserManager.getInstance().addUser(username, realname, password);
		if (ok) JOptionPane.showMessageDialog(null, "User "+username+" created.");
		else JOptionPane.showMessageDialog(null, "Sorry, couldn't create user "+username);
		// close this window
		final Node source = (Node) evt.getSource();
	    final Stage stage = (Stage) source.getScene().getWindow();
	    stage.close();
	}
	
	/**
	 * Handle user cancels add-user form.
	 * @param evt
	 */
	public void handleCancel(ActionEvent evt) {
		// close this window
		final Node source = (Node) evt.getSource();
	    final Stage stage = (Stage) source.getScene().getWindow();
	    stage.close();
	}


	/** Display a message in the information field. */
	private void showErrorMessage(String message) {
		messageField.setText(message);	
	}

	/** Clear the information field. */
	private void clearErrorMessage() {
		messageField.setText("");	
	}
}
