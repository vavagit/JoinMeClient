package vava.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vava.app.Config;
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
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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
		
		if(passwordString.isEmpty() || passwordStringAgain.isEmpty() || usernameString.isEmpty()
				|| contactString.isEmpty() || firstNameString.isEmpty() || lastNameString.isEmpty()
				|| addressString.isEmpty()) {
			new Alert(AlertType.ERROR, "je nutne vyplnit vsetky udaje").showAndWait();
			return;
		}
		
		//kontrola zhody zadaneho hesla
		if(!passwordString.equals(passwordStringAgain)) {
			new Alert(AlertType.ERROR, "Hesla sa nezhoduju").showAndWait();
			return;
		}
		
		//vytvorenie objektu uzivatela
		User user = new User(usernameString, passwordString, new Date(new java.util.Date().getTime()), firstNameString, lastNameString, 0, 
							contactString, addressString, new Location(10,10));
		
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplateFactory factory = context.getBean(RestTemplateFactory.class);
			RestTemplate template = factory.getObject();
			
			ResponseEntity<User> returnedEntity = template.postForEntity("http://localhost:8009/register", user, User.class);
			
			user.setId(returnedEntity.getBody().getId());
			//nastavenie autorizacie pre dalsie requesty
			factory.setAuthorization(usernameString, passwordString);
			//ulozenie prihlaseneho uzivatela
			Dataset.getInstance().setLoggedIn(user);
			//uzatvorenie contextu
			((ConfigurableApplicationContext)context).close();
		}catch(HttpClientErrorException e){
			new Alert(AlertType.ERROR, "Registracia sa nepodarila. Uzivatelske meno uz existuje").showAndWait();
			e.printStackTrace();
			return;
		}
		
		//prepnutie sceny na hlavne okno
		Stage currentStage = (Stage) passwordPF.getScene().getWindow();
		currentStage.close();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/MainView.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
	        currentStage.setScene(scene);
	        currentStage.show();
		} catch (IOException e) {
			e.printStackTrace();
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
	
}
