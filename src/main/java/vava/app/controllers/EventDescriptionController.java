package vava.app.controllers;

import java.io.IOException;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
	@FXML Button joinedUsersButton;
	@FXML Pane gmaps;
	Event event;
	private	EventHandler<MouseEvent> join;
	private EventHandler<MouseEvent> leave;
	private String buttonJoinedS;
	private String buttonJoinS;
	private List<User> joinedUser;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		GmComponent gm = GmComponent.getInstance();
		gmaps.getChildren().add(gm.mapComponent);
	}
	
	public void fillEventObject(Event event) {
		this.event = event;
		User user;
		
		//naplnenie create usera
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplateFactory factory = context.getBean(RestTemplateFactory.class);
			RestTemplate template = factory.getObject();
			String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
			String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");
			ResponseEntity<User> returnedEntity = template.getForEntity("http://"+ip+":"+port+"/users/"+event.getCreatorId(), User.class);
			
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
		joinedUsersButton.setText(pm.getProperty("joinedUsersButton"));
		
		dataEventTitleLabel.setText(event.getEventName());
		dataAgeLabel.setText(event.getNecessaryAge()+"");
		dataAddressLabel.setText(event.getAddress());
		dataDescriptionLabel.setWrapText(true);
		dataDescriptionLabel.setText(event.getDescription());
		dataDateLabel.setText(event.getDate().toString());
		dataCreatorLabel.setText(user.getUserName());
		buttonJoinedS=pm.getProperty("joinButtonJoined");
		buttonJoinS=pm.getProperty("joinButton");
		//joinButton.setText();
		refillJoinedUser();
		
		
		
		
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
		
		//event handlery pre prihlasenie a odhlasenie 
		 join = new EventHandler<MouseEvent>() {
	 			@Override
	 			public void handle(MouseEvent event) {
	 				//users/idUsera/event/ideventu
	 				ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
	 				RestTemplate template = context.getBean(RestTemplate.class);
	 				((ConfigurableApplicationContext) context).close();
	 				
	 				String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
	 				String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");
	 				final String url = "http://" + ip + ":"+port+"/users/"+Dataset.getInstance().getLoggedIn().getId()+"/event/"+EventDescriptionController.this.event.getEventId();
	 				try {
	 					template.postForEntity(url, null, Void.class);
	 					joinButton.setText(buttonJoinedS);
	 					joinButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, join);
	 					joinButton.addEventHandler(MouseEvent.MOUSE_CLICKED, leave);
	 				} catch (HttpStatusCodeException e) {
	 					new Alert(AlertType.ERROR, "Nepodarilo sa pripojic").showAndWait();
	 					return;
	 				} catch (RestClientException e) {
	 					new Alert(AlertType.ERROR, "Chyba spojenia").showAndWait();
	 					return;
	 				}
	 			}
	     		 
	 		};
	 	leave = new EventHandler<MouseEvent>() {
	 			@Override
	 			public void handle(MouseEvent event) {
	 				//users/idUsera/event/ideventu
	 				ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
	 				RestTemplate template = context.getBean(RestTemplate.class);
	 				((ConfigurableApplicationContext) context).close();
	 				
	 				String ip = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("host");
	 				String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");
	 				final String url = "http://" + ip + ":"+port+"/users/"+Dataset.getInstance().getLoggedIn().getId()+"/event/"+EventDescriptionController.this.event.getEventId();
	 				try {
	 					template.delete(url, null, Void.class);
	 					refillJoinedUser();
	 					joinButton.setText("("+joinedUser.size()+"/"+EventDescriptionController.this.event.getMaxUsersOnEvent()+")"+buttonJoinS);
	 					joinButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, leave);
	 					joinButton.addEventHandler(MouseEvent.MOUSE_CLICKED, join);
	 				} catch (HttpStatusCodeException e) {
	 					new Alert(AlertType.ERROR, "Nepodarilo sa leavnut").showAndWait();
	 					return;
	 				} catch (RestClientException e) {
	 					new Alert(AlertType.ERROR, "Chyba spojenia").showAndWait();
	 					return;
	 				}
	 				
	 			}
	     		 
	 		};
	 		
	 		
	 		if(joinedUser.contains(Dataset.getInstance().getLoggedIn())) {
				joinButton.setText(buttonJoinedS);
				joinButton.addEventHandler(MouseEvent.MOUSE_CLICKED, leave);
			}
			else{
				String buttontext = buttonJoinS;
				buttontext = buttontext + " ("+joinedUser.size()+"/"+event.getMaxUsersOnEvent()+")";
				joinButton.setText(buttontext);
				joinButton.addEventHandler(MouseEvent.MOUSE_CLICKED, join);
			}
	}
	@FXML
	private void joinedUsersButtonHandle(ActionEvent event) {
		Stage s =(Stage) joinedUsersButton.getScene().getWindow();
		Stage newS = new Stage();
		newS.initOwner(s);
		//newS.setAlwaysOnTop(true);
		newS.initModality(Modality.WINDOW_MODAL);
		newS.setTitle("JoinMe - Joined users");
        newS.setResizable(false);
        Image image = new Image(getClass().getResourceAsStream("/img/titleIco.jpg"));
        newS.getIcons().add(image);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/UsersView.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);
	        UsersViewController ec = loader.getController();
	        if(ec!=null)
	        	ec.fillEventObject(this.event);
			newS.setScene(scene);
	        newS.show();
		} catch (IOException e) {
			System.out.println("nastal problem-----------------");
			e.printStackTrace();
			return;
		}
		
	}
	
	private void refillJoinedUser() {
		joinedUser = new ArrayList<>();
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			RestTemplateFactory factory = context.getBean(RestTemplateFactory.class);
			RestTemplate template = factory.getObject();
			String ip = new PropertyManager("src/main/resources/connectionConfig").getProperty("host");
			String port = new PropertyManager(getClass().getResource("/connectionConfig").getFile()).getProperty("port");

			String url = "http://"+ip+":"+port+"/events/" + event.getEventId() + "/users";
			ResponseEntity<List<User>> returnedEntity = template.exchange(url, HttpMethod.GET, null,new ParameterizedTypeReference<List<User>>() {});
			for(User q: returnedEntity.getBody()) {
				joinedUser.add(q);
			}
			((ConfigurableApplicationContext)context).close();
		}
		catch(RestClientException p){
			p.printStackTrace();
			return;
		}
	}

}
