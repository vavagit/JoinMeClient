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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import vava.app.Config;
import vava.app.PropertyManager;
import vava.app.components.GmComponent;
import vava.app.model.Dataset;
import vava.app.model.Event;
import vava.app.model.User;
import vava.app.model.communication.RestTemplateFactory;

public class EventDescriptionController implements Initializable {
	@FXML Label eventTitleLabel;
	@FXML Label dataEventTitleLabel;
	
	@FXML Label dateLabel; //
	@FXML Label descriptionLabel;
	@FXML Label sportCategoryLabel;
	@FXML Label addressLabel;
	@FXML Label ageLabel;
	
	@FXML Label dataSportCategoryLabel; //
	@FXML Label dataDateLabel; //
	@FXML Label dataAddressLabel; //
	@FXML Label dataAgeLabel;//
	@FXML Label dataDescriptionLabel;//
	@FXML Label creatorLabel;
	@FXML Label dataCreatorLabel;
	@FXML Button joinButton;
	@FXML Pane gmaps;
	Event event;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		GmComponent gm = GmComponent.getInstance();
		gmaps.getChildren().add(gm.mapComponent);
	}
	
	public void fillEventObject(Event event) {
		this.event = event;
		User user;
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplateFactory factory = context.getBean(RestTemplateFactory.class);
			RestTemplate template = factory.getObject();
			String ip = new PropertyManager("src/main/resources/connectionConfig").getProperty("host");
			ResponseEntity<User> returnedEntity = template.getForEntity("http://"+ip+":8009/users/"+event.getCreatorId(), User.class);
			
			user = returnedEntity.getBody();
			//ulozenie prihlaseneho uzivatela
			//uzatvorenie contextu
			((ConfigurableApplicationContext)context).close();
		}
		catch(RestClientException p){
			p.printStackTrace();
			return;
		}
		PropertyManager pm = new PropertyManager("");
		pm.loadLanguageSet(getClass());
		dateLabel.setText(pm.getProperty("dateLabel"));
		descriptionLabel.setText(pm.getProperty("descriptionLabel"));
		sportCategoryLabel.setText(pm.getProperty("sportCategoryLabel"));
		addressLabel.setText(pm.getProperty("addressLabel"));
		ageLabel.setText(pm.getProperty("ageLabel"));
		creatorLabel.setText(pm.getProperty("creatorLabel"));
		
		dataEventTitleLabel.setText(event.getEventName());
		dataAgeLabel.setText(event.getNecessaryAge()+"");
		dataAddressLabel.setText(event.getAddress());
		dataDescriptionLabel.setWrapText(true);
		dataDescriptionLabel.setText(event.getDescription());
		dataDateLabel.setText(event.getDate().toString());
		dataCreatorLabel.setText(user.getUserName());
		
		//joinButton.setText();
		List<User> joinedUser = new ArrayList<>();
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplateFactory factory = context.getBean(RestTemplateFactory.class);
			RestTemplate template = factory.getObject();
			String ip = new PropertyManager("src/main/resources/connectionConfig").getProperty("host");
			String url = "http://"+ip+":8009/events/" + event.getEventId() + "/users";
			ResponseEntity<List<User>> returnedEntity = template.exchange(url, HttpMethod.GET, null,new ParameterizedTypeReference<List<User>>() {});
			for(User u : returnedEntity.getBody()) {
				System.out.println("nejde---------");
				joinedUser.add(u);
			}
			((ConfigurableApplicationContext)context).close();
		}
		catch(RestClientException p){
			p.printStackTrace();
			return;
		}
		if(joinedUser.contains(Dataset.getInstance().getLoggedIn())) {
			joinButton.setText(pm.getProperty("joinButtonJoined"));
		}
		else{
			String buttontext = pm.getProperty("joinButton");
			buttontext = buttontext + " ("+joinedUser.size()+"/"+event.getMaxUsersOnEvent()+")";
			joinButton.setText(buttontext);
		}
		String language = "sk";
		if("sk".equals(language)) {
			dataSportCategoryLabel.setText(event.getSportCategory().getSport_sk());
		}
		else {
			dataSportCategoryLabel.setText(event.getSportCategory().getSport_en());
		}
		LatLong l = new LatLong(event.getEventLocation().getLatitude(), event.getEventLocation().getLongitude());
		GmComponent.getInstance().map.clearMarkers();
		GmComponent.getInstance().map.addMarker(new Marker(new MarkerOptions().position(l)));
		GmComponent.getInstance().map.setCenter(l);
		GmComponent.getInstance().map.setZoom(12);
	}
	
	
	@FXML
	private void joinButtonHandle(ActionEvent event) {
		//users/idUsera/event/ideventu
		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		RestTemplate template = context.getBean(RestTemplate.class);
		((ConfigurableApplicationContext) context).close();
		
		String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
		final String url = "http://" + ip + ":8009/users/"+Dataset.getInstance().getLoggedIn().getId()+"/event/"+this.event.getEventId();
		try {
			template.postForEntity(url, null, Void.class);
		} catch (HttpStatusCodeException e) {
			new Alert(AlertType.ERROR, "Nepodarilo sa pripojic").showAndWait();
			return;
		} catch (RestClientException e) {
			new Alert(AlertType.ERROR, "Chyba spojenia").showAndWait();
			return;
		}
	}
	

}
