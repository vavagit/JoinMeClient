package vava.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lynden.gmapsfx.javascript.object.LatLong;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import vava.app.Config;
import vava.app.PropertyManager;
import vava.app.components.EventPaneComponent;
import vava.app.components.GmComponent;
import vava.app.model.Dataset;
import vava.app.model.Event;
import vava.app.model.Location;
import vava.app.model.User;


public class MainViewController implements Initializable {
	private MainViewController mwc;
	private LatLong location;
	@FXML ListView<EventPaneComponent> eventListView;
	@FXML Label titleLeftLabel;
	@FXML Label nameOfUserLabel;
	@FXML Label locationLabel;
	@FXML TextField locationTextField;
	@FXML Label rangeLabel;
	@FXML TextField rangeTextField;
	@FXML Hyperlink createEventLink;
	@FXML Hyperlink myEventsLink;
	@FXML Hyperlink joinedEventsLink;
	@FXML Button filterButton;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mwc = this;
		
		User user = Dataset.getInstance().getLoggedIn();
		
		//multilanguage
		PropertyManager manager = new PropertyManager("");
		manager.loadLanguageSet(getClass());
		createEventLink.setText(manager.getProperty("createEventLink"));
		myEventsLink.setText(manager.getProperty("myEventsLink"));
		joinedEventsLink.setText(manager.getProperty("joinedEventsLink"));
		filterButton.setText(manager.getProperty("filterButton"));
		locationLabel.setText(manager.getProperty("locationLabel"));
		rangeLabel.setText(manager.getProperty("rangeLabel"));
		titleLeftLabel.setText(user.getName() + " " + user.getLastName());
		
		//nastavenie nazvov podla jazyku
		loadEvents(Dataset.getInstance().getLoggedIn().getAddressLocation(), 20);
	}
	
	private void loadEvents(Location location, int radius) {
		try {
     		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
     		RestTemplate template = context.getBean(RestTemplate.class);
     		((ConfigurableApplicationContext)context).close();     		
     		
     		final String ip = new PropertyManager("src/main/resources/connectionConfig").getProperty("host");
     		final String url = "http://" + ip + ":8009/events?lon={lon}&lat={lat}&radius={radius}";
     		
     		Map<String, Object> map = new HashMap<>();
     		map.put("lon", location.getLongitude());
     		map.put("lat", location.getLatitude());
     		map.put("radius", radius);
     		
     		ResponseEntity<List<Event>> returnedEntity = template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {}, map);
    		List<EventPaneComponent> list = new ArrayList<>();
     		for(Event current : returnedEntity.getBody()) {
     			list.add(new EventPaneComponent(current));
     		}
     		ObservableList<EventPaneComponent> listEventPane = FXCollections.observableArrayList(list);
     		eventListView.setItems(listEventPane);
     	}
       catch(RestClientException e) {
     		e.printStackTrace();
     	}
	}
	
	@FXML
	private void mouseEnteredFilterBt(MouseEvent event) {
		filterButton.setStyle("-fx-background-color: #6dcde9");
		//filterButton.getStyleClass().add("buttonChange");
	}
	
	@FXML
	private void mouseExitedFilterBt(MouseEvent event) {
		filterButton.setStyle("-fx-background-color: #19b9e7");
		//filterButton.getStyleClass().remove("buttonChange");
	}
	
	@FXML 
	private void inputHandle(KeyEvent event) {
			String place = locationTextField.getText();
			System.out.println("text-------- "+place);
			GmComponent.getInstance().geocodingAddress(locationTextField.getText(), mwc);
	}
	
	@FXML
	private void createEventHandle(ActionEvent event) {
		Stage newStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/CreateEvents.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			newStage.setScene(scene);
			newStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
	}
		
	@FXML
	private void filterHandle(ActionEvent event) {
		String selectedArea = locationTextField.getText();
		String rangeString = rangeTextField.getText();
		
		
		if(rangeString.isEmpty()) {
			new Alert(AlertType.ERROR, "Je nutne vyplnit rozsah vyhladavania filtra").showAndWait();
			return;
		}
		
		int range = 0;
		try {
			range = Integer.parseInt(rangeString);
		}catch(NumberFormatException e) {
			new Alert(AlertType.ERROR, "Nespravne vyplnene hodnoty").showAndWait();
			return;
		}
		
		double latitude = Dataset.getInstance().getLoggedIn().getAddressLocation().getLatitude();
		double longitude =Dataset.getInstance().getLoggedIn().getAddressLocation().getLongitude();

		if(!selectedArea.isEmpty()) {
			if(location == null) {
				new Alert(AlertType.ERROR, "Poloha nebola najdena").showAndWait();
				return;
			}
			
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			System.out.println(location);
		}
			
		loadEvents(new Location(latitude,longitude), range);
	}
	
	public void fillLongLitude(LatLong l) {
		this.location = l;
		System.out.println("Naplnam polohu "+l);
	}
	
	@FXML
	private void showMyHandle(ActionEvent event) {
		try {
     		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
     		RestTemplate template = context.getBean(RestTemplate.class);
     		((ConfigurableApplicationContext)context).close();     		
     		
     		final String ip = new PropertyManager("src/main/resources/connectionConfig").getProperty("host");
     		final String url = "http://" + ip + ":8009/users/" + Dataset.getInstance().getLoggedIn().getId() + "/created";
     		
     		ResponseEntity<List<Event>> returnedEntity = template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {});
    		List<EventPaneComponent> list = new ArrayList<>();
     		for(Event current : returnedEntity.getBody()) {
     			list.add(new EventPaneComponent(current));
     		}
     		ObservableList<EventPaneComponent> listEventPane = FXCollections.observableArrayList(list);
     		eventListView.setItems(listEventPane);
     	}
       catch(RestClientException e) {
     		e.printStackTrace();
     	}
	}
	
	@FXML
	private void showJoinedHandle(ActionEvent event) {
		try {
     		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
     		RestTemplate template = context.getBean(RestTemplate.class);
     		((ConfigurableApplicationContext)context).close();     		
     		
     		final String ip = new PropertyManager("src/main/resources/connectionConfig").getProperty("host");
     		final String url = "http://" + ip + ":8009/users/" + Dataset.getInstance().getLoggedIn().getId()+ "/events";
     	  		
     		ResponseEntity<List<Event>> returnedEntity = template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {});
    		List<EventPaneComponent> list = new ArrayList<>();
     		for(Event current : returnedEntity.getBody()) {
     			list.add(new EventPaneComponent(current));
     		}
     		ObservableList<EventPaneComponent> listEventPane = FXCollections.observableArrayList(list);
     		eventListView.setItems(listEventPane);
		}
		catch(RestClientException e) {
     		e.printStackTrace();
     	}
	}
	
	
}