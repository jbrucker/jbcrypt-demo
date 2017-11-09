package org.ske.passwdmgr;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Start the login app.
 * @author jim
 */
public class LoginApp extends Application {
	public static final String LOGIN_FORM = "org/ske/passwdmgr/LoginUI.fxml";
	public static final String ADD_USER_FORM = "org/ske/passwdmgr/AddUserForm.fxml";

	/**
	 * Select the scene to display and start JavaFX.
	 * @param stage the primary "stage" for showing the scene.
	 */
	@Override
	public void start(Stage stage) {
		Parent parent;
        try {
            parent = FXMLLoader.load(
                    this.getClass().getClassLoader().getResource(LOGIN_FORM) );
        } catch (IOException ex) {
            Logger.getLogger("LoginApplication").log(Level.SEVERE, null, ex);
            return;
        }
        Scene scene = new Scene(parent);

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
