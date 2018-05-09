package vava.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import vava.app.Config;
import vava.app.PropertyManager;
import vava.app.components.UserPaneComponent;
import vava.app.model.Event;
import vava.app.model.User;
import vava.app.model.communication.RestTemplateFactory;

public class UsersViewController implements Initializable{
 @FXML ListView<UserPaneComponent> userListView;
 private Event event;
 private List<User> joinedUser = new ArrayList<>();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	
		
	}
	public void fillEventObject(Event e) {
		this.event = e;
		System.out.println(e);
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplateFactory factory = context.getBean(RestTemplateFactory.class);
			RestTemplate template = factory.getObject();
			String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
			String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");
			String url = "http://"+ip+":"+port+"/events/" + this.event.getEventId() + "/users";
			ResponseEntity<List<User>> returnedEntity = template.exchange(url, HttpMethod.GET, null,new ParameterizedTypeReference<List<User>>() {});
			for(User u : returnedEntity.getBody()) {
				//System.out.println("nejde---------");
				joinedUser.add(u);
			}
			((ConfigurableApplicationContext)context).close();
		}
		catch(RestClientException p){
			p.printStackTrace();
			return;
		}
		List<UserPaneComponent> list = new ArrayList<>();
		for(User u : joinedUser) {
			list.add(new UserPaneComponent(u));
		}
		ObservableList<UserPaneComponent> olist = FXCollections.observableArrayList(list);
		userListView.setItems(olist);
	}
}
