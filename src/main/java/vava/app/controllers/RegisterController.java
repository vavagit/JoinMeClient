package vava.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lynden.gmapsfx.javascript.object.LatLong;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import vava.app.Config;
import vava.app.PropertyManager;
import vava.app.components.GmComponent;
import vava.app.model.Dataset;
import vava.app.model.Location;
import vava.app.model.User;
import vava.app.model.communication.RestTemplateFactory;

public class RegisterController implements Initializable {
	@FXML private TextField userNameTF;
	@FXML private PasswordField passwordPF;
	@FXML private PasswordField passwordRPF;
	@FXML private TextField contactTF;
	@FXML private TextField firstNameTF;
	@FXML private TextField lastNameTF;
	@FXML private TextField addressTF;
	@FXML private Button signUpButton;
	@FXML private Label signUpLabel;
	@FXML private Label fillInLabel;
	
	private Logger logger = LogManager.getLogger(PropertyManager.class);
	private RegisterController rc;
	private LatLong address;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rc = this;
		
		//multilanguage
		PropertyManager manager = new PropertyManager("");
		String language = manager.loadLanguageSet(getClass());
		logger.debug("initialize, Nastavenie multilanguage: " + language);
		//nastavenie nazvov podla jazyku
		userNameTF.setPromptText(manager.getProperty("userNameTF"));
		passwordPF.setPromptText(manager.getProperty("passwordPF"));
		contactTF.setPromptText(manager.getProperty("contactTF"));
		firstNameTF.setPromptText(manager.getProperty("firstNameTF"));
		lastNameTF.setPromptText(manager.getProperty("lastNameTF"));
		addressTF.setPromptText(manager.getProperty("addressTF"));
		signUpButton.setText(manager.getProperty("signUpButton"));
		signUpLabel.setText(manager.getProperty("signUpLabel"));
		fillInLabel.setText(manager.getProperty("fillInLabel"));
	}
	
	@FXML
	private void signUpHandle(ActionEvent event) {
		String passwordString = passwordPF.getText();
		String passwordStringAgain = passwordRPF.getText();
		String usernameString = userNameTF.getText();
		String contactString = contactTF.getText();
		String firstNameString = firstNameTF.getText();
		String lastNameString = lastNameTF.getText();
		String addressString = addressTF.getText();
		
		//kontorola vyplnenia udajov
		if(passwordString.isEmpty() || passwordStringAgain.isEmpty() || usernameString.isEmpty()
				|| contactString.isEmpty() || firstNameString.isEmpty() || lastNameString.isEmpty()
				|| addressString.isEmpty()) {
			logger.debug("signUpHandle, nevyplnene udaje");
			new Alert(AlertType.ERROR, "je nutne vyplnit vsetky udaje").showAndWait();
			return;
		}
		logger.info("signUpHandle, Pokus o prihlasenie uzivatela " + usernameString);

		//kontrola zhody zadaneho hesla
		if(!passwordString.equals(passwordStringAgain)) {
			logger.debug("signUpHandle, nezhodne hesla");
			new Alert(AlertType.ERROR, "Hesla sa nezhoduju").showAndWait();
			return;
		}
		
		//vytvorenie objektu uzivatela
		User user = new User(usernameString, passwordString, new Date(new java.util.Date().getTime()), firstNameString, lastNameString, 0, 
							contactString, addressString, new Location(address.getLatitude(),address.getLongitude()));
		
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplateFactory factory = context.getBean(RestTemplateFactory.class);
			RestTemplate template = factory.getObject();
			
			logger.debug("signUpHandle, vytvorenie spojenia");
			
			//vratenie konfiguracie pripojenia
			String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
			String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");
			ResponseEntity<User> returnedEntity = template.postForEntity("http://" + ip + ":"+port+"/register", user, User.class);
			
			logger.info("signUpHandle, uzivatel " + usernameString + " uspesne prihlaseny");
			user.setId(returnedEntity.getBody().getId());
			//ulozenie prihlaseneho uzivatela
			Dataset.getInstance().setLoggedIn(user);
			logger.debug("signUpHandle, ulozenie prihlaseneho uzivatela");
			//uzatvorenie contextu
			((ConfigurableApplicationContext)context).close();
		}catch(HttpClientErrorException e){
			new Alert(AlertType.ERROR, "Registracia sa nepodarila. Uzivatelske meno uz existuje").showAndWait();
			logger.catching(Level.ERROR, e);
			return;
		}
		catch(RestClientException p){
			new Alert(AlertType.ERROR, "Registracia sa nepodarila. Pripojenie je nefungujuce").showAndWait();
			logger.catching(Level.ERROR, p);
			return;
		}
		
		//prepnutie sceny na hlavne okno
		Stage currentStage = (Stage) passwordPF.getScene().getWindow();
		currentStage.close();
		
		logger.info("signUpHandle, Otvorenie hlavneho okna");
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/MainView.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
	        currentStage.setScene(scene);
	        currentStage.show();
		} catch (IOException e) {
			logger.catching(Level.ERROR, e);
			return;
		}
	}
	@FXML
	private void enterMouse(MouseEvent event) {
		signUpButton.getStyleClass().add("buttonChange");
	}
	
	@FXML
	private void exitMouse(MouseEvent event) {
		signUpButton.getStyleClass().remove("buttonChange");
	}
	
	@FXML 
	private void inputHandle(KeyEvent event) {
			String place = addressTF.getText();
			System.out.println("text-------- "+place);
			GmComponent.getInstance().geocodingAddress(addressTF.getText(), rc);
	}
	
	public void fillLongLitude(LatLong l) {
		this.address = l;
		System.out.println("Naplnam polohu "+l);
	}
}
