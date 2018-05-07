package vava.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import vava.app.Config;
import vava.app.components.EventPaneComponent;
import vava.app.model.Dataset;
import vava.app.model.Event;
import vava.app.model.communication.RestTemplateFactory;


public class MainViewController implements Initializable {
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
		titleLeftLabel.setText(leftTitle);
		nameOfUserLabel.setText(nameUser);
		locationLabel.setText(locationLabelS);
		rangeLabel.setText("Range: ");
        /*list.add(new EventPaneComponent(new Event(1, 12, "Fc pod hrat", "skuska", Date.valueOf(LocalDate.now()), 30, 15, new SportCategory(4,"hockey"), "Krizova Ves", new Location())));
        list.add(new EventPaneComponent(new Event(1, 12, "Fc pod hrat", "skuska", Date.valueOf(LocalDate.now()), 30, 5, new SportCategory(2,"hockey"), "Krizova Ves", new Location())));

        ObservableList<EventPaneComponent> myObservableList = FXCollections.observableList(list);
        eventListView.setItems(myObservableList);
        */
        try {
     		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
     		RestTemplate template = context.getBean(RestTemplate.class);
     		     		
     		final String url = "http://localhost:8009/events?lon={lon}&lat={lat}&radius={radius}";
     		Map<String, Object> map = new HashMap<>();
     		map.put("lon", 50.0);
     		map.put("lat", 50.0);
     		map.put("radius", 1000);
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
	
	
}