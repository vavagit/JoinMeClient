package vava.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vava.app.Config;
import vava.app.PropertyManager;
import vava.app.model.Dataset;
import vava.app.model.User;
import vava.app.model.communication.RestTemplateFactory;

public class LoginController implements Initializable {
	Stage stage;
	@FXML 
	private Button logInButton;
	@FXML 
	private TextField userNameTF;
	@FXML
	private Label logInLabel;
	@FXML 
	private PasswordField passwordPF;
	@FXML 
	private Label errLabel;
	@FXML 
	private Hyperlink register;
	@FXML
	private Label invitationLabel;
	
	private static Logger logger = LogManager.getLogger(LoginController.class);
	
	public void initialize(URL location, ResourceBundle resources) {
		passwordPF.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			 public void handle(KeyEvent ke) {
		            if (ke.getCode() == KeyCode.ENTER) {
		               logInHandle(null);
		            }
		        }
		});
		
		logger.info("Prihlasenie uzivatela");
		
		//multilanguage
		PropertyManager manager = new PropertyManager("");
		String language = manager.loadLanguageSet(getClass());
		
		logger.info("Nastaveny jazyk: " + language);
		
		logger.debug("initialize, Nastavenie multilanguage");
		//nastavenie nazvov podla jazyku
		logInButton.setText(manager.getProperty("logInButton"));
		userNameTF.setPromptText(manager.getProperty("userNameTF"));
		passwordPF.setPromptText(manager.getProperty("passwordPF"));
		logInLabel.setText(manager.getProperty("logInLabel"));
		invitationLabel.setText(manager.getProperty("invitationLabel"));
		register.setText(manager.getProperty("register"));
	}
	
	@FXML
	private void registerHandle(ActionEvent event) {
		//zatvorenie aktualneho okna
		Stage s = (Stage)logInButton.getScene().getWindow();
		s.close();
		
		//nacitanie novej sceny 
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/Register.fxml"));
		AnchorPane root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			logger.catching(Level.ERROR, e);
		}
		
		logger.debug("registerHandle, Ovorenie okna registracie");
		Scene scene = new Scene(root);
        s.setTitle("Join me - REGISTER");
        s.setScene(scene);
        //otvorenie noveho okna
        s.show();
	}
	
	@FXML
	private void logInChangeCollor(MouseEvent event) {
		logInButton.getStyleClass().add("buttonChange");
	}
	
	@FXML
	private void logInMouseExited(MouseEvent event) {
		logInButton.getStyleClass().remove("buttonChange");
	}
	
	
	
	@FXML
	private void logInHandle(ActionEvent event) {
		errLabel.setText("");
		String userName = null;
		String password = null;
		userName = userNameTF.getText();
		password = passwordPF.getText();
		
		//kontrola vyplnenia udajov
		if(userName.isEmpty() || password.isEmpty()) {
			errLabel.setText("empty username or password");
			return;
		}
	
		logger.debug("logInHandle, Overenie udajov uspesne");
		
		logger.info("logInHandle, Poziadavka na prihlasenie uzivatela");
		
		//vytvorenie objektu uzivatela
		User user = new User(userName,password);
		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		RestTemplateFactory factory = context.getBean(RestTemplateFactory.class);
		RestTemplate template = factory.getObject();
		
		((ConfigurableApplicationContext) context).close();
		
		PropertyManager manager = new PropertyManager("");
		String language = manager.loadLanguageSet(getClass());
		
		logger.debug("logInHandle, Nastavenie jazyka: " + language);
		
		try {
			String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
			String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");

			String url = "http://" + ip + ":"+port+"/login";
			ResponseEntity<User> returnedEntity = template.postForEntity(url, user, User.class);
			//nastavenie autorizacnych udajov pre dalsiu komunikaciu
			User returnedUser = returnedEntity.getBody();
			returnedUser.setPassword(password);
			returnedUser.setUserName(userName);
			Dataset.getInstance().setLoggedIn(returnedUser);
			logger.info("logInHandle, prihlasenie uspesne " + returnedUser.getId());
		}
		catch(HttpStatusCodeException e){
			errLabel.setText(manager.getProperty("invalidAuth"));
			logger.debug("logInHandle, Prihlasenie neuspesne");
			userNameTF.clear();
			passwordPF.clear();
			return;
		}
		catch(RestClientException p){
			errLabel.setText(manager.getProperty("connectionError"));
			logger.catching(Level.ERROR, p);
			return;
		}
		
		//prepnutie sceny na hlavne okno
		Stage currentStage = (Stage) passwordPF.getScene().getWindow();
		currentStage.close();
				
		//otvorenie noveho okna
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
	
}
