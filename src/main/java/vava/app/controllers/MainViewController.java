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
import vava.app.model.User;


public class MainViewController implements Initializable {
	private MainViewController mwc;
	private LatLong l;
	Stage s;
	String leftTitle = "Hi!";
	String nameUser = "Damian Majercak";
	String locationLabelS = "Lokacia: ";
	@FXML ListView<EventPaneComponent> eventListView;
	@FXML Label titleLeftLabel;
	@FXML Label nameOfUserLabel;
	@FXML Label locationLabel;
	@FXML TextField locationTextField;
	@FXML Label rangeLabel;
	@FXML TextField rangeTextField;
	@FXML Button filterButton;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mwc = this;
		titleLeftLabel.setText(leftTitle);
		nameOfUserLabel.setText(nameUser);
		locationLabel.setText(locationLabelS);
		rangeLabel.setText("Range: ");
       try {
     		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
     		RestTemplate template = context.getBean(RestTemplate.class);
     		((ConfigurableApplicationContext)context).close();     		
     		
     		final String ip = new PropertyManager("src/main/resources/connectionConfig").getProperty("host");
     		final String url = "http://" + ip + ":8009/events?lon={lon}&lat={lat}&radius={radius}";
     		User loggedIn = Dataset.getInstance().getLoggedIn();
     		
     		Map<String, Object> map = new HashMap<>();
     		map.put("lon", loggedIn.getAddressLocation().getLongitude());
     		map.put("lat", loggedIn.getAddressLocation().getLatitude());
     		map.put("radius", 10);
     		
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
		s = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/CreateEvents.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
	        s.setScene(scene);
	        s.show();
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
		
		Double latitude = null ;
		Double longitude =null;

		if(!selectedArea.isEmpty()) {
			if(l == null) {
				new Alert(AlertType.ERROR, "Poloha nebola najdena").showAndWait();
				return;
			}
			
			latitude = l.getLatitude();
			longitude = l.getLongitude();
			System.out.println(l);
		}
			
		try {
     		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
     		RestTemplate template = context.getBean(RestTemplate.class);
     		((ConfigurableApplicationContext)context).close();     		
     		String ip = new PropertyManager("src/main/resources/connectionConfig").getProperty("host");
     		final String url = "http://"+ip+":8009/events?lon={lon}&lat={lat}&radius={radius}";
     		Map<String, Object> map = new HashMap<>();
     		map.put("lon", longitude);
     		map.put("lat", latitude);
     		map.put("radius", range);
     		ResponseEntity<List<Event>> returnedEntity = template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {}, map);
     		
    		List<EventPaneComponent> list = new ArrayList<>();
     		for(Event current : returnedEntity.getBody()) {
     			list.add(new EventPaneComponent(current));
     		}
     		eventListView.setItems(FXCollections.observableList(list));
        	
     	}catch(RestClientException e) {
     		e.printStackTrace();
     	}
	}
	public void fillLongLitude(LatLong l) {
		this.l = l;
		System.out.println("Naplnam polohu "+l);
	}
	
}