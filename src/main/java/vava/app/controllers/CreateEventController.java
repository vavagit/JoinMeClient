package vava.app.controllers;

import java.lang.Character.UnicodeBlock;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ToolTipManager;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import vava.app.PropertyManager;
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
	@FXML private Button createButton;
	@FXML private ChoiceBox<SportCategory> sportCategoryChB;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GmComponent gm = GmComponent.getInstance();
		gmapsPane.getChildren().add(gm.mapComponent);
		setTextToLabel();
		Tooltip e = new Tooltip("choice language");
		sportCategoryChB.setTooltip(e);
		
		gm.map.setZoom(7);
		
		addressTF.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			 public void handle(KeyEvent ke) {
		            if (ke.getCode() == KeyCode.ENTER) {
		               gm.geocodingAddress(addressTF.getText());
		            }
		        }
		});
		
	}
	private void setTextToLabel() {
		String language = Locale.getDefault().getLanguage();
		System.out.println(language);
		if(!"sk".equals(language) && !"en".equals(language)) {
			language = "en";
		}
		System.out.println(language);
		PropertyManager pm = new PropertyManager(getClass().getResourceAsStream("/language/CreateEvents_"+language));
		titleLabel.setText(pm.getProperty("titleLabel"));
		titleDescriptionLabel.setText(pm.getProperty("titleDescriptionLabel"));
		createButton.setText(pm.getProperty("createButton"));
		eventNameTF.setPromptText(pm.getProperty("eventNameTF"));
		addressTF.setPromptText(pm.getProperty("addressTF"));
		eventDateDP.setPromptText(pm.getProperty("eventDateDP"));
		neccessaryAgeTF.setPromptText(pm.getProperty("neccessaryAgeTF"));
		maxUsersTF.setPromptText(pm.getProperty("maxUsersTF"));
		descriptionTA.setPromptText(pm.getProperty("descriptionTA"));

	}
	
	
	

	
		
}
