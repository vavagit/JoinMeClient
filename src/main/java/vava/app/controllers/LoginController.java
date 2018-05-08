package vava.app.controllers;



import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
	@FXML Button logInButton;
	@FXML TextField userNameTF;
	@FXML PasswordField passwordPF;
	@FXML Label errLabel;
	@FXML Hyperlink register;
	
	
	public void initialize(URL location, ResourceBundle resources) {
		passwordPF.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			 public void handle(KeyEvent ke) {
		            if (ke.getCode() == KeyCode.ENTER) {
		               logInHandle(null);
		            }
		        }
		});
	}
	
	@FXML
	private void registerHandle(ActionEvent event) {
		Stage s = (Stage)logInButton.getScene().getWindow();
		s.close();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/Register.fxml"));
		AnchorPane root=null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
        s.setTitle("JoinMe - REGISTER");
        s.setScene(scene);
        
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
		if(userName.isEmpty() || password.isEmpty()) {
			errLabel.setText("empty username or password");
			return;
		}
	
		User user = new User(userName,password);
		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		RestTemplateFactory factory = context.getBean(RestTemplateFactory.class);
		RestTemplate template = factory.getObject();
		
		((ConfigurableApplicationContext) context).close();
		
		try {
			String ip = new PropertyManager(getClass().getResourceAsStream("/connectionConfig")).getProperty("host");
			String url = "http://"+ip+":8009/login";
			ResponseEntity<User> returnedEntity = template.postForEntity(url, user, User.class);
			//nastavenie autorizacnych udajov pre dalsiu komunikaciu
			User returnedUser = returnedEntity.getBody();
			returnedUser.setPassword(password);
			returnedUser.setUserName(userName);
			Dataset.getInstance().setLoggedIn(returnedUser);
		}
		catch(HttpStatusCodeException e){
			errLabel.setText("Invalid username or password");
			userNameTF.clear();
			passwordPF.clear();
			return;
		}
		catch(RestClientException p){
			errLabel.setText("error connection. Try later");
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
	
}
