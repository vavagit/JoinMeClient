package vava.app.controllers;



import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.http.conn.HttpHostConnectException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import vava.app.model.User;

public class LoginController implements Initializable {
	Stage stage;
	@FXML Button logInButton;
	@FXML TextField userNameTF;
	@FXML PasswordField passwordPF;
	@FXML Label errLabel;
	@FXML Hyperlink register;
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/views/Register.fxml"));
		AnchorPane root=null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RegisterController rec = loader.getController();
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
		
		/*User usr = new User(userName,password);
		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		RestTemplate restTemplate = context.getBean(RestTemplate.class);
		String ip = "http://25.19.186.82:8009/";
		try {
			ResponseEntity<User> user = restTemplate.exchange(ip+"login", HttpMethod.POST, new HttpEntity<User>(usr),new ParameterizedTypeReference<User>() {});
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
		}*/
		
		
		Stage s = (Stage)logInButton.getScene().getWindow();
		s.close();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/views/MainView.fxml"));
		AnchorPane root=null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MainViewController cec = loader.getController();
		//root.getChildrenUnmodifiable().add(new GmComponent(stage).mapComponent);
		Scene scene = new Scene(root);
        s.setTitle("FXML Welcome");
        s.setScene(scene);
        s.show();
	}
}
