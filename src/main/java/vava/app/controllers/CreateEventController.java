package vava.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import vava.app.components.GmComponent;

public class CreateEventController implements Initializable{
	@FXML Pane gmapsPane;
	@FXML TextField dekoduj;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GmComponent gm = new GmComponent(null);
		gmapsPane.getChildren().add(gm.mapComponent);
		dekoduj.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			 public void handle(KeyEvent ke) {
		            if (ke.getCode() == KeyCode.ENTER) {
		               gm.geocodingAddress(dekoduj.getText());
		            }
		        }
		});
		
	}
	

	
		
}
