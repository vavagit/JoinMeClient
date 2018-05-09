package vava.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import vava.app.PropertyManager;
import vava.app.model.User;

public class UserDetailsController implements Initializable {
	
	@FXML
	private Label userNameLabel;
	@FXML
	private Label firstNameLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label addressLabel;
	@FXML
	private Label contactLabel;
	@FXML
	private Label registeredAtLabel;

	//datova cast
	@FXML
	private Label firstNameDataLabel;
	@FXML
	private Label lastNameDataLabel;
	@FXML
	private Label addressDataLabel;
	@FXML
	private Label contactDataLabel;
	@FXML
	private Label registeredAtDataLabel;

	private static Logger logger = LogManager.getLogger(UserDetailsController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

		//nastavenie textu mulilanguage
		PropertyManager manager = new PropertyManager("");
		String language = manager.loadLanguageSet(getClass());
		
		logger.debug("initialize, nastavenie jazyku: " + language);
		
		firstNameLabel.setText(manager.getProperty("firstNameLabel"));
		lastNameLabel.setText(manager.getProperty("lastNameLabel"));
		addressLabel.setText(manager.getProperty("addressLabel"));
		contactLabel.setText(manager.getProperty("contactLabel"));
		registeredAtLabel.setText(manager.getProperty("registeredAtLabel"));
	}
	
	public void fillUserObject(User user) {
		userNameLabel.setText(user.getUserName());
		firstNameDataLabel.setText(user.getName());
		lastNameDataLabel.setText(user.getLastName());
		addressDataLabel.setText(user.getAddress());
		contactDataLabel.setText(user.getContact());
		registeredAtDataLabel.setText(formatDate(user.getRegisteredAt().toString()));
	}
	
	private String formatDate(String date) {
		String[] splited = date.split("-");
		return splited[2] + "." + splited[1] + "." + splited[0];
	}

	@FXML
	private void closeHandle(ActionEvent event) {
		((Stage)userNameLabel.getScene().getWindow()).close();
	}
}
