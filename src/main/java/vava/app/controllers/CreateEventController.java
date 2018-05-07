package vava.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import vava.app.components.GmComponent;
import vava.app.model.SportCategory;

public class CreateEventController implements Initializable{
	@FXML Pane gmapsPane;
	@FXML private Label titleDescriptionLabel;
	@FXML private Label titleLabel;
	@FXML private TextField eventNameTF;
	@FXML private TextField addressTF;
	@FXML private TextField neccessaryAgeTF;
	@FXML private TextField maxUsersTF;
	@FXML private TextArea descriptionTA;
	@FXML private DatePicker eventDateDP;
	@FXML private ChoiceBox<SportCategory> sportCategoryChB;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GmComponent gm = GmComponent.getInstance();
		gmapsPane.getChildren().add(gm.mapComponent);
		gm.map.setZoom(15);
		
		addressTF.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			 public void handle(KeyEvent ke) {
		            if (ke.getCode() == KeyCode.ENTER) {
		               gm.geocodingAddress(addressTF.getText());
		            }
		        }
		});
		
	}
	
	
	

	
		
}
