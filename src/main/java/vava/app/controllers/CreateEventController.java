package vava.app.controllers;

import java.lang.Character.UnicodeBlock;
import java.net.URL;
import java.rmi.server.Skeleton;
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
	@FXML private TextArea descriptionTA;
	@FXML private DatePicker eventDateDP;
	@FXML private Button createButton;
	@FXML private ChoiceBox<SportCategory> sportCategoryChB;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GmComponent gm = GmComponent.getInstance();
		gmapsPane.getChildren().add(gm.mapComponent);
		init();
		Tooltip e = new Tooltip("choice language");
		sportCategoryChB.setTooltip(e);
		
		gm.map.setZoom(7);
		
		addressTF.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			 public void handle(KeyEvent ke) {
		            if (ke.getCode() == KeyCode.ENTER) {
		               gm.geocodingAddress(addressTF.getText());
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
	     		
	     		final String url = "http://25.19.186.82:8009/events/categories";
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

	}
	
	
	

	
		
}
