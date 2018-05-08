package vava.app.controllers;

import java.lang.Character.UnicodeBlock;
import java.net.URL;
import java.rmi.server.Skeleton;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ToolTipManager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lynden.gmapsfx.javascript.object.Animation;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import vava.app.Config;
import vava.app.PropertyManager;
import vava.app.components.EventPaneComponent;
import vava.app.components.GmComponent;
import vava.app.components.GmReturn;
import vava.app.model.Event;
import vava.app.model.SportCategory;

public class CreateEventController implements Initializable{
	@FXML Pane gmapsPane;
	@FXML private Label titleDescriptionLabel;
	@FXML private Label titleLabel;
	@FXML private TextField eventNameTF;
	@FXML private TextField addressTF;
	@FXML private TextField neccessaryAgeTF;
	@FXML private TextField maxUsersTF;
	@FXML TextArea descriptionTA;
	@FXML private DatePicker eventDateDP;
	@FXML private Button createButton;
	@FXML private ChoiceBox<SportCategory> sportCategoryChB;
	CreateEventController e;
	LatLong l;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		e = this;
		GmComponent gm = GmComponent.getInstance();
		gmapsPane.getChildren().add(gm.mapComponent);
		init();
		gmapsPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getClickCount() == 2) {
					GmComponent.getInstance().refillLatLong(e);
				}
				
			}
			
		});
		gm.map.setZoom(7);
		addressTF.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			 public void handle(KeyEvent ke) {
		            if (ke.getCode() == KeyCode.ENTER) {
		                gm.geocodingAddress(addressTF.getText(),e);
		             }
		        }
		});
		
	}
	private void init() {
		List<SportCategory> list=null;
		String language = Locale.getDefault().getLanguage();
		System.out.println(language);
		if(!"sk".equals(language) && !"en".equals(language)) {
			language = "en";
		}
		final String lang = language;
		 try {
	     		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
	     		RestTemplate template = context.getBean(RestTemplate.class);
	     		((ConfigurableApplicationContext)context).close();     		
	     		String ip = new PropertyManager(getClass().getResourceAsStream("/connectionConfig")).getProperty("host");
	     		final String url = "http://"+ip+":8009/events/categories";
	     		ResponseEntity<List<SportCategory>> returnedEntity = template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<SportCategory>>() {});
	     		
	    		list = new ArrayList<>();
	     		for(SportCategory current : returnedEntity.getBody()) {
	     			list.add(current);
	     		}
	     	}catch(RestClientException e) {
	     		e.printStackTrace();
	     	}
		ObservableList<SportCategory> list2 = FXCollections.observableArrayList(list);
		sportCategoryChB.setConverter(new StringConverter<SportCategory>() {
			
			@Override
			public String toString(SportCategory object) {
				// TODO Auto-generated method stub
				if("sk".equals(lang)) {
					return object.getSport_sk();
				}
				return object.getSport_en();
			}
			
			@Override
			public SportCategory fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		sportCategoryChB.setItems(list2);
		System.out.println(language);
		PropertyManager pm = new PropertyManager(getClass().getResourceAsStream("/language/CreateEvents_"+language));
		titleLabel.setText(pm.getProperty("titleLabel"));
		titleDescriptionLabel.setText(pm.getProperty("titleDescriptionLabel"));
		createButton.setText(pm.getProperty("createButton"));
		eventNameTF.setPromptText(pm.getProperty("eventNameTF"));
		addressTF.setPromptText(pm.getProperty("addressTF"));
		eventDateDP.setPromptText(pm.getProperty("eventDateDP"));
		neccessaryAgeTF.setPromptText(pm.getProperty("neccessaryAgeTF"));
		maxUsersTF.setPromptText(pm.getProperty("maxUsersTF"));
		descriptionTA.setPromptText(pm.getProperty("descriptionTA"));
		Tooltip e = new Tooltip(pm.getProperty("sportCategoryChB"));
		sportCategoryChB.setTooltip(e);
	}
	public void fillLongLitude(LatLong l) {
		this.l = l;
		descriptionTA.setText(this.l.toString());
		addMarker(this.l);
	}
	private void addMarker(LatLong l) {
		GmComponent.getInstance().map.clearMarkers();
		GmComponent.getInstance().map.addMarker(new Marker(new MarkerOptions().position(l)));
		GmComponent.getInstance().map.setCenter(l);
		//GmComponent.getInstance().map.setZoom(12);
	}
	
	
private	void createEventHandle(){
		/*Event newEv = new Event(0,Integer.parseInt(maxUsersTF.getText()), 
				eventNameTF.getText(), descriptionTA.getText(),
				eventDateDP.getValue(), necessaryAge, creatorId, sportCategory, address, eventLocation); */
 		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
 		RestTemplate template = context.getBean(RestTemplate.class);
 		((ConfigurableApplicationContext)context).close();     		
 		String ip = new PropertyManager(getClass().getResourceAsStream("/connectionConfig")).getProperty("host");
 		final String url = "http://"+ip+":8009/events/events";
 		//template.postForEntity(url, newEv, Void.class);
	     	
	}
	

	
		
}
