package vava.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
	
	@FXML Label dateLabel;
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
	@FXML Pane gmaps;
	private Event event;
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
			String ip = new PropertyManager("src/resources/connectionConfig").getProperty("host");
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
		
		dataEventTitleLabel.setText(event.getEventName());
		dataAgeLabel.setText(event.getNecessaryAge()+"");
		dataAddressLabel.setText(event.getAddress());
		dataDescriptionLabel.setText(event.getDescription());
		dataDateLabel.setText(event.getDate().toString());
		dataCreatorLabel.setText(user.getUserName());
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
	

}
