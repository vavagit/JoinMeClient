package vava.controllers;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	@FXML Button logInButton;
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	@FXML
	private void logInHandle(ActionEvent event) {
		Stage s = (Stage)logInButton.getScene().getWindow();
		s.close();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/views/CreateEvents.fxml"));
		AnchorPane root=null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CreateEventController cec = loader.getController();
		//root.getChildrenUnmodifiable().add(new GmComponent(stage).mapComponent);
		Scene scene = new Scene(root);
        s.setTitle("FXML Welcome");
        s.setScene(scene);
        s.show();
	}
}
